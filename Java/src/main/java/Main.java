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

            for(int row = 0; row < height; row++)
            {
                for(int col = 0; col < width; col++)
                {
                    Color pixel = new Color(
                            (int) (col * 255.0 / (width - 1)),
                            (int) (row * 255.0 / (height - 1)),
                            0);

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
}
