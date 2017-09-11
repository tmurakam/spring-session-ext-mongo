package org.tmurakam.spring.session.data.mongodb;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Mongo Configuration
 */
@EnableMongoHttpSession
public class TestMongoConfiguration {
    /*
    @Bean
    public MongoClientFactoryBean mongo() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost("localhost"); // test
        return mongo;
    }
    */

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("localhost");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoClient(), "mongoSession");
        return template;
    }
}
