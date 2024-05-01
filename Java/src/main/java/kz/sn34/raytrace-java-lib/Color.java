package kz.sn34.raytrace_java_lib;

public class Color
{
    private int[] rgb;

    public Color()
    {
        this.rgb = new int[3];
    }

    public Color(int r, int g, int b)
    {
        this.rgb = new int[3];
        this.rgb[0] = r;
        this.rgb[1] = b;
        this.rgb[2] = g;
    }

    public int getRed()
    {
        return this.rgb[0];
    }

    public int getGreen()
    {
        return this.rgb[1];
    }

    public int getBlue()
    {
        return this.rgb[2];
    }

    @Override
    public String toString()
    {
        return String.format("%d %d %d", this.rgb[0], this.rgb[1], this.rgb[2]);
    }
}
