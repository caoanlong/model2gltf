package com.threedcger;

import com.threedcger.lib.obj.Obj2glTF;

import java.io.IOException;

public class ThreedcgerApp {
    public static void main(String[] args) {
        String path = "/Users/caoanlong/Desktop/3dcger/blender/a/tank/tank.obj";
        try {
            new Obj2glTF(path, null).convert();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
