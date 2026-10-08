package com.proelectricos.mdserp.erpdb;

import com.proelectricos.mdserp.erpdb.branch.Branch;
import com.proelectricos.mdserp.erpdb.client.Client;
import com.proelectricos.mdserp.erpdb.measurunit.MeasurUnit;
import com.proelectricos.mdserp.erpdb.order.OrderHeader;
import com.proelectricos.mdserp.erpdb.order.OrderNote;
import com.proelectricos.mdserp.erpdb.order.OrderReferStatus;
import com.proelectricos.mdserp.erpdb.order.OrderReference;
import com.proelectricos.mdserp.erpdb.project.Project;
import com.proelectricos.mdserp.erpdb.referclassification.ReferClassification;
import com.proelectricos.mdserp.erpdb.reference.Reference;
import com.proelectricos.mdserp.erpdb.thirdparty.ThirdParty;
import com.proelectricos.mdserp.erpdb.vendor.Vendor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Pruebas de solo lectura contra la base ErpDb real: validan que el mapeo JPA
 * coincida con el esquema y que las relaciones naveguen a los registros correctos.
 */
@SpringBootTest
@Transactional(readOnly = true)
class ErpDbEntityMappingTest {

    @PersistenceContext
    private EntityManager em;

    @Qualifier("sqlFactoryDataSource")
    @Autowired
    private DataSource sqlFactoryDataSource;

    @Autowired
    private Environment env;

