package org.tmurakam.spring.session.data.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.session.ExpiringSession;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * MongoSession for spring session
 */
public class MongoSession implements ExpiringSession {
    public static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = 1800;

    /**
     * MongoDB Object ID
     */
    @Id
    private String id;

    /**
     * Session ID
     */
    @Indexed(unique = true)
    private String sessionId;
    public static final String KEY_SESSION_ID = "sessionId";

    /**
     * Serialized session attributes
     */
    private byte[] serializedAttributes;

    /**
     * Sesison attributes (not saved to MongoDB)
     */
    @Transient
    private Map<String,Object> attributes;

    /**
     * Creation time (epoch in ms)
     */
    private long creationTime;

    /**
     * Last accessed time (epoch in ms)
     */
    private long lastAccessedTime;

    /**
     * Max inactive interval (sec)
     */
    private int maxInactiveIntervalInSeconds;

    /**
     * Expire time (epoch in ms)
     */
    private long expireTime;
    public static final String KEY_EXPIRE_TIME = "expireTime";

    /**
     * Constructor
     */
    public MongoSession() {
        sessionId = UUID.randomUUID().toString();
        attributes = new HashMap<>();
        creationTime = System.currentTimeMillis();
        lastAccessedTime = creationTime;
        maxInactiveIntervalInSeconds = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;
        updateExpireTime();
    }

    @Override
    public String getId() {
        return sessionId;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
        updateExpireTime();
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(int interval) {
        maxInactiveIntervalInSeconds = interval;
        updateExpireTime();
    }

    @Override
    public int getMaxInactiveIntervalInSeconds() {
        return maxInactiveIntervalInSeconds;
    }

    private void updateExpireTime() {
        expireTime = lastAccessedTime + maxInactiveIntervalInSeconds * 1000;
    }

    @Override
    public boolean isExpired() {
        long now = System.currentTimeMillis();
        return expireTime <= now;
    }

    @Override
    public <T> T getAttribute(String attributeName) {
        return (T)attributes.get(attributeName);
    }

    @Override
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    @Override
    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    /**
     * Serialize session attributes
     */
    public void serializeAttributes() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(attributes);
            oos.flush();
            serializedAttributes = bos.toByteArray();
        } catch (IOException e) {
            //e.printStackTrace();

        }
    }

    /**
     * Deserialize session attirbutes
     */
    public void deserializeAttributes() {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedAttributes);
             ObjectInputStream ois = new ObjectInputStream(bis))  {
            attributes = (Map<String,Object>)ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
