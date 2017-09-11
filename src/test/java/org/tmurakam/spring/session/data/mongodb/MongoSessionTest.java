package org.tmurakam.spring.session.data.mongodb;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MongoSession test
 */
public class MongoSessionTest {
    private MongoSession session;

    @Before
    public void before() {
        session = new MongoSession();
    }

    @Test
    public void testAttributes() {
        session.setAttribute("key1", 12345);
        session.setAttribute("key2", "string");

        session.serializeAttributes();

        session.removeAttribute("key1");
        session.removeAttribute("key2");

        assertThat((String)session.getAttribute("key1")).isNull();
        assertThat((String)session.getAttribute("key2")).isNull();
        assertThat(session.getAttributeNames()).isEmpty();

        session.deserializeAttributes();

        assertThat(session.getAttributeNames()).hasSize(2);
        assertThat((int)session.getAttribute("key1")).isEqualTo(12345);
        assertThat((String)session.getAttribute("key2")).isEqualTo("string");
    }

    @Test
    public void testSessionId() {
        MongoSession session2 = new MongoSession();
        assertThat(session.getId()).isNotEqualTo(session2.getId());
    }

    @Test
    public void testGetCreationTime() {
        Instant now = Instant.now();
        assertThat(session.getCreationTime()).isBeforeOrEqualTo(now).isAfter(now.minusSeconds(10));
    }

    @Test
    public void testGetLastAccessedTime() {
        Instant now = Instant.now();
        assertThat(session.getLastAccessedTime()).isBeforeOrEqualTo(now).isAfter(now.minusSeconds(10));
    }

    @Test
    public void testSetMaxInactiveIntervalInSeconds() {
        session.setLastAccessedTime(Instant.ofEpochMilli(1000));
        session.setMaxInactiveInterval(Duration.ZERO);

        assertThat(session.getMaxInactiveInterval()).isEqualTo(Duration.ZERO);
        assertThat(session.getExpireTime()).isEqualTo(Instant.ofEpochMilli(1000));

        session.setMaxInactiveInterval(Duration.ofSeconds(1));
        assertThat(session.getMaxInactiveInterval()).isEqualTo(Duration.ofSeconds(1));
        assertThat(session.getExpireTime()).isEqualTo(Instant.ofEpochMilli(2000));
    }

    @Test
    public void testExpireTime() {
        session.setLastAccessedTime(Instant.EPOCH);
        assertThat(session.getExpireTime())
                .isEqualTo(Instant.EPOCH.plusMillis(session.getMaxInactiveInterval().toMillis()));
    }

    @Test
    public void testIsExpired() {
        session.setLastAccessedTime(Instant.EPOCH);
        assertThat(session.isExpired()).isTrue();

        session.setLastAccessedTime(Instant.now());
        assertThat(session.isExpired()).isFalse();
    }
}
