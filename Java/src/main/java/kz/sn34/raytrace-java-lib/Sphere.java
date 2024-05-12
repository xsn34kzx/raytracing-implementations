package kz.sn34.raytrace_java_lib;

import java.io.Serializable;

public class Sphere implements Hitable, Serializable
{
    private static final long serialVersionUID = 1L;

    private Vector3 center;
    private double radius;
    private Material mat;

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

    public Material getMaterial()
    {
        return this.mat;
    }

    public void copy(Sphere sphere)
    {
        this.center = sphere.center;
        this.radius = sphere.radius;
        this.mat = sphere.mat;
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
        Vector3 normal = point.subtract(this.center).divide(this.radius);

        rec.setT(root);
        rec.setPoint(point);
        rec.setMaterial(this.mat);
        rec.setFaceNormal(rayDirection, normal);

        return true;
    }
}
