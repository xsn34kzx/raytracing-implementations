import kz.sn34.raytrace_java_lib.*;
import java.io.*;

public class Main
{
    public static void main(String[] args)
    {
        int width = 200;
        int height = 100;

        File outputFolder = new File("./img");
        if(!outputFolder.exists())
            outputFolder.mkdir();

        File ppmFile = new File("./img/helloWorld.ppm");
        try {
            ppmFile.createNewFile();
            PrintWriter ppmOutput = new PrintWriter(
                    new FileOutputStream(ppmFile));

            ppmOutput.printf("P3\n%d %d\n255\n", width, height);

            Ray originToDir = new Ray();

            Vector3 upLeftPlane = new Vector3(-1, 1, -1);
            Vector3 deltaX = new Vector3(2, 0, 0);
            Vector3 deltaY = new Vector3(0, -2, 0);

            double effHeight = height - 1;
            double effWidth = width - 1;

            for(double row = 0; row <= effHeight; row++)
            {
                double v = row / effHeight;

                for(double col = 0; col <= effWidth; col++)
                {
                    double u = col / effWidth;

                    Vector3[] terms = {deltaX.multiply(u), deltaY.multiply(v)};
                    originToDir.setDirection(upLeftPlane.add(terms));

                    Vector3 pixelPercents = Main.color(originToDir);

                    Color pixel = new Color(
                        (int) (pixelPercents.getX() * 255),
                        (int) (pixelPercents.getY() * 255),
                        (int) (pixelPercents.getZ() * 255));

                    ppmOutput.println(pixel);
                }
            }

            ppmOutput.close();
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

    static private Vector3 color(Ray r)
    {
        if(Main.hitSphere(new Vector3(0, 0, -1), 0.5, r))
            return new Vector3(1, 0, 0);

        Ray colorPercents = new Ray(new Vector3(1), new Vector3(0.5, 0.7, 1));
        double t = 0.5 * (r.getDirection().getUnitVector().getY() + 1);

        return colorPercents.lerp(t);
    }

    static private boolean hitSphere(Vector3 center, double radius, Ray r)
    {
        Vector3 rayOriginMinusCenter = r.getOrigin().subtract(center);
        Vector3 rayDirection = r.getDirection();

        double a = rayDirection.dot(rayDirection);
        double b = 2 * rayDirection.dot(rayOriginMinusCenter);
        double c = rayOriginMinusCenter.dot(rayOriginMinusCenter)
            - radius * radius;

        double discriminant = b * b - 4 * a * c;

        return (b * b - 4 * a * c) > 0;
    }
}
