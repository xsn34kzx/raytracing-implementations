package kz.sn34.raytrace_java_lib;

public class Vector3
{
    private double[] vec;

    public Vector3()
    {
        this(0);
    }

    public Vector3(double a, double b, double c)
    {
        this.vec = new double[3];
        this.vec[0] = a;
        this.vec[1] = b;
        this.vec[2] = c;
    }

    public Vector3(double a)
    {
        this.vec = new double[3];
        this.vec[0] = this.vec[1] = this.vec[2] = a;
    }

    public Vector3 multiply(double c)
    {
        return new Vector3(this.vec[0] * c, this.vec[1] * c, this.vec[2] * c);
    }

    public Vector3 divide(double c)
    {
        return new Vector3(this.vec[0] / c, this.vec[1] / c, this.vec[2] / c);
    }

    public Vector3 add(Vector3 v)
    {
        return new Vector3(
                this.vec[0] + v.vec[0],
                this.vec[1] + v.vec[1],
                this.vec[2] + v.vec[2]);
    }

    public Vector3 add(Vector3[] terms)
    {
        double x = this.vec[0];
        double y = this.vec[1];
        double z = this.vec[2];

        for(int i = 0; i < terms.length; i++)
        {
            x += terms[i].vec[0];
            y += terms[i].vec[1];
            z += terms[i].vec[2];
        }

        return new Vector3(x, y, z);
    }

    public double getX()
    {
        return this.vec[0];
    }

    public double getY()
    {
        return this.vec[1];
    }

    public double getZ()
    {
        return this.vec[2];
    }

    public double getMagnitude()
    {
        return Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
    }

    public Vector3 getUnitVector()
    {
        return this.divide(this.getMagnitude());
    }
}
