/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui.componentsMenu;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Panel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import simulator.CirSim;
import simulator.CircuitElm;
import simulator.ComponentClassLoader;
import simulator.cirsim.ComponentType;
import static simulator.cirsim.ComponentType.ACTIVE;
import static simulator.cirsim.ComponentType.CHIPS;
import static simulator.cirsim.ComponentType.IO;
import static simulator.cirsim.ComponentType.LOGIC;
import static simulator.cirsim.ComponentType.OTHER;
import static simulator.cirsim.ComponentType.PASSIVE;
import static simulator.cirsim.ComponentType.ROOT;

/**
 *
 * @author Campbell Suter
 */
public class SidebarComponentPlacer implements ComponentPlacer {

    private final Container simpleMenu;
    private final Container passMenu;
    private final Container ioMenu;
    private final Container activeMenu;
    private final Container logicMenu;
    private final Container chipsMenu;
    private final Container otherMenu;
    private final Container mainMenu;
    ////////////
    private final JTabbedPane tabbedPane;
    private final Panel tabbedPane_panel;
    ////////////
    private final ButtonGroup buttons;
    private final boolean simpleMode;
    private final CirSim cs;
    private ComponentType defaultType;
    ///////////
    public static Container sidebar;

    public SidebarComponentPlacer(boolean simpleMode, CirSim cs) {
        this.simpleMode = simpleMode;

        mainMenu = newPanel();
        sidebar.add(mainMenu, BorderLayout.NORTH);

        tabbedPane_panel = new Panel();
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        simpleMenu = newPanel();
        tabbedPane.addTab("Simple", simpleMenu);
        passMenu = newPanel();
        tabbedPane.addTab("Passive", passMenu);
        ioMenu = newPanel();
        tabbedPane.addTab("Input/Output", ioMenu);
        activeMenu = newPanel();
        tabbedPane.addTab("Active", activeMenu);
        logicMenu = newPanel();
        tabbedPane.addTab("Logic", logicMenu);
        chipsMenu = newPanel();
        tabbedPane.addTab("Chips", chipsMenu);
        otherMenu = newPanel();
        tabbedPane.addTab("Other", otherMenu);

        tabbedPane_panel.add(tabbedPane);
        sidebar.add(tabbedPane_panel);

        buttons = new ButtonGroup();

        this.cs = cs;
    }

    private Panel newPanel() {
        Panel p = new Panel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    public JRadioButton selected(CirSim cs, String title, String className) {
        JRadioButton but = null;
        try {
//            System.out.println("ok: " + t);
            Class c = ComponentClassLoader.getComponentClass(className);
            CircuitElm elm = cs.constructElement(c, 0, 0);
            cs.register(c, elm);
            if (elm.needsShortcut()) {
                title += " (" + (char) elm.getShortcut() + ")";
            }
            elm.delete();


            but = new JRadioButton(title);
            but.addItemListener(cs);
            but.setActionCommand(className);
            buttons.add(but);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Exception loading class:", ex);
        }
        return but;
    }

    private void addItem(String title, String name, ComponentType type, boolean isSimple, JComponent classCheckItem) {
        if (isSimple && simpleMode) {
            simpleMenu.add(classCheckItem);
            return;
        }
        if (type == ComponentType.ROOT && simpleMode) {
            simpleMenu.add(classCheckItem);
            return;
        }
        switch (type) {
            case ACTIVE:
                activeMenu.add(classCheckItem);
                break;
            case CHIPS:
                chipsMenu.add(classCheckItem);
                break;
            case IO:
                ioMenu.add(classCheckItem);
                break;
            case LOGIC:
                logicMenu.add(classCheckItem);
                break;
            case OTHER:
                otherMenu.add(classCheckItem);
                break;
            case PASSIVE:
                passMenu.add(classCheckItem);
                break;
            case ROOT:
                mainMenu.add(classCheckItem);
                mainMenu.revalidate();
                break;
        }
        tabbedPane.revalidate();
        sidebar.revalidate();
    }

    @Override
    public void addButton(String title, String name, ComponentType type, boolean isSimple) {
        JRadioButton classCheckItem = selected(cs, title, name);
        addItem(title, name, type, isSimple, classCheckItem);
    }

    @Override
    public void addButton(String title, String name) {
        addButton(title, name, defaultType);
    }

    @Override
    public void setDefaultType(ComponentType type) {
        defaultType = type;
    }

    @Override
    public void addButton(String title, String name, boolean isSimple) {
        addButton(title, name, defaultType, isSimple);
    }

    @Override
    public void addButton(String title, String name, ComponentType type) {
        addButton(title, name, type, false);
    }

    @Override
    public void addCheckButton(String title, String name) {
        addCheckButton(title, name, false);
    }

    @Override
    public void addCheckButton(String title, String name, boolean simple) {
        JRadioButton classCheckItem = null;
        try {
            classCheckItem = new JRadioButton(title);
            classCheckItem.addItemListener(cs);
            classCheckItem.setActionCommand(name);
            buttons.add(classCheckItem);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Exception loading class:", ex);
        }
        addItem(title, name, defaultType, false, classCheckItem);
    }
}
