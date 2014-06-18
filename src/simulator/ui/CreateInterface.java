/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Scrollbar;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import simulator.CirSim;
import static simulator.CirSim.main;
import simulator.CircuitElm;
import simulator.Scope;
import simulator.cirsim.ComponentType;
import simulator.ui.componentsMenu.ComponentPlacer;
import simulator.ui.componentsMenu.DropdownComponentPlacer;
import simulator.ui.componentsMenu.SidebarComponentPlacer;

/**
 *
 * @author Campbell Suter
 */
public class CreateInterface {

    private CreateInterface() {
    }

    public static void makeMainMenu(CirSim cs, boolean euro, boolean printable, boolean convention) {
        cs.mdc.mainMenu = new JPopupMenu();
        JPopupMenu mainMenu = cs.mdc.mainMenu;
        JMenuBar mb = null;
        if (cs.useFrame) {
            mb = new JMenuBar();
        }
        JMenu m = new JMenu("File");
        if (cs.useFrame) {
            mb.add(m);
        } else {
            cs.mdc.mainMenu.add(m);
        }
        m.add(cs.mdc.openItem = cs.getMenuItem("Open"));
        cs.mdc.openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        m.add(cs.mdc.newItem = cs.getMenuItem("New"));
        cs.mdc.newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        m.add(cs.mdc.saveItem = cs.getMenuItem("Save"));
        cs.mdc.saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        m.add(cs.mdc.saveAsItem = cs.getMenuItem("Save As"));
        cs.mdc.saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        m.add(cs.mdc.exportLinkItem = cs.getMenuItem("Export Link"));
        m.add(cs.mdc.importWebItem = cs.getMenuItem("Import Web"));
        m.addSeparator();
        m.add(cs.mdc.exitItem = cs.getMenuItem("Exit"));

        m = new JMenu("Edit");
        m.add(cs.mdc.undoItem = cs.getMenuItem("Undo"));
        cs.mdc.undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        m.add(cs.mdc.redoItem = cs.getMenuItem("Redo"));
        cs.mdc.redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        m.addSeparator();
        m.add(cs.mdc.cutItem = cs.getMenuItem("Cut"));
        cs.mdc.cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        m.add(cs.mdc.copyItem = cs.getMenuItem("Copy"));
        cs.mdc.copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        m.add(cs.mdc.pasteItem = cs.getMenuItem("Paste"));
        cs.mdc.pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        cs.mdc.pasteItem.setEnabled(false);
        m.add(cs.mdc.selectAllItem = cs.getMenuItem("Select All"));
        cs.mdc.selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        if (cs.useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }

        m = new JMenu("Scope");
        if (cs.useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }
        m.add(cs.getMenuItem("Stack All", "stackAll"));
        m.add(cs.getMenuItem("Unstack All", "unstackAll"));

        cs.mdc.optionsMenu = m = new JMenu("Options");
        if (cs.useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }
        m.add(cs.mdc.dotsCheckItem = cs.getCheckItem("Show Current"));
        m.add(cs.mdc.voltsCheckItem = cs.getCheckItem("Show Voltage"));
        m.add(cs.mdc.powerCheckItem = cs.getCheckItem("Show Power"));
        m.add(cs.mdc.showValuesCheckItem = cs.getCheckItem("Show Values"));
        //m.add(conductanceCheckItem = getCheckItem("Show Conductance"));
        m.add(cs.mdc.smallGridCheckItem = cs.getCheckItem("Small Grid"));
        m.add(cs.mdc.euroResistorCheckItem = cs.getCheckItem("European Resistors"));
        m.add(cs.mdc.printableCheckItem = cs.getCheckItem("White Background"));
        m.add(cs.mdc.conventionCheckItem = cs.getCheckItem("Conventional Current Motion"));
        m.add(cs.mdc.optionsItem = cs.getMenuItem("Other Options..."));
        m.add(cs.mdc.userInterfaceItem = cs.getMenuItem("User Interface Options..."));

        cs.mdc.windowMenu = m = new JMenu("Windows");
        if (cs.useFrame) {
            mb.add(m);
        } else {
            mainMenu.add(m);
        }
        m.add(cs.mdc.projectsWindowItem = cs.getMenuItem("Projects"));
        cs.mdc.projectsWindowItem.addActionListener(cs);

        JMenu circuitsMenu = new JMenu("Circuits");
        if (cs.useFrame) {
            mb.add(circuitsMenu);
        } else {
            mainMenu.add(circuitsMenu);
        }
        createMenus(cs, mainMenu);
        main.add(mainMenu);

        main.add(cs.mdc.resetButton = new Button("Reset"));
        cs.mdc.resetButton.addActionListener(cs);
        cs.mdc.dumpMatrixButton = new Button("Dump Matrix");
        //main.add(dumpMatrixButton);
        cs.mdc.dumpMatrixButton.addActionListener(cs);
        cs.mdc.stoppedCheck = new Checkbox("Stopped");
        cs.mdc.stoppedCheck.addItemListener(cs);
        main.add(cs.mdc.stoppedCheck);

        main.add(new Label("Simulation Speed", Label.CENTER));

        // was max of 140
        main.add(cs.mdc.speedBar = new Scrollbar(Scrollbar.HORIZONTAL, 3, 1, 0, 260));
        cs.mdc.speedBar.addAdjustmentListener(cs);

        main.add(new Label("Current Speed", Label.CENTER));
        cs.mdc.currentBar = new Scrollbar(Scrollbar.HORIZONTAL,
                50, 1, 1, 100);
        cs.mdc.currentBar.addAdjustmentListener(cs);
        main.add(cs.mdc.currentBar);

        main.add(cs.mdc.powerLabel = new Label("Power Brightness", Label.CENTER));
        main.add(cs.mdc.powerBar = new Scrollbar(Scrollbar.HORIZONTAL,
                50, 1, 1, 100));
        cs.mdc.powerBar.addAdjustmentListener(cs);
        cs.mdc.powerBar.disable();
        cs.mdc.powerLabel.disable();

        main.add(new Label("www.falstad.com"));

        if (cs.useFrame) {
            main.add(new Label(""));
        }
        Font f = new Font("SansSerif", 0, 10);
        Label l;
        l = new Label("Current Circuit:");
        l.setFont(f);
        cs.mdc.titleLabel = new Label("Label");
        cs.mdc.titleLabel.setFont(f);
        if (cs.useFrame) {
            main.add(l);
            main.add(cs.mdc.titleLabel);
        }

        cs.setGrid();
        cs.elmList = new Vector<CircuitElm>();
//	setupList = new Vector();
        cs.undoStack = new Vector<String>();
        cs.redoStack = new Vector<String>();

        cs.scopes = new Scope[20];
        cs.scopeColCount = new int[20];
        cs.scopeCount = 0;

        cs.random = new Random();
        cs.cv.setBackground(Color.black);
        cs.cv.setForeground(Color.lightGray);

        cs.mdc.elmMenu = new JPopupMenu();
        cs.mdc.elmMenu.add(cs.mdc.elmEditMenuItem = cs.getMenuItem("Edit"));
        cs.mdc.elmMenu.add(cs.mdc.elmScopeMenuItem = cs.getMenuItem("View in Scope"));
        cs.mdc.elmMenu.add(cs.mdc.elmCutMenuItem = cs.getMenuItem("Cut"));
        cs.mdc.elmMenu.add(cs.mdc.elmCopyMenuItem = cs.getMenuItem("Copy"));
        cs.mdc.elmMenu.add(cs.mdc.elmDeleteMenuItem = cs.getMenuItem("Delete"));
        main.add(cs.mdc.elmMenu);

        cs.mdc.scopeMenu = cs.buildScopeMenu(false);
        cs.mdc.transScopeMenu = cs.buildScopeMenu(true);

        cs.getSetupList(circuitsMenu, false);
        if (cs.useFrame) {
            cs.setJMenuBar(mb);
        }
        if (cs.startCircuitText != null) {
            cs.readSetup(cs.startCircuitText);
        } else if (cs.stopMessage == null && cs.startCircuit != null) {
            cs.readSetupFile(cs.startCircuit, cs.startLabel);
        } else {
            cs.readSetup(null, 0, false);
        }

        if (cs.useFrame) {
            Dimension screen = cs.getToolkit().getScreenSize();
            cs.resize(860, 640);
            cs.handleResize();
            Dimension x = cs.getSize();
            cs.setLocation((screen.width - x.width) / 2,
                    (screen.height - x.height) / 2);
            cs.show();
        } else {
            if (!cs.mdc.powerCheckItem.isSelected()) {
                main.remove(cs.mdc.powerBar);
                main.remove(cs.mdc.powerLabel);
                main.validate();
            }
            cs.hide();
            cs.handleResize();
            cs.applet.validate();
        }

        cs.mdc.conventionCheckItem.setSelected(convention);
        cs.mdc.printableCheckItem.setSelected(printable);
        cs.mdc.euroResistorCheckItem.setSelected(euro);
        cs.mdc.showValuesCheckItem.setSelected(true);
        cs.mdc.voltsCheckItem.setSelected(true);
        cs.mdc.dotsCheckItem.setSelected(true);
    }

