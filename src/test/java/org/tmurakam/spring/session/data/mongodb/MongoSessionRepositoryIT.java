package org.tmurakam.spring.session.data.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;

/**
 * Session repository test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {MongoSessionConfiguration.class, TestMongoConfiguration.class})
public class MongoSessionRepositoryIT {
    @Autowired
    MongoSessionRepository repository;

    @Test
    public void test() {
        assertNotNull(repository);
    }

    @Test
    public void testSaveRead() {
        MongoSession session = repository.createSession();
        session.setAttribute("key1", "value1");
        repository.save(session);

        MongoSession session2 = repository.getSession(session.getId());
        assertEquals("value1", session2.getAttribute("key1"));
    }
}
