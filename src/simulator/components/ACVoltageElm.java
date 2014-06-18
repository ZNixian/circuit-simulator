package simulator.components;

    public class ACVoltageElm extends VoltageElm {
	public ACVoltageElm(int xx, int yy) { super(xx, yy, WF_AC); }
	public Class getDumpClass() { return VoltageElm.class; }
    }
