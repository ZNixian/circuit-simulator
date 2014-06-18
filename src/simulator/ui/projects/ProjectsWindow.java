/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ui.projects;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import simulator.CirSim;
import simulator.ImportWebDialog;
import simulator.cirsim.FileIO;

/**
 *
 * @author Campbell Suter
 */
public class ProjectsWindow {

    private JFrame win = new JFrame("Projects");
    private JTree tree;
    private CirSim cirsim;
    private static boolean reload;

    public ProjectsWindow(CirSim cirsim) {
        this.cirsim = cirsim;
        Map<String, Project> projects = ProjectList.getInstance().getProjects();
        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("Projects");
        for (String projectName : projects.keySet()) {
            Project project = projects.get(projectName);
            DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(projects.get(projectName));
            for (ProjectFile file : project.getFiles()) {
                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file);
                projectNode.add(fileNode);
            }
            top.add(projectNode);
        }
        tree = new JTree(top);
//        TreeSelectionListener ml = new TreeSelectionListener() {
//            @Override
//            public void valueChanged(TreeSelectionEvent e) {
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//
//                if (node == null) {
//                    return;
//                }
//
//                Object nodeInfo = node.getUserObject();
//                if (node.isLeaf()) {
//                    ProjectFile file = (ProjectFile) nodeInfo;
//                    openFile(file);
//                }
//            }
//        };
//        tree.addTreeSelectionListener(ml);
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (e.getClickCount() == 1) {
                        } else if (e.getClickCount() == 2) {
                            if (node.isLeaf()) {
                                ProjectFile fi = (ProjectFile) node.getUserObject();
                                openFile(fi);
                            }
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        if (e.getClickCount() == 1) {
                            Object nodeData = node.getUserObject();
                            if (nodeData instanceof Project) {
                                String name = ((Project) nodeData).getName();
                                ProjectProperty.main(ProjectsWindow.this, name);
                            }
                        } else if (e.getClickCount() == 2) {
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);
        win.add(tree);
        win.setLocation(cirsim.getLocation());
        win.setSize(300, 300);
        win.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JToolBar toolBar = new JToolBar();
        JButton load = new JButton(UIManager.getIcon("FileView.directoryIcon"));
        toolBar.add(load);
        load.setToolTipText("Open Project");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenProject.main(null);
            }
        });
        JButton loadWeb = new JButton(UIManager.getIcon("FileView.computerIcon"));
        toolBar.add(loadWeb);
        loadWeb.setToolTipText("Open Web Project");
        loadWeb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenWebProject.main(null);
            }
        });
        JButton newProject = new JButton(UIManager.getIcon("FileView.fileIcon"));
        toolBar.add(newProject);
        toolBar.setToolTipText("New Project");
        newProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewProject.main(ProjectsWindow.this);
            }
        });
        win.add(toolBar, BorderLayout.NORTH);
    }

    private void openFile(ProjectFile file) {
        if (file.isURL()) {
            ImportWebDialog.load(file.getPath(), cirsim);
        } else {
            String path = file.toFile().getPath();
            FileIO.load(path, cirsim);
        }
    }

    public void show() {
        win.setVisible(true);
    }

    public void reload() {
        if (win.isVisible()) {
            win.dispose();
        }
        cirsim.mdc.projectsWindow = null;
    }

    public static void showWindow(CirSim cs) {
        if (cs.mdc.projectsWindow != null && reload) {
            cs.mdc.projectsWindow.win.dispose();
            cs.mdc.projectsWindow = null;
        }
        reload = false;
        if (cs.mdc.projectsWindow == null) {
            cs.mdc.projectsWindow = new ProjectsWindow(cs);
        }
        cs.mdc.projectsWindow.show();
    }

    public static void reloadNext() {
        reload = true;
    }
}
