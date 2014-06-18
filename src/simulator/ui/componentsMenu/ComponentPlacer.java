/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui.componentsMenu;

import simulator.cirsim.ComponentType;

/**
 *
 * @author Campbell Suter
 */
public interface ComponentPlacer {

    public void addButton(String title, String name, ComponentType type, boolean isSimple);
    public void addButton(String title, String name, ComponentType type);
    public void addButton(String title, String name, boolean isSimple);
    public void addButton(String title, String name);
    
    public void addCheckButton(String title, String name);
    public void addCheckButton(String title, String name, boolean simple);
    
    public void setDefaultType(ComponentType type);
}
