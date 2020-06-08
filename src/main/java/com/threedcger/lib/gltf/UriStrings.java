package com.threedcger.lib.gltf;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.logging.Logger;

public class UriStrings {
    /**
     * The logger used in this class
     */
    private static final Logger logger =
            Logger.getLogger(UriStrings.class.getName());

    /**
     * Create an unspecified, new URI string that is not yet used as
     * a URI string.
     *
     * @param existingUriStrings The existing URI strings
     * @return The new URI string
     */
    public static String createBufferUriString(
            Collection<? extends String> existingUriStrings)
    {
        int counter = 0;
        while (true)
        {
            String uri = "buffer" + counter + "." + "bin";
            if (!existingUriStrings.contains(uri))
            {
                return uri;
            }
        }
    }

    /**
     * Create an unspecified, new URI string that is not yet used as
     * a URI string
     *
     * @param imageModel The {@link ImageModel} to create the string for
     * @param existingUriStrings The existing URI strings
     * @return The new URI string
     */
    public static String createImageUriString(ImageModel imageModel,
                                              Collection<? extends String> existingUriStrings)
    {
        String extensionWithoutDot =
                determineImageFileNameExtension(imageModel);
        int counter = 0;
        while (true)
        {
            String uri = "image" + counter + "." + extensionWithoutDot;
            if (!existingUriStrings.contains(uri))
            {
                return uri;
            }
        }
    }

    /**
     * Determine the extension for an image file name (without the
     * <code>"."</code> dot), for the given {@link ImageModel}
     *
     * @param imageModel The {@link ImageModel}
     * @return The file extension
     */
    private static String determineImageFileNameExtension(
            ImageModel imageModel)
    {
        // Try to figure out the MIME type
        String mimeTypeString = imageModel.getMimeType();
        if (mimeTypeString == null)
        {
            ByteBuffer imageData = imageModel.getImageData();
            mimeTypeString =
                    MimeTypes.guessImageMimeTypeStringUnchecked(imageData);
        }

        // Try to figure out the extension based on the MIME type
        if (mimeTypeString != null)
        {
            String extensionWithoutDot =
                    MimeTypes.imageFileNameExtensionForMimeTypeString(
                            mimeTypeString);
            if (extensionWithoutDot != null)
            {
                return extensionWithoutDot;
            }
        }
        logger.warning("Could not determine file extension for image URI");
        return "";
    }

    /**
     * Private constructor to prevent instantiation
     */
    private UriStrings()
    {
        // Private constructor to prevent instantiation
    }
}
