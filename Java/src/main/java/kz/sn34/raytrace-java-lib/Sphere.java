package kz.sn34.raytrace_java_lib;

public class Sphere implements Hitable
{
    private Vector3 center;
    private double radius;
    private Material mat;

    public Sphere(double radius, Material mat)
    {
        this(new Vector3(), radius, mat);
    }

    public Sphere(Vector3 center, double radius, Material mat)
    {
        this.radius = radius;
        this.center = center;
        this.mat = mat;
    }

    public Vector3 getCenter()
    {
        return this.center;
    }

    public double getRadius()
    {
        return this.radius;
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec)
    {
        Vector3 rayOriginMinusCenter = r.getOrigin().subtract(this.center);
        Vector3 rayDirection = r.getDirection();

        double a = rayDirection.getMagnitudeSquared();
        double b = rayDirection.dot(rayOriginMinusCenter);
        double c = rayOriginMinusCenter.getMagnitudeSquared()
            - radius * radius;

        double discriminant = b * b - a * c;

        if(discriminant > 0)
        {
            double sqrtDiscriminant = Math.sqrt(discriminant);

            double closestRoot = (-b - sqrtDiscriminant) / a;
            if(closestRoot < tMax && closestRoot > tMin)
            {
                Vector3 point = r.pointAt(closestRoot);

                rec.setT(closestRoot);
                rec.setPoint(point);
                rec.setNormal(point.subtract(this.center).divide(this.radius));
                rec.setMaterial(this.mat);

                return true;
            }

            double farthestRoot = (-b + sqrtDiscriminant) / a;
            if(farthestRoot < tMax && farthestRoot > tMin)
            {
                Vector3 point = r.pointAt(farthestRoot);

                rec.setT(farthestRoot);
                rec.setPoint(point);
                rec.setNormal(point.subtract(this.center).divide(this.radius));
                rec.setMaterial(this.mat);

                return true;
            }
        }
        
        return false;
    }
}
