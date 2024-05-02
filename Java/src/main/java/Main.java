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

                Ray originToDir = new Ray();

                Vector3 upLeftPlane = new Vector3(-1, 1, -1);
                Vector3 deltaX = new Vector3(2, 0, 0);
                Vector3 deltaY = new Vector3(0, -2, 0);

                HitableList world = new HitableList();
                world.add(new Spheroid(new Vector3(4, 1,1), 
                            new Vector3(0, 0, -1), 0.5));
                world.add(new Sphere(new Vector3(-0.75, 0, -1.5), 0.25));
                world.add(new Plane(new Vector3(0, -1, -0.25), -1));

                double effHeight = height - 1;
                double effWidth = width - 1;

                for(double row = 0; row <= effHeight; row++)
                {
                    double v = row / effHeight;

                    for(double col = 0; col <= effWidth; col++)
                    {
                        double u = col / effWidth;

                        Vector3[] terms = {deltaX.multiply(u), 
                            deltaY.multiply(v)};
                        originToDir.setDirection(upLeftPlane.add(terms));

                        Vector3 pixelPercents = Main.color(originToDir, world);

                        Color pixel = new Color(
                                (int) (pixelPercents.getX() * 255),
                                (int) (pixelPercents.getY() * 255),
                                (int) (pixelPercents.getZ() * 255));

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

    static private Vector3 color(Ray r, Hitable world)
    {
        HitRecord finalRec = new HitRecord();

        if(world.hit(r, Hitable.TMIN, Hitable.TMAX, finalRec))
        {
            Vector3 finalNormal = finalRec.getNormal();
            return new Vector3(finalNormal.getX() + 1, finalNormal.getY() + 1,
                    finalNormal.getZ() + 1).divide(2);
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
