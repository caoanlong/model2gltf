package com.threedcger.lib.obj;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ObjGroup {
    private String name;
    private List<ObjFace> faces;

    public ObjGroup(String name) {
        this.name = name;
        faces = new ArrayList<ObjFace>();
    }

    public String getName()
    {
        return name;
    }

    public void addFace(ObjFace face)
    {
        faces.add(face);
    }

    public int getNumFaces()
    {
        return faces.size();
    }

    public ObjFace getFace(int index)
    {
        return faces.get(index);
    }

}
