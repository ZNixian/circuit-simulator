/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import simulator.cirsim.InitCirSim;

/**
 *
 * @author Campbell Suter
 */
public class SplashScreen {

    private JFrame f;
    private JPanel jp = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(InitCirSim.icon, 0, 0, 200, 200, this);
        }
    };

    private SplashScreen() {
        f = new JFrame();
        f.setUndecorated(true);
        Dimension centere = Toolkit.getDefaultToolkit().getScreenSize();
        centere.height /= 2;
        centere.width /= 2;
        f.setBounds(centere.width - 100, centere.height - 100, 200, 200);
        f.setFocusable(false);
        f.setFocusableWindowState(false);
        f.setContentPane(jp);

        f.setVisible(true);
    }

    public static void showSplash() {
        try {
            SplashScreen ss = new SplashScreen();
            Thread.sleep(5000);
            ss.f.dispose();
        } catch (InterruptedException ex) {
            Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
