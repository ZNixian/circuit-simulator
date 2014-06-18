/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author Campbell Suter
 */
public class ArgsUnit {

    private ArgsUnit() {
    }

    public static boolean isSplah(String[] args) {
        for (String string : args) {
            if (string.equals("nosplash")) {
                return false;
            }
        }
        return true;
    }
}
