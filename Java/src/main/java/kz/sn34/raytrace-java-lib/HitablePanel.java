package kz.sn34.raytrace_java_lib;

import javax.swing.*;
import java.awt.*;
import java.text.*;

public class HitablePanel extends JPanel
{
    private JTextField[] center;
    private JTextField[] coefficients;
    private JTextField constant;
    private WorldEntry entry;

    public HitablePanel(WorldEntry entry)
    {
        super(new GridBagLayout());
        this.entry = entry;

        // Constraints for components
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.weightx = 0;
        titleConstraints.gridwidth = GridBagConstraints.REMAINDER;

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;

        // Initializing components
        constant = new JTextField();
        constant.addKeyListener(new NumberListener(true, false));

        center = new JTextField[3];
        coefficients = new JTextField[3];
        for(int i = 0; i < 3; i++)
        {
            center[i] = new JTextField();
            coefficients[i] = new JTextField();

            center[i].addKeyListener(new NumberListener(true, true));
            coefficients[i].addKeyListener(new NumberListener(true, true));
        }

        // Putting components together
        if(entry.getType() != EntryType.SPHEROID)
        {
            String constantName;
            if(entry.getType() == EntryType.SPHERE)
                constantName = "Radius";
            else
                constantName = "Constant";

            super.add(new JLabel(constantName), titleConstraints);
            super.add(constant, fieldConstraints);
        }

        titleConstraints.insets = new Insets(25, 0, 0, 0);

        super.add(new JLabel("Center - (X, Y, Z)"), titleConstraints);
        for(int i = 0; i < center.length; i++)
        {
            super.add(center[i], fieldConstraints);
        }

        if(entry.getType() != EntryType.SPHERE)
        {
            super.add(new JLabel("Coefficients - (X, Y, Z)"), titleConstraints);
            for(int i = 0; i < coefficients.length; i++)
            {
                super.add(coefficients[i], fieldConstraints);
            }
        }
    }

    public void populateFields()
    {
        EntryType type = entry.getType();
        System.out.println(type);

        double constant = 0;
        Vector3 center = new Vector3();
        Vector3 coefficients = new Vector3();

        if(type == EntryType.SPHERE)
        {
            Sphere sphere = (Sphere) entry.getHitable();
            constant = sphere.getRadius();
            center = sphere.getCenter();
        }
        else if(type == EntryType.SPHEROID)
        {
            Spheroid spheroid = (Spheroid) entry.getHitable();
            center = spheroid.getCenter();
            coefficients = spheroid.getCoefficients();
        }

        if(type != EntryType.SPHEROID)
            this.constant.setText(Double.toString(constant));

        this.center[0].setText(Double.toString(center.getX()));
        this.center[1].setText(Double.toString(center.getY()));
        this.center[2].setText(Double.toString(center.getZ()));

        if(type != EntryType.SPHERE)
        { 
            this.coefficients[0].setText(Double.toString(coefficients.getX()));
            this.coefficients[1].setText(Double.toString(coefficients.getY()));
            this.coefficients[2].setText(Double.toString(coefficients.getZ()));
        }
    }

    public void applyFields() throws NumberFormatException
    {
        double constant = 0;
        Vector3 center = new Vector3();
        Vector3 coefficients = new Vector3();

        EntryType type = entry.getType();

        if(type != EntryType.SPHEROID)
            constant = Double.parseDouble(this.constant.getText());

        center = new Vector3(
                Double.parseDouble(this.center[0].getText()),
                Double.parseDouble(this.center[1].getText()),
                Double.parseDouble(this.center[2].getText()));

        if(type != EntryType.SPHERE)
        {
            coefficients = new Vector3(
                    Double.parseDouble(this.coefficients[0].getText()),
                    Double.parseDouble(this.coefficients[1].getText()),
                    Double.parseDouble(this.coefficients[2].getText()));
        }

        if(type == EntryType.SPHERE)
        {
            Sphere sphere = (Sphere) entry.getHitable();
            Sphere newSphere = new Sphere(center, constant, 
                    sphere.getMaterial());
            sphere.copy(newSphere);
        }
        else if(type == EntryType.SPHEROID)
        {
            Spheroid spheroid = (Spheroid) entry.getHitable();
            Spheroid newSpheroid = new Spheroid(coefficients, center,
                    spheroid.getMaterial());
            spheroid.copy(newSpheroid);
        }
    }
}
