package kz.sn34.raytrace.components;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import kz.sn34.raytrace.Camera;
import kz.sn34.raytrace.Raytracer;
import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.event.NumberListener;

@SuppressWarnings("serial")
public class CameraPanel extends JPanel
{
    private Camera cam;
    private Raytracer raytracer;
    private JTextField width;
    private JTextField height;
    private JTextField verticalFOV;
    private JTextField lensAperture;
    private JTextField[] lookAt;
    private JTextField[] lookFrom;
    private JTextField[] camUp;
    
    public CameraPanel(Camera cam, Raytracer raytracer)
    {
        super(new GridBagLayout());
        this.cam = cam;
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
        width = new JTextField();
        width.addKeyListener(new NumberListener(false, false));

        height = new JTextField();
        height.addKeyListener(new NumberListener(false, false));

        verticalFOV = new JTextField();
        verticalFOV.addKeyListener(new NumberListener(true, false));

        lensAperture = new JTextField();
        lensAperture.addKeyListener(new NumberListener(true, false));

        lookAt = new JTextField[3];
        lookFrom = new JTextField[3];
        camUp = new JTextField[3];
        for(int i = 0; i < 3; i++)
        {
            lookAt[i] = new JTextField();
            lookFrom[i] = new JTextField();
            camUp[i] = new JTextField();

            lookAt[i].addKeyListener(new NumberListener(true, true));
            lookFrom[i].addKeyListener(new NumberListener(true, true));
            camUp[i].addKeyListener(new NumberListener(true, true));
        }

        // Putting components together
        super.add(new JLabel("Image Width (px)"), titleConstraints);
        super.add(width, fieldConstraints);

        titleConstraints.insets = new Insets(25, 0, 0, 0);

        super.add(new JLabel("Image Height (px)"), titleConstraints);
        super.add(height, fieldConstraints);

        super.add(new JLabel("Look From - (X, Y, Z)"), titleConstraints);
        for(int i = 0; i < lookFrom.length; i++)
        {
            super.add(lookFrom[i], fieldConstraints);
        }

        super.add(new JLabel("Look At - (X, Y, Z)"), titleConstraints);
        for(int i = 0; i < lookAt.length; i++)
        {
            super.add(lookAt[i], fieldConstraints);
        }

        super.add(new JLabel("Cam Up - (X, Y, Z)"), titleConstraints);
        for(int i = 0; i < camUp.length; i++)
        {
            super.add(camUp[i], fieldConstraints);
        }

        super.add(new JLabel("Vertical Field of Vision (VFOV)"), titleConstraints);
        super.add(verticalFOV, fieldConstraints);

        super.add(new JLabel("Lens Aperture"), titleConstraints);
        super.add(lensAperture, fieldConstraints);
    }

    public void populateFields()
    {
        this.width.setText(Integer.toString(this.raytracer.getWidth()));
        this.height.setText(Integer.toString(this.raytracer.getHeight()));

        this.lensAperture.setText(Double.toString(this.cam.getLensAperture()));
        this.verticalFOV.setText(Double.toString(this.cam.getVFOV()));

        Vector3 lookFrom = this.cam.getLookFrom();
        this.lookFrom[0].setText(Double.toString(lookFrom.getX()));
        this.lookFrom[1].setText(Double.toString(lookFrom.getY()));
        this.lookFrom[2].setText(Double.toString(lookFrom.getZ()));

        Vector3 lookAt = this.cam.getLookAt();
        this.lookAt[0].setText(Double.toString(lookAt.getX()));
        this.lookAt[1].setText(Double.toString(lookAt.getY()));
        this.lookAt[2].setText(Double.toString(lookAt.getZ()));

        Vector3 camUp = this.cam.getCamUp();
        this.camUp[0].setText(Double.toString(camUp.getX()));
        this.camUp[1].setText(Double.toString(camUp.getY()));
        this.camUp[2].setText(Double.toString(camUp.getZ()));
    }

    public void applyFields() throws NumberFormatException
    {
        int width = Math.min(Integer.parseInt(this.width.getText()), 
                super.getSize().width);
        int height = Math.min(Integer.parseInt(this.height.getText()),
                super.getSize().height);

        double lensAperture = Double.parseDouble(this.lensAperture.getText());
        double verticalFOV = Double.parseDouble(this.verticalFOV.getText());

        Vector3 lookFrom = new Vector3(
                Double.parseDouble(this.lookFrom[0].getText()),
                Double.parseDouble(this.lookFrom[1].getText()),
                Double.parseDouble(this.lookFrom[2].getText()));

        Vector3 lookAt = new Vector3(
                Double.parseDouble(this.lookAt[0].getText()),
                Double.parseDouble(this.lookAt[1].getText()),
                Double.parseDouble(this.lookAt[2].getText()));

        Vector3 camUp = new Vector3(
                Double.parseDouble(this.camUp[0].getText()),
                Double.parseDouble(this.camUp[1].getText()),
                Double.parseDouble(this.camUp[2].getText()));

        this.cam.copy(new Camera(lookFrom, lookAt, camUp, verticalFOV,
                    (double) width / height, lensAperture, 
                    (lookFrom.subtract(lookAt).getMagnitude())));

        this.raytracer.setWidth(width);
        this.raytracer.setHeight(height);
        this.raytracer.refreshDimensionDependents();
    }
}
