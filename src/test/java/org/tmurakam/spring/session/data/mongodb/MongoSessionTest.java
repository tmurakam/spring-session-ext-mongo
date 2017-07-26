package org.tmurakam.spring.session.data.mongodb;

import org.junit.Before;
import org.junit.Test;

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
        long now = System.currentTimeMillis();
        assertThat(session.getCreationTime()).isLessThanOrEqualTo(now).isGreaterThan(now - 10000);
    }

    @Test
    public void testGetLastAccessedTime() {
        long now = System.currentTimeMillis();
        assertThat(session.getLastAccessedTime()).isLessThanOrEqualTo(now).isGreaterThan(now - 10000);
    }

    @Test
    public void testSetMaxInactiveIntervalInSeconds() {
        session.setLastAccessedTime(1000);
        session.setMaxInactiveIntervalInSeconds(0);

        assertThat(session.getMaxInactiveIntervalInSeconds()).isEqualTo(0);
        assertThat(session.getExpireTime()).isEqualTo(1000);

        session.setMaxInactiveIntervalInSeconds(1);
        assertThat(session.getMaxInactiveIntervalInSeconds()).isEqualTo(1);
        assertThat(session.getExpireTime()).isEqualTo(2000);
    }

    @Test
    public void testExpireTime() {
        session.setLastAccessedTime(0);
        assertThat(session.getExpireTime()).isEqualTo(session.getMaxInactiveIntervalInSeconds() * 1000);
    }

    @Test
    public void testIsExpired() {
        session.setLastAccessedTime(0);
        assertThat(session.isExpired()).isTrue();

        session.setLastAccessedTime(System.currentTimeMillis());
        assertThat(session.isExpired()).isFalse();
    }
}
