/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.cirsim;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import simulator.CirSim;
import static simulator.CirSim.expDialog;
import simulator.CircuitElm;
import simulator.ImportExportDialog;
import simulator.ImportExportDialogFactory;
import simulator.ImportExportFileDialog;
import simulator.Scope;

/**
 *
 * @author Campbell Suter
 */
public class FileIO {

    public static void save(CirSim cs, String dump, boolean as) {
        if (expDialog == null) {
            expDialog = ImportExportDialogFactory.Create(cs,
                    ImportExportDialog.Action.SAVE);
//	    expDialog = new ImportExportClipboardDialog(this,
//		 ImportExportDialog.Action.EXPORT);
        }
        expDialog.setDump(dump);
        if (as || cs.mdc.loaded == null) {
            expDialog.execute();
        } else {
            ((ImportExportFileDialog) expDialog).save(cs.mdc.loaded.getAbsolutePath());
        }
    }

    public static boolean promptForRead(CirSim cs) {
        int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Do You Want To Save?", "Save Confirm?", JOptionPane.YES_NO_CANCEL_OPTION);
        switch (showConfirmDialog) {
            case JOptionPane.CANCEL_OPTION:
                return false;
            case JOptionPane.YES_OPTION:
                save(cs, cs.dumpCircuit(), false);
                break;
        }
        return true;
    }

    public static void readSetupAndPrompt(CirSim cs, byte b[], int len, boolean retain) {
        if (promptForRead(cs)) {
            readSetup(cs, b, len, retain);
        }
    }

    public static void readSetup(CirSim cs, byte b[], int len, boolean retain) {
        int i;
        if (!retain) {
            for (i = 0; i != cs.elmList.size(); i++) {
                CircuitElm ce = cs.getElm(i);
                ce.delete();
            }
            cs.elmList.removeAllElements();
            cs.hintType = -1;
            cs.timeStep = 5e-6;
            cs.mdc.dotsCheckItem.setSelected(false);
            cs.mdc.smallGridCheckItem.setSelected(false);
            cs.mdc.powerCheckItem.setSelected(false);
            cs.mdc.voltsCheckItem.setSelected(true);
            cs.mdc.showValuesCheckItem.setSelected(true);
            cs.setGrid();
            cs.mdc.speedBar.setValue(117); // 57
            cs.mdc.currentBar.setValue(50);
            cs.mdc.powerBar.setValue(50);
            CircuitElm.voltageRange = 5;
            cs.scopeCount = 0;
        }
        cs.cv.repaint();
        int p;
        for (p = 0; p < len;) {
            int l;
            int linelen = 0;
            for (l = 0; l != len - p; l++) {
                if (b[l + p] == '\n' || b[l + p] == '\r') {
                    linelen = l++;
                    if (l + p < b.length && b[l + p] == '\n') {
                        l++;
                    }
                    break;
                }
            }
            String line = new String(b, p, linelen);
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                String type = st.nextToken();
                int tint = type.charAt(0);
                try {
                    if (tint == 'o') {
                        Scope sc = new Scope(cs);
                        sc.position = cs.scopeCount;
                        sc.undump(st);
                        cs.scopes[cs.scopeCount++] = sc;
                        break;
                    }
                    if (tint == 'h') {
                        cs.readHint(st);
                        break;
                    }
                    if (tint == '$') {
                        cs.readOptions(st);
                        break;
                    }
                    if (tint == '%' || tint == '?' || tint == 'B') {
                        // ignore afilter-specific stuff
                        break;
                    }
                    if (tint >= '0' && tint <= '9') {
                        tint = new Integer(type).intValue();
                    }
                    int x1 = new Integer(st.nextToken()).intValue();
                    int y1 = new Integer(st.nextToken()).intValue();
                    int x2 = new Integer(st.nextToken()).intValue();
                    int y2 = new Integer(st.nextToken()).intValue();
                    int f = new Integer(st.nextToken()).intValue();
                    CircuitElm ce = null;
                    Class cls = cs.dumpTypes[tint];
                    if (cls == null) {
                        System.out.println("unrecognized dump type: " + type);
                        break;
                    }
                    // find element class
                    Class carr[] = new Class[6];
                    //carr[0] = getClass();
                    carr[0] = carr[1] = carr[2] = carr[3] = carr[4] =
                            int.class;
                    carr[5] = StringTokenizer.class;
                    Constructor cstr = null;
                    cstr = cls.getConstructor(carr);

                    // invoke constructor with starting coordinates
                    Object oarr[] = new Object[6];
                    //oarr[0] = this;
                    oarr[0] = new Integer(x1);
                    oarr[1] = new Integer(y1);
                    oarr[2] = new Integer(x2);
                    oarr[3] = new Integer(y2);
                    oarr[4] = new Integer(f);
                    oarr[5] = st;
                    ce = (CircuitElm) cstr.newInstance(oarr);
                    ce.setPoints();
                    cs.elmList.addElement(ce);
                } catch (java.lang.reflect.InvocationTargetException ee) {
                    ee.getTargetException().printStackTrace();
                    break;
                } catch (Exception ee) {
                    ee.printStackTrace();
                    break;
                }
                break;
            }
            p += l;

        }
        cs.enableItems();
        if (!retain) {
            cs.handleResize(); // for scopes
        }
        cs.needAnalyze();
    }
    
    public static void load(String fi, CirSim cirsim) {
        try {
            String dump = ImportExportFileDialog.readFile(fi);
            FileIO.readSetupAndPrompt(cirsim, dump.getBytes(), dump.length(), false);
            cirsim.mdc.loaded = new File(fi);
        } catch (Exception ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, "Exception: {0}", ex.toString());
        }
    }
}
