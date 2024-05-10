import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.image.*;
import java.text.NumberFormat;

import kz.sn34.raytrace_java_lib.Raytracer;
import kz.sn34.raytrace_java_lib.Camera;

public class App
{
    static public Raytracer raytracer;
    private BufferedImage img;

    public static void main(String[] args)
    {
        App.raytracer = new Raytracer();

        // Tree Exploration
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode tn1 = new DefaultMutableTreeNode("Test1");
        DefaultMutableTreeNode tn2 = new DefaultMutableTreeNode("Test2");
        DefaultMutableTreeNode tn3 = new DefaultMutableTreeNode("Test3");

        root.add(tn1);
        root.add(tn2);
        tn2.add(tn3);
        DefaultTreeModel tm = new DefaultTreeModel(root);

        // Screen setup
        JFrame mainWindow = new JFrame("Raytracing Implementations - Java");
        JPanel mainPanel = new JPanel(new GridBagLayout());
        ImagePanel imagePanel = new ImagePanel();
        mainWindow.setSize(1000, 1000);

        JButton renderBtn = new JButton("RENDER");
        renderBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    imagePanel.setImage(App.raytracer.render());
                    imagePanel.repaint();
                    System.out.println("Done!");
                }
            }
        );

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.weightx = 0.85;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(imagePanel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.15;
        mainPanel.add(infoPanel, c);

        GridBagConstraints infoC = new GridBagConstraints();
        infoC.gridwidth = GridBagConstraints.REMAINDER;
        infoPanel.add(new JLabel("Scene Editor"), infoC);
        infoC.fill = GridBagConstraints.BOTH;
        infoC.weightx = 1;
        infoC.weighty = 1;
        
        JTree sample = new JTree(tm);
        sample.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e)
                {
                    DefaultMutableTreeNode curNode = 
                        (DefaultMutableTreeNode) sample.getLastSelectedPathComponent();
                    System.out.println(curNode);
                }
            }
        );
        JScrollPane worldPane = new JScrollPane(sample);
        sample.setRootVisible(false);

        infoPanel.add(worldPane, infoC);

        // Settings pane
        JTabbedPane settingsTabs = new JTabbedPane();
        JPanel objectTab = new JPanel(new FlowLayout());
        CameraTab camTab = new CameraTab(App.raytracer.getCamera());
        settingsTabs.addTab("Object", camTab);
        settingsTabs.addTab("Test2", new JPanel());
        infoPanel.add(settingsTabs, infoC);

        infoC.fill = GridBagConstraints.HORIZONTAL;
        infoC.weighty = 0;
        infoPanel.add(renderBtn, infoC);

        // Main menu configuration
        JMenuBar mainMenu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("Export World..."));
        fileMenu.add(new JMenuItem("Import World..."));
        fileMenu.add(new JMenuItem("Save Render..."));

        JMenu renderMenu = new JMenu("Render");
        renderMenu.add(new JMenuItem("Quick"));
        renderMenu.add(new JMenuItem("Complete"));

        mainMenu.add(fileMenu);
        mainMenu.add(renderMenu);

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainWindow.add(mainPanel);
        mainWindow.setJMenuBar(mainMenu);
        mainWindow.setVisible(true);
    }
}

class ImagePanel extends JPanel
{
    private Image img;

    public ImagePanel()
    {
        super();
    }

    public void setImage(Image img)
    {
        this.img = img;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        g.drawImage(this.img, 0, 0, null);
    }
}

class CameraTab extends JPanel
{
    private Camera cam;
    private JTextField lookAtX;

    public CameraTab(Camera cam)
    {
        super(new GridBagLayout());
        this.cam = cam;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        lookAtX = new JTextField();

        lookAtX.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e)
                {
                    JTextField source = (JTextField) e.getSource();
                    String sourceText = source.getText();
                    char curChar = e.getKeyChar();

                    if(curChar == '-')
                    {
                        if(!sourceText.startsWith("-"))
                            source.setText("-" + sourceText);
                        e.consume();
                    }
                    else if(curChar == '.')
                    {
                        if(sourceText.contains("."))
                           e.consume();
                    }
                    else if(!Character.isDigit(curChar))
                        e.consume();
                }

                @Override
                public void keyReleased(KeyEvent e)
                {}

                @Override
                public void keyPressed(KeyEvent e)
                {}
            }
        );
        this.add(lookAtX, c);
    }
}
