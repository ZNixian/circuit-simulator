/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author Campbell Suter
 */
public class CircuitLauncher {

    public static CirSim ogf;

    public static void main(String args[]) {
        if (ArgsUnit.isSplah(args)) {
            SplashScreen.showSplash();
        }
        ogf = new CirSim((Circuit) null);
        ogf.init();
    }
}
