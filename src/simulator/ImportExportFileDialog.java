package simulator;

import com.sun.org.apache.bcel.internal.generic.FCMPG;
import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import simulator.cirsim.FileIO;

public class ImportExportFileDialog
        implements ImportExportDialog {

    public CirSim cframe;
    private static String circuitDump;
    public Action type;
    private static String directory = ".";

    public ImportExportFileDialog(CirSim f, Action type) {
        if (directory.equals(".")) {
            File file = new File("circuits");
            if (file.isDirectory()) {
                directory = "circuits";
            }
        }
        this.type = type;
        cframe = f;
    }

    @Override
    public void setDump(String dump) {
        circuitDump = dump;
    }

    public String getDump() {
        return circuitDump;
    }

    @Override
    public void execute() {
        FileDialog fd = new FileDialog(new Frame(),
                (type == Action.SAVE) ? "Save File"
                : "Open File",
                (type == Action.SAVE) ? FileDialog.SAVE
                : FileDialog.LOAD);
        fd.setDirectory(directory);
        fd.setVisible(true);
        String file = fd.getFile();
        String dir = fd.getDirectory();
        if (dir != null) {
            directory = dir;
        }
        if (file == null) {
            return;
        }
//        System.err.println(dir + File.separator + file);
        if (type == Action.SAVE) {
            cframe.mdc.loaded = new File(dir, file + ".circ");
            save(dir + file + ".circ");
        } else {
            try {
                String dump = readFile(dir + file);
                circuitDump = dump;
//                cframe.readSetup(circuitDump);
                FileIO.readSetupAndPrompt(cframe, circuitDump.getBytes(), circuitDump.length(), false);
                cframe.mdc.loaded = new File(dir, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save(String fi) {
        try {
            writeFile(fi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String path)
            throws IOException, FileNotFoundException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(path));
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY,
                    0, fc.size());
            return Charset.forName("UTF-8").decode(bb).toString();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            return null;
        } finally {
            stream.close();
        }
    }

    public static void writeFile(String path)
            throws IOException, FileNotFoundException {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(path));
            FileChannel fc = stream.getChannel();
            ByteBuffer bb = Charset.forName("UTF-8").encode(circuitDump);
            fc.write(bb);
//            System.err.println(path);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        } finally {
            stream.close();
        }
    }
}
