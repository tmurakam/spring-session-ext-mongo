package org.tmurakam.spring.session.data.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * XML Configuration test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:testContext.xml")
public class XmlConfigTest {
    @Autowired
    private MongoSessionRepository repository;

    @Autowired
    private SessionRepositoryFilter<MongoSession> filter;

    @Test
    public void test() {
        assertNotNull(repository);
        assertNotNull(filter);
    }
}
