package com.threedcger.lib.obj;

import lombok.Data;

@Data
public class ObjFace {
    private int[] vertexIndices;
    private int[] texCoordIndices;
    private int[] normalIndices;

    public ObjFace(int[] vertexIndices, int[] texCoordIndices, int[] normalIndices) {
        this.vertexIndices = vertexIndices;
        this.texCoordIndices = texCoordIndices;
        this.normalIndices = normalIndices;
    }

    public boolean containsTexCoordIndices()
    {
        return texCoordIndices != null;
    }

    public boolean containsNormalIndices()
    {
        return normalIndices != null;
    }

    public int getVertexIndex(int number)
    {
        return this.vertexIndices[number];
    }

    public int getTexCoordIndex(int number)
    {
        return this.texCoordIndices[number];
    }

    public int getNormalIndex(int number)
    {
        return this.normalIndices[number];
    }

    public void setVertexIndex(int n, int index)
    {
        vertexIndices[n] = index;
    }

    public void setNormalIndex(int n, int index)
    {
        normalIndices[n] = index;
    }

    public void setTexCoordIndex(int n, int index)
    {
        texCoordIndices[n] = index;
    }

    public int getNumVertices()
    {
        return this.vertexIndices.length;
    }
}
