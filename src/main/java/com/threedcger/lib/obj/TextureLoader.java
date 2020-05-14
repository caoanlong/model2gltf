package com.threedcger.lib.obj;

import com.threedcger.lib.obj.model.Texture;
import com.threedcger.lib.obj.model.TextureOptions;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

public class TextureLoader {
    private TextureOptions options;

    public TextureLoader(TextureOptions options) {
        this.options = new TextureOptions();
        this.options.setCheckTransparency(options.getCheckTransparency() ? options.getCheckTransparency() : false);
        this.options.setDecode(options.getDecode() ? options.getDecode() : false);
        this.options.setKeepSource(options.getKeepSource() ? options.getKeepSource() : false);
    }
    public Texture load(String texturePath) throws IOException {
        File file = new File(texturePath);
        String fullName = file.getName();
        Integer dotIndex = fullName.lastIndexOf('.');
        String name = (dotIndex == -1) ? fullName : fullName.substring(0, dotIndex);
        String extension = ((dotIndex == -1) ? fullName : fullName.substring(dotIndex + 1)).toLowerCase();
        FileInputStream source = new FileInputStream(file);

        Texture texture = new Texture();
        texture.setSource(source);
        texture.setName(name);
        texture.setExtension(extension);
        texture.setPath(texturePath);

        Boolean decode = options.getDecode() || options.getCheckTransparency();
        if (decode) {
            BufferedImage decodedResults = ImageIO.read(source);
            DataBuffer dataBuffer = decodedResults.getRaster().getDataBuffer();
            int size = dataBuffer.getSize();
            int[] list = new int[size];
            for (int i = 0; i < size; i++) {
                list[i] = dataBuffer.getElem(i);
            }
            IntBuffer pixels = IntBuffer.wrap(list);

            if (options.getCheckTransparency()) {
                texture.setTransparent(hasTransparency(pixels));
            }
            if (options.getDecode()) {
                texture.setPixels(pixels);
                texture.setWidth(decodedResults.getWidth());
                texture.setHeight(decodedResults.getHeight());
                if (!options.getKeepSource()) {
                    // Unload resources
                    texture.setSource(null);
                }
            }
        }
        return texture;
    }
    private Boolean hasTransparency(IntBuffer pixels) {
        Integer pixelsLength = pixels.capacity() / 4;
        for (int i = 0; i < pixelsLength; i++) {
            if (pixels.get(i * 4 + 3) < 255) {
                return true;
            }
        }
        return false;
    }
}
