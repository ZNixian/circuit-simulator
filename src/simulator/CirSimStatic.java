/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author Campbell Suter
 */
public class CirSimStatic {

    public static int getNextFreeDumpType(CirSim cs) {
        int i = 0;
        for (Class class1 : cs.dumpTypes) {
            System.out.println(class1);
            if (class1 == null) {
                return i;
            }
            i++;
        }
        return 0;
    }
}
