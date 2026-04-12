package rax2;

/**
 * Represents an RSS expression (rule) from the rssani server.
 */
public class Expresion {
    private final String nombre;
    private final String vencimiento;
    private final boolean mail;
    private final String tracker;
    private final int dias;
    private final boolean activa;

    /**
     * Creates a new Expresion.
     * @param nombre rule name
     * @param vencimiento expiration date string
     * @param mail whether email notifications are enabled
     * @param tracker tracker URL
     * @param dias number of days
     * @param activa whether the rule is active
     */
    public Expresion(String nombre, String vencimiento, boolean mail,
                     String tracker, int dias, boolean activa) {
        this.nombre = nombre;
        this.vencimiento = vencimiento;
        this.mail = mail;
        this.tracker = tracker;
        this.dias = dias;
        this.activa = activa;
    }

    /**
     * Returns the rule name.
     * @return rule name
     */
    public String getNombre() { return nombre; }
    /**
     * Returns the expiration date string.
     * @return expiration date string
     */
    public String getVencimiento() { return vencimiento; }
    /**
     * Returns whether email notifications are enabled.
     * @return true if email notifications are enabled
     */
    public boolean isMail() { return mail; }
    /**
     * Returns the tracker URL.
     * @return tracker URL
     */
    public String getTracker() { return tracker; }
    /**
     * Returns the number of days.
     * @return number of days
     */
    public int getDias() { return dias; }
    /**
     * Returns whether the rule is active.
     * @return true if the rule is active
     */
    public boolean isActiva() { return activa; }
}
