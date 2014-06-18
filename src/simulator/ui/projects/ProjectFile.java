/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui.projects;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Campbell Suter
 */
public class ProjectFile {

    private final Project owner;
    private final String name;
    private final String title;
    private final boolean url;

    public ProjectFile(Project owner, String name, boolean url) {
        this(owner, name, null, url);
    }

    public ProjectFile(Project owner, String name) {
        this(owner, name, name.startsWith("http://") || name.startsWith("https://"));
    }
    
    public ProjectFile(Project owner, String name, String title, boolean url) {
        this.owner = owner;
        this.name = name;
        this.url = url;
        this.title = title;
    }

    public ProjectFile(Project owner, String name, String title) {
        this(owner, name, title, name.startsWith("http://") || name.startsWith("https://"));
    }

    @Override
    public String toString() {
        if (title != null) {
            return title;
        }
        return name;
    }

    public String getPath() {
        return name;
    }

    public String getName() {
        return name;
    }

    public URL toURL() {
        if (!url) {
            throw new IllegalStateException("This file is a url: it cannot be converted to a file!");
        }
        try {
            return new URL(name);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("An exception occured when creating this URL!", ex);
        }
    }

    public File toFile() {
        if (url) {
            throw new IllegalStateException("This file is a url: it cannot be converted to a file!");
        }
        return new File(owner.getFolder(), name);
    }

    public boolean isURL() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
