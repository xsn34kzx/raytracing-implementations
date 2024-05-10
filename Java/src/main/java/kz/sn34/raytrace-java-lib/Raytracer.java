package kz.sn34.raytrace_java_lib;

import java.io.*;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.*;

public class Raytracer 
{
    private HitableList world;
    private Camera cam;
    private int width;
    private int height;

    //TODO: Background/LERP settings

    public Raytracer()
    {
        this.width = 100;
        this.height = 100;
        Vector3 lookFrom = new Vector3(-2, 2, 5);
        Vector3 lookAt = new Vector3(0, 2, -1);

        this.cam = new Camera(lookFrom, lookAt, new Vector3(0, 1, 0),
                45, (double) width / height, 0, 
                (lookFrom.subtract(lookAt).getMagnitude()));

        this.world = new HitableList();
        this.world.add(new Sphere(new Vector3(1, 2.5, -1.5), 0.5,
                    new Lambertian(new Vector3(0.1, 0.2, 0.5))));
        this.world.add(new Spheroid(new Vector3(0.25, 1, 1),
                    new Vector3(0, 1, -1), new Lambertian(new Vector3(0.8, 0, 0.8))));

        this.world.add(new Sphere(new Vector3(0, -100.5, -1), 100,
                    new Lambertian(new Vector3(0.8, 0.8, 0))));
    }

    public Camera getCamera()
    {
        return this.cam;
    }

    public BufferedImage render()
    {
        String testPath = "./img/appTest.png";

        BufferedImage img = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_RGB);

        File outputFolder = new File("./img");
        if(!outputFolder.exists())
            outputFolder.mkdir();

        File pngFile = new File(testPath);

        double effHeight = height - 1;
        double effWidth = width - 1;

        double pixelHeight = 1 / effHeight; 
        double pixelWidth = 1 / effWidth;

        double samples = 25;
        double samplesSqr = samples * samples;

        double subPixelHeight = pixelHeight / (samples - 1);
        double subPixelWidth = pixelWidth / (samples - 1);

        for(double row = 0; row <= effHeight; row++)
        {
            double v = row / effHeight;

            for(double col = 0; col <= effWidth; col++)
            {
                double u = col / effWidth;

                Vector3 pixelPercents = new Vector3();

                for(double subRow = 0; subRow < samples; subRow++)
                {
                    double subV = v + subRow * subPixelHeight;

                    for(double subCol = 0; subCol < samples; subCol++)
                    {
                        double subU = u + subCol * subPixelWidth;

                        pixelPercents = pixelPercents.add(
                                this.rayColor(cam.getRay(subU, subV),
                                    world, 50));
                    }
                }

                pixelPercents = pixelPercents.divide(samplesSqr).sqrt();

                Color pixel = new Color(
                        (int) (pixelPercents.getX() * 255),
                        (int) (pixelPercents.getY() * 255),
                        (int) (pixelPercents.getZ() * 255)
                        );

                img.setRGB((int) col, (int) row, pixel.getRGBValue());
            }
        }

        //ImageIO.write(img, "PNG", pngFile);

        return img;
    }

    public BufferedImage quickRender()
    {
        String testPath = "./img/appQuickTest.png";

        BufferedImage img = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_RGB);

        File outputFolder = new File("./img");
        if(!outputFolder.exists())
            outputFolder.mkdir();

        File pngFile = new File(testPath);

        double effHeight = height - 1;
        double effWidth = width - 1;

        for(double row = 0; row <= effHeight; row++)
        {
            double v = row / effHeight;

            for(double col = 0; col <= effWidth; col++)
            {
                double u = col / effWidth;

                Vector3 pixelPercents = this.quickColor(cam.getRay(u, v), world);

                Color pixel = new Color(
                        (int) (pixelPercents.getX() * 255),
                        (int) (pixelPercents.getY() * 255),
                        (int) (pixelPercents.getZ() * 255)
                        );

                img.setRGB((int) col, (int) row, pixel.getRGBValue());
            }
        }

        //ImageIO.write(img, "PNG", pngFile);

        return img;
    }
    private Vector3 rayColor(Ray r, Hitable world, int depth)
    {
        if(depth <= 0)
            return new Vector3();

        HitRecord curRec = new HitRecord();

        if(world.hit(r, Hitable.TMIN, Hitable.TMAX, curRec))
        {
            Ray scattered = new Ray();
            Vector3 attenuation = new Vector3();
            Material recMat = curRec.getMaterial();

            if(recMat.scatter(r, curRec, attenuation, scattered))
            {
                return attenuation.multiply(
                        rayColor(scattered, world, depth - 1));
            }

            return new Vector3(1);
        }
        else
        {
            Ray colorPercents = new Ray(new Vector3(1),
                    new Vector3(0.5, 0.7, 1));
            double t = 0.5 * (r.getDirection().getUnitVector().getY() + 1);

            return colorPercents.lerp(t);
        }
    }

    private Vector3 quickColor(Ray r, Hitable world)
    {
        HitRecord curRec = new HitRecord();

        if(world.hit(r, Hitable.TMIN, Hitable.TMAX, curRec))
        {
            return curRec.getMaterial().getAlbedo();
        }
        else
        {
            Ray colorPercents = new Ray(new Vector3(1),
                    new Vector3(0.5, 0.7, 1));
            double t = 0.5 * (r.getDirection().getUnitVector().getY() + 1);

            return colorPercents.lerp(t);
        }
    }
}
