package org.tmurakam.spring.session.data.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Session repository test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestMongoConfiguration.class)
@WebAppConfiguration
public class MongoSessionRepositoryIT {
    @Autowired
    MongoSessionRepository repository;

    @Test
    public void test() {
        assertNotNull(repository);
    }

    @Test
    public void testSaveReadDelete() {
        MongoSession session = repository.createSession();
        session.setAttribute("key1", "value1");
        repository.save(session);

        MongoSession session2 = repository.getSession(session.getId());
        assertEquals("value1", session2.getAttribute("key1"));

        repository.delete(session.getId());
        assertNull(repository.getSession(session.getId()));
    }

    @Test
    public void testGetSessionExpired() {
        MongoSession session = repository.createSession();
        session.setLastAccessedTime(0);
        repository.save(session);

        assertNull(repository.getSession(session.getId()));
    }

    @Test
    public void testExpire() {
        MongoSession session = repository.createSession();
        session.setLastAccessedTime(0);
        repository.save(session);

        repository.removeAllExpiredSessions();

        assertNull(repository._getSession(session.getId()));
    }

    @Test
    public void testExpirePeriodically() {
        for (int i = 0; i < 1000; i++) {
            repository.createSession();
        }
        // TODO:
    }
}
