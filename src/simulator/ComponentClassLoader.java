/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author Campbell Suter
 */
public class ComponentClassLoader {

    public static final String[] PACKAGES = {
        "simulator.components",
        "simulator.components.intergratedCircuits",};

    public static Class getComponentClass(String componentClass) {
        for (String packageName : PACKAGES) {
            try {
                Class c = Class.forName(packageName + "." + componentClass);
                return c;
            } catch (ClassNotFoundException ex) {
            }
        }
        return null;
    }
}
