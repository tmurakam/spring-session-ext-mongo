package org.tmurakam;

import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * MongoSession Repository
 */
@Service
public class MongoSessionRepository implements SessionRepository<MongoSession> {
    // for testing purpose
    private Map<String,MongoSession> fStore = new HashMap<>();

    @Override
    public MongoSession createSession() {
        MongoSession session = new MongoSession();
        fStore.put(session.getId(), session);
        return session;
    }

    @Override
    public MongoSession getSession(String id) {
        return fStore.get(id);
    }

    @Override
    public void save(MongoSession session) {
        // nothing
    }

    @Override
    public void delete(String id) {
        fStore.remove(id);
    }
}
