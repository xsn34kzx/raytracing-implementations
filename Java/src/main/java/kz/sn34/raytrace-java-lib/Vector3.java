package kz.sn34.raytrace_java_lib;

import java.util.Random;

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

    public Vector3 subtract(Vector3 v)
    {
        return new Vector3(
                this.vec[0] - v.vec[0],
                this.vec[1] - v.vec[1],
                this.vec[2] - v.vec[2]);
    }

    public double dot(Vector3 v)
    {
        return this.vec[0] * v.vec[0] + this.vec[1] * v.vec[1]
            + this.vec[2] * v.vec[2];
    }

    public double weightedDot(Vector3 v, Vector3 weights)
    {
        return weights.vec[0] * this.vec[0] * v.vec[0]
            + weights.vec[1] * this.vec[1] * v.vec[1]
            + weights.vec[2] * this.vec[2] * v.vec[2];
    }

    public Vector3 multiply(double c)
    {
        return new Vector3(this.vec[0] * c, this.vec[1] * c, this.vec[2] * c);
    }

    public Vector3 multiply(Vector3 v)
    {
        return new Vector3(this.vec[0] * v.vec[0],
                this.vec[1] * v.vec[1],
                this.vec[2] * v.vec[2]);
    }

    public Vector3 divide(double c)
    {
        return new Vector3(this.vec[0] / c, this.vec[1] / c, this.vec[2] / c);
    }

    public Vector3 pow(double exponent)
    {
        return new Vector3(Math.pow(this.vec[0], exponent),
                Math.pow(this.vec[1], exponent),
                Math.pow(this.vec[2], exponent));
    }

    public Vector3 sqrt()
    {
        return new Vector3(Math.sqrt(this.vec[0]), Math.sqrt(this.vec[1]),
                Math.sqrt(this.vec[2]));
    }

    public Vector3 reflect(Vector3 normal)
    {
        return this.subtract(normal.multiply(2 * this.dot(normal)));
    }

    public Vector3 refract(Vector3 normal, double refractionIndex)
    {
        double cosTheta = Math.min(this.multiply(-1).dot(normal), 1);

        Vector3 rayOutPerpendicular = this.add(
            normal.multiply(cosTheta)).multiply(refractionIndex);
        Vector3 rayOutParallel = normal.multiply(-Math.sqrt(
            Math.abs(1 - rayOutPerpendicular.getMagnitudeSquared())));

        return rayOutPerpendicular.add(rayOutParallel);
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

    public void copy(Vector3 v)
    {
        this.vec[0] = v.vec[0];
        this.vec[1] = v.vec[1];
        this.vec[2] = v.vec[2];
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.vec[0] * this.vec[0]
                + this.vec[1] * this.vec[1]
                + this.vec[2] * this.vec[2]);
    }

    public double getMagnitudeSquared()
    {
        return this.vec[0] * this.vec[0]
            + this.vec[1] * this.vec[1]
            + this.vec[2] * this.vec[2];
    }

    public Vector3 getUnitVector()
    {
        return this.divide(this.getMagnitude());
    }

    static public Vector3 random(double min, double max)
    {
        Random rng = new Random();

        double difference = max - min;

        Vector3 randomVector = new Vector3(rng.nextDouble(), rng.nextDouble(),
                rng.nextDouble());

        return randomVector.multiply(difference).add(new Vector3(min));
    }

    static public Vector3 getRandomUnitVector()
    {
        Vector3 randVector;
        do {
            randVector = Vector3.random(-1, 1);
        } while(randVector.getMagnitudeSquared() >= 1);

        return randVector.getUnitVector();
    }

    static boolean isNearZero(Vector3 v)
    {
        double min = 1e-8;

        return (Math.abs(v.vec[0]) < min)
            && (Math.abs(v.vec[1]) < min)
            && (Math.abs(v.vec[2]) < min);
    }
}
