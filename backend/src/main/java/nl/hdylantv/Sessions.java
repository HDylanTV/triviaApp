package nl.hdylantv;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class Sessions {

    Map<UUID, Session> sessionMap;

    public Sessions() {
        sessionMap = new HashMap<>();
    }

    public Session createNewSession() {
        Session session = new Session();
        sessionMap.put(session.sessionId, session);

        return session;
    }

    public Session fetch(UUID sessionId) {
        return sessionMap.get(sessionId);
    }
}
