package com.proelectricos.mdserp.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
//@PropertySource({"classpath:persistence-multiple-db-boot.properties"})
@EnableJpaRepositories(
        basePackages = {
                "com.proelectricos.mdserp.erpdb",
                "com.proelectricos.mdserp.emp001_comp",
                "com.proelectricos.mdserp.emp001_fact",
                "com.proelectricos.mdserp.emp001_inv",
                "com.proelectricos.mdserp.emp001_ofer",
                "com.proelectricos.mdserp.emp004_inv",
                "com.proelectricos.mdserp.mds_erp"},
        entityManagerFactoryRef = "sqlFactoryEntityManager",
        transactionManagerRef = "sqlFactoryTransactionManager")
public class PersistenceSqlFactoryAutoConfiguration {

    @Autowired
    private Environment env;

    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource sqlFactoryDataSource() {
        System.out.println("PDM DataSource created");
        return DataSourceBuilder.create().build();

    }
    // sqlFactoryEntityManager bean
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean sqlFactoryEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(sqlFactoryDataSource());
        em.setPackagesToScan(
                new String[] {
                "com.proelectricos.mdserp.erpdb",
                "com.proelectricos.mdserp.emp001_comp",
                "com.proelectricos.mdserp.emp001_fact",
                "com.proelectricos.mdserp.emp001_inv",
                "com.proelectricos.mdserp.emp001_ofer",
                "com.proelectricos.mdserp.emp004_inv",
                "com.proelectricos.mdserp.mds_erp" });

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect",
                env.getProperty("spring.jpa.properties.hibernate.dialect"));
        // Este EntityManager no toma spring.jpa.properties.* automaticamente: se pasan aqui.
        // Carga relaciones lazy en lotes (IN de hasta N ids) en vez de una consulta por entidad (N+1)
        properties.put("hibernate.default_batch_fetch_size",
                env.getProperty("spring.jpa.properties.hibernate.default_batch_fetch_size", "100"));
        // Redondea los IN a potencias de 2 para reutilizar planes de ejecucion en SQL Server
        properties.put("hibernate.query.in_clause_parameter_padding",
                env.getProperty("spring.jpa.properties.hibernate.query.in_clause_parameter_padding", "true"));
        em.setJpaPropertyMap(properties);
        System.out.println("PDM EntityManager created " + env.getProperty("spring.jpa.hibernate.ddl-auto"));
        return em;
    }


   // userTransactionManager bean

    @Primary
    @Bean
    public PlatformTransactionManager sqlFactoryTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                sqlFactoryEntityManager().getObject());
        return transactionManager;
    }
}