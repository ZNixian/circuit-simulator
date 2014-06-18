package simulator.components;

public class NMosfetElm extends MosfetElm {

    public NMosfetElm(int xx, int yy) {
        super(xx, yy, false);
    }

    public Class getDumpClass() {
        return MosfetElm.class;
    }
}
