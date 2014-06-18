package simulator;

import java.awt.*;
import java.awt.event.*;

import java.awt.datatransfer.Clipboard;
import simulator.cirsim.FileIO;

public class ImportExportClipboardDialog
        extends Dialog
        implements ImportExportDialog, ActionListener {

    public CirSim cframe;
    public Button openButton, closeButton;
    public TextArea text;
    public Action type;
    public Clipboard clipboard = null;

    public ImportExportClipboardDialog(CirSim f, Action type) {
        super(f, (type == Action.SAVE) ? "Export" : "Import", false);
        cframe = f;
        setLayout(new ImportExportDialogLayout());
        add(text = new TextArea("", 10, 60, TextArea.SCROLLBARS_BOTH));
        /*
         if (type == Action.EXPORT)
         importButton = new Button("Copy to clipboard");
         else
         */
        openButton = new Button("Import");
        this.type = type;
        add(openButton);
        openButton.addActionListener(this);
        add(closeButton = new Button("Close"));
        closeButton.addActionListener(this);
        Point x = CirSim.trueMain.getLocationOnScreen();
        setSize(400, 300);
        Dimension d = getSize();
        setLocation(x.x + (cframe.winSize.width - d.width) / 2,
                x.y + (cframe.winSize.height - d.height) / 2);
    }

    @Override
    public void setDump(String dump) {
        text.setText(dump);
    }

    @Override
    public void execute() {
        if (type == Action.SAVE) {
            text.selectAll();
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i;
        Object src = e.getSource();
        if (src == openButton) {
            /*
             if (clipboard == null)
             clipboard = getToolkit().getSystemClipboard();
             if ( type == Action.EXPORT )
             {
             StringSelection data = new StringSelection(text.getText());
             clipboard.setContents(data, data);
             }
             else
             */
            {
                FileIO.readSetupAndPrompt(cframe, text.getText().getBytes(), text.getText().length(), false);
            }
        }
        if (src == closeButton) {
            setVisible(false);
        }
    }

    @Override
    public boolean handleEvent(Event ev) {
        if (ev.id == Event.WINDOW_DESTROY) {
            CirSim.main.requestFocus();
            setVisible(false);
            CirSim.impDialog = null;
            return true;
        }
        return super.handleEvent(ev);
    }
}
