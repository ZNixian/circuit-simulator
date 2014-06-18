package simulator;

public interface ImportExportDialog {

    public enum Action {

        IMPORT, SAVE
    };

    public void setDump(String dump);

    public void execute();
}
