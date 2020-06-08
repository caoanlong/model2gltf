package com.threedcger.lib.obj;

import com.threedcger.lib.gltf.model.GlTF;
import com.threedcger.lib.gltf.model.Image;
import com.threedcger.lib.gltf.model.Sampler;
import com.threedcger.lib.gltf.model.Texture;
import com.threedcger.utils.Optionals;

import java.util.LinkedHashMap;
import java.util.Map;

public class TextureHandler {
    private final GlTF glTF;
    private int samplerIndex;
    private final Map<String, Integer> imageUriToTextureIndex;

    public TextureHandler(GlTF glTF) {
        this.glTF = glTF;
        this.samplerIndex = -1;
        this.imageUriToTextureIndex = new LinkedHashMap<String, Integer>();
    }

    public int getTextureIndex(String imageUri) {
        // Create the common (default) sampler if it was not created yet
        if (samplerIndex == -1) {
            Sampler sampler = new Sampler();
            samplerIndex = Optionals.of(glTF.getSamplers()).size();
            glTF.addSamplers(sampler);
        }

        Integer textureIndex = imageUriToTextureIndex.get(imageUri);
        if (textureIndex != null) {
            return textureIndex;
        }

        // Create the image
        Image image = new Image();
        image.setUri(imageUri);
        int imageIndex = Optionals.of(glTF.getImages()).size();
        glTF.addImages(image);

        // Create the texture that refers to the image
        Texture texture = new Texture();
        texture.setSource(imageIndex);
        texture.setSampler(samplerIndex);
        textureIndex = Optionals.of(glTF.getTextures()).size();
        glTF.addTextures(texture);

        imageUriToTextureIndex.put(imageUri, textureIndex);
        return textureIndex;
    }
}
