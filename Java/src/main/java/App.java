import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FileDialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import kz.sn34.raytrace.Raytracer;
import kz.sn34.raytrace.hitables.Hitable;
import kz.sn34.raytrace.components.*;
import kz.sn34.raytrace.components.util.*;

public class App
{
    private JLabel propertiesLabel;
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

        this.propertiesLabel = new JLabel("Properties"); 
        this.objProperties = new JPanel();
        this.root = new DefaultMutableTreeNode();
        this.tree = new DefaultTreeModel(root);

        this.worldNode = new DefaultMutableTreeNode("WORLD");
        this.camNode = new DefaultMutableTreeNode("CAMERA");
        this.root.add(this.camNode);
        this.root.add(this.worldNode);
    }

    public static void main(String[] args)
    {
        App app = new App();

        // Initialize window and its parameters
        JFrame mainWindow = new JFrame("Raytracing Implementations - Java");
        mainWindow.setSize(1000, 1000);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initializing all components in order of apperance
        JPanel mainPanel = new JPanel(new GridBagLayout());

        CameraPanel camTab = new CameraPanel(app.raytracer.getCamera(), 
                app.raytracer);
        WorldPanel worldTab = new WorldPanel(app.raytracer);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        JScrollPane propertiesPane = new JScrollPane();

        JTree guiTree = new JTree(app.tree);
        JScrollPane worldPane = new JScrollPane(guiTree);

        JButton subtractBtn = new JButton("-");
        JButton addBtn = new JButton("+");
        
        JButton cancelBtn = new JButton("CANCEL");
        JButton confirmBtn = new JButton("CONFIRM");

        // Setting up components
        app.refreshSceneTree();
        guiTree.setRootVisible(false);
        guiTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                DefaultMutableTreeNode curNode = (DefaultMutableTreeNode) 
                    guiTree.getLastSelectedPathComponent();

                app.objProperties.removeAll();
                subtractBtn.setEnabled(false);

                if(curNode != null)
                {
                    if(curNode.toString().equals("CAMERA"))
                    {
                        app.objProperties.add(camTab);
                        camTab.populateFields();
                    }
                    else if(curNode.toString().equals("WORLD"))
                    {
                        app.objProperties.add(worldTab);
                        worldTab.populateFields();
                    }
                    else
                    {
                        WorldEntry curEntry = (WorldEntry) 
                            curNode.getUserObject();
                        if(!curEntry.isChild())
                        {
                            HitablePanel objTab = new HitablePanel(curEntry);
                            app.objProperties.add(objTab);
                            objTab.populateFields();
                        }

                        subtractBtn.setEnabled(true);
                    }

                    app.propertiesLabel.setText(curNode + " - Properties");
                    app.objProperties.revalidate();
                    app.objProperties.repaint();

                    confirmBtn.setEnabled(true);
                    cancelBtn.setEnabled(true);
                }
                else
                    app.propertiesLabel.setText("Properties");
            }
        });

        subtractBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode curNode = (DefaultMutableTreeNode)
                    guiTree.getLastSelectedPathComponent();

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

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame addFrame = new JFrame();
                JPanel addPanel = new JPanel(new GridBagLayout());
                addFrame.setDefaultCloseOperation(
                        WindowConstants.DISPOSE_ON_CLOSE);

                AddObjectPanel objPanel = new AddObjectPanel();

                GridBagConstraints titleConstraints = new GridBagConstraints();
                titleConstraints.weightx = 0;
                titleConstraints.gridwidth = GridBagConstraints.REMAINDER;

                GridBagConstraints fieldConstraints = new GridBagConstraints();
                fieldConstraints.weightx = 1;
                fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
                fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;

                addPanel.add(
                        new JLabel("Object Type"), titleConstraints);

                JComboBox<EntryType> objectChoice = new JComboBox<>();
                objectChoice.addItem(EntryType.SPHERE);
                objectChoice.addItem(EntryType.SPHEROID);
                addPanel.add(objectChoice, fieldConstraints);

                JButton confirmAddBtn = new JButton("CONFIRM");

                objectChoice.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        EntryType typeSelected = 
                            (EntryType) objectChoice.getSelectedItem();

                        objPanel.init(typeSelected);
                        addPanel.add(objPanel, fieldConstraints);
                        addPanel.add(confirmAddBtn, fieldConstraints);
                        addPanel.revalidate();
                        addPanel.repaint();
                    }
                });

                confirmAddBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        EntryType typeSelected = 
                            (EntryType) objectChoice.getSelectedItem();

                        Hitable newObject = objPanel.createObject(typeSelected);
                        if(newObject != null)
                        {
                            app.raytracer.addWorldObject(typeSelected,
                                    newObject);

                            app.refreshSceneTree();
                        }
                    }
                });

                JDialog addDialog = new JDialog(addFrame, "Add Hitable Object",
                        true);
                addDialog.setSize(250, 250);
                addDialog.add(addPanel);
                addDialog.setVisible(true);
            }       
        });

        propertiesPane.setViewportView(app.objProperties);
        propertiesPane.revalidate();

        cancelBtn.setEnabled(false);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                guiTree.clearSelection();

                confirmBtn.setEnabled(false);
                cancelBtn.setEnabled(false);
            }
        });

        confirmBtn.setEnabled(false);
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                DefaultMutableTreeNode curNode = (DefaultMutableTreeNode)
                    guiTree.getLastSelectedPathComponent();

                if(curNode != null)
                {
                    try { 
                        if(curNode.toString().equals("CAMERA"))
                            camTab.applyFields();
                        else if(curNode.toString().equals("WORLD"))
                            worldTab.applyFields();
                        else
                        {
                            HitablePanel curPanel = 
                                (HitablePanel) app.objProperties.getComponents()[0];
                            curPanel.applyFields();
                        }
                    }
                    catch(NumberFormatException ex)
                    {}
                }
                else
                    app.propertiesLabel.setText("Properties");

            }
        });

        // Creating constraints used to arrange components
        GridBagConstraints mainConstraints = new GridBagConstraints();
        GridBagConstraints infoConstraints = new GridBagConstraints();
        GridBagConstraints infoLabelConstraints = new GridBagConstraints();
        GridBagConstraints infoBtnConstraints = new GridBagConstraints();
         
        // Setting up constraints
        mainConstraints.gridwidth = GridBagConstraints.RELATIVE;
        mainConstraints.weightx = 0.85;
        mainConstraints.weighty = 1;
        mainConstraints.fill = GridBagConstraints.BOTH;

        infoConstraints.gridwidth = GridBagConstraints.REMAINDER;
        infoConstraints.fill = GridBagConstraints.BOTH;
        infoConstraints.weightx = 1;
        infoConstraints.weighty = 1;

        infoLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;

        infoBtnConstraints.gridwidth = GridBagConstraints.RELATIVE;
        infoBtnConstraints.fill = GridBagConstraints.HORIZONTAL;
        infoBtnConstraints.weightx = 1;
        infoBtnConstraints.weighty = 0;
        
        // Arranging components within their corrresponding container
        // components
        mainPanel.add(app.imagePanel, mainConstraints);

        mainConstraints.gridwidth = GridBagConstraints.REMAINDER;
        mainConstraints.weightx = 0.15;
        mainPanel.add(infoPanel, mainConstraints); 

        infoPanel.add(new JLabel("Scene Editor"), infoLabelConstraints);
        infoPanel.add(worldPane, infoConstraints);
        infoPanel.add(subtractBtn, infoBtnConstraints);
        infoBtnConstraints.gridwidth = GridBagConstraints.REMAINDER;
        infoPanel.add(addBtn, infoBtnConstraints);

        infoPanel.add(app.propertiesLabel, infoLabelConstraints);
        infoPanel.add(propertiesPane, infoConstraints);
        infoBtnConstraints.gridwidth = GridBagConstraints.RELATIVE;
        infoPanel.add(cancelBtn, infoBtnConstraints);
        infoBtnConstraints.gridwidth = GridBagConstraints.REMAINDER;
        infoPanel.add(confirmBtn, infoBtnConstraints);

        mainWindow.add(mainPanel);
        
        // Main menu configuration
        app.initMenuBar(mainWindow); 

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
