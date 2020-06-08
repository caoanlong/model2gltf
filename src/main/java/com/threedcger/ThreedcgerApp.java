package com.threedcger;

import com.threedcger.lib.ObjLoader;
import com.threedcger.lib.obj.MtlDto;

import java.io.IOException;

public class ThreedcgerApp {
    public static void main(String[] args) {
        String objPath = "/Users/caoanlong/Desktop/3dcger/blender/monkey/monkey.obj";
        String gltfPath = "/Users/caoanlong/Desktop/3dcger/blender/monkey/monkey.gltf";
        MtlDto mtlDto = new MtlDto();
        mtlDto.setObjUrl(objPath);
        mtlDto.setGltfUrl(gltfPath);
        mtlDto.setMetalness(1.0f);
        mtlDto.setRoughness(0.0f);
        mtlDto.setMap("albedo.png");
        mtlDto.setNormalMap("normal.png");
        mtlDto.setMetalnessMap("metallic.png");
        mtlDto.setAoMap("ao.png");
        mtlDto.setAoMapIntensity(0.5f);
        try {
            new ObjLoader().load(mtlDto);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
