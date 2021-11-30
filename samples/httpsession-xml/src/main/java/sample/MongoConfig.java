package sample;

import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.tmurakam.spring.session.data.mongodb.EnableMongoHttpSession;

/**
 * MongoConfig
 */
@EnableMongoHttpSession
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "mongoSession";
    }
}
