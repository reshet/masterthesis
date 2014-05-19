package edu.naukma.reshet.configuration;


import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestMongoConfiguration  extends MongoConfiguration {
    @Override
    public Mongo mongo() throws Exception {
        return new Fongo("foo test server").getMongo();
    }
}