    public static void createMenus(CirSim cs, JPopupMenu mainMenu) {
        JMenu passMenu = new JMenu("Passive Components");
        JMenu inputMenu = new JMenu("Inputs/Outputs");
        JMenu activeMenu = new JMenu("Active Components");
        JMenu gateMenu = new JMenu("Logic Gates");
        JMenu chipMenu = new JMenu("Chips");
        JMenu otherMenu = new JMenu("Other");
        boolean sidebar = Boolean.parseBoolean(cs.mdc.cfg.get("ui_components_sidebar"));

        //////////////////////////////////////////////////////////
        JMenu simpleMenu = null;
        boolean simple = Boolean.parseBoolean(cs.mdc.cfg.get("simple"));
        if (simple) {
            simpleMenu = new JMenu("Simple Components");
        }

        ComponentPlacer cp;
        if (sidebar) {
            cp = new SidebarComponentPlacer(simple, cs);
        } else {
            cp = new DropdownComponentPlacer(mainMenu, simpleMenu, passMenu, inputMenu, activeMenu, gateMenu, chipMenu, otherMenu, cs, simple);
        }
        //////////////////////////////////////////////////////////
        cp.addButton("Add Wire", "WireElm", ComponentType.ROOT);
        cp.addButton("Add Resistor", "ResistorElm", ComponentType.ROOT);


        cp.setDefaultType(ComponentType.PASSIVE);
        cp.addButton("Add Capacitor", "CapacitorElm");
        cp.addButton("Add Inductor", "InductorElm");
        cp.addButton("Add Switch", "SwitchElm", true);
        cp.addButton("Add Push Switch", "PushSwitchElm", true);
        cp.addButton("Add SPDT Switch", "Switch2Elm", true);
        cp.addButton("Add Potentiometer", "PotElm");
        cp.addButton("Add Transformer", "TransformerElm");
        cp.addButton("Add Tapped Transformer",
                "TappedTransformerElm");
        cp.addButton("Add Transmission Line", "TransLineElm");
        cp.addButton("Add Relay", "RelayElm");
        cp.addButton("Add Memristor", "MemristorElm");
        cp.addButton("Add Spark Gap", "SparkGapElm");

        cp.setDefaultType(ComponentType.IO);
        cp.addButton("Add Ground", "GroundElm", true);
        cp.addButton("Add Voltage Source (2-terminal)", "DCVoltageElm", true);
        cp.addButton("Add A/C Source (2-terminal)", "ACVoltageElm");
        cp.addButton("Add Voltage Source (1-terminal)", "RailElm");
        cp.addButton("Add A/C Source (1-terminal)", "ACRailElm");
        cp.addButton("Add Square Wave (1-terminal)", "SquareRailElm");
        cp.addButton("Add Analog Output", "OutputElm");
        cp.addButton("Add Logic Input", "LogicInputElm");
        cp.addButton("Add Logic Output", "LogicOutputElm");
        cp.addButton("Add Clock", "ClockElm");
        cp.addButton("Add A/C Sweep", "SweepElm");
        cp.addButton("Add Var. Voltage", "VarRailElm");
        cp.addButton("Add Antenna", "AntennaElm");
        cp.addButton("Add AM source", "AMElm");
        cp.addButton("Add FM source", "FMElm");
        cp.addButton("Add Current Source", "CurrentElm");
        cp.addButton("Add LED", "LEDElm");
        cp.addButton("Add Simple Bulb", "SimpleLampElm", true);
        cp.addButton("Add Lamp (beta)", "LampElm");
        cp.addButton("Add LED Matrix", "LEDMatrixElm");
//        inputMenu.add(getClassCheckItem("Add Microphone Input", "SignalInElm"));
//        inputMenu.add(getClassCheckItem("Add Speaker Output", "SignalOutElm"));

        cp.setDefaultType(ComponentType.ACTIVE);
        cp.addButton("Add Diode", "DiodeElm", true);
        cp.addButton("Add Zener Diode", "ZenerElm");
        cp.addButton("Add Transistor (bipolar, NPN)", "NTransistorElm");
        cp.addButton("Add Transistor (bipolar, PNP)", "PTransistorElm");
        cp.addButton("Add Op Amp (- on top)", "OpAmpElm");
        cp.addButton("Add Op Amp (+ on top)", "OpAmpSwapElm");
        cp.addButton("Add MOSFET (n-channel)", "NMosfetElm");
        cp.addButton("Add MOSFET (p-channel)", "PMosfetElm");
        cp.addButton("Add JFET (n-channel)", "NJfetElm");
        cp.addButton("Add JFET (p-channel)", "PJfetElm");
        cp.addButton("Add Analog Switch (SPST)", "AnalogSwitchElm");
        cp.addButton("Add Analog Switch (SPDT)", "AnalogSwitch2Elm");
        cp.addButton("Add Tristate buffer", "TriStateElm");
        cp.addButton("Add Schmitt Trigger", "SchmittElm");
        cp.addButton("Add Schmitt Trigger (Inverting)", "InvertingSchmittElm");
        cp.addButton("Add SCR", "SCRElm");
        //activeMenu.add(getClassCheckItem("Add Varactor/Varicap", "VaractorElm"));
        cp.addButton("Add Tunnel Diode", "TunnelDiodeElm");
        cp.addButton("Add Triode", "TriodeElm");
        //activeMenu.add(getClassCheckItem("Add Diac", "DiacElm"));
        //activeMenu.add(getClassCheckItem("Add Triac", "TriacElm"));
        //activeMenu.add(getClassCheckItem("Add Photoresistor", "PhotoResistorElm"));
        //activeMenu.add(getClassCheckItem("Add Thermistor", "ThermistorElm"));
        cp.addButton("Add CCII+", "CC2Elm");
        cp.addButton("Add CCII-", "CC2NegElm");

        cp.setDefaultType(ComponentType.LOGIC);
        cp.addButton("Add Inverter", "InverterElm");
        cp.addButton("Add NAND Gate", "NandGateElm");
        cp.addButton("Add NOR Gate", "NorGateElm");
        cp.addButton("Add AND Gate", "AndGateElm");
        cp.addButton("Add OR Gate", "OrGateElm");
        cp.addButton("Add XOR Gate", "XorGateElm");

        cp.setDefaultType(ComponentType.CHIPS);
        cp.addButton("Add D Flip-Flop", "DFlipFlopElm");
        cp.addButton("Add JK Flip-Flop", "JKFlipFlopElm");
        cp.addButton("Add T Flip-Flop", "TFlipFlopElm");
        cp.addButton("Add 7 Segment LED", "SevenSegElm");
        cp.addButton("Add 7 Segment Decoder", "SevenSegDecoderElm");
        cp.addButton("Add Multiplexer", "MultiplexerElm");
        cp.addButton("Add Demultiplexer", "DeMultiplexerElm");
        cp.addButton("Add SIPO shift register", "SipoShiftElm");
        cp.addButton("Add PISO shift register", "PisoShiftElm");
        cp.addButton("Add Phase Comparator", "PhaseCompElm");
        cp.addButton("Add Counter", "CounterElm");
        cp.addButton("Add Decade Counter", "DecadeElm");
        cp.addButton("Add 555 Timer", "TimerElm");
        cp.addButton("Add DAC", "DACElm");
        cp.addButton("Add ADC", "ADCElm");
        cp.addButton("Add Latch", "LatchElm");
        //chipMenu.add(getClassCheckItem("Add Static RAM", "SRAMElm"));
        cp.addButton("Add Sequence generator", "SeqGenElm");
        cp.addButton("Add VCO", "VCOElm");
        cp.addButton("Add Full Adder", "FullAdderElm");
        cp.addButton("Add Half Adder", "HalfAdderElm");

        cp.setDefaultType(ComponentType.OTHER);
        cp.addButton("Add Text", "TextElm");
        cp.addButton("Add Box", "BoxElm");
        cp.addButton("Add Scope Probe", "ProbeElm");
        cp.addCheckButton("Drag All (Alt-drag)", "DragAll");
        cp.addCheckButton("Drag Row (S-right)", "DragRow");
        cp.addCheckButton("Drag Column (C-right)", "DragColumn");
        cp.addCheckButton("Drag Selected", "DragSelected");
        cp.addCheckButton("Drag Post (ctrl-drag)", "DragPost");

        cp.setDefaultType(ComponentType.ROOT);
        cp.addCheckButton("Select/Drag Selected (space or Shift-drag)", "Select");
    }

