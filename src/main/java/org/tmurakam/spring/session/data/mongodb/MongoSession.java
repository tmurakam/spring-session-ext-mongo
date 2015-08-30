package org.tmurakam.spring.session.data.mongodb;

import lombok.Getter;
import lombok.Setter;
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

    @Id
    private String id;

    @Indexed(unique = true)
    private String sessionId;

    private byte[] serializedAttributes;

    @Transient
    private Map<String,Object> attributes;

    @Getter
    private long creationTime;

    @Getter
    private long lastAccessedTime;

    @Getter
    private int maxInactiveIntervalInSeconds;

    private long expireTime;

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
    public void setMaxInactiveIntervalInSeconds(int interval) {
        maxInactiveIntervalInSeconds = interval;
        updateExpireTime();
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
