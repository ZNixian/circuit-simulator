/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.cirsim;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import simulator.CirSim;
import static simulator.CirSim.MODE_DRAG_COLUMN;
import static simulator.CirSim.MODE_DRAG_POST;
import static simulator.CirSim.MODE_DRAG_ROW;
import static simulator.CirSim.MODE_DRAG_SELECTED;
import simulator.CircuitElm;
import simulator.CircuitNode;
import simulator.CircuitNodeLink;
import simulator.components.GraphicElm;

/**
 *
 * @author Campbell Suter
 */
public class PainterUpdater {

    public void updateCircuit(CirSim cs, Graphics realg) {
        CircuitElm realMouseElm;
        if (cs.winSize == null || cs.winSize.width == 0) {
            return;
        }
        if (cs.analyzeFlag) {
            cs.analyzeCircuit();
            cs.analyzeFlag = false;
        }
        if (cs.editDialog != null && cs.editDialog.elm instanceof CircuitElm) {
            cs.mouseElm = (CircuitElm) (cs.editDialog.elm);
        }
        realMouseElm = cs.mouseElm;
        if (cs.mouseElm == null) {
            cs.mouseElm = cs.stopElm;
        }
        cs.setupScopes();
        Graphics2D g = null; // hausen: changed to Graphics2D
        g = (Graphics2D) cs.dbimage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        CircuitElm.selectColor = Color.cyan;
        if (cs.mdc.printableCheckItem.isSelected()) {
            CircuitElm.whiteColor = Color.black;
            CircuitElm.lightGrayColor = Color.black;
            g.setColor(Color.white);
        } else {
            CircuitElm.whiteColor = Color.white;
            CircuitElm.lightGrayColor = Color.lightGray;
            g.setColor(Color.black);
        }
        g.fillRect(0, 0, cs.winSize.width, cs.winSize.height);
        if (!cs.mdc.stoppedCheck.getState()) {
            try {
                cs.runCircuit();
            } catch (Exception e) {
                e.printStackTrace();
                cs.analyzeFlag = true;
                cs.cv.repaint();
                return;
            }
        }
        if (!cs.mdc.stoppedCheck.getState()) {
            long sysTime = System.currentTimeMillis();
            if (cs.lastTime != 0) {
                int inc = (int) (sysTime - cs.lastTime);
                double c = cs.mdc.currentBar.getValue();
                c = java.lang.Math.exp(c / 3.5 - 14.2);
                CircuitElm.currentMult = 1.7 * inc * c;
                if (!cs.mdc.conventionCheckItem.isSelected()) {
                    CircuitElm.currentMult = -CircuitElm.currentMult;
                }
            }
            if (sysTime - cs.secTime >= 1000) {
                cs.framerate = cs.frames;
                cs.steprate = cs.steps;
                cs.frames = 0;
                cs.steps = 0;
                cs.secTime = sysTime;
            }
            cs.lastTime = sysTime;
        } else {
            cs.lastTime = 0;
        }
        CircuitElm.powerMult = Math.exp(cs.mdc.powerBar.getValue() / 4.762 - 7);

        int i;
        Font oldfont = g.getFont();
        for (i = 0; i != cs.elmList.size(); i++) {
            if (cs.mdc.powerCheckItem.isSelected()) {
                g.setColor(Color.gray);
            }
            /*else if (conductanceCheckItem.getState())
             g.setColor(Color.white);*/
            cs.getElm(i).draw(g);
        }
        if (cs.tempMouseMode == MODE_DRAG_ROW || cs.tempMouseMode == MODE_DRAG_COLUMN
                || cs.tempMouseMode == MODE_DRAG_POST || cs.tempMouseMode == MODE_DRAG_SELECTED) {
            for (i = 0; i != cs.elmList.size(); i++) {
                CircuitElm ce = cs.getElm(i);
                ce.drawPost(g, ce.x, ce.y);
                ce.drawPost(g, ce.x2, ce.y2);
            }
        }
        int badnodes = 0;
        // find bad connections, nodes not connected to other elements which
        // intersect other elements' bounding boxes
        // debugged by hausen: nullPointerException
        if (cs.nodeList != null) {
            for (i = 0; i != cs.nodeList.size(); i++) {
                CircuitNode cn = cs.getCircuitNode(i);
                if (!cn.internal && cn.links.size() == 1) {
                    int bb = 0, j;
                    CircuitNodeLink cnl = cn.links.elementAt(0);
                    for (j = 0; j != cs.elmList.size(); j++) { // TODO: (hausen) see if this change does not break stuff
                        CircuitElm ce = cs.getElm(j);
                        if (ce instanceof GraphicElm) {
                            continue;
                        }
                        if (cnl.elm != ce
                                && cs.getElm(j).boundingBox.contains(cn.x, cn.y)) {
                            bb++;
                        }
                    }
                    if (bb > 0) {
                        g.setColor(Color.red);
                        g.fillOval(cn.x - 3, cn.y - 3, 7, 7);
                        badnodes++;
                    }
                }
            }
        }
        /*if (mouseElm != null) {
         g.setFont(oldfont);
         g.drawString("+", mouseElm.x+10, mouseElm.y);
         }*/
        if (cs.dragElm != null
                && (cs.dragElm.x != cs.dragElm.x2 || cs.dragElm.y != cs.dragElm.y2)) {
            cs.dragElm.draw(g);
        }
        g.setFont(oldfont);
        int ct = cs.scopeCount;
        if (cs.stopMessage != null) {
            ct = 0;
        }
        for (i = 0; i != ct; i++) {
            cs.scopes[i].draw(g);
        }
        g.setColor(CircuitElm.whiteColor);
        if (cs.stopMessage != null) {
            g.drawString(cs.stopMessage, 10, cs.circuitArea.height);
        } else {
            if (cs.circuitBottom == 0) {
                cs.calcCircuitBottom();
            }
            String info[] = new String[10];
            if (cs.mouseElm != null) {
                if (cs.mousePost == -1) {
                    cs.mouseElm.getInfo(info);
                } else {
                    info[0] = "V = "
                            + CircuitElm.getUnitText(cs.mouseElm.getPostVoltage(cs.mousePost), "V");
                }
                /* //shownodes
                 for (i = 0; i != mouseElm.getPostCount(); i++)
                 info[0] += " " + mouseElm.nodes[i];
                 if (mouseElm.getVoltageSourceCount() > 0)
                 info[0] += ";" + (mouseElm.getVoltageSource()+nodeList.size());
                 */

            } else {
                CircuitElm.showFormat.setMinimumFractionDigits(2);
                info[0] = "t = " + CircuitElm.getUnitText(cs.t, "s");
                CircuitElm.showFormat.setMinimumFractionDigits(0);
            }
            if (cs.hintType != -1) {
                for (i = 0; info[i] != null; i++)
		    ;
                String s = cs.getHint();
                if (s == null) {
                    cs.hintType = -1;
                } else {
                    info[i] = s;
                }
            }
            int x = 0;
            if (ct != 0) {
                x = cs.scopes[ct - 1].rightEdge() + 20;
            }
            x = cs.max(x, cs.winSize.width * 2 / 3);

            // count lines of data
            for (i = 0; info[i] != null; i++)
		;
            if (badnodes > 0) {
                info[i++] = badnodes + ((badnodes == 1)
                        ? " bad connection" : " bad connections");
            }

            // find where to show data; below circuit, not too high unless we need it
            int ybase = cs.winSize.height - 15 * i - 5;
            ybase = cs.min(ybase, cs.circuitArea.height);
            ybase = cs.max(ybase, cs.circuitBottom);
            for (i = 0; info[i] != null; i++) {
                g.drawString(info[i], x,
                        ybase + 15 * (i + 1));
            }
        }
        if (cs.selectedArea != null) {
            g.setColor(CircuitElm.selectColor);
            g.drawRect(cs.selectedArea.x, cs.selectedArea.y, cs.selectedArea.width, cs.selectedArea.height);
        }
        cs.mouseElm = realMouseElm;
        cs.frames++;
        /*
         g.setColor(Color.white);
         g.drawString("Framerate: " + framerate, 10, 10);
         g.drawString("Steprate: " + steprate,  10, 30);
         g.drawString("Steprate/iter: " + (steprate/getIterCount()),  10, 50);
         g.drawString("iterc: " + (getIterCount()),  10, 70);
         */

        realg.drawImage(cs.dbimage, 0, 0, cs);
        if (!cs.mdc.stoppedCheck.getState() && cs.circuitMatrix != null) {
            // Limit to 50 fps (thanks to Jurgen Klotzer for this)
            long delay = 1000 / 50 - (System.currentTimeMillis() - cs.lastFrameTime);
            //realg.drawString("delay: " + delay,  10, 90);
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                }
            }

            cs.cv.repaint(0);
        }
        cs.lastFrameTime = cs.lastTime;
    }
}
