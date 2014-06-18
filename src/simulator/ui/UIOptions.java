package simulator.ui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import simulator.*;

public class UIOptions implements ActionListener {

    private CirSim sim;
    private JFrame window;
    private Button okButton; //, applyButton;
    private JPanel cp;
    ///////////////////////////////
    private JPanel panelocation_menu;
    private ButtonGroup panelocation_group;
    private JRadioButton panelocation_dropdown, panelocation_sidebar;
    private JCheckBox simplemenus_button;

    public UIOptions(CirSim s) {
        sim = s;
        window = new JFrame("UI Settings");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        //////////////////////////////////////////
        panelocation_menu = new JPanel();
        panelocation_group = new ButtonGroup();
        panelocation_menu.setLayout(new BoxLayout(panelocation_menu, BoxLayout.X_AXIS));
        panelocation_menu.add(panelocation_dropdown = new JRadioButton("Dropdown menu"));
        panelocation_group.add(panelocation_dropdown);
        panelocation_dropdown.addActionListener(this);
        panelocation_menu.add(panelocation_sidebar = new JRadioButton("Sidebar"));
        panelocation_group.add(panelocation_sidebar);
        panelocation_sidebar.addActionListener(this);
        if ("true".equals(sim.mdc.cfg.get("ui_components_sidebar"))) {
            panelocation_sidebar.setSelected(true);
        } else {
            panelocation_dropdown.setSelected(true);
        }
        /////////
        panelocation_menu.setBackground(Color.black);
        cp.add(panelocation_menu);
        //////////////////////////////////////////
        simplemenus_button = new JCheckBox("Simple Menus");
        simplemenus_button.setSelected(Boolean.parseBoolean(sim.mdc.cfg.get("simple")));
        ///////
        cp.add(simplemenus_button);
        //////////////////////////////////////////

//        cp.add(applyButton = new Button("Apply"));
//        applyButton.addActionListener(this);
        cp.add(okButton = new Button("OK - requires restart"));
        okButton.addActionListener(this);
        ////////
        window.setContentPane(cp);
        window.pack();
        window.setVisible(true);
        Point x = CirSim.trueMain.getLocationOnScreen();
        Dimension d = window.getSize();
        window.setLocation(x.x + (sim.winSize.width - d.width) / 2,
                x.y + (sim.winSize.height - d.height) / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            sim.mdc.cfg.set("ui_components_sidebar", "" + panelocation_sidebar.isSelected());
            sim.mdc.cfg.set("simple", "" + simplemenus_button.isSelected());
        }
    }
}