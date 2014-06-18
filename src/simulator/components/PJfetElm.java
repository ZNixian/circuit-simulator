/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.components;
public class PJfetElm extends JfetElm {

    public PJfetElm(int xx, int yy) {
        super(xx, yy, true);
    }

    public Class getDumpClass() {
        return JfetElm.class;
    }
}
