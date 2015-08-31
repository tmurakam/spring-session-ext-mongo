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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

/**
 * MongoSession Repository
 */
@Service
public class MongoSessionRepository implements SessionRepository<MongoSession> {
    @Autowired
    private MongoTemplate mongoTemplate;

    private int expireCounter = 0;

    /** {@inheritDoc} */
    @Override
    public MongoSession createSession() {
        MongoSession session = new MongoSession();
        session.serializeAttributes();
        mongoTemplate.save(session);

        expireCounter++;
        if (expireCounter > 100){
            expireCounter = 0;
            removeAllExpiredSessions();
        }

        return session;
    }

    /** {@inheritDoc} */
    @Override
    public MongoSession getSession(String id) {
        MongoSession session = mongoTemplate.findOne(createQueryById(id), MongoSession.class);
        if (session == null) return null;

        session.deserializeAttributes();
        if (session.isExpired()) {
            delete(session.getId());
            return null;
        }
        session.setLastAccessedTime(System.currentTimeMillis());
        return session;
    }

    /** {@inheritDoc} */
    @Override
    public void save(MongoSession session) {
        session.serializeAttributes();
        mongoTemplate.save(session);
    }

    /** {@inheritDoc} */
    @Override
    public void delete(String id) {
        mongoTemplate.remove(createQueryById(id), MongoSession.class);
    }

    private Query createQueryById(String id) {
        return new Query(Criteria.where(MongoSession.KEY_SESSION_ID).is(id));
    }

    /**
     * Remove all expired sessions
     */
    private void removeAllExpiredSessions() {
        long now = System.currentTimeMillis();
        Criteria criteria = Criteria.where(MongoSession.KEY_EXPIRE_TIME).lte(now);
        mongoTemplate.remove(new Query(criteria), MongoSession.class);
    }
}
