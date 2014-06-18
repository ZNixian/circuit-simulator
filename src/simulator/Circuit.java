package simulator;

// Circuit.java (c) 2005,2008 by Paul Falstad, www.falstad.com
import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;

public class Circuit extends Applet implements ComponentListener {

    public static CirSim ogf;
    public boolean finished = false;

    public void destroyFrame() {
        if (ogf != null) {
            ogf.dispose();
        }
        ogf = null;
        repaint();
        finished = true;
    }
    public boolean started = false;

    @Override
    public void init() {
        addComponentListener(this);
    }

    public static void main(String args[]) {
        if (ArgsUnit.isSplah(args)) {
            SplashScreen.showSplash();
        }
        ogf = new CirSim(null);
        ogf.init();
    }

    public void showFrame() {
        if (finished) {
            repaint();
            return;
        }
//        if (ogf == null) {
//            started = true;
//            ogf = new CirSim(this);
//            ogf.init();
//        }
        ogf.setVisible(true);
        repaint();
    }

    public void hideFrame() {
        if (finished) {
            return;
        }
        ogf.setVisible(false);
        repaint();
    }

    public void toggleSwitch(int x) {
        ogf.toggleSwitch(x);
    }

    @Override
    public void paint(Graphics g) {
        String s = "Applet is open in a separate window.";
        if (ogf != null && !ogf.isVisible()) {
            s = "Applet window is hidden.";
        }
        if (!started) {
            s = "Applet is starting.";
        } else if (ogf == null || finished) {
            s = "Applet is finished.";
        } else if (ogf != null && ogf.useFrame) {
            ogf.triggerShow();
        }
        g.drawString(s, 10, 30);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
        showFrame();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (ogf != null) {
            ogf.componentResized(e);
        }
    }

    @Override
    public void destroy() {
        if (ogf != null) {
            ogf.dispose();
        }
        ogf = null;
        repaint();
    }
};
