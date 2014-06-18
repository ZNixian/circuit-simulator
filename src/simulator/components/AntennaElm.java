package simulator.components;

import java.util.StringTokenizer;

public class AntennaElm extends RailElm {

    public AntennaElm(int xx, int yy) {
        super(xx, yy, WF_DC);
    }

    public AntennaElm(int xa, int ya, int xb, int yb, int f,
            StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        waveform = WF_DC;
    }
    public double fmphase;

    public void stamp() {
        sim.stampVoltageSource(0, nodes[0], voltSource);
    }

    public void doStep() {
        sim.updateVoltageSource(0, nodes[0], voltSource, getVoltage());
    }

    public double getVoltage() {
        fmphase += 2 * pi * (2200 + Math.sin(2 * pi * sim.t * 13) * 100) * sim.timeStep;
        double fm = 3 * Math.sin(fmphase);
        return Math.sin(2 * pi * sim.t * 3000) * (1.3 + Math.sin(2 * pi * sim.t * 12)) * 3
                + Math.sin(2 * pi * sim.t * 2710) * (1.3 + Math.sin(2 * pi * sim.t * 13)) * 3
                + Math.sin(2 * pi * sim.t * 2433) * (1.3 + Math.sin(2 * pi * sim.t * 14)) * 3 + fm;
    }

    public int getDumpType() {
        return 'A';
    }

    public int getShortcut() {
        return 0;
    }
}
