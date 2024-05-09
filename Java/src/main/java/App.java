import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;
import java.awt.image.*;
import kz.sn34.raytrace_java_lib.Raytracer;

public class App
{
    static public Raytracer raytracer;
    private BufferedImage img;

    public static void main(String[] args)
    {
        App.raytracer = new Raytracer();
        String[] strArr = {"Test1", "Test2"};

        JFrame mainWindow = new JFrame("Raytracing Implementations - Java");
        JPanel mainPanel = new JPanel(new GridBagLayout());
        ImagePanel imagePanel = new ImagePanel();
        mainWindow.setSize(500, 500);

        // Screen setup
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
        c.weightx = 0.8;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        mainPanel.add(imagePanel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.2;
        mainPanel.add(infoPanel, c);

        GridBagConstraints infoC = new GridBagConstraints();
        infoC.weightx = 1;
        infoC.weighty = 1;
        infoC.gridwidth = GridBagConstraints.REMAINDER;
        infoC.fill = GridBagConstraints.BOTH;
        JTree sample = new JTree(strArr);
        JScrollPane worldPane = new JScrollPane(sample);
        sample.setRootVisible(false);
        infoPanel.add(worldPane, infoC);

        infoC.fill = GridBagConstraints.HORIZONTAL;
        infoC.weighty = 0;
        infoPanel.add(renderBtn, infoC);

        // Main menu configuration
        JMenuBar mainMenu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("Export World"));
        fileMenu.add(new JMenuItem("Import World"));
        fileMenu.add(new JMenuItem("Save Render"));

        JMenu cameraMenu = new JMenu("Camera");
        cameraMenu.add(new JMenuItem("Settings"));

        JMenu renderMenu = new JMenu("Render");
        renderMenu.add(new JMenuItem("Quick"));
        renderMenu.add(new JMenuItem("Full"));

        mainMenu.add(fileMenu);
        mainMenu.add(cameraMenu);
        mainMenu.add(renderMenu);

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainWindow.add(mainPanel);
        mainWindow.setJMenuBar(mainMenu);
        mainWindow.setVisible(true);

        // Camera window
        cameraWindow = new JFrame("Camera - Settings");
        cameraWindow.setSize(750, 500);
        JPanel cameraPanel = new JPanel(new GridBagLayout())

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
