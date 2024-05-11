package kz.sn34.raytrace_java_lib;

import java.io.Serializable;

public class WorldEntry implements Serializable
{
    private static final long serialVersionUID = 1L;

    private boolean isChild;
    private int i;
    private EntryType type;
    private Hitable obj;

    public WorldEntry(boolean isChild, int i, EntryType type, Hitable obj)
    {
        this.isChild = isChild;
        this.i = i;
        this.type = type;
        this.obj = obj;
    }

    public WorldEntry(WorldEntry parentEntry, EntryType type)
    {

    }

    public boolean isChild()
    {
        return this.isChild;
    }

    public int getIndex()
    {
        return this.i;
    }

    public void setIndex(int i)
    {
        this.i = i;
    }

    @Override
    public String toString()
    {
        String entryName = this.type.name();

        if(!isChild)  
            entryName += " (" + Integer.toString(this.i) + ")";

        return entryName;
    }
}

