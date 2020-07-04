package com.threedcger;

import com.threedcger.lib.ObjLoader;
import com.threedcger.lib.ObjLoader2;
import com.threedcger.lib.gltf.model.Image;
import com.threedcger.lib.obj.MtlDto;
import com.threedcger.utils.ImgUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreedcgerApp {
    public static void main(String[] args) throws IOException {
//        String objPath = "/Users/caoanlong/Desktop/3dcger/blender/a/box.obj";
//        String gltfPath = "/Users/caoanlong/Desktop/3dcger/blender/a/box.gltf";
//        MtlDto mtlDto = new MtlDto();

//        String path1 = "/Users/caoanlong/Desktop/data/test/466685065607524352/metallic.png";
//        String path2 = "/Users/caoanlong/Desktop/data/test/466685065607524352/roughness.png";
//        String path3 = "/Users/caoanlong/Desktop/data/test/466685065607524352/metallicRoughness.jpg";
//        File file1 = new File(path1);
//        File file2 = new File(path2);
//        FileInputStream source1 = new FileInputStream(file1);
//        FileInputStream source2 = new FileInputStream(file2);
//        BufferedImage result1 = ImageIO.read(source1);
//        BufferedImage result2 = ImageIO.read(source2);
//        BufferedImage img = ImgUtils.combineImgByChannel(result1, result2);
//        ImageIO.write(img, "jpg", new File(path3));

//        String objPath = "/Users/caoanlong/Desktop/data/test/466685065607524352/monkey.obj";
//        String gltfPath = "/Users/caoanlong/Desktop/data/test/466685065607524352/monkey.gltf";
//        MtlDto mtlDto = new MtlDto();
//        mtlDto.setName("Suzanne");
//        mtlDto.setMap("albedo.png");
//        mtlDto.setMetalness(1.0f);
//        mtlDto.setRoughness(1.0f);
//        mtlDto.setMetalnessMap("metallicRoughness.jpg");
//        mtlDto.setRoughnessMap("metallicRoughness.jpg");
//        mtlDto.setNormalMap("normal.png");
//        mtlDto.setAoMap("ao.png");

//        String objPath = "/Users/caoanlong/Desktop/data/test/467138984406167552/hat.obj";
//        String gltfPath = "/Users/caoanlong/Desktop/data/test/467138984406167552/hat.gltf";
//        MtlDto mtlDto = new MtlDto();
//        mtlDto.setName("hat:Mesh");
//        mtlDto.setMap("Default_albedo.jpg");
//        mtlDto.setMetalness(1.0f);
//        mtlDto.setRoughness(0.0f);
//        mtlDto.setMetalnessMap("Default_metalRoughness.jpg");
//        mtlDto.setNormalMap("Default_normal.jpg");
//        mtlDto.setEmissiveMap("Default_emissive.jpg");
//        mtlDto.setAoMap("Default_AO.jpg");
//        mtlDto.setEmissive("#ffffff");


        String objPath = "/Users/caoanlong/Desktop/3dcger/blender/ishikawa ordinary/ishikawa.obj";
        String gltfPath = "/Users/caoanlong/Desktop/3dcger/blender/ishikawa ordinary/ishikawa.gltf";
        MtlDto mtlDto = new MtlDto();
        mtlDto.setName("body_1_:polySurface12_polySurface6_polySurface3_FBXASC0504_Material87FBXASC0402FBXASC041_1_0_0");
        mtlDto.setMetalness(0.0f);
        mtlDto.setRoughness(1.0f);
        mtlDto.setMap("WechatIMG322.png");
        mtlDto.setNormalMap("WechatIMG320.png");

        MtlDto mtlDto2 = new MtlDto();
        mtlDto2.setName("body_1_:polySurface13");
        mtlDto2.setMetalness(0.0f);
        mtlDto2.setRoughness(1.0f);
        mtlDto2.setMap("WechatIMG323.png");
        mtlDto2.setNormalMap("WechatIMG319.png");

        List<MtlDto> mtlDtos = new ArrayList<>();
        mtlDtos.add(mtlDto);
        mtlDtos.add(mtlDto2);
        new ObjLoader().load(mtlDtos, objPath, gltfPath);
    }
}
