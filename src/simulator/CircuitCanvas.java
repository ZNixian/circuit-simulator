package simulator;

import java.awt.*;

public class CircuitCanvas extends Canvas {

    public CirSim pg;

    public CircuitCanvas(CirSim p) {
        pg = p;
    }

    public Dimension getPreferredSize() {
        return new Dimension(300, 400);
    }

    public void update(Graphics g) {
        pg.updateCircuit(g);
    }

    public void paint(Graphics g) {
        pg.updateCircuit(g);
    }

    @Override
    public void setBackground(Color c) {
        super.setBackground(c); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c); //To change body of generated methods, choose Tools | Templates.
    }
};
