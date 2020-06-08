package com.threedcger.lib.gltf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class GltfModelWriter {
    public void write(GltfModel gltfModel, File file) throws IOException {
        DefaultAssetCreator assetCreator = new DefaultAssetCreator();
        GltfAsset gltfAsset = assetCreator.create(gltfModel);
        GltfAssetWriter gltfAssetWriter = new GltfAssetWriter();
        gltfAssetWriter.write(gltfAsset, file);
    }

//    public void writeBinary(GltfModel gltfModel, OutputStream outputStream) throws IOException {
//        BinaryAssetCreator assetCreator = new BinaryAssetCreator();
//        GltfAsset gltfAsset = assetCreator.create(gltfModel);
//        GltfAssetWriter gltfAssetWriter = new GltfAssetWriter();
//        gltfAssetWriter.writeBinary(gltfAsset, outputStream);
//    }
}
