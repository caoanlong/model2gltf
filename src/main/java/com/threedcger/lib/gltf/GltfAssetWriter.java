package com.threedcger.lib.gltf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Map;

public class GltfAssetWriter {
    /**
     * Default constructor
     */
    public GltfAssetWriter()
    {
        // Default constructor
    }

    /**
     * Write the the given {@link GltfAsset} to a file with the given name.
     * The {@link GltfAsset#getBinaryData() binary data} will be ignored.
     * The {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be written to files that are determined by resolving the
     * (relative) URLs of the references against the parent of the specified
     * file.
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param fileName The file name for the JSON file
     * @throws IOException If an IO error occurred
     */
    public void write(GltfAsset gltfAsset, String fileName)
            throws IOException
    {
        write(gltfAsset, new File(fileName));
    }

    /**
     * Write the the given {@link GltfAsset} to the given file.
     * The {@link GltfAsset#getBinaryData() binary data} will be ignored.
     * The {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be written to files that are determined by resolving the
     * (relative) URLs of the references against the parent of the specified
     * file.
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param file The file for the JSON part
     * @throws IOException If an IO error occurred
     */
    public void write(GltfAsset gltfAsset, File file) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            writeJson(gltfAsset, outputStream);
        }
        for (Map.Entry<String, ByteBuffer> entry : gltfAsset.getReferenceDatas().entrySet()) {
            String relativeUrlString = entry.getKey();
            ByteBuffer data = entry.getValue();
            String referenceFileName = file.toPath().getParent().resolve(relativeUrlString).toString();

            try (@SuppressWarnings("resource")
                 WritableByteChannel writableByteChannel =
                         Channels.newChannel(new FileOutputStream(referenceFileName))) {
                writableByteChannel.write(data.slice());
            }
        }
    }

    /**
     * Write the JSON part of the given {@link GltfAsset} to a file with
     * the given name. The {@link GltfAsset#getBinaryData() binary data}
     * and {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be ignored.
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param fileName The file name for the JSON file
     * @throws IOException If an IO error occurred
     */
    public void writeJson(GltfAsset gltfAsset, String fileName)
            throws IOException
    {
        writeJson(gltfAsset, new File(fileName));
    }

    /**
     * Write the JSON part of the given {@link GltfAsset} to a file with
     * the given name. The {@link GltfAsset#getBinaryData() binary data}
     * and {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be ignored.
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param file The file for the JSON part
     * @throws IOException If an IO error occurred
     */
    public void writeJson(GltfAsset gltfAsset, File file)
            throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            writeJson(gltfAsset, outputStream);
        }
    }

    /**
     * Write the JSON part of the given {@link GltfAsset} to the given
     * output stream. The {@link GltfAsset#getBinaryData() binary data}
     * and {@link GltfAsset#getReferenceDatas() reference data elements}
     * will be ignored. The caller is responsible for closing the given
     * stream.
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param outputStream The output stream
     * @throws IOException If an IO error occurred
     */
    public void writeJson(GltfAsset gltfAsset, OutputStream outputStream)
            throws IOException
    {
        Object gltf = gltfAsset.getGltf();
        GltfWriter gltfWriter = new GltfWriter();
        gltfWriter.write(gltf, outputStream);
    }

    /**
     * Write the given {@link GltfAsset} as a binary glTF asset to file with
     * the given name.
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param fileName The file name for the JSON file
     * @throws IOException If an IO error occurred
     */
    public void writeBinary(GltfAsset gltfAsset, String fileName)
            throws IOException
    {
        writeBinary(gltfAsset, new File(fileName));
    }

    /**
     * Write the given {@link GltfAsset} as a binary glTF asset to the given
     * file
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param file The file
     * @throws IOException If an IO error occurred
     */
    public void writeBinary(GltfAsset gltfAsset, File file)
            throws IOException
    {
        try (OutputStream outputStream = new FileOutputStream(file))
        {
            writeBinary(gltfAsset, outputStream);
        }
    }

    /**
     * Write the given {@link GltfAsset} as a binary glTF asset to the
     * given output stream. The caller is responsible for closing the
     * given stream.
     *
     * @param gltfAsset The {@link GltfAsset}
     * @param outputStream The output stream
     */
    public void writeBinary(GltfAsset gltfAsset, OutputStream outputStream) {
            GltfAssetWriter gltfAssetWriter = new GltfAssetWriter();
            gltfAssetWriter.writeBinary(gltfAsset, outputStream);
    }
}
