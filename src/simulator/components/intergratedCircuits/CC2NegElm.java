/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.components.intergratedCircuits;

public class CC2NegElm extends CC2Elm {

    public CC2NegElm(int xx, int yy) {
        super(xx, yy, -1);
    }

    public Class getDumpClass() {
        return CC2Elm.class;
    }
}
