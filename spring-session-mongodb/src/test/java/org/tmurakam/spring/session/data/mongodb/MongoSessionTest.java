package org.tmurakam.spring.session.data.mongodb;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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

        assertNull(session.getAttribute("key1"));
        assertNull(session.getAttribute("key2"));
        assertEquals(0, session.getAttributeNames().size());

        session.deserializeAttributes();

        assertEquals(2, session.getAttributeNames().size());
        assertEquals(12345, (int)session.getAttribute("key1"));
        assertEquals("string", session.getAttribute("key2"));
    }

    @Test
    public void testSessionId() {
        MongoSession session2 = new MongoSession();
        assertNotEquals(session.getId(), session2.getId());
    }

    @Test
    public void testGetCreationTime() {
        long now = System.currentTimeMillis();
        assertThat(session.getCreationTime(), is(lessThanOrEqualTo(now)));
        assertThat(session.getCreationTime(), is(greaterThan(now - 10000)));
    }

    @Test
    public void testGetLastAccessedTime() {
        long now = System.currentTimeMillis();
        assertThat(session.getLastAccessedTime(), is(lessThanOrEqualTo(now)));
        assertThat(session.getLastAccessedTime(), is(greaterThan(now - 10000)));
    }

    @Test
    public void testSetMaxInactiveIntervalInSeconds() {
        session.setLastAccessedTime(1000);
        session.setMaxInactiveIntervalInSeconds(0);

        assertEquals(0, session.getMaxInactiveIntervalInSeconds());
        assertEquals(1000, session.getExpireTime());

        session.setMaxInactiveIntervalInSeconds(1);
        assertEquals(1, session.getMaxInactiveIntervalInSeconds());
        assertEquals(2000, session.getExpireTime());
    }

    @Test
    public void testExpireTime() {
        session.setLastAccessedTime(0);
        assertEquals(session.getMaxInactiveIntervalInSeconds() * 1000, session.getExpireTime());
    }

    @Test
    public void testIsExpired() {
        session.setLastAccessedTime(0);
        assertTrue(session.isExpired());

        session.setLastAccessedTime(System.currentTimeMillis());
        assertFalse(session.isExpired());
    }
}
