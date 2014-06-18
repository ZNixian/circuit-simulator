/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui.projects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 *
 * @author Campbell Suter
 */
public class ProjectList {

    private Map<String, Project> projects;
    private Yaml yaml = new Yaml(new SafeConstructor());

    private ProjectList() {
        InputStream input;
        projects = new HashMap<>();
        try {
            Map<String, Object> projectsRaw;
            input = new FileInputStream(new File("projects.txt"));
            projectsRaw = (Map<String, Object>) yaml.load(input);
            if (projectsRaw != null) {
                for (String string : projectsRaw.keySet()) {
                    Map<String, Object> this_project = (Map<String, Object>) projectsRaw.get(string);
                    projects.put(string, new Project(this_project, yaml));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectList.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    public Map<String, Project> getProjects() {
        return projects;
    }

    public boolean newProject(String name) {
        if (projects.containsKey(name)) {
            JOptionPane.showMessageDialog(null, "This project already exists!", "Project name conflict", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        File projectFile = new File("projects/" + name);
        if (projectFile.exists()) {
            JOptionPane.showMessageDialog(null, "This project's folder already exists!", "Project file conflict", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        projectFile.mkdirs();
        File mainFile = new File(projectFile, "main.circ");
        File dataFile = new File(projectFile, "data.circproject");
        try {
            mainFile.createNewFile();
            dataFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ProjectList.class.getName()).log(Level.SEVERE, null, ex);
        }

        Project project = new Project(name, new File("projects/", name));
        projects.put(name, project);

        save();

        return true;
    }
    //////////////////////////////

    public boolean openWebProject(String path, String name) {
        Project project = new Project(name, path, yaml);
        
        if (projects.containsKey(name)) {
            JOptionPane.showMessageDialog(null, "This project is already open!", "Project open", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        projects.put(name, project);

        save();

        return true;
    }
    //////////////////////////////

    public boolean openProject(File path) {
        String name = path.getName();
        if (projects.containsKey(name)) {
            JOptionPane.showMessageDialog(null, "This project is already open!", "Project open", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!path.exists()) {
            JOptionPane.showMessageDialog(null, "This project's folder does not exist!", "Project file conflict", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Project project = new Project(name, path);
        projects.put(name, project);

        save();

        return true;
    }
    //////////////////////////////

    public boolean closeProject(String name) {
        if (!projects.containsKey(name)) {
            JOptionPane.showMessageDialog(null, "This project is not open!", "Project close", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        projects.remove(name);

        save();

        return true;
    }
    //////////////////////////////

    public void save() {
        Map<String, Object> projectSers = new HashMap<>();

        for (String string : projects.keySet()) {
            projects.get(string).serilize(projectSers, yaml);
        }

        String data = yaml.dump(projectSers);
        try {
            try (OutputStream output = new FileOutputStream(new File("projects.txt"))) {
                output.write(data.getBytes());
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ProjectList getInstance() {
        return ProjectListHolder.INSTANCE;
    }

    private static class ProjectListHolder {

        private static final ProjectList INSTANCE = new ProjectList();
    }
}
