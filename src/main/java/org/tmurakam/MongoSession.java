package org.tmurakam;

import lombok.Getter;
import lombok.Setter;
import org.springframework.session.ExpiringSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * MongoSession for spring session
 */
public class MongoSession implements ExpiringSession {
    @Getter
    private String id;

    @Getter
    private long creationTime;

    @Getter
    private long lastAccessedTime;

    @Getter @Setter
    private int maxInactiveIntervalInSeconds;

    private Map<String,Object> attributes;

    public MongoSession() {
        id = UUID.randomUUID().toString();

        attributes = new HashMap<>();
    }

    @Override
    public boolean isExpired() {
        return false;
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
}