    @Test
    void mappingMatchesDatabaseSchema() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(sqlFactoryDataSource);
        factory.setPackagesToScan(ErpDbEntityMappingTest.class.getPackageName());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPersistenceUnitName("erpDbSchemaValidation");
        factory.setJpaPropertyMap(Map.of(
                "hibernate.hbm2ddl.auto", "validate",
                "hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect")));

        assertThatCode(factory::afterPropertiesSet).doesNotThrowAnyException();
        factory.destroy();
    }

    @Test
    void everyEntityCountMatchesTableCount() {
        Map<Class<?>, String> tables = Map.ofEntries(
                Map.entry(Branch.class, "Branch"),
                Map.entry(Client.class, "Client"),
                Map.entry(MeasurUnit.class, "MeasurUnit"),
                Map.entry(OrderHeader.class, "OrderHeader"),
                Map.entry(OrderNote.class, "OrderNote"),
                Map.entry(OrderReference.class, "OrderReference"),
                Map.entry(OrderReferStatus.class, "OrderReferStatus"),
                Map.entry(Project.class, "Project"),
                Map.entry(ReferClassification.class, "ReferClassification"),
                Map.entry(Reference.class, "Reference"),
                Map.entry(ThirdParty.class, "ThirdParty"),
                Map.entry(Vendor.class, "Vendor"));

        tables.forEach((entity, table) -> {
            Long jpaCount = em.createQuery("select count(e) from " + entity.getSimpleName() + " e", Long.class)
                    .getSingleResult();
            Number sqlCount = (Number) em.createNativeQuery("select count(*) from ErpDb.dbo." + table)
                    .getSingleResult();
            assertThat(jpaCount).as(table).isEqualTo(sqlCount.longValue());
        });
    }

    @Test
    void clientNavigatesToThirdPartyAndBranches() {
        Branch branch = first("select b from Branch b", Branch.class);
        Client client = branch.getBranchClient();

        Object[] row = nativeRow("select c.clientId, t.thirdPartyId from ErpDb.dbo.Branch b "
                + "join ErpDb.dbo.Client c on c.clientId = b.branchClient "
                + "join ErpDb.dbo.ThirdParty t on t.thirdPartyId = c.clientThirdParty "
                + "where b.branchId = ?1", branch.getId());

        assertThat(client.getId()).isEqualTo(toLong(row[0]));
        assertThat(client.getClientThirdParty().getId()).isEqualTo(toLong(row[1]));
        assertThat(client.getClientThirdParty().getThirdPartyName()).isNotBlank();
        assertThat(client.getBranches()).extracting(Branch::getId).contains(branch.getId());
    }

    @Test
    void vendorNavigatesToThirdPartyAndOrders() {
        OrderHeader header = first("select h from OrderHeader h where h.orderHeaderVendor is not null", OrderHeader.class);
        Vendor vendor = header.getOrderHeaderVendor();

        assertThat(vendor.getVendorCode()).isNotBlank();
        assertThat(vendor.getVendorThirdParty().getThirdPartyName()).isNotBlank();
        assertThat(vendor.getOrderHeaders()).extracting(OrderHeader::getId).contains(header.getId());
    }

    @Test
    void orderHeaderWithoutVendorLoads() {
        OrderHeader header = first("select h from OrderHeader h where h.orderHeaderVendor is null", OrderHeader.class);

        assertThat(header.getOrderHeaderVendor()).isNull();
        assertThat(header.getOrderHeaderBranch()).isNotNull();
    }

    @Test
    void orderHeaderNavigatesToBranchLinesAndNotes() {
        OrderHeader header = first("select h from OrderHeader h where exists "
                + "(select 1 from OrderNote n where n.orderNoteOrderHeader = h)", OrderHeader.class);

        long lines = countNative("select count(*) from ErpDb.dbo.OrderReference where orderReferOrderHeader = ?1", header.getId());
        long notes = countNative("select count(*) from ErpDb.dbo.OrderNote where orderNoteOrderHeader = ?1", header.getId());

        assertThat(header.getOrderReferences()).hasSize((int) lines);
        assertThat(header.getOrderNotes()).hasSize((int) notes).isNotEmpty();
        assertThat(header.getOrderReferences())
                .allSatisfy(line -> assertThat(line.getOrderReferOrderHeader()).isSameAs(header));
        assertThat(header.getOrderHeaderBranch().getOrderHeaders()).contains(header);
        assertThat(header.getOrderHeaderCreatTimeStamp()).isNotNull();
    }

    @Test
    void orderReferenceNavigatesToReference() {
        OrderReference line = first("select r from OrderReference r", OrderReference.class);
        Reference reference = line.getOrderReferReference();

        Object[] row = nativeRow("select ref.referId, ref.referCod from ErpDb.dbo.OrderReference o "
                + "join ErpDb.dbo.Reference ref on ref.referId = o.orderReferReference "
                + "where o.orderReferId = ?1", line.getId());

        assertThat(reference.getId()).isEqualTo(toLong(row[0]));
        assertThat(reference.getReferCod()).isEqualTo(row[1]);
        assertThat(line.getOrderReferUnitPrice()).isNotNull();
        assertThat(reference.getOrderReferences()).contains(line);
    }

    @Test
    void orderReferenceNavigatesToStatus() {
        OrderReference line = first("select r from OrderReference r", OrderReference.class);

        Object[] row = nativeRow("select s.orderReferStatusId, s.orderReferStatusName from ErpDb.dbo.OrderReference o "
                + "join ErpDb.dbo.OrderReferStatus s on s.orderReferStatusId = o.orderReferStatus "
                + "where o.orderReferId = ?1", line.getId());

        assertThat(line.getOrderReferStatus().getId()).isEqualTo(toLong(row[0]));
        assertThat(line.getOrderReferStatus().getOrderReferStatusName()).isEqualTo(row[1]);
        assertThat(line.getOrderReferStatus().getOrderReferStatusDescription()).isNotBlank();
    }

    @Test
    void orderReferenceNavigatesToOptionalProject() {
        OrderReference withProject = first("select r from OrderReference r where r.orderReferProject is not null",
                OrderReference.class);
        OrderReference withoutProject = first("select r from OrderReference r where r.orderReferProject is null",
                OrderReference.class);

        Object[] row = nativeRow("select p.projeId, p.projeName from ErpDb.dbo.OrderReference o "
                + "join ErpDb.dbo.Project p on p.projeId = o.orderReferProject "
                + "where o.orderReferId = ?1", withProject.getId());

        assertThat(withProject.getOrderReferProject().getId()).isEqualTo(toLong(row[0]));
        assertThat(withProject.getOrderReferProject().getProjeName()).isEqualTo(row[1]);
        assertThat(withoutProject.getOrderReferProject()).isNull();
    }

    @Test
    void referenceNavigatesToMeasureUnitsAndClassification() {
        Reference reference = first("select r from Reference r where r.referClassification is not null "
                + "and r.referAlterMeasuUnit is not null", Reference.class);

        MeasurUnit unit = reference.getReferMeasuUnit();
        assertThat(unit.getMeasuUnitCode()).isNotBlank();
        assertThat(unit.getMeasuUnitDianCode()).isNotBlank();
        assertThat(reference.getReferAlterMeasuUnit().getMeasuUnitName()).isNotBlank();
        assertThat(reference.getReferConveFactor()).isNotNull();
        assertThat(reference.getReferClassification().getReferences()).contains(reference);
    }

    @Test
    void referClassificationTreeIsConsistent() {
        ReferClassification child = first("select c from ReferClassification c where c.referClassFather is not null",
                ReferClassification.class);
        ReferClassification father = child.getReferClassFather();

        long sons = countNative("select count(*) from ErpDb.dbo.ReferClassification where referClassFather = ?1", father.getId());

        assertThat(father.getReferClassificationSons()).hasSize((int) sons).contains(child);
    }

    @Test
    void thirdPartyVerifDigitMapsTinyint() {
        List<ThirdParty> parties = em.createQuery("select t from ThirdParty t where t.thirdPartyVerifDigit is not null",
                ThirdParty.class).setMaxResults(20).getResultList();

        assertThat(parties).isNotEmpty()
                .allSatisfy(t -> assertThat(t.getThirdPartyVerifDigit()).isBetween((short) 0, (short) 9));
    }

    private <T> T first(String jpql, Class<T> type) {
        List<T> results = em.createQuery(jpql, type).setMaxResults(1).getResultList();
        assertThat(results).as("sin datos para: " + jpql).isNotEmpty();
        return results.get(0);
    }

    private Object[] nativeRow(String sql, Object param) {
        return (Object[]) em.createNativeQuery(sql).setParameter(1, param).getSingleResult();
    }

    private long countNative(String sql, Object param) {
        return ((Number) em.createNativeQuery(sql).setParameter(1, param).getSingleResult()).longValue();
    }

    private static Long toLong(Object value) {
        return ((Number) value).longValue();
    }
}
