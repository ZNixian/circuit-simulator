/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.cirsim;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Label;
import java.awt.Scrollbar;
import java.io.File;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import simulator.ui.projects.ProjectsWindow;

/**
 *
 * @author Campbell Suter
 */
public class MenuDataHolder {

    public Label titleLabel;
    public Button resetButton;
    public Button dumpMatrixButton;
    public JMenuItem saveItem, saveAsItem, exportLinkItem, importWebItem, openItem, newItem, exitItem, undoItem, redoItem,
            cutItem, copyItem, pasteItem, selectAllItem, optionsItem, userInterfaceItem;
    public JMenu optionsMenu, windowMenu;
    public Checkbox stoppedCheck;
    public JRadioButtonMenuItem dotsCheckItem;
    public JRadioButtonMenuItem voltsCheckItem;
    public JRadioButtonMenuItem powerCheckItem;
    public JRadioButtonMenuItem smallGridCheckItem;
    public JRadioButtonMenuItem showValuesCheckItem;
    public JRadioButtonMenuItem conductanceCheckItem;
    public JRadioButtonMenuItem euroResistorCheckItem;
    public JRadioButtonMenuItem printableCheckItem;
    public JRadioButtonMenuItem conventionCheckItem;
    public Scrollbar speedBar;
    public Scrollbar currentBar;
    public Label powerLabel;
    public Scrollbar powerBar;
    public JPopupMenu elmMenu;
    public JMenuItem elmEditMenuItem;
    public JMenuItem elmCutMenuItem;
    public JMenuItem elmCopyMenuItem;
    public JMenuItem elmDeleteMenuItem;
    public JMenuItem elmScopeMenuItem;
    public JPopupMenu scopeMenu;
    public JPopupMenu transScopeMenu;
    public JPopupMenu mainMenu;
    public JCheckBoxMenuItem scopeVMenuItem;
    public JCheckBoxMenuItem scopeIMenuItem;
    public JCheckBoxMenuItem scopeMaxMenuItem;
    public JCheckBoxMenuItem scopeMinMenuItem;
    public JCheckBoxMenuItem scopeFreqMenuItem;
    public JCheckBoxMenuItem scopePowerMenuItem;
    public JCheckBoxMenuItem scopeIbMenuItem;
    public JCheckBoxMenuItem scopeIcMenuItem;
    public JCheckBoxMenuItem scopeIeMenuItem;
    public JCheckBoxMenuItem scopeVbeMenuItem;
    public JCheckBoxMenuItem scopeVbcMenuItem;
    public JCheckBoxMenuItem scopeVceMenuItem;
    public JCheckBoxMenuItem scopeVIMenuItem;
    public JCheckBoxMenuItem scopeXYMenuItem;
    public JCheckBoxMenuItem scopeResistMenuItem;
    public JCheckBoxMenuItem scopeVceIcMenuItem;
    public JMenuItem scopeSelectYMenuItem;
    /////////////////////// WINDOW MENU ////////////////
    public JMenuItem projectsWindowItem;
    ///////////////////////
    public File loaded;
    public Config cfg;
    public ProjectsWindow projectsWindow;
}
