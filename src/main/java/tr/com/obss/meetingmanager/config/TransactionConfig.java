package tr.com.obss.meetingmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement

public class TransactionConfig {
//    @Bean
//    public PlatformTransactionManager transactionManager(){
//        JpaTransactionManager transactionManager
//                = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                entityManagerFactoryBean().getObject() );
//        return transactionManager;
//    }


//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        final JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }


//    @Bean
//    EntityManagerFactory entityManagerFactory() {
//        EntityManagerFactory emf =
//                Persistence.createEntityManagerFactory("example-unit");
//        return emf;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory());
//        return txManager;
//    }




//
//@Bean
//public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//    em.setDataSource(dataSource);
//    em.setPackagesToScan("tr.com.obss.meetingmanager.entity");
//
//    final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//    em.setJpaVendorAdapter(vendorAdapter);
//    return em;
//}
    @Bean
    public PlatformTransactionManager dstm(DataSource dataSource,EntityManagerFactory em) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(em);
        return transactionManager;
    }
//    @Bean
//    public PlatformTransactionManager dstm(DataSource dataSource) {
//        final JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory(dataSource).getObject());
//        return transactionManager;
//    }

}
