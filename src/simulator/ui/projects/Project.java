/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui.projects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Campbell Suter
 */
public class Project {

    private ArrayList<ProjectFile> files;
    private String name;
    private File path;
    private String url;

    public Project(Map<String, Object> data, Yaml yaml) {
        name = (String) data.get("name");
        Map<String, Object> specificdata;
        if (data.get("path") != null) {
            path = new File((String) data.get("path"));
            specificdata = loadMapDisk(path, yaml);
        } else {
            url = (String) data.get("url");
            specificdata = loadMapWeb(url, yaml);
        }

        files = new ArrayList<>();
        Map<String, Map<String, String>> filesStrings = (Map<String, Map<String, String>>) specificdata.get("files");
        for (String thisName : filesStrings.keySet()) {
            Map<String, String> file = filesStrings.get(thisName);
            files.add(new ProjectFile(this, thisName, file.get("title")));
        }
    }

    public Project(String name, String path, Yaml yaml) {
        this.path = null;
        this.url = path;
        files = new ArrayList<>();

        Map<String, Object> data = loadMapWeb(url, yaml);
        Map<String, Map<String, String>> filesStrings = (Map<String, Map<String, String>>) data.get("files");
        for (String thisName : filesStrings.keySet()) {
            Map<String, String> file = filesStrings.get(thisName);
            files.add(new ProjectFile(this, thisName, file.get("title")));
        }
        this.name = name;
    }

    private Map<String, Object> loadMapDisk(File path, Yaml yaml) {
        try {
            Map<String, Object> specificdata;
            InputStream input;
            input = new FileInputStream(getProjectFile());
            specificdata = (Map<String, Object>) yaml.load(input);
            return specificdata;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    private Map<String, Object> loadMapWeb(String url, Yaml yaml) {
        try {
            InputStream is;
            BufferedReader br;
            String line;
            is = new URL(url).openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder build = new StringBuilder();
            while ((line = br.readLine()) != null) {
                build.append(line).append('\n');
            }
            Map<String, Object> data;
            data = (Map<String, Object>) yaml.load(build.toString());
            br.close();
            return data;
        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public Project(String name, File path) {
        this.path = path;
        files = new ArrayList<>();
        files.add(new ProjectFile(this, "main.circ"));
        this.name = name;
    }

    public void serilize(Map<String, Object> data, Yaml yaml) {
        HashMap<String, Object> pdata = new HashMap<>();
        serilizeTo(pdata);
        data.put(name, pdata);
        save(yaml);
    }

    public void serilizeTo(Map<String, Object> data) {
        data.put("name", name);
        if (path != null) {
            data.put("path", path.getAbsolutePath());
        } else {
            data.put("url", url);
        }
    }

    public void save(Yaml yaml) {
        Map<String, Map<String, String>> fileMaps = new HashMap<>();
        for (ProjectFile file : files) {
            Map<String, String> thisFileMap = new HashMap<>();
            if (file.getTitle() != null) {
                thisFileMap.put("title", file.getTitle());
            }
            fileMaps.put(file.getName(), thisFileMap);
        }
        Map<String, Object> projectSers = new HashMap<>();
        projectSers.put("files", fileMaps);

        String data = yaml.dump(projectSers);
        try {
            try (OutputStream output = new FileOutputStream(getProjectFile())) {
                output.write(data.getBytes());
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<ProjectFile> getFiles() {
        return files;
    }

    public String getName() {
        return name;
    }

    public File getFolder() {
        return path;
    }

    public boolean isWeb() {
        return path == null;
    }

    public File getProjectFile() {
        return new File(getFolder(), "data.circproject");
    }

    @Override
    public String toString() {
        return name;
    }
}
