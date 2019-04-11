package se.inera.intyg.intygsbestallning.web.controller.dto;


public class SessionState {
    /**
     * Is this request associated with a session at all?.
     */
    boolean hasSession;
    /**
     * Is this request associated with a session that has a Spring securityContext in it?.
     */
    boolean isAuthenticated;
    /**
     * How many seconds until the session expires (0 if no session).
     */
    long secondsUntilExpire;

    public SessionState() {
    }

    public SessionState(boolean hasSession, boolean isAuthenticated, long secondsUntilExpire) {
        this.hasSession = hasSession;
        this.isAuthenticated = isAuthenticated;
        this.secondsUntilExpire = secondsUntilExpire;
    }


    public boolean isHasSession() {
        return hasSession;
    }

    public void setHasSession(boolean hasSession) {
        this.hasSession = hasSession;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public long getSecondsUntilExpire() {
        return secondsUntilExpire;
    }

    public void setSecondsUntilExpire(long secondsUntilExpire) {
        this.secondsUntilExpire = secondsUntilExpire;
    }
}