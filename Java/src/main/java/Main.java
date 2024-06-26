import kz.sn34.raytrace_java_lib.*;
import java.io.*;
import java.util.Random;

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

                Vector3 lookFrom = new Vector3(-2, 2, 5);
                Vector3 lookAt = new Vector3(0, 2, -1);

                Camera cam = new Camera(lookFrom, lookAt, new Vector3(0, 1, 0),
                        45, (double) width / height, 0, 
                        (lookFrom.subtract(lookAt).getMagnitude()));

                HitableList world = new HitableList();
                world.add(new Sphere(new Vector3(1, 2.5, -1.5), 0.5,
                            new Lambertian(new Vector3(0.1, 0.2, 0.5))));
                world.add(new Spheroid(new Vector3(0.25, 1, 1),
                            new Vector3(0, 1, -1), new Lambertian(new Vector3(0.8, 0.8, 0))));

                world.add(new Sphere(new Vector3(0, -100.5, -1), 100,
                            new Lambertian(new Vector3(0.8, 0.8, 0))));
                /*
                world.add(new Sphere(new Vector3(-1, 0, -1), 0.4,
                            new Dielectric(1/1.5)));
                world.add(new Sphere(new Vector3(-1, 0, -1), 0.5,
                            new Dielectric(1.5)));
                world.add(new Sphere(new Vector3(1, 0, -1), 0.5,
                            new Metal(new Vector3(0.8, 0.6, 0.2), 1)));
                            */

                double effHeight = height - 1;
                double effWidth = width - 1;

                double pixelHeight = 1 / effHeight; 
                double pixelWidth = 1 / effWidth;

                double samples = 25;
                double samplesSqr = samples * samples;

                double subPixelHeight = pixelHeight / (samples - 1);
                double subPixelWidth = pixelWidth / (samples - 1);

                Random rng = new Random();

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

                        pixelPercents = pixelPercents.divide(samplesSqr).sqrt();

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
}
