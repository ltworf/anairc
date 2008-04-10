/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util.log;

/**
 *
 * @author salvo
 */
public class Logger {
    
    public final static int LOG_DEBUG=1;
    public final static int LOG_INFO=2;
    public final static int LOG_WARN=3;
    public final static int LOG_ERR=4;
    
    
    private static int min=LOG_DEBUG;
    /**
     * Sets the minimum level for a log to be shown
     */
    public static void setMinLogLevel(int level) {
        min=level;
    }
    
    /**
     * Logs a message
     * @param level: level of the log
     * @param message: message to log
     */
    public static void log(int level,String message) {
        if (level>=min)
            System.out.println(message);
    }

}
