/*
 * Copyright (c) 2015, Takuya Murakami.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.tmurakam.spring.session.data.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.session.ExpiringSession;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * MongoSession for spring session
 */
@Document(collection = "springMongoSession")
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
     * Session attributes (not saved to MongoDB)
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
    @Indexed
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

    protected long getExpireTime() {
        return expireTime;
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
            serializedAttributes = new byte[0];
        }
    }

    /**
     * Deserialize session attributes
     */
    public void deserializeAttributes() {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedAttributes);
             ObjectInputStream ois = new ObjectInputStream(bis))  {
            attributes = (Map<String,Object>)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
            attributes = new HashMap<>();
        }
    }
}
