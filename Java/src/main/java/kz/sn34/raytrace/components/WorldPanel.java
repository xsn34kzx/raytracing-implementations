package kz.sn34.raytrace.components;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import kz.sn34.raytrace.Raytracer;
import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.event.NumberListener;

@SuppressWarnings("serial")
public class WorldPanel extends JPanel
{
    private Raytracer raytracer;
    private JTextField samples;
    private JTextField depth;
    private JTextField[] startColor;
    private JTextField[] endColor;

    public WorldPanel(Raytracer raytracer)
    {
        super(new GridBagLayout());
        this.raytracer = raytracer;

        // Constraints for components
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.weightx = 0;
        titleConstraints.gridwidth = GridBagConstraints.REMAINDER;

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;

        // Initializing components
        samples = new JTextField();
        samples.addKeyListener(new NumberListener(false, false));

        depth = new JTextField();
        depth.addKeyListener(new NumberListener(false, false));

        startColor = new JTextField[3];
        endColor = new JTextField[3];
        for(int i = 0; i < 3; i++)
        {
            startColor[i] = new JTextField();
            endColor[i] = new JTextField();

            startColor[i].addKeyListener(new NumberListener(true, false));
            endColor[i].addKeyListener(new NumberListener(true, false));
        }

        // Putting components together
        super.add(new JLabel("Number of Samples"), titleConstraints);
        super.add(samples, fieldConstraints);

        titleConstraints.insets = new Insets(25, 0, 0, 0);

        super.add(new JLabel("Ray Depth"), titleConstraints);
        super.add(depth, fieldConstraints);

        super.add(new JLabel("Background Start Color - (R, G, B)"), 
                titleConstraints);
        for(int i = 0; i < startColor.length; i++)
        {
            super.add(startColor[i], fieldConstraints);
        }

        super.add(new JLabel("Background End Color - (R, G, B)"), 
                titleConstraints);
        for(int i = 0; i < endColor.length; i++)
        {
            super.add(endColor[i], fieldConstraints);
        }
    }

    public void populateFields()
    {
        this.samples.setText(Integer.toString(this.raytracer.getSamples()));
        this.depth.setText(Integer.toString(this.raytracer.getDepth()));

        Vector3 startColor = this.raytracer.getStartColor();
        this.startColor[0].setText(Double.toString(startColor.getX()));
        this.startColor[1].setText(Double.toString(startColor.getY()));
        this.startColor[2].setText(Double.toString(startColor.getZ()));

        Vector3 endColor = this.raytracer.getEndColor();
        this.endColor[0].setText(Double.toString(endColor.getX()));
        this.endColor[1].setText(Double.toString(endColor.getY()));
        this.endColor[2].setText(Double.toString(endColor.getZ()));
    }

    public void applyFields() throws NumberFormatException
    {
        int samples = Integer.parseInt(this.samples.getText());
        int depth = Integer.parseInt(this.depth.getText());

        Vector3 startColor = new Vector3(
                Double.parseDouble(this.startColor[0].getText()),
                Double.parseDouble(this.startColor[1].getText()),
                Double.parseDouble(this.startColor[2].getText()));

        Vector3 endColor = new Vector3(
                Double.parseDouble(this.endColor[0].getText()),
                Double.parseDouble(this.endColor[1].getText()),
                Double.parseDouble(this.endColor[2].getText()));

        this.raytracer.setSamples(samples);
        this.raytracer.setDepth(depth);
        this.raytracer.setStartColor(startColor);
        this.raytracer.setEndColor(endColor);
    }
}

