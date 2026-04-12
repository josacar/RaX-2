package rax2;

/**
 * Tracker authentication credentials returned by rssani.listaAuths.
 */
public class TrackerAuth {
    private final String tracker;
    private final String uid;
    private final String pass;
    private final String passkey;

    /**
     * Creates new TrackerAuth.
     * @param tracker tracker URL
     * @param uid user identifier
     * @param pass password hash
     * @param passkey tracker passkey
     */
    public TrackerAuth(String tracker, String uid, String pass, String passkey) {
        this.tracker = tracker;
        this.uid = uid;
        this.pass = pass;
        this.passkey = passkey;
    }

    /**
     * Returns the tracker URL.
     * @return tracker URL
     */
    public String getTracker() { return tracker; }
    /**
     * Returns the user identifier.
     * @return user identifier
     */
    public String getUid() { return uid; }
    /**
     * Returns the password hash.
     * @return password hash
     */
    public String getPass() { return pass; }
    /**
     * Returns the tracker passkey.
     * @return tracker passkey
     */
    public String getPasskey() { return passkey; }
}
