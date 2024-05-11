package kz.sn34.raytrace_java_lib;

import java.util.ArrayList;

public class HitableList implements Hitable
{
    private ArrayList<Hitable> list;

    public HitableList()
    {
        this.list = new ArrayList<Hitable>();
    }

    public void add(Hitable object)
    {
        this.list.add(object);
    }

    public int getSize()
    {
        return this.list.size();
    }

    public Hitable getHitable(int i)
    {
        return this.list.get(i);
    }

    public void removeHitable(int i)
    {
        this.list.remove(i);
    }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec)
    {
        HitRecord curRec = new HitRecord();
        boolean hitSomething = false;
        double closestSoFar = tMax;

        for(Hitable object : this.list)
        {
            if(object.hit(r, tMin, closestSoFar, curRec))
            {
                hitSomething = true;
                closestSoFar = curRec.getT();
            }
        }

        rec.copy(curRec);
        return hitSomething;
    }
}
