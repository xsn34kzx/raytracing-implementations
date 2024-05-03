import kz.sn34.raytrace_java_lib.*;
import java.io.*;

public class Main
{
    public static void main(String[] args)
    {
        if(args.length == 3)
        {
            try {
                int width = Integer.parseInt(args[0]);
                int height = Integer.parseInt(args[1]);
                String fullPath = "./img/" + args[2] + ".ppm"; 

                File outputFolder = new File("./img");
                if(!outputFolder.exists())
                    outputFolder.mkdir();

                File ppmFile = new File(fullPath);
                ppmFile.createNewFile();
                PrintWriter ppmOutput = new PrintWriter(
                        new FileOutputStream(ppmFile));

                ppmOutput.printf("P3\n%d %d\n255\n", width, height);

                Camera cam = new Camera();

                HitableList world = new HitableList();
                world.add(new Sphere(new Vector3(0, 0, -1), 0.5,
                            new Lambertian(new Vector3(0.7, 0.3, 0.3))));
                //world.add(new Spheroid(new Vector3(1, 1,1), 
                //            new Vector3(0, 0, -1), 0.5));
                //world.add(new Sphere(new Vector3(-0.75, 0, -1.5), 0.25));
                world.add(new Sphere(new Vector3(0, -100.5, -1), 100,
                            new Lambertian(new Vector3(0.8, 0.8, 0))));
                //world.add(new Plane(new Vector3(0, -1, -0.25), -1));
                world.add(new Sphere(new Vector3(-1, 0, -1), 0.5,
                            new Metal(new Vector3(0.8, 0.8, 0.8), 0.3)));
                world.add(new Sphere(new Vector3(1, 0, -1), 0.5,
                            new Metal(new Vector3(0.8, 0.6, 0.2), 1)));

                double effHeight = height - 1;
                double effWidth = width - 1;

                double pixelHeight = 1 / effHeight; 
                double pixelWidth = 1 / effWidth;

                double samples = 5;
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
                                        Main.rayColor(cam.getRay(subU, subV),
                                        world, 50));
                            }
                        }

                        pixelPercents = pixelPercents.divide(samplesSqr)
                            .pow(0.5);

                        Color pixel = new Color(
                                (int) (pixelPercents.getX() * 255),
                                (int) (pixelPercents.getY() * 255),
                                (int) (pixelPercents.getZ() * 255)
                                );

                        ppmOutput.println(pixel);
                    }
                }

                ppmOutput.close();
            }
            catch(NumberFormatException e)
            {
                System.out.println("Incorrect arguments given!");
            }
            catch(FileNotFoundException e)
            {
                System.out.println("File could not be found!");
            }
            catch(IOException e)
            {
                System.out.println("File could not be created!");
            }
        }
        else
            System.out.println("Only three arguments allowed!");
    }

    static private Vector3 rayColor(Ray r, Hitable world, int depth)
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

            return new Vector3();
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
