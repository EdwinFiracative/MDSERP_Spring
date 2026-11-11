package com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.repository;

import com.proelectricos.mdserp.emp001_inv.vimatopsabiertas.ViMatOpsAbiertas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Listado de materiales de las OPs abiertas, con cantidades de receta y kardex, y su valor.
 * Consulta nativa (no existe vista en BD); el resultado se mapea a {@link ViMatOpsAbiertas}
 * por nombre de columna.
 */
@Repository
public interface ViMatOpsAbiertasRepository extends JpaRepository<ViMatOpsAbiertas, Long> {

    /** OPs abiertas (FECHA_T nula) con FECHA_I dentro del año :anio. */
    String TB_OPS = """
            WITH
            tbOps AS (
                SELECT op, cod AS Ref, cantp, cante, fecha_i, fecha_T, estado,
                       CASE WHEN SUBSTRING(LTRIM(cod),5,1) = 'E' THEN 'Estandar' ELSE 'Proyecto' END AS tipo
                FROM EMP001_INV.dbo.op
                WHERE fecha_t IS NULL
                  AND fecha_i >= DATEFROMPARTS(:anio, 1, 1)
                  AND fecha_i <  DATEFROMPARTS(:anio + 1, 1, 1)
            """;

    String FILTRO_OP = """
                  AND op = :op
            """;

    String RESTO = """
            ),
            viClass AS (
                SELECT cod, nom, ud, grup, cstd,
                       [ca_cla-ni1_nombre] AS Negocio, [ca_cla-ni2_nombre] AS Clase,
                       [ca_cla-ni3_nombre] AS Linea,   [ca_cla-ni4_nombre] AS Grupo,
                       [ca_cla-ni5_nombre] AS Nivel5
                FROM EMP001_INV.dbo.MAESTRO
                INNER JOIN EMP001_INV.dbo.co_clasificacion ON grup = [ca_cla-ni5_gru_factory]
            ),
            MaterialesReceta AS (
                SELECT o.op, m.modelo, m1.cod AS codMp,
                       -- (Cantidad del master / lote minimo) * cantidad programada en la OP
                       (CAST(m1.CANT AS FLOAT) / NULLIF(m.lmin, 0)) * o.cantp AS cantMaster
                FROM tbOps o
                INNER JOIN EMP001_PROD.dbo.MASTER m   ON o.Ref = m.COD
                INNER JOIN EMP001_PROD.dbo.MASTER1 m1 ON m.COD = m1.CODP AND m.MODELO = m1.MODELO_MAS
                WHERE (o.tipo = 'Proyecto' AND m.modelo = 'T')
                   OR (o.tipo = 'Estandar' AND (m.modelo = '' OR m.modelo IS NULL))
            ),
            MaterialesKardex AS (
                SELECT k.op_oc AS op, k.cod,
                       SUM(k.CANTIDAD * -1) AS cantKardex,
                       SUM(k.valor * -1)    AS valorK
                FROM EMP001_INV.dbo.co_KARDEX3 k
                INNER JOIN tbOps o ON k.op_oc = o.op
                WHERE k.tipo IN ('30','301','92','89')
                  AND k.fecha >= DATEFROMPARTS(:anio, 1, 1)
                  AND k.fecha <  DATEFROMPARTS(:anio + 1, 1, 1)
                GROUP BY k.op_oc, k.cod
            ),
            MaterialesCombinados AS (
                SELECT ISNULL(r.op, k.op) AS op, ISNULL(r.codMp, k.cod) AS codMaterial,
                       r.modelo, r.cantMaster, k.cantKardex, k.valorK
                FROM MaterialesReceta r
                FULL OUTER JOIN MaterialesKardex k ON r.op = k.op AND r.codMp = k.cod
            )
            SELECT
                CAST(ROW_NUMBER() OVER (ORDER BY o.fecha_i, o.op, mc.codMaterial) AS BIGINT) AS id,
                o.op, o.Ref, c.NOM AS NombreProducto, c.ud, c.grup, c.cstd, o.tipo,
                o.cantp, o.cante, o.FECHA_I, o.FECHA_T, o.ESTADO,
                mc.codMaterial, v.NOM AS NombreMaterial, mc.modelo,
                CAST(mc.cantMaster AS DECIMAL(38,6))                    AS cantMaster,
                CAST(ROUND(mc.cantMaster * v.cstd, 2) AS DECIMAL(38,2)) AS valorMaster,
                mc.cantKardex, mc.valorK,
                v.Negocio, v.Clase, v.Linea, v.Grupo, v.Nivel5
            FROM tbOps o
            INNER JOIN viClass c ON o.Ref = c.cod
            LEFT JOIN MaterialesCombinados mc ON o.op = mc.op
            LEFT JOIN viClass v ON mc.codMaterial = v.cod
            ORDER BY o.fecha_i ASC, o.op, mc.codMaterial
            """;

    @Query(value = TB_OPS + RESTO, nativeQuery = true)
    List<ViMatOpsAbiertas> findMaterialesOpsAbiertas(@Param("anio") int anio);

    @Query(value = TB_OPS + FILTRO_OP + RESTO, nativeQuery = true)
    List<ViMatOpsAbiertas> findMaterialesOpsAbiertasByOp(@Param("anio") int anio, @Param("op") Integer op);
}
