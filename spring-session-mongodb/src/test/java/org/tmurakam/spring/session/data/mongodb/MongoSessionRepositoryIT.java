package org.tmurakam.spring.session.data.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
    public void testFlush() {
        MongoSession session = repository.createSession();
        session.setLastAccessedTime(0);
        repository.save(session);

        assertNotNull(repository._getSession(session.getId()));

        repository.flushExpiredSessions();

        assertNull(repository._getSession(session.getId()));
    }

    @Test
    public void testFlushPeriodically() {
        MongoSession session = repository.createSession();
        session.setLastAccessedTime(0);
        repository.save(session);

        assertNotNull(repository._getSession(session.getId()));

        // force flush
        repository.lastFlushTime = System.currentTimeMillis() - MongoSessionRepository.FLUSH_INTERVAL_SECONDS * 1000;
        repository.createSession();

        assertNull(repository._getSession(session.getId()));
    }
}
