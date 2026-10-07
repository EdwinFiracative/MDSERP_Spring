package com.proelectricos.mdserp.chatbot.erpdb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SqlGuardTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "SELECT TOP 20 * FROM dbo.OrderHeader",
            "select top 5 h.orderHeaderNumber from ErpDb.dbo.OrderHeader h;",
            "WITH t AS (SELECT 1 AS x) SELECT x FROM t",
            "SELECT TOP 20 TABLE_NAME, COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME LIKE '%Order%'",
            "SELECT TOP 5 referName FROM [ErpDb].[dbo].[Reference] WHERE referName LIKE '%delete; drop%'",
            "-- comentario\nSELECT 1"
    })
    void permiteConsultasDeLecturaSobreErpDb(String sql) {
        assertThat(SqlGuard.validar(sql)).doesNotEndWith(";").doesNotContain("--");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "UPDATE dbo.OrderHeader SET orderHeaderDescription = 'x'",
            "DELETE FROM dbo.OrderNote",
            "SELECT 1; DROP TABLE dbo.OrderNote",
            "SELECT * INTO dbo.Copia FROM dbo.OrderHeader",
            "SELECT TOP 5 * FROM MDS_ERP.dbo.tb_imp_retec",
            "SELECT TOP 5 * FROM [MDS_ERP]..tb_imp_retec",
            "SELECT name FROM sys.databases",
            "EXEC sp_who",
            "SELECT * FROM OPENROWSET('SQLNCLI', 'x', 'SELECT 1')",
            "WITH t AS (SELECT 1 AS x) DELETE FROM dbo.OrderNote",
            "",
            "SELECT 1 /* ; */; EXEC xp_cmdshell 'dir'"
    })
    void rechazaEscriturasYOtrasBases(String sql) {
        assertThatThrownBy(() -> SqlGuard.validar(sql)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void quitaComentariosYPuntoYComaFinal() {
        assertThat(SqlGuard.validar("SELECT 1 /* nota */ ;")).isEqualTo("SELECT 1");
    }
}
