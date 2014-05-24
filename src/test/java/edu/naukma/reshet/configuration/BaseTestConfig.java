package edu.naukma.reshet.configuration;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("test")
@PropertySource(value = "classpath:test.properties")
@EnableAutoConfiguration
@EnableMongoRepositories
@ComponentScan(basePackages = {"edu.naukma.reshet.core", "edu.naukma.reshet.repositories", "edu.naukma.reshet.configuration"})
public class BaseTestConfig{
    @Bean
    public static PropertySourcesPlaceholderConfigurer getConfigurator(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
