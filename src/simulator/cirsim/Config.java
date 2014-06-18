/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.cirsim;

import com.sun.istack.internal.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import simulator.CirSim;

/**
 *
 * @author Campbell Suter
 */
public class Config {

    private Properties prop = new Properties();
    private File configFile = new File("config.properties");
    private CirSim cs;

    public Config(CirSim cs) {
        this.cs = cs;
        try {
            InputStream input;
            input = new FileInputStream(configFile);
            prop.load(input);
        } catch (IOException ex) {
            Logger.getLogger(getClass()).log(Level.SEVERE, "Exception: " + ex.toString());
        }
    }

    public String get(String key) {
        String val = prop.getProperty(key);
        return val;
    }

    public void set(String key, String val) {
        prop.remove(key);
        prop.setProperty(key, val);
        try {
            FileOutputStream output = new FileOutputStream(configFile);
            prop.store(output, null);
            Logger.getLogger(getClass()).log(Level.INFO, "Stored.");
        } catch (IOException ex) {
            Logger.getLogger(getClass()).log(Level.SEVERE, "Exception: " + ex.toString());
        }
    }
}
