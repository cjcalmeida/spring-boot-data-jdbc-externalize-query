package com.cjcalmeida.spring.data.jdbc.externalize.query.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.support.JdbcExternalizedRepositoryFactoryBean;

@SpringBootApplication
@EnableJdbcRepositories(namedQueriesLocation = "classpath:queries.xml", repositoryFactoryBeanClass = JdbcExternalizedRepositoryFactoryBean.class)
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    @Autowired
    CommandLineRunner findPerson(PersonRepository repository){
        return args -> {
            System.out.println(repository.findByFirstname("BARACK"));
            System.out.println(repository.findByFirstname("DONALD"));

            PersonEntity barack = repository.findByFirstname("BARACK").get(0);
            repository.updateLastname("TRUMP", barack.getId());
            System.out.println(repository.findAll());
        };
    }

}
