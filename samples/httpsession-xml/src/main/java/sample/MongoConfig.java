package sample;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.tmurakam.spring.session.data.mongodb.EnableMongoHttpSession;

/**
 * MongoConfig
 */
@EnableMongoHttpSession
public class MongoConfig {
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
