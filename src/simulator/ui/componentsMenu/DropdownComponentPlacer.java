/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui.componentsMenu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import simulator.CirSim;
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
public class DropdownComponentPlacer implements ComponentPlacer {

    private final JPopupMenu rootMenu;
    private final JMenu simpleMenu;
    private final JMenu passMenu;
    private final JMenu ioMenu;
    private final JMenu activeMenu;
    private final JMenu logicMenu;
    private final JMenu chipsMenu;
    private final JMenu otherMenu;
    private final CirSim cs;
    private final boolean simpleMode;
    private ComponentType defaultType;

    public DropdownComponentPlacer(JPopupMenu rootMenu, JMenu simpleMenu, JMenu passMenu, JMenu ioMenu, JMenu activeMenu, JMenu logicMenu, JMenu chipsMenu, JMenu otherMenu, CirSim cs, boolean simpleMode) {
        this.rootMenu = rootMenu;
        this.simpleMenu = simpleMenu;
        if (simpleMenu != null) {
            rootMenu.add(simpleMenu);
        }
        this.passMenu = passMenu;
        rootMenu.add(passMenu);
        this.ioMenu = ioMenu;
        rootMenu.add(ioMenu);
        this.activeMenu = activeMenu;
        rootMenu.add(activeMenu);
        this.logicMenu = logicMenu;
        rootMenu.add(logicMenu);
        this.chipsMenu = chipsMenu;
        rootMenu.add(chipsMenu);
        this.otherMenu = otherMenu;
        rootMenu.add(otherMenu);
        this.cs = cs;
        this.simpleMode = simpleMode;
        if (simpleMode) {
            rootMenu.add(simpleMenu);
        }
    }

    private void addItem(String title, String name, ComponentType type, boolean isSimple, JMenuItem classCheckItem) {
        if (isSimple && simpleMode) {
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
                rootMenu.add(classCheckItem);
                break;
        }
    }

    @Override
    public void addButton(String title, String name, ComponentType type, boolean isSimple) {
        JRadioButtonMenuItem classCheckItem = cs.getClassCheckItem(title, name);
        addItem(title, name, type, isSimple, classCheckItem);
    }

    @Override
    public void addButton(String title, String name, ComponentType type) {
        addButton(title, name, type, false);
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
    public void addCheckButton(String title, String name) {
        addCheckButton(title, name, false);
    }

    @Override
    public void addCheckButton(String title, String name, boolean simple) {
        JRadioButtonMenuItem checkItem = cs.getCheckItem(title, name);
        addItem(title, name, defaultType, simple, checkItem);
    }
}
