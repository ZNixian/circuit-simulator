/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.cirsim;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URLDecoder;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import simulator.CirSim;
import static simulator.CirSim.main;
import static simulator.CirSim.muString;
import static simulator.CirSim.ohmString;
import static simulator.CirSim.trueMain;
import simulator.CircuitCanvas;
import simulator.CircuitElm;
import simulator.Scope;
import simulator.ui.CreateInterface;
import simulator.ui.componentsMenu.SidebarComponentPlacer;

/**
 *
 * @author Campbell Suter
 */
public class InitCirSim {

    public static final Image icon = Toolkit.getDefaultToolkit().createImage(
            InitCirSim.class.getResource("/icon raw.png"));

    public static void init(final CirSim cs) {
        cs.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        cs.setIconImage(icon);
        String euroResistor = null;
        String useFrameStr = null;
        boolean printable = false;
        boolean convention = true;

        CircuitElm.initClass(cs);

        try {
            cs.baseURL = cs.applet.getDocumentBase().getFile();
            // look for circuit embedded in URL
            String doc = cs.applet.getDocumentBase().toString();
            int in = doc.indexOf('#');
            if (in > 0) {
                String x = null;
                try {
                    x = doc.substring(in + 1);
                    x = URLDecoder.decode(x);
                    cs.startCircuitText = x;
                } catch (Exception e) {
                    System.out.println("can't decode " + x);
                    e.printStackTrace();
                }
            }
            in = doc.lastIndexOf('/');
            if (in > 0) {
                cs.baseURL = doc.substring(0, in + 1);
            }

            String param = cs.applet.getParameter("PAUSE");
            if (param != null) {
                cs.pause = Integer.parseInt(param);
            }
            cs.startCircuit = cs.applet.getParameter("startCircuit");
            cs.startLabel = cs.applet.getParameter("startLabel");
            euroResistor = cs.applet.getParameter("euroResistors");
            useFrameStr = cs.applet.getParameter("useFrame");
            String x = cs.applet.getParameter("whiteBackground");
            if (x != null && x.equalsIgnoreCase("true")) {
                printable = true;
            }
            x = cs.applet.getParameter("conventionalCurrent");
            if (x != null && x.equalsIgnoreCase("true")) {
                convention = false;
            }
        } catch (Exception e) {
        }

        boolean euro = (euroResistor != null && euroResistor.equalsIgnoreCase("true"));
        cs.useFrame = (useFrameStr == null || !useFrameStr.equalsIgnoreCase("false"));
        if (cs.useFrame) {
            cs.main = cs;
        } else {
            cs.main = cs.applet;
        }
        trueMain = main;

        String os = System.getProperty("os.name");
        cs.isMac = (os.indexOf("Mac ") == 0);
        cs.ctrlMetaKey = (cs.isMac) ? "\u2318" : "Ctrl";
        String jv = System.getProperty("java.class.version");
        double jvf = new Double(jv).doubleValue();
        if (jvf >= 48) {
            muString = "\u03bc";
            ohmString = "\u03a9";
            cs.useBufferedImage = true;
        }

        cs.dumpTypes = new Class[300];
        cs.shortcuts = new Class[127];

        // these characters are reserved
        cs.dumpTypes[(int) 'o'] = Scope.class;
        cs.dumpTypes[(int) 'h'] = Scope.class;
        cs.dumpTypes[(int) '$'] = Scope.class;
        cs.dumpTypes[(int) '%'] = Scope.class;
        cs.dumpTypes[(int) '?'] = Scope.class;
        cs.dumpTypes[(int) 'B'] = Scope.class;

        CirSim.main.setLayout(new BorderLayout());//(new CircuitLayout());
        cs.cv = new CircuitCanvas(cs);
        cs.cv.addComponentListener(cs);
        cs.cv.addMouseMotionListener(cs);
        cs.cv.addMouseListener(cs);
        cs.cv.addKeyListener(cs);
        CirSim.main.add(cs.cv);

        if (Boolean.parseBoolean(cs.mdc.cfg.get("ui_components_sidebar"))) {
            JToolBar tb;
            SidebarComponentPlacer.sidebar = tb = new JToolBar("Components");
            tb.setLayout(new BorderLayout());
            tb.setOrientation(JToolBar.VERTICAL);
            main.add(SidebarComponentPlacer.sidebar, BorderLayout.WEST);
        }
        {
            JToolBar newmain = new JToolBar("Tools");
            newmain.setOrientation(JToolBar.VERTICAL);
            newmain.setLayout(new BoxLayout(newmain, BoxLayout.Y_AXIS));
            main.add(newmain, BorderLayout.EAST);
            main = newmain;
        }

        CreateInterface.makeMainMenu(cs, euro, printable, convention);
        cs.requestFocus();

        cs.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                cs.destroyFrame();
            }
        });
    }
}
