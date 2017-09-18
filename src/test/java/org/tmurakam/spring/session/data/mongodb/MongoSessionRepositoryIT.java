package org.tmurakam.spring.session.data.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Session repository test
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestMongoConfiguration.class)
@WebAppConfiguration
public class MongoSessionRepositoryIT {
    @Autowired
    MongoSessionRepository repository;

    @Test
    public void test() {
        assertThat(repository).isNotNull();
    }

    @Test
    public void testSaveReadDelete() {
        MongoSession session = repository.createSession();
        session.setAttribute("key1", "value1");
        repository.save(session);

        MongoSession session2 = repository.findById(session.getId());
        assertThat((String)session2.getAttribute("key1")).isEqualTo("value1");

        repository.deleteById(session.getId());
        assertThat(repository.findById(session.getId())).isNull();
    }

    @Test
    public void testGetSessionExpired() {
        MongoSession session = repository.createSession();
        session.setLastAccessedTime(Instant.EPOCH);
        repository.save(session);

        assertThat(repository.findById(session.getId())).isNull();
    }

    @Test
    public void testFlush() {
        MongoSession session = repository.createSession();
        session.setLastAccessedTime(Instant.EPOCH);
        repository.save(session);

        assertThat(repository._getSession(session.getId())).isNotNull();

        repository.flushExpiredSessions();

        assertThat(repository._getSession(session.getId())).isNull();
    }

    @Test
    public void testFlushPeriodically() {
        MongoSession session = repository.createSession();
        session.setLastAccessedTime(Instant.EPOCH);
        repository.save(session);

        assertThat(repository._getSession(session.getId())).isNotNull();

        // force flush
        repository.lastFlushTime = Instant.now().minusSeconds(MongoSessionRepository.FLUSH_INTERVAL_SECONDS);
        repository.createSession();

        assertThat(repository._getSession(session.getId())).isNull();
    }
}
