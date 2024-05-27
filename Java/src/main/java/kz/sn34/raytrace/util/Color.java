package kz.sn34.raytrace.util;

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
        this.rgb[1] = g;
        this.rgb[2] = b;
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

    public int getRGBValue()
    {
        // Creating composite RGB integer based on default RGB ColorModel
        return (this.rgb[0] << 16) | (this.rgb[1] << 8) | this.rgb[2];
    }

    @Override
    public String toString()
    {
        return String.format("%d %d %d", this.rgb[0], this.rgb[1], this.rgb[2]);
    }
}
