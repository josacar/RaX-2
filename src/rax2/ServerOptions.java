package rax2;

/**
 * Server-side options returned by rssani.verOpciones.
 */
public class ServerOptions {
    private final String fromMail;
    private final String toMail;
    private final String path;

    /**
     * Creates new ServerOptions.
     * @param fromMail sender email address
     * @param toMail recipient email address
     * @param path torrents download path
     */
    public ServerOptions(String fromMail, String toMail, String path) {
        this.fromMail = fromMail;
        this.toMail = toMail;
        this.path = path;
    }

    /**
     * Returns the sender email address.
     * @return sender email address
     */
    public String getFromMail() { return fromMail; }
    /**
     * Returns the recipient email address.
     * @return recipient email address
     */
    public String getToMail() { return toMail; }
    /**
     * Returns the torrents download path.
     * @return torrents download path
     */
    public String getPath() { return path; }
}
