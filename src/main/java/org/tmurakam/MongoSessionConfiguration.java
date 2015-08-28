package org.tmurakam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.SessionRepositoryFilter;

/**
 * MongoSession Configuration
 */
@Configuration
public class MongoSessionConfiguration {
    @Bean
    public SessionRepositoryFilter<MongoSession> springSessionRepositoryFilter(MongoSessionRepository repository) {
        SessionRepositoryFilter<MongoSession> filter = new SessionRepositoryFilter<>(repository);
        return filter;
    }
}
