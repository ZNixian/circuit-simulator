package simulator.components;

/**
 *
 * @author Campbell Suter
 */
import simulator.EditInfo;
import java.awt.*;
import java.util.StringTokenizer;
import simulator.CirSim;
import static simulator.CircuitElm.drawThickCircle;
import static simulator.CircuitElm.drawThickLine;
import static simulator.CircuitElm.getUnitText;
import static simulator.CircuitElm.sim;

public class SimpleLampElm extends ResistorElm {

    public Point bulbLead[], filament[], bulb;
    public final int filament_len = 24;
    public int bulbR;

    public SimpleLampElm(int xx, int yy) {
        super(xx, yy);
    }

    public SimpleLampElm(int xa, int ya, int xb, int yb, int f,
            StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    @Override
    public void draw(Graphics g) {
        double v1 = volts[0];
        double v2 = volts[1];
        setBbox(point1, point2, 4);
        adjustBbox(bulb.x - bulbR, bulb.y - bulbR,
                bulb.x + bulbR, bulb.y + bulbR);
        // adjustbbox
        draw2Leads(g);
        setPowerColor(g, true);
        g.setColor(getTempColor());
        g.fillOval(bulb.x - bulbR, bulb.y - bulbR, bulbR * 2, bulbR * 2);
        g.setColor(Color.white);
        drawThickCircle(g, bulb.x, bulb.y, bulbR);
        setVoltageColor(g, v1);
        drawThickLine(g, lead1, filament[0]);
        setVoltageColor(g, v2);
        drawThickLine(g, lead2, filament[1]);
        setVoltageColor(g, (v1 + v2) * .5);
        drawThickLine(g, filament[0], filament[1]);
        updateDotCount();
        if (sim.dragElm != this) {
            drawDots(g, point1, lead1, curcount);
            double cc = curcount + (dn - 16) / 2;
            drawDots(g, lead1, filament[0], cc);
            cc += filament_len;
            drawDots(g, filament[0], filament[1], cc);
            cc += 16;
            drawDots(g, filament[1], lead2, cc);
            cc += filament_len;
            drawDots(g, lead2, point2, curcount);
        }
        drawPosts(g);
    }

    public Color getTempColor() {
        calculateCurrent();
        int h = (int) (255 * 50 * current);
        h = Math.abs(h);
        h = Math.min(h, 255);
        h = Math.max(h, 0);
        return new Color(h, h, h);
    }

    @Override
    public int getDumpType() {
//        String cn = getClass().getName();
//        int hash = 7;
//        for (int i = 0; i < cn.length(); i++) {
//            hash = hash * 31 + cn.charAt(i);
//        }
//        return (byte) hash;
        return 20;
    }

    @Override
    public void setPoints() {
        super.setPoints();
        int llen = 16;
        calcLeads(llen);
        bulbLead = newPointArray(2);
        filament = newPointArray(2);
        bulbR = 20;
        filament[0] = interpPoint(lead1, lead2, 0, filament_len);
        filament[1] = interpPoint(lead1, lead2, 1, filament_len);
        double br = filament_len - Math.sqrt(bulbR * bulbR - llen * llen);
        bulbLead[0] = interpPoint(lead1, lead2, 0, br);
        bulbLead[1] = interpPoint(lead1, lead2, 1, br);
        bulb = interpPoint(filament[0], filament[1], .5);
    }

    @Override
    public void calculateCurrent() {
        current = (volts[0] - volts[1]) / resistance;
        //System.out.print(this + " res current set to " + current + "\n");
    }

    @Override
    public void stamp() {
        sim.stampResistor(nodes[0], nodes[1], resistance);
    }

    @Override
    public void getInfo(String arr[]) {
        arr[0] = "bulb";
        getBasicInfo(arr);
        arr[3] = "R = " + getUnitText(resistance, CirSim.ohmString);
        arr[4] = "P = " + getUnitText(getPower(), "W");
        arr[4] = "V = " + getUnitText(getVoltageDiff(), "V");
    }

    @Override
    public EditInfo getEditInfo(int n) {
        // ohmString doesn't work here on linux
        if (n == 0) {
            return new EditInfo("Resistance (ohms)", resistance, 0, 0);
        }
        return null;
    }

    @Override
    public void setEditValue(int n, EditInfo ei) {
        if (ei.value > 0) {
            resistance = ei.value;
        }
    }

    @Override
    public int getShortcut() {
        return 'b';
    }
}
