package simulator;

import java.awt.*;
import java.awt.event.*;

import java.awt.datatransfer.Clipboard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import simulator.cirsim.FileIO;

public class ImportWebDialog
        extends Dialog
        implements ImportExportDialog, ActionListener {

    public CirSim cframe;
    public Button openButton, closeButton;
    public TextField text;
    public Clipboard clipboard = null;

    public ImportWebDialog(CirSim f) {
        super(f, "Import From Web", false);
        cframe = f;
        setLayout(new ImportExportDialogLayout());
        add(text = new TextField("", 30));
        /*
         if (type == Action.EXPORT)
         importButton = new Button("Copy to clipboard");
         else
         */
        openButton = new Button("Import");
        add(openButton);
        openButton.addActionListener(this);
        add(closeButton = new Button("Close"));
        closeButton.addActionListener(this);
        Point x = CirSim.trueMain.getLocationOnScreen();
        resize(400, 300);
        Dimension d = getSize();
        setLocation(x.x + (cframe.winSize.width - d.width) / 2,
                x.y + (cframe.winSize.height - d.height) / 2);
    }

    @Override
    public void setDump(String dump) {
    }

    @Override
    public void execute() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i;
        Object src = e.getSource();
        if (src == openButton) {
            text.setText(text.getText().trim());
            if (text.getText().isEmpty()) {
                return;
            }
            load(text.getText());
        }
        if (src == closeButton) {
            setVisible(false);
        }
    }
    
    public void load(String location) {
        load(location, cframe);
    }

    public static void load(String location, CirSim cframe) {
        try {
            cframe.mdc.loaded = null;
            URL oracle = new URL(location);
            StringBuilder data;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()))) {
                data = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (data.length() != 0) {
                        data.append('\n');
                    }
                    data.append(inputLine);
                }
            }
            FileIO.readSetupAndPrompt(cframe, data.toString().getBytes(), data.length(), false);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ImportWebDialog.class.getName()).log(Level.SEVERE, null, ex);
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
