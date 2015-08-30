package org.tmurakam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * MongoSession Configuration
 */
@Configuration
public class MongoSessionConfiguration implements ServletContextAware {
    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

    }
    @Bean
    public SessionRepositoryFilter<MongoSession> springSessionRepositoryFilter(MongoSessionRepository repository) {
        SessionRepositoryFilter<MongoSession> filter = new SessionRepositoryFilter<>(repository);
        filter.setServletContext(this.servletContext);
        return filter;
    }

    @Bean
    public MongoSessionRepository mongoSessionRepository() {
        return new MongoSessionRepository();
    }
}
