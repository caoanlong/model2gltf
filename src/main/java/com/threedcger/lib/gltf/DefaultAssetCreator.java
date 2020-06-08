package com.threedcger.lib.gltf;

import com.threedcger.lib.gltf.model.Buffer;
import com.threedcger.lib.gltf.model.GlTF;
import com.threedcger.lib.gltf.model.Image;
import com.threedcger.utils.IO;
import com.threedcger.utils.Optionals;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultAssetCreator {
    /**
     * The {@link GltfAsset} that is currently being created
     */
    private GltfAsset gltfAsset;

    /**
     * The set of {@link Buffer} URI strings that are already used
     */
    private Set<String> existingBufferUriStrings;

    /**
     * The set of {@link Image} URI strings that are already used
     */
    private Set<String> existingImageUriStrings;

    /**
     * Creates a new asset creator
     */
    DefaultAssetCreator()
    {
        // Default constructor
    }

    /**
     * Create a default {@link GltfAsset} from the given {@link GltfModel}.
     *
     * @param gltfModel The input {@link GltfModel}
     * @return The default {@link GltfAsset}
     */
    GltfAsset create(GltfModel gltfModel)
    {
        GlTF inputGltf = gltfModel.getGltf();
        GlTF outputGltf = GltfUtils.copy(inputGltf);

        List<Buffer> buffers = Optionals.of(outputGltf.getBuffers());
        List<Image> images = Optionals.of(outputGltf.getImages());

        existingBufferUriStrings = collectUriStrings(buffers, Buffer::getUri);
        existingImageUriStrings = collectUriStrings(images, Image::getUri);

        this.gltfAsset = new GltfAsset(outputGltf, null);

        for (int i = 0; i < buffers.size(); i++)
        {
            Buffer buffer = buffers.get(i);
            storeBufferAsDefault(gltfModel, i, buffer);
        }
        for (int i = 0; i < images.size(); i++)
        {
            Image image = images.get(i);
            storeImageAsDefault(gltfModel, i, image);
        }

        return gltfAsset;
    }

    /**
     * Collect all strings that are obtained from the given elements by 
     * applying the given function, if these strings are not <code>null</code>
     * and no data URI strings
     *
     * @param elements The elements
     * @param uriFunction The function to obtain the string
     * @return The strings
     */
    private static <T> Set<String> collectUriStrings(Collection<T> elements,
                                                     Function<? super T, ? extends String> uriFunction)
    {
        return elements.stream()
                .map(uriFunction)
                .filter(Objects::nonNull)
                .filter(uriString -> !IO.isDataUriString(uriString))
                .collect(Collectors.toSet());
    }

    /**
     * Store the given {@link Buffer} with the given index in the current 
     * output asset. <br>
     * <br>
     * If the {@link Buffer#getUri() buffer URI} is <code>null</code> or a 
     * data URI, it will receive a new URI, which refers to the buffer data, 
     * which is then stored as {@link GltfAsset#getReferenceData(String)
     * reference data} in the asset.<br>
     * <br>
     * The given {@link Buffer} object will be modified accordingly, if 
     * necessary: Its URI will be set to be the new URI. 
     *
     * @param gltfModel The {@link GltfModel} 
     * @param index The index of the {@link Buffer}
     * @param buffer The {@link Buffer}
     */
    private void storeBufferAsDefault(GltfModel gltfModel, int index, Buffer buffer) {
        BufferModel bufferModel = gltfModel.getBufferModels().get(index);
        ByteBuffer bufferData = bufferModel.getBufferData();

        String oldUriString = buffer.getUri();
        String newUriString = oldUriString;
        if (oldUriString == null || IO.isDataUriString(oldUriString)) {
            newUriString = UriStrings.createBufferUriString(existingBufferUriStrings);
            buffer.setUri(newUriString);
            existingBufferUriStrings.add(newUriString);
        }

        gltfAsset.putReferenceData(newUriString, bufferData);
    }


    /**
     * Store the given {@link Image} with the given index in the current 
     * output asset. <br>
     * <br>
     * If the {@link Image#getUri() image URI} is <code>null</code> or a 
     * data URI, it will receive a new URI, which refers to the image data, 
     * which is then stored as {@link GltfAsset#getReferenceData(String)
     * reference data} in the asset.<br>
     * <br>
     * The given {@link Image} object will be modified accordingly, if 
     * necessary: Its URI will be set to be the new URI. If it referred
     * to a {@link Image#getBufferView() image buffer view}, then this
     * reference will be set to be <code>null</code>.
     *
     * @param gltfModel The {@link GltfModel} 
     * @param index The index of the {@link Image}
     * @param image The {@link Image}
     */
    private void storeImageAsDefault(
            GltfModel gltfModel, int index, Image image)
    {
        ImageModel imageModel = gltfModel.getImageModels().get(index);
        ByteBuffer imageData = imageModel.getImageData();

        String oldUriString = image.getUri();
        String newUriString = oldUriString;
        if (oldUriString == null || IO.isDataUriString(oldUriString))
        {
            newUriString = UriStrings.createImageUriString(
                    imageModel, existingImageUriStrings);
            image.setUri(newUriString);
            existingImageUriStrings.add(newUriString);
        }

        if (image.getBufferView() != null)
        {
            image.setBufferView(null);
        }

        gltfAsset.putReferenceData(newUriString, imageData);
    }
}
