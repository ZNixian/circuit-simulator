/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author Campbell Suter
 */
public interface Editable {

    public EditInfo getEditInfo(int n);

    public void setEditValue(int n, EditInfo ei);
}