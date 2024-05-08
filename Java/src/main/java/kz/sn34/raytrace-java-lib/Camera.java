package kz.sn34.raytrace_java_lib;

import java.util.Random;

public class Camera
{
    private double lensRadius;
    private Vector3 origin;
    private Vector3 upLeftCanonical;
    private Vector3 deltaX;
    private Vector3 deltaY;
    private Vector3 u;
    private Vector3 v;
    private Vector3 w;

    public Camera(Vector3 lookFrom, Vector3 lookAt, Vector3 camUp, 
            double verticalFOV, double aspectRatio, double lensAperture,
            double focusDistance)
    {
        this.lensRadius = lensAperture / 2;
        double theta = Math.toRadians(verticalFOV);
        double halfHeight = Math.tan(theta / 2) * focusDistance;
        double halfWidth = aspectRatio * halfHeight;
        this.origin = lookFrom;
        
        this.w = lookFrom.subtract(lookAt).getUnitVector();
        this.u = camUp.cross(w).getUnitVector();
        this.v = w.cross(u);

        Vector3[] terms = {u.multiply(halfWidth),
            v.multiply(halfHeight)};
        this.upLeftCanonical = this.origin.add(terms)
            .subtract(w.multiply(focusDistance));
        this.deltaX = u.multiply(-2 * halfWidth);
        this.deltaY = v.multiply(-2 * halfHeight);
    }

    public Ray getRay(double u, double v)
    {
        Vector3 randUnitVector = Vector2.getRandomInUnitCircle().toVector3().
            multiply(this.lensRadius);

        Vector3 offset = this.u.multiply(randUnitVector.getX()).add(
                this.v.multiply(randUnitVector.getY()));

        Vector3[] terms = {this.deltaX.multiply(u), this.deltaY.multiply(v)};

        return new Ray(this.origin.add(offset), 
            this.upLeftCanonical.add(terms).subtract(this.origin.add(offset)));
    }
}
