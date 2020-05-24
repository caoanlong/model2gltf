package com.threedcger.lib.obj;

import lombok.Data;

@Data
public class FloatTuple {
    private float[] values;

    public FloatTuple(float[] values) {
        this.values = values;
    }

    public FloatTuple(float x, float y, float z, float w) {
        this(new float[]{x,y,z,w});
    }

    public FloatTuple(float x, float y, float z) {
        this(new float[]{x,y,z});
    }

    public FloatTuple(float x, float y) {
        this(new float[]{x,y});
    }

    public FloatTuple(float x) {
        this(new float[]{x});
    }

    public FloatTuple(FloatTuple other) {
        this.values = getValues(other);
    }

    private float[] getValues(FloatTuple f) {
        float[] values = new float[f.values.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = f.get(i);
        }
        return values;
    }
    public float get(int index)
    {
        return values[index];
    }
    public float getX()
    {
        return values[0];
    }
    public void setX(float x)
    {
        values[0] = x;
    }
    public float getY()
    {
        return values[1];
    }
    public void setY(float y)
    {
        values[1] = y;
    }
    public float getZ()
    {
        return values[2];
    }
    public void setZ(float z)
    {
        values[2] = z;
    }
    public float getW()
    {
        return values[3];
    }
    public void setW(float w)
    {
        values[3] = w;
    }
}
