package kz.sn34.raytrace_java_lib;

import java.io.Serializable;

public class Spheroid extends Sphere implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Vector3 coefficients;

    public Spheroid(Vector3 coefficients, Vector3 center, Material mat)
    {
        super(center, 1, mat);
        this.coefficients = coefficients;
    }

    public Vector3 getCoefficients()
    {
        return this.coefficients;
    }

    public void copy(Spheroid spheroid)
    {
        super.copy(spheroid);
        this.coefficients = spheroid.coefficients;
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec)
    {
        Vector3 center = this.getCenter();

        Vector3 rayOriginMinusCenter = r.getOrigin().subtract(center);
        Vector3 rayDirection = r.getDirection();

        double a = rayDirection.weightedDot(rayDirection, this.coefficients);
        double b = rayDirection.weightedDot(rayOriginMinusCenter, 
                this.coefficients);
        double c = rayOriginMinusCenter.weightedDot(rayOriginMinusCenter,
                this.coefficients) - 1;

        double discriminant = b * b - a * c;

        if(discriminant <= 0)
            return false;

        double sqrtDiscriminant = Math.sqrt(discriminant);

        double root = (-b - sqrtDiscriminant) / a;
        if(root >= tMax || root <= tMin)
        {
            root = (-b + sqrtDiscriminant) / a;
            if(root >= tMax || root <= tMin)
                return false;
        }

        Vector3 point = r.pointAt(root);
        Vector3 normal = point.subtract(center).getUnitVector();


        // TODO: Investigate normal vector calculation/setting issues
        rec.setT(root);
        rec.setPoint(point);
        rec.setMaterial(this.getMaterial());
        rec.setFaceNormal(rayDirection, normal);

        return true;
    }
}
