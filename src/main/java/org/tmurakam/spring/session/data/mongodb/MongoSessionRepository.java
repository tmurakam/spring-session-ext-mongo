package org.tmurakam.spring.session.data.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * MongoSession Repository
 */
@Service
public class MongoSessionRepository implements SessionRepository<MongoSession> {
    @Autowired
    private MongoTemplate mongoTemplate;

    // for testing purpose
    private Map<String,MongoSession> fStore = new HashMap<>();

    @Override
    public MongoSession createSession() {
        MongoSession session = new MongoSession();
        session.serializeAttributes();
        mongoTemplate.save(session);
        return session;
    }

    @Override
    public MongoSession getSession(String id) {
        MongoSession session = mongoTemplate.findOne(createQuery(id), MongoSession.class);
        session.deserializeAttributes();
        return session;
    }

    @Override
    public void save(MongoSession session) {
        session.serializeAttributes();
        mongoTemplate.save(session);
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(createQuery(id));
    }

    private Query createQuery(String id) {
        return new Query(Criteria.where("sessionId").is(id));
    }
}
