package models;

/**
 * Represents an active User Session in PrimeLogix Logistics & Management System.
 * To be stored inside HashTableManager mapping Session ID to UserSession metadata.
 *
 * @author Senior Java Software Architect
 */
public class UserSession {
    private final String sessionId;
    private final String username;
    private final String role;
    private final long loginTimestamp;

    /**
     * UserSession constructor.
     * @param sessionId Session UID.
     * @param username Authorized employee name.
     * @param role Level of authority (e.g. Administrator, Warehouse Clerk, Logistics Coordinator).
     */
    public UserSession(String sessionId, String username, String role) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Session ID cannot be empty.");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.sessionId = sessionId.trim();
        this.username = username.trim();
        this.role = role != null ? role.trim() : "Standard User";
        this.loginTimestamp = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public long getLoginTimestamp() {
        return loginTimestamp;
    }

    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(new java.util.Date(loginTimestamp));
        return String.format("UserSession[ID=%s, User=%s, Role=%s, LoginAt=%s]",
                sessionId, username, role, formattedDate);
    }
}
