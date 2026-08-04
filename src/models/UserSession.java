package models;

import java.time.Instant;

/**
 * Represents metadata of a user session in the logistics application.
 * Handled via Hash Table Chaining Manager.
 *
 * Time Complexity (Creation/Access): O(1)
 * Space Complexity: O(1)
 */
public class UserSession {
    private String sessionId;
    private String username;
    private String role;
    private Instant loginTime;

    /**
     * Constructs a UserSession instance.
     * @param sessionId Unique session identifier
     * @param username Username of the logged-in user
     * @param role User access control role (e.g., ADMIN, MANAGER)
     */
    public UserSession(String sessionId, String username, String role) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Session ID cannot be null or empty");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        this.sessionId = sessionId;
        this.username = username;
        this.role = role;
        this.loginTime = Instant.now();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Session ID cannot be null or empty");
        }
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        this.role = role;
    }

    public Instant getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSession that = (UserSession) o;
        return sessionId.equals(that.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "SessionID='" + sessionId + '\'' +
                ", Username='" + username + '\'' +
                ", Role='" + role + '\'' +
                ", LoginTime=" + loginTime +
                '}';
    }
}
