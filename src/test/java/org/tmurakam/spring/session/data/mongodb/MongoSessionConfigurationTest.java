package org.tmurakam.spring.session.data.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MongoSession Configuration Test
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestMongoConfiguration.class)
@WebAppConfiguration
public class MongoSessionConfigurationTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    SessionRepositoryFilter<MongoSession> filter;

    @Test
    public void getBeanByName() {
        Object filter = context.getBean("springSessionRepositoryFilter");
        assertThat(this.filter).isSameAs(filter);
    }

    @Test
    public void testFilter() {
    }
}
