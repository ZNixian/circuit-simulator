package simulator.components;

public class NJfetElm extends JfetElm {

    public NJfetElm(int xx, int yy) {
        super(xx, yy, false);
    }

    public Class getDumpClass() {
        return JfetElm.class;
    }
}