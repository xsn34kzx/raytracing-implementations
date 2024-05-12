package kz.sn34.raytrace_java_lib;

import java.io.*;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.*;
import java.util.ArrayList;


public class Raytracer
{
    private HitableList world;
    private ArrayList<WorldEntry> entries; 
    private Camera cam;

    private int width;
    private double subWidth;

    private int height;
    private double subHeight;

    private int depth;
    private int samples;

    //TODO: Background/LERP settings

    public Raytracer()
    {
        this.width = 200;
        this.height = 200;

        this.samples = 25;
        this.depth = 50;

        Vector3 lookFrom = new Vector3(-2, 2, 5);
        Vector3 lookAt = new Vector3(0, 2, -1);

        this.cam = new Camera(lookFrom, lookAt, new Vector3(0, 1, 0),
                45, (double) width / height, 0, 
                (lookFrom.subtract(lookAt).getMagnitude()));

        // Default World
        this.world = new HitableList();
        this.entries = new ArrayList<WorldEntry>();

        this.world.add(new Sphere(new Vector3(1, 2.5, -1.5), 0.5,
                    new Lambertian(new Vector3(0.1, 0.2, 0.5))));
        this.entries.add(new WorldEntry(false, 0, 
                    EntryType.SPHERE, this.world.getHitable(0)));
        this.entries.add(new WorldEntry(true, 0,
                    EntryType.LAMBERTIAN, this.world.getHitable(0)));

        this.world.add(new Spheroid(new Vector3(0.25, 1, 1),
                    new Vector3(0, 1, -1), new Lambertian(new Vector3(0.8, 0, 0.8))));
        this.entries.add(new WorldEntry(false, 1, 
                    EntryType.SPHEROID, this.world.getHitable(1)));
        this.entries.add(new WorldEntry(true, 1,
                    EntryType.LAMBERTIAN, this.world.getHitable(1)));

        this.world.add(new Sphere(new Vector3(0, -100.5, -1), 100,
                    new Lambertian(new Vector3(0.8, 0.8, 0))));
        this.entries.add(new WorldEntry(false, 2, 
                    EntryType.SPHERE, this.world.getHitable(2)));
        this.entries.add(new WorldEntry(true, 2,
                    EntryType.LAMBERTIAN, this.world.getHitable(2)));
    }

    public Camera getCamera()
    {
        return this.cam;
    }

