package com.threedcger.lib.gltf;

import com.threedcger.lib.gltf.model.Buffer;
import com.threedcger.lib.gltf.model.GlTF;
import com.threedcger.lib.gltf.model.Image;
import com.threedcger.utils.IO;
import com.threedcger.utils.Optionals;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GltfAsset {
    private final GlTF gltf;

    private final ByteBuffer binaryData;

    private final Map<String, ByteBuffer> referenceDatas;

    public GltfAsset(GlTF gltf, ByteBuffer binaryData) {
        this.gltf = Objects.requireNonNull(gltf, "The gltf may not be null");
        this.binaryData = binaryData;
        this.referenceDatas = new ConcurrentHashMap<String, ByteBuffer>();
    }

    void putReferenceData(String uriString, ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            referenceDatas.remove(uriString);
        } else {
            referenceDatas.put(uriString, byteBuffer);
        }
    }

    public GlTF getGltf()
    {
        return gltf;
    }

    public ByteBuffer getBinaryData()
    {
        return Buffers.createSlice(binaryData);
    }

    public List<GltfReference> getReferences() {
        List<GltfReference> references = new ArrayList<GltfReference>();
        references.addAll(getBufferReferences());
        references.addAll(getImageReferences());
        return references;
    }

    public List<GltfReference> getBufferReferences() {
        List<GltfReference> references = new ArrayList<GltfReference>();
        List<Buffer> buffers = Optionals.of(gltf.getBuffers());
        for (int i = 0; i < buffers.size(); i++) {
            Buffer buffer = buffers.get(i);
            if (buffer.getUri() == null) {
                // This is the binary glTF buffer
                continue;
            }
            String uri = buffer.getUri();
            if (!IO.isDataUriString(uri)) {
                Consumer<ByteBuffer> target = byteBuffer -> putReferenceData(uri, byteBuffer);
                GltfReference reference = new GltfReference("buffer " + i, uri, target);
                references.add(reference);
            }
        }
        return references;
    }

    public List<GltfReference> getImageReferences() {
        List<GltfReference> references = new ArrayList<GltfReference>();
        List<Image> images = Optionals.of(gltf.getImages());
        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            if (image.getBufferView() != null) {
                // This is an image that refers to a buffer view
                continue;
            }
            String uri = image.getUri();
            if (!IO.isDataUriString(uri)) {
                Consumer<ByteBuffer> target =
                        byteBuffer -> putReferenceData(uri, byteBuffer);
                GltfReference reference =
                        new GltfReference("image " + i, uri, target);
                references.add(reference);
            }
        }
        return references;
    }

    public ByteBuffer getReferenceData(String uriString)
    {
        return Buffers.createSlice(referenceDatas.get(uriString));
    }

    public Map<String, ByteBuffer> getReferenceDatas()
    {
        return Collections.unmodifiableMap(referenceDatas);
    }
}
