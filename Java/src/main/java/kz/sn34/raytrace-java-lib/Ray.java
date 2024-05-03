package kz.sn34.raytrace_java_lib;

public class Ray
{
    private Vector3 origin;
    private Vector3 direction;

    public Ray()
    {
        this.origin = new Vector3();
        this.direction = new Vector3();
    }

    public Ray(Vector3 origin, Vector3 direction)
    {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3 getOrigin()
    {
        return this.origin;
    }

    public Vector3 getDirection()
    {
        return this.direction;
    }

    public void setOrigin(Vector3 origin)
    {
        this.origin = origin;
    }

    public void setDirection(Vector3 direction)
    {
        this.direction = direction;
    }

    public Vector3 pointAt(double t)
    {
        return this.origin.add(this.direction.multiply(t));
    }

    public Vector3 lerp(double t)
    {
        return this.origin.multiply(1 - t).add(this.direction.multiply(t));
    }
}
