package com.threedcger;

import com.threedcger.lib.ObjLoader;
import com.threedcger.lib.obj.Obj;

import java.io.IOException;

public class ThreedcgerApp {
    public static void main(String[] args) {
        String path = "/Users/caoanlong/Desktop/3dcger/blender/a/tank/tank.obj";
        try {
            Obj objData = new ObjLoader().load(path);
            System.out.println(objData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
