package kz.sn34.raytrace_java_lib;

import java.util.Random;

public class Vector2
{
    private double[] vec;

    public Vector2(double a)
    {
        this(a, a);
    }

    public Vector2(double a, double b)
    {
        this.vec = new double[2];
        this.vec[0] = a;
        this.vec[1] = b;
    }

    public Vector2 add(Vector2 v)
    {
        return new Vector2(
                this.vec[0] + v.vec[0],
                this.vec[1] + v.vec[1]);
    }

    public Vector2 multiply(double c)
    {
        return new Vector2(this.vec[0] * c, this.vec[1] * c);
    }

    public Vector2 divide(double c)
    {
        return new Vector2(this.vec[0] / c, this.vec[1] / c);
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.vec[0] * this.vec[0]
                + this.vec[1] * this.vec[1]);
    }

    public double getMagnitudeSquared()
    {
        return this.vec[0] * this.vec[0] + this.vec[1] * this.vec[1];
    }

    public Vector2 getUnitVector()
    {
        return this.divide(this.getMagnitude());
    }

    public Vector3 toVector3()
    {
        return new Vector3(this.vec[0], this.vec[1], 0);
    }

    static public Vector2 random(double min, double max)
    {
        Random rng = new Random();

        double difference = max - min;

        Vector2 randomVector = new Vector2(rng.nextDouble(), rng.nextDouble());

        return randomVector.multiply(difference).add(new Vector2(min));
    }

    static public Vector2 getRandomInUnitCircle()
    {
        Vector2 randVector;
        do {
            randVector = Vector2.random(-1, 1);
        } while(randVector.getMagnitudeSquared() >= 1);

        return randVector;
    }
}
