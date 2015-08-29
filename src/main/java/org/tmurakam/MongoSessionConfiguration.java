package org.tmurakam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.servlet.ServletContext;

/**
 * MongoSession Configuration
 */
@Configuration
public class MongoSessionConfiguration {
    @Bean
    public SessionRepositoryFilter<MongoSession> springSessionRepositoryFilter(
            MongoSessionRepository repository, ServletContext servletContext) {
        SessionRepositoryFilter<MongoSession> filter = new SessionRepositoryFilter<>(repository);
        filter.setServletContext(servletContext);
        return filter;
    }
}
