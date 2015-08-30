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

    @Override
    public MongoSession createSession() {
        MongoSession session = new MongoSession();
        session.serializeAttributes();
        mongoTemplate.save(session);

        expireCounter++;
        if (expireCounter > 100){
            removeAllExpiredSessions();
            expireCounter = 0;
        }

        return session;
    }

    @Override
    public MongoSession getSession(String id) {
        MongoSession session = mongoTemplate.findOne(createQueryById(id), MongoSession.class);
        session.deserializeAttributes();
        session.setLastAccessedTime(System.currentTimeMillis());
        return session;
    }

    @Override
    public void save(MongoSession session) {
        session.serializeAttributes();
        mongoTemplate.save(session);
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(createQueryById(id));
    }

    private Query createQueryById(String id) {
        return new Query(Criteria.where("sessionId").is(id));
    }

    /**
     * Remove all expired sessions
     */
    private void removeAllExpiredSessions() {
        long now = System.currentTimeMillis();
        Criteria criteria = Criteria.where("expireTime").lte(now);
        mongoTemplate.remove(new Query(criteria), MongoSession.class);
    }
}
