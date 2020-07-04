package com.threedcger.lib.obj;

import com.threedcger.lib.gltf.*;
import com.threedcger.lib.gltf.model.*;
import com.threedcger.utils.Optionals;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.function.Function;

public class Convert {

    private BufferStructureBuilder bufferStructureBuilder;
    private MtlMaterialHandler mtlMaterialHandler;

    private int indicesComponentType = GltfConstants.GL_UNSIGNED_SHORT;

    private GlTF glTF;

    public GltfAsset start(Obj obj, String baseName, URI baseUri, List<MtlDto> mtlList) throws IOException {
        glTF = new GlTF();
        glTF.setAsset(createAsset());
        mtlMaterialHandler = new MtlMaterialHandler(glTF, baseUri);
        bufferStructureBuilder = new BufferStructureBuilder();
        List<Primitive> primitives = new ArrayList<>();
        Set<Map.Entry<String, ObjGroup>> entries = obj.getGroupMap().entrySet();
        int i = 0;
        for (Map.Entry<String, ObjGroup> entry: entries) {
            ObjGroup objGroup = entry.getValue();
            if (objGroup.getFaces() != null && objGroup.getFaces().size() > 0) {
                String name = "materialGroup_" + String.valueOf(i);
                Obj materialObj = ObjUtils.groupToObj(obj, objGroup, null);
                Primitive subPrimitive = createPrimitive(materialObj, name);
                MtlDto mtlDto = mtlList.get(i);
                assignMaterial(subPrimitive, mtlDto);
                primitives.add(subPrimitive);
                i++;
            }
        }

        String uri = baseName + ".bin";
        bufferStructureBuilder.createBufferModel(baseName, uri);
        BufferStructure bufferStructure = bufferStructureBuilder.build();

        List<Accessor> accessors = BufferStructureGltf.createAccessors(bufferStructure);
        glTF.setAccessors(accessors);

        List<BufferView> bufferViews = BufferStructureGltf.createBufferViews(bufferStructure);
        glTF.setBufferViews(bufferViews);

        List<Buffer> buffers = BufferStructureGltf.createBuffers(bufferStructure);
        glTF.setBuffers(buffers);

        Mesh mesh = new Mesh();
        mesh.setPrimitives(primitives);
        int meshIndex = Optionals.of(glTF.getMeshes()).size();
        glTF.addMeshes(mesh);

        Node node = new Node();
        node.setMesh(meshIndex);
        int nodeIndex = Optionals.of(glTF.getNodes()).size();
        glTF.addNodes(node);

        Scene scene = new Scene();
        scene.setNodes(Collections.singletonList(nodeIndex));
        int sceneIndex = Optionals.of(glTF.getScenes()).size();
        glTF.addScenes(scene);
        glTF.setScene(sceneIndex);

        GltfAsset gltfAsset = new GltfAsset(glTF, null);
        List<GltfReference> bufferReferences = gltfAsset.getBufferReferences();
        BufferStructures.resolve(bufferReferences, bufferStructure);

        if (baseUri != null) {
            // Resolve the image data, by reading the data from the URIs in
            // the Images, resolved against the root path of the input OBJ
            Function<String, ByteBuffer> externalUriResolver = UriResolvers.createBaseUriResolver(baseUri);
            List<GltfReference> imageReferences = gltfAsset.getImageReferences();
            GltfReferenceResolver.resolveAll(imageReferences, externalUriResolver);
        }

        return gltfAsset;
    }

