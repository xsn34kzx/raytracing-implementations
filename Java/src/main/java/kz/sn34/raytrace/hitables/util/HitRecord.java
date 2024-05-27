package kz.sn34.raytrace.hitables.util;

import kz.sn34.raytrace.util.Vector3;
import kz.sn34.raytrace.materials.Material;

public class HitRecord
{
    private boolean frontFace;
    private double t;
    private Vector3 point;
    private Vector3 normal;
    private Material mat;

    public HitRecord()
    {
        this.t = 0;
        this.point = new Vector3();
        this.normal = new Vector3();
    }

    public boolean getFrontFace()
    {
        return this.frontFace;
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

    public Material getMaterial()
    {
        return this.mat;
    }

    public void copy(HitRecord rec)
    {
        this.frontFace = rec.frontFace;
        this.t = rec.t;
        this.point = rec.point;
        this.normal = rec.normal;
        this.mat = rec.mat;
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

    public void setMaterial(Material mat)
    {
        this.mat = mat;
    }

    public void setFaceNormal(Vector3 rayDirection, Vector3 normal)
    {
        this.frontFace = (rayDirection.dot(normal) < 0);
        this.normal = this.frontFace ? normal : normal.multiply(1);
    }
}
