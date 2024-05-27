package kz.sn34.raytrace.components;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.hitables.*;
import kz.sn34.raytrace.materials.*;
import kz.sn34.raytrace.components.util.EntryType;
import kz.sn34.raytrace.event.NumberListener;

@SuppressWarnings("serial")
public class AddObjectPanel extends JPanel
{
    private JTextField[] center;
    private JTextField[] coefficients;
    private JTextField constant;

    public AddObjectPanel()
    {
        super(new GridBagLayout());
        constant = new JTextField();
        constant.addKeyListener(new NumberListener(true, false));

        center = new JTextField[3];
        coefficients = new JTextField[3];

        for(int i = 0; i < 3; i++)
        {
            center[i] = new JTextField();
            coefficients[i] = new JTextField();

            center[i].addKeyListener(new NumberListener(true, true));
            coefficients[i].addKeyListener(
                    new NumberListener(true, true));
        }
    }

    public void init(EntryType type)
    {
        super.removeAll();

        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.weightx = 0;
        titleConstraints.gridwidth = GridBagConstraints.REMAINDER;

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.gridwidth = GridBagConstraints.REMAINDER;

        this.add(new JLabel("Center - (X, Y, Z)"), 
                titleConstraints);
        for(int i = 0; i < center.length; i++)
        {
            this.add(center[i], fieldConstraints);
        }

        if(type == EntryType.SPHERE)
        {
            String constantName;
            if(type == EntryType.SPHERE)
                constantName = "Radius";
            else
                constantName = "Constant";

            this.add(new JLabel(constantName), titleConstraints);
            this.add(constant, fieldConstraints);

        }
        else if(type == EntryType.SPHEROID)
        {
            this.add(new JLabel("Coefficients - (X, Y, Z)"), 
                    titleConstraints);
            for(int i = 0; i < coefficients.length; i++)
            {
                this.add(coefficients[i], fieldConstraints);
            }
        }
    }

    public Hitable createObject(EntryType type)
    {
        double constant = 0;
        Vector3 center = new Vector3();
        Vector3 coefficients = new Vector3();
        Hitable newWorldObject;

        try {
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
                newWorldObject = new Sphere(center, constant,
                        new Lambertian(new Vector3(0.5)));
            }
            else
            {
                newWorldObject = new Spheroid(coefficients, center,
                        new Lambertian(new Vector3(0.5))); 
            }
        }
        catch(NumberFormatException e)
        {
            return null;
        }

        return newWorldObject;
    }
}
