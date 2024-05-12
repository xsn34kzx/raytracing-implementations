import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.text.*;

import kz.sn34.raytrace_java_lib.*;

public class App
{
    private JLabel propertiesTag;

    private JScrollPane properties;
    private JPanel objProperties;

    private DefaultTreeModel tree;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode worldNode;
    private DefaultMutableTreeNode camNode;

    private Raytracer raytracer;
    private BufferedImage img;
    private ImagePanel imagePanel;

    public App()
    {
        this.raytracer = new Raytracer();
        this.imagePanel = new ImagePanel();

        this.propertiesTag = new JLabel("Properties"); 
        this.properties = new JScrollPane();
        this.root = new DefaultMutableTreeNode();
        this.tree = new DefaultTreeModel(root);

        this.worldNode = new DefaultMutableTreeNode("WORLD");
        this.camNode = new DefaultMutableTreeNode("CAMERA");
        this.root.add(camNode);
        this.root.add(worldNode);
    }

    public static void main(String[] args)
    {
        App app = new App();

        // Screen setup
        JFrame mainWindow = new JFrame("Raytracing Implementations - Java");
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainWindow.setSize(1000, 1000);
        
        // Initializing all components in order of apperance
        CameraPanel camTab = new CameraPanel(app.raytracer.getCamera(), 
                app.raytracer);

        JPanel infoPanel = new JPanel(new GridBagLayout());


        app.refreshSceneTree();
        JTree sample = new JTree(app.tree);
        JScrollPane worldPane = new JScrollPane(sample);

        JButton subtractBtn = new JButton("-");
        JButton addBtn = new JButton("+");
        
        JPanel prop = new JPanel();

        JButton cancelBtn = new JButton("CANCEL");
        JButton confirmBtn = new JButton("CONFIRM");

        // Constraints objects
        GridBagConstraints c = new GridBagConstraints();
        GridBagConstraints infoC = new GridBagConstraints();
        GridBagConstraints infoLabelConstraints = new GridBagConstraints();
                
        
        // Component setup and arragement
        confirmBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
                

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.weightx = 0.85;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(app.imagePanel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.15;
        mainPanel.add(infoPanel, c);


        infoLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
        infoPanel.add(new JLabel("Scene Editor"), infoLabelConstraints);

        infoC.gridwidth = GridBagConstraints.REMAINDER;
        infoC.fill = GridBagConstraints.BOTH;
        infoC.weightx = 1;
        infoC.weighty = 1;
        
        sample.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                DefaultMutableTreeNode curNode = 
                    (DefaultMutableTreeNode) sample.getLastSelectedPathComponent();

                prop.removeAll();
                prop.revalidate();
                prop.repaint();

                subtractBtn.setEnabled(false);

                if(curNode != null)
                {
                    if(curNode.toString().equals("CAMERA"))
                    {
                        prop.add(camTab);
                        camTab.populateFields();
                    }
                    else if(curNode.toString().equals("WORLD"))
                    {
                    }
                    else
                    {
                        subtractBtn.setEnabled(true);
                    }

                    app.propertiesTag.setText(curNode + " - Properties");
                    prop.revalidate();
                    prop.repaint();

                    confirmBtn.setEnabled(true);
                    cancelBtn.setEnabled(true);
                }
                else
                    app.propertiesTag.setText("Properties");

                //TODO: Remove
                System.out.println(curNode);
            }
        });

        subtractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode curNode = 
                    (DefaultMutableTreeNode) sample.getLastSelectedPathComponent();

                System.out.println(curNode);

                WorldEntry curEntry = (WorldEntry) curNode.getUserObject();

                if(curEntry.isChild())
                {
                    curNode = (DefaultMutableTreeNode) curNode.getParent();
                    curEntry = (WorldEntry) curNode.getUserObject();
                }

                app.raytracer.deleteWorldEntry(curEntry);

                app.refreshSceneTree();
                if(app.raytracer.getWorldEntries().size() == 0)
                    subtractBtn.setEnabled(false);
            }
        });

        sample.setRootVisible(false);

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sample.clearSelection();

                confirmBtn.setEnabled(false);
                cancelBtn.setEnabled(false);
            }
        });

        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode curNode = 
                    (DefaultMutableTreeNode) sample.getLastSelectedPathComponent();

                if(curNode != null)
                {
                    try { 
                        if(curNode.toString().equals("CAMERA"))
                        {
                            camTab.applyFields();
                        }
                        else if(curNode.toString().equals("WORLD"))
                        {
                        }
                        else
                        {
                        }

                    }
                    catch(NumberFormatException ex)
                    {}
                }
                else
                    app.propertiesTag.setText("Properties");

            }
        });

        infoPanel.add(worldPane, infoC);

        GridBagConstraints infoBtnConstraints = new GridBagConstraints();
        infoBtnConstraints.gridwidth = GridBagConstraints.RELATIVE;
        infoBtnConstraints.fill = GridBagConstraints.HORIZONTAL;
        infoBtnConstraints.weightx = 1;
        infoBtnConstraints.weighty = 0;

        infoPanel.add(subtractBtn, infoBtnConstraints);
        infoBtnConstraints.gridwidth = GridBagConstraints.REMAINDER;
        infoPanel.add(addBtn, infoBtnConstraints);

        // Settings pane
        //JTabbedPane settingsTabs = new JTabbedPane();
        infoPanel.add(app.propertiesTag, infoLabelConstraints);
        
        app.properties.setViewportView(prop);
        app.properties.revalidate();
        //settingsTabs.addTab("Object", camTab);
        //settingsTabs.addTab("Test2", new JPanel());
        infoPanel.add(app.properties, infoC);

        infoBtnConstraints.gridwidth = GridBagConstraints.RELATIVE;
        infoPanel.add(cancelBtn, infoBtnConstraints);
        infoBtnConstraints.gridwidth = GridBagConstraints.REMAINDER;
        infoPanel.add(confirmBtn, infoBtnConstraints);

        // Main menu configuration
        app.initMenuBar(mainWindow); 

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainWindow.add(mainPanel);
        mainWindow.setVisible(true);
    }

    private void refreshSceneTree()
    {
        this.worldNode.removeAllChildren();
        ArrayList<WorldEntry> entries = this.raytracer.getWorldEntries();

        if(entries.size() != 0)
        {
            DefaultMutableTreeNode parent = 
                new DefaultMutableTreeNode(entries.get(0));

            for(WorldEntry entry : this.raytracer.getWorldEntries())
            {
                DefaultMutableTreeNode curNode = new DefaultMutableTreeNode(entry);

                if(entry.isChild())
                    parent.add(curNode);
                else
                {
                    parent = curNode;
                    this.worldNode.add(curNode);
                }
            }
        }

        this.tree.nodeChanged(this.worldNode);
        this.tree.reload(this.worldNode);
    }

    private void initMenuBar(JFrame frame)
    {
        JMenuBar mainMenu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
    
        // TODO: Disable export option if world is empty
        JMenuItem exportWorldOption = new JMenuItem("Export World...");
        exportWorldOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                FileDialog savePrompt = new FileDialog(
                        new JFrame(), "Export World", FileDialog.SAVE);
                savePrompt.setFile("MyWorld.dat");
                savePrompt.setDirectory(System.getProperty("user.dir"));
                savePrompt.setVisible(true);

                String fileName = savePrompt.getFile();
                if(fileName != null)
                {
                    String filePath = savePrompt.getDirectory() + fileName;
                    try {
                        File worldFile = new File(filePath);

                        if(filePath.endsWith(".dat"))
                            raytracer.exportWorld(worldFile);
                        else
                            throw new Exception("Incorrect file extension!");
                    }
                    catch(IOException ex)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), 
                                "File could not be saved!",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        System.out.println(ex.getCause());
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), 
                                ex.getCause(), "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        fileMenu.add(exportWorldOption);

        JMenuItem importWorldOption = new JMenuItem("Import World...");
        importWorldOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                FileDialog loadPrompt = new FileDialog(
                        new JFrame(), "Import World", FileDialog.LOAD);
                loadPrompt.setFile("*.dat");
                loadPrompt.setDirectory(System.getProperty("user.dir"));
                loadPrompt.setVisible(true);

                String fileName = loadPrompt.getFile();
                if(fileName != null)
                {
                    String filePath = loadPrompt.getDirectory() + fileName;
                    try {
                        File worldFile = new File(filePath);

                        if(filePath.endsWith(".dat"))
                        {
                            raytracer.importWorld(worldFile);
                            refreshSceneTree();
                        }
                        else
                            throw new Exception("Incorrect file extension!");
                    }
                    catch(IOException ex)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), 
                                "File could not be loaded!",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        System.out.println(ex.getCause());
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), 
                                ex.getCause(), "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        fileMenu.add(importWorldOption);

        JMenuItem saveRenderOption = new JMenuItem("Save Render...");
        saveRenderOption.setEnabled(false);
        saveRenderOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                FileDialog savePrompt = new FileDialog(
                        new JFrame(), "Save Render", FileDialog.SAVE);
                savePrompt.setFile("MyWorldRender.png");
                savePrompt.setDirectory(System.getProperty("user.dir"));
                savePrompt.setVisible(true);

                String fileName = savePrompt.getFile();
                if(fileName != null)
                {
                    String filePath = savePrompt.getDirectory() + fileName;
                    try {
                        File imgFile = new File(filePath);

                        if(filePath.endsWith(".png"))
                            ImageIO.write(img, "PNG", imgFile);
                        else if(filePath.endsWith(".jpg"))
                            ImageIO.write(img, "JPG", imgFile);
                        else
                            throw new Exception("Incorrect file extension!");
                    }
                    catch(IOException ex)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), 
                                "File could not be saved!",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(new JFrame(), 
                                ex.getCause(), "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        fileMenu.add(saveRenderOption);

        JMenu renderMenu = new JMenu("Render");

        // Creating "Render" -> "Quick" option and its behavior
        JMenuItem quickRenderOption = new JMenuItem("Quick");
        quickRenderOption.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    img = raytracer.quickRender();
                    saveRenderOption.setEnabled(true);
                    imagePanel.setImage(img);
                    imagePanel.repaint();
                }
            }
        );

        // Creating "Render" -> "Complete" option and its behavior
        JMenuItem renderOption = new JMenuItem("Complete");
        renderOption.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    img = raytracer.render();
                    saveRenderOption.setEnabled(true);
                    imagePanel.setImage(img);
                    imagePanel.repaint();
                }
            }
        );

        // Adding all "Render" options to "Render" menu
        renderMenu.add(quickRenderOption);
        renderMenu.add(renderOption);

        // Adding all menus to menu bar
        mainMenu.add(fileMenu);
        mainMenu.add(renderMenu);

        frame.setJMenuBar(mainMenu);
    }
}

//TODO:
//  - Get Tree working
//      - Addition change tree
//  - Add & delete objects buttons
//      - Pop up menu with combo boxes and confirmation button
//  - Change World & Camera setings
