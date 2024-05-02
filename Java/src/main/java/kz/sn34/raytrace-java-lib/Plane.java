package kz.sn34.raytrace_java_lib;

public class Plane implements Hitable
{
    private Vector3 center;
    private Vector3 coefficients;
    private double constant;

    public Plane()
    {
        this(0);
    }

    public Plane(double constant)
    {
        this(new Vector3(1), constant);
    }

    public Plane(Vector3 coefficients, double constant)
    {
        this(new Vector3(), coefficients, constant);
    }

    public Plane(Vector3 center, Vector3 coefficients, double constant)
    {
        this.center = center;
        this.coefficients = coefficients;
        this.constant = constant;
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec)
    {
        Vector3 originMinusCenter = r.getOrigin().subtract(this.center);
        Vector3 direction = r.getDirection();

        double termOne = originMinusCenter.dot(this.coefficients);
        double termTwo = direction.dot(this.coefficients);

        if(termTwo != 0)
        {
            double root = (-this.constant + termOne) / termTwo;

            if(root < tMax && root > tMin)
            {
                rec.setT(root);
                rec.setPoint(r.pointAt(root));
                rec.setNormal(this.coefficients.getUnitVector());

                return true;
            }
        }

        return false;
    }
}
