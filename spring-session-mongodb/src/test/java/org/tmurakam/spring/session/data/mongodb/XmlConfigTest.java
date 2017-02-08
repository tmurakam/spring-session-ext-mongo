package org.tmurakam.spring.session.data.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * XML Configuration test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:testContext.xml")
@WebAppConfiguration
public class XmlConfigTest {
    @Autowired
    private MongoSessionRepository repository;

    @Autowired
    private SessionRepositoryFilter<MongoSession> filter;

    @Test
    public void test() {
        assertThat(repository).isNotNull();
        assertThat(filter).isNotNull();
    }
}
