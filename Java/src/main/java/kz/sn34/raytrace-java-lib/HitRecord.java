package kz.sn34.raytrace_java_lib;

public class HitRecord
{
    private double t;
    private Vector3 point;
    private Vector3 normal;

    public HitRecord()
    {
        this.t = 0;
        this.point = new Vector3();
        this.normal = new Vector3();
    }

    // Need to copy into this record to replace pointer usage in C++ ver
    public void copy(HitRecord rec)
    {
        this.t = rec.t;
        this.point = rec.point;
        this.normal = rec.normal;
    }

    public double getT()
    {
        return this.t;
    }

    public Vector3 getPoint()
    {
        return this.point;
    }

    public Vector3 getNormal()
    {
        return this.normal;
    }

    public void setT(double t)
    {
        this.t = t;
    }

    public void setPoint(Vector3 point)
    {
        this.point = point;
    }

    public void setNormal(Vector3 normal)
    {
        this.normal = normal;
    }
}