    private Asset createAsset() {
        Asset asset = new Asset();
        asset.setGenerator("threedcger from https://3dcger.com");
        asset.setVersion("2.0");
        return asset;
    }
    private List<Primitive> createPrimitives(Obj obj, String name) {
        List<Primitive> primitives = new ArrayList<Primitive>();
        Primitive primitive = createPrimitive(obj, name);
        primitives.add(primitive);
        return primitives;
    }
    private Primitive createPrimitive(Obj obj, String name) {
        Primitive primitive = new Primitive();
        primitive.setMode(GltfConstants.GL_TRIANGLES);

        int indicesAccessorIndex = bufferStructureBuilder.getNumAccessorModels();
        bufferStructureBuilder.createAccessorModel(
                "indicesAccessor_" + indicesAccessorIndex,
                indicesComponentType, "SCALAR",
                createIndicesByteBuffer(obj, indicesComponentType)
        );
        primitive.setIndices(indicesAccessorIndex);
        bufferStructureBuilder.createArrayElementBufferViewModel(name + "_indices_bufferView");

        int positionsAccessorIndex = bufferStructureBuilder.getNumAccessorModels();
        FloatBuffer objVertices = ObjData.getVertices(obj);
        bufferStructureBuilder.createAccessorModel(
                "positionsAccessor_" + positionsAccessorIndex,
                GltfConstants.GL_FLOAT, "VEC3",
                Buffers.createByteBufferFrom(objVertices)
        );
        primitive.addAttributes("POSITION", positionsAccessorIndex);
        bufferStructureBuilder.createArrayBufferViewModel(name + "_position_bufferView");

        boolean flipY = true;
        FloatBuffer objTexCoords = ObjData.getTexCoords(obj, 2, flipY);
        if (objTexCoords.capacity() > 0) {
            int texCoordsAccessorIndex = bufferStructureBuilder.getNumAccessorModels();
            bufferStructureBuilder.createAccessorModel(
                    "texCoordsAccessor_" + texCoordsAccessorIndex,
                    GltfConstants.GL_FLOAT, "VEC2",
                    Buffers.createByteBufferFrom(objTexCoords)
            );
            primitive.addAttributes("TEXCOORD_0", texCoordsAccessorIndex);
            bufferStructureBuilder.createArrayBufferViewModel(name + "_uv_bufferView");
        }

        FloatBuffer objNormals = ObjData.getNormals(obj);
        if (objNormals.capacity() > 0) {
            ObjNormals.normalize(objNormals);
            int normalsAccessorIndex = bufferStructureBuilder.getNumAccessorModels();
            bufferStructureBuilder.createAccessorModel(
                    "normalsAccessor_" + normalsAccessorIndex,
                    GltfConstants.GL_FLOAT, "VEC3",
                    Buffers.createByteBufferFrom(objNormals));
            primitive.addAttributes("NORMAL", normalsAccessorIndex);
            bufferStructureBuilder.createArrayBufferViewModel(name + "_normal_bufferView");
        }
//        bufferStructureBuilder.createArrayBufferViewModel(name + "_attributes_bufferView");

        return primitive;
    }
    private List<Primitive> createPartPrimitives(Obj obj, String name) {
        List<Primitive> primitives = new ArrayList<Primitive>();
//        int maxNumVertices = 65536 - 3;
//        List<Obj> parts = ObjSplitting.splitByMaxNumVertices(obj, maxNumVertices);
//        for (int i = 0; i < parts.size(); i++) {
//            Obj part = parts.get(i);
//            String partName = name;
//            if (parts.size() > 1) {
//                partName += "_part_" + i;
//            }
//            Primitive primitive = createPrimitive(part, partName);
//            primitives.add(primitive);
//        }
        return primitives;
    }
    private static ByteBuffer createIndicesByteBuffer(Obj obj, int indicesComponentType) {
        int numVerticesPerFace = 3;
        IntBuffer objIndices = ObjData.getFaceVertexIndices(obj, numVerticesPerFace);
        int indicesComponentSize = Accessors.getNumBytesForAccessorComponentType(indicesComponentType);
        ByteBuffer indicesByteBuffer = IntBuffers.convertToByteBuffer(objIndices, indicesComponentSize);
        return indicesByteBuffer;
    }
    private void assignMaterial(List<Primitive> primitives, MtlDto mtlDto) throws IOException {
        Material material = mtlMaterialHandler.createMaterial(mtlDto);
        int materialIndex = Optionals.of(glTF.getMaterials()).size();
        glTF.addMaterials(material);
        for (Primitive primitive : primitives) {
            primitive.setMaterial(materialIndex);
        }
    }
    private void assignMaterial(Primitive primitive, MtlDto mtlDto) throws IOException {
        Material material = mtlMaterialHandler.createMaterial(mtlDto);
        int materialIndex = Optionals.of(glTF.getMaterials()).size();
        glTF.addMaterials(material);
        primitive.setMaterial(materialIndex);
    }
}