    public ArrayList<WorldEntry> getWorldEntries()
    {
        return this.entries;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public double getSubWidth()
    {
        return this.subWidth;
    }

    public double getSubHeight()
    {
        return this.subHeight;
    }

    public int getSamples()
    {
        return this.samples;
    }

    public int getDepth()
    {
        return this.depth;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void refreshDimensionDependents()
    {
        double effWidth = this.width - 1;
        double pixelWidth = 1 / effWidth;
        this.subWidth = pixelWidth / (this.samples - 1);

        double effHeight = this.height - 1;
        double pixelHeight = 1 / effHeight;
        this.subHeight = pixelHeight / (this.samples - 1);
    }

    public void deleteWorldEntry(WorldEntry parentEntry)
    {
        int index = parentEntry.getIndex();
        int entryIndex = 2 * index;

        this.entries.remove(entryIndex);
        this.entries.remove(entryIndex);

        if(index + 1 != this.world.getSize())
        {
            int j = index;
            for(int i = entryIndex; i + 1 < this.entries.size(); i += 2)
            {
                this.entries.get(i).setIndex(j);
                this.entries.get(i + 1).setIndex(j++);
            }
        }

        this.world.removeHitable(index);
    }

    public void exportWorld(File file) throws IOException
    {
        FileOutputStream exportStream = new FileOutputStream(file);
        ObjectOutputStream objectWriter = new ObjectOutputStream(exportStream);

        objectWriter.writeObject(new WorldSnapshot(this.world, this.entries));

        objectWriter.close();
        exportStream.close();
    }

    public void importWorld(File file) throws IOException, ClassNotFoundException
    {
        FileInputStream importStream = new FileInputStream(file);
        ObjectInputStream objectReader = new ObjectInputStream(importStream);

        WorldSnapshot snapshot = (WorldSnapshot) objectReader.readObject();
        objectReader.close();
        importStream.close();

        this.entries = snapshot.getWorldEntries();
        this.world = snapshot.getWorld();
    }

    public BufferedImage render()
    {
        BufferedImage img = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_RGB);
        
        this.refreshDimensionDependents();
        SampleThread.setRaytracer(this);

        double effHeight = height - 1;
        double effWidth = width - 1;

        double samplesSqr = samples * samples;

        int threadAmount = (samples >= 20) ? Math.floorDiv(samples, 10) : 1;
        int intervalLength = Math.round((float) samples / threadAmount);
        SampleThread[] threads = new SampleThread[threadAmount];

        for(double row = 0; row <= effHeight; row++)
        {
            double v = row / effHeight;
            SampleThread.setV(v);

            for(double col = 0; col <= effWidth; col++)
            {
                double u = col / effWidth;
                SampleThread.setU(u);

                Vector3 pixelPercents = new Vector3();
                if(threadAmount > 1)
                {
                    SampleThread.setDestination(pixelPercents);
                    int intervalStart = 0;
                    for(int i = 0; i < threads.length; i++)
                    {
                        if(i + 1 == threads.length)
                        {
                            threads[i] = new SampleThread(intervalStart, 
                                    samples);
                        }
                        else
                        {
                            threads[i] = new SampleThread(intervalStart,
                                    intervalStart + intervalLength);
                        }

                        intervalStart += intervalLength + 1;
                        threads[i].start();
                    }

                    for(int i = 0; i < threads.length; i++)
                    {
                        try {
                            threads[i].join();
                        }
                        catch(Exception e)
                        {}
                    }
                }
                else
                {
                    for(double subRow = 0; subRow < this.samples; subRow++)
                    {
                        double subV = v + subRow * subHeight;

                        for(double subCol = 0; subCol < this.samples; subCol++)
                        {
                            double subU = u + subCol * subWidth;

                            pixelPercents.addEquals(this.color(
                                        cam.getRay(subU, subV), this.depth));
                        }
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

        return img;
    }

    public BufferedImage quickRender()
    {
        BufferedImage img = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_RGB);

        double effHeight = height - 1;
        double effWidth = width - 1;

        for(double row = 0; row <= effHeight; row++)
        {
            double v = row / effHeight;

            for(double col = 0; col <= effWidth; col++)
            {
                double u = col / effWidth;

                Vector3 pixelPercents = this.quickColor(cam.getRay(u, v));

                Color pixel = new Color(
                        (int) (pixelPercents.getX() * 255),
                        (int) (pixelPercents.getY() * 255),
                        (int) (pixelPercents.getZ() * 255)
                        );

                img.setRGB((int) col, (int) row, pixel.getRGBValue());
            }
        }

        return img;
    }
    
    public Vector3 getColor(double u, double v)
    {
        return this.color(this.cam.getRay(u, v), this.depth);
    }

    private Vector3 color(Ray r, int depth)
    {
        if(depth <= 0)
            return new Vector3();

        HitRecord curRec = new HitRecord();

        if(this.world.hit(r, Hitable.TMIN, Hitable.TMAX, curRec))
        {
            Ray scattered = new Ray();
            Vector3 attenuation = new Vector3();
            Material recMat = curRec.getMaterial();

            if(recMat.scatter(r, curRec, attenuation, scattered))
            {
                return attenuation.multiply(
                        color(scattered, depth - 1));
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

    private Vector3 quickColor(Ray r)
    {
        HitRecord curRec = new HitRecord();

        if(this.world.hit(r, Hitable.TMIN, Hitable.TMAX, curRec))
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

class SampleThread extends Thread
{
    private static Vector3 destination;
    private static Raytracer raytracer;
    private static double u;
    private static double v;

    private int start;
    private int end;

    public SampleThread(int start, int end)
    {
        this.start = start;
        this.end = end;
    }

    static public void setDestination(Vector3 destination)
    {
        SampleThread.destination = destination;
    }

    static public void setRaytracer(Raytracer raytracer)
    {
        SampleThread.raytracer = raytracer;
    }

    static public void setU(double u)
    {
        SampleThread.u = u;
    }

    static public void setV(double v)
    {
        SampleThread.v = v;
    }

    @Override
    public void run()
    {
        double subWidth = raytracer.getSubWidth();
        double subHeight = raytracer.getSubHeight();
        int samples = raytracer.getSamples();

        for(double subRow = start; subRow < end; subRow++)
        {
            double subV = v + subRow * subHeight;

            for(double subCol = 0; subCol < samples; subCol++)
            {
                double subU = u + subCol * subWidth;

                Vector3 colorPercents = raytracer.getColor(subU, subV);

                synchronized(destination)
                {
                    destination.addEquals(colorPercents);
                }
            }
        }
    }
}

class WorldSnapshot implements Serializable
{
    private static final long serialVersionUID = 1L;

    private HitableList world;
    private ArrayList<WorldEntry> entries;

    public WorldSnapshot(HitableList world, ArrayList<WorldEntry> entries)
    {
        this.world = world;
        this.entries = entries;
    }

    public ArrayList<WorldEntry> getWorldEntries()
    {
        return this.entries;
    }

    public HitableList getWorld()
    {
        return this.world;
    }
}
