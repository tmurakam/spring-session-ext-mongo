package org.tmurakam.spring.session.data.mongodb;

import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Mongo Configuration
 */
@Configuration
public class TestMongoConfiguration {
    @Bean
    public MongoClientFactoryBean mongo() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost("localhost"); // test
        return mongo;
    }

    @Bean
    public MongoTemplate mongoTemplate(Mongo mongo) {
        MongoTemplate template = new MongoTemplate(mongo, "mongoSession");
        return template;
    }
}