    public static JPopupMenu buildScopeMenu(CirSim c, boolean t) {
        JPopupMenu m = new JPopupMenu();
        m.add(c.getMenuItem("Remove", "remove"));
        m.add(c.getMenuItem("Speed 2x", "speed2"));
        m.add(c.getMenuItem("Speed 1/2x", "speed1/2"));
        m.add(c.getMenuItem("Scale 2x", "scale"));
        m.add(c.getMenuItem("Max Scale", "maxscale"));
        m.add(c.getMenuItem("Stack", "stack"));
        m.add(c.getMenuItem("Unstack", "unstack"));
        m.add(c.getMenuItem("Reset", "reset"));
        if (t) {
            m.add(c.mdc.scopeIbMenuItem = c.getToggleCheckItem("Show Ib"));
            m.add(c.mdc.scopeIcMenuItem = c.getToggleCheckItem("Show Ic"));
            m.add(c.mdc.scopeIeMenuItem = c.getToggleCheckItem("Show Ie"));
            m.add(c.mdc.scopeVbeMenuItem = c.getToggleCheckItem("Show Vbe"));
            m.add(c.mdc.scopeVbcMenuItem = c.getToggleCheckItem("Show Vbc"));
            m.add(c.mdc.scopeVceMenuItem = c.getToggleCheckItem("Show Vce"));
            m.add(c.mdc.scopeVceIcMenuItem = c.getToggleCheckItem("Show Vce vs Ic"));
        } else {
            m.add(c.mdc.scopeVMenuItem = c.getToggleCheckItem("Show Voltage"));
            m.add(c.mdc.scopeIMenuItem = c.getToggleCheckItem("Show Current"));
            m.add(c.mdc.scopePowerMenuItem = c.getToggleCheckItem("Show Power Consumed"));
            m.add(c.mdc.scopeMaxMenuItem = c.getToggleCheckItem("Show Peak Value"));
            m.add(c.mdc.scopeMinMenuItem = c.getToggleCheckItem("Show Negative Peak Value"));
            m.add(c.mdc.scopeFreqMenuItem = c.getToggleCheckItem("Show Frequency"));
            m.add(c.mdc.scopeVIMenuItem = c.getToggleCheckItem("Show V vs I"));
            m.add(c.mdc.scopeXYMenuItem = c.getToggleCheckItem("Plot X/Y"));
            m.add(c.mdc.scopeSelectYMenuItem = c.getMenuItem("Select Y", "selecty"));
            m.add(c.mdc.scopeResistMenuItem = c.getToggleCheckItem("Show Resistance"));
        }
        return m;
    }
}
