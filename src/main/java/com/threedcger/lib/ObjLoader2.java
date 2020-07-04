package com.threedcger.lib;

import com.threedcger.lib.gltf.model.*;
import com.threedcger.lib.gltf2.GlTF;
import com.threedcger.lib.gltf2.MtlDto;
import com.threedcger.utils.IO;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.URI;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;
import java.util.*;

public class ObjLoader2 {
    private String name;
    private List<String> groupNames;
    private List<String> materialNames;
    private List<String> globalVerts;
    private List<String> globalNorms;
    private List<String> globalUVs;
    private List<String> globalIndices;
    private GlTF glTF;
    private List<Node> nodes;
    public ObjLoader2() {
        this.groupNames = new ArrayList<>();
        this.materialNames = new ArrayList<>();
        this.globalVerts = new ArrayList<>();
        this.globalNorms = new ArrayList<>();
        this.globalUVs = new ArrayList<>();
        this.globalIndices = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }
    public void load(List<MtlDto> mtlList, String objUrl, String gltfUrl) throws IOException {
        URI objUri = Paths.get(objUrl).toUri();
        URI baseUri = IO.getParent(objUri);
        String objFileName = IO.extractFileName(objUri);
        String baseName = stripFileNameExtension(objFileName);
        this.glTF = new GlTF();
        this.glTF.setAsset(createAsset());
        this.glTF.setScene(0);
        List<Scene> scenes = new ArrayList<>();
        Scene scene = new Scene();
        scene.setNodes(new ArrayList<>());
        scenes.add(scene);
        this.glTF.setScenes(scenes);
        this.glTF.setNodes(new ArrayList<>());
        this.glTF.setAccessors(new ArrayList<>());
        this.glTF.setBufferViews(new ArrayList<>());
        this.glTF.setMeshes(new ArrayList<>());

        ObjResourse objResourse = read(objUri);
        int index = 0;
        for (Map.Entry<String, List<Float>> entry: objResourse.getVertsMap().entrySet()) {
            List<Float> vertsList = objResourse.getVertsMap().get(entry.getKey());
            List<Float> normsList = objResourse.getNormalsMap().get(entry.getKey());
            List<Float> uvsList = objResourse.getUvsMap().get(entry.getKey());
            List<Integer> indicesList = objResourse.getIndicesMap().get(entry.getKey());
            float[] verts = new float[vertsList.size()];
            float[] norms = new float[normsList.size()];
            float[] uvs = new float[uvsList.size()];
            int[] indices = new int[indicesList.size()];
            for (int j = 0; j < vertsList.size(); j++) {
                verts[j] = vertsList.get(j);
            }
            for (int j = 0; j < normsList.size(); j++) {
                norms[j] = normsList.get(j);
            }
            for (int j = 0; j < uvsList.size(); j++) {
                uvs[j] = uvsList.get(j);
            }
            for (int j = 0; j < indicesList.size(); j++) {
                indices[j] = indicesList.get(j);
            }

            String meshName = groupNames.size() == 0 ? name : groupNames.get(index);
            Node node = new Node();
            Mesh mesh = new Mesh();
            Primitive primitive = new Primitive();
            HashMap<String, Integer> attributes = new HashMap<>();

            Accessor indicesAccessor = new Accessor();
            Map<String, Number[]> indiceMinMax = getMinMax(indices);
            indicesAccessor.setName(meshName + "_" + index + "_indices");
            indicesAccessor.setCount(indices.length);
            indicesAccessor.setComponentType(5123);
            indicesAccessor.setType("SCALAR");
            indicesAccessor.setMin(indiceMinMax.get("min"));
            indicesAccessor.setMax(indiceMinMax.get("max"));
            indicesAccessor.setBufferView(this.glTF.getBufferViews().size());
            primitive.setIndices(this.glTF.getAccessors().size());
            this.glTF.getAccessors().add(indicesAccessor);
            IntBuffer indicesBuffer = IntBuffer.wrap(indices);
            BufferView indicesBufferView = new BufferView();
            indicesBufferView.setBuffer(0);
            indicesBufferView.setByteLength(indicesBuffer.capacity() * 4);
            indicesBufferView.setTarget(34963);
            this.glTF.getBufferViews().add(indicesBufferView);

            Accessor vertsAccessor = new Accessor();
            Map<String, Number[]> vertMinMax = getMinMax(verts, 3);
            vertsAccessor.setName(meshName + "_" + index + "_positions");
            vertsAccessor.setCount(verts.length / 3);
            vertsAccessor.setComponentType(5126);
            vertsAccessor.setType("VEC3");
            vertsAccessor.setMin(vertMinMax.get("min"));
            vertsAccessor.setMax(vertMinMax.get("max"));
            vertsAccessor.setBufferView(this.glTF.getBufferViews().size());
            attributes.put("POSITION", this.glTF.getAccessors().size());
            this.glTF.getAccessors().add(vertsAccessor);
            FloatBuffer vertsBuffer = FloatBuffer.wrap(verts);
            BufferView vertsBufferView = new BufferView();
            vertsBufferView.setBuffer(0);
            vertsBufferView.setByteLength(vertsBuffer.capacity() * 4);
            vertsBufferView.setTarget(34962);
            this.glTF.getBufferViews().add(vertsBufferView);

            Accessor normsAccessor = new Accessor();
            Map<String, Number[]> normMinMax = getMinMax(norms, 3);
            normsAccessor.setName(meshName + "_" + index + "_normals");
            normsAccessor.setCount(norms.length / 3);
            normsAccessor.setComponentType(5126);
            normsAccessor.setType("VEC3");
            normsAccessor.setMin(normMinMax.get("min"));
            normsAccessor.setMax(normMinMax.get("max"));
            normsAccessor.setBufferView(this.glTF.getBufferViews().size());
            attributes.put("NORMAL", this.glTF.getAccessors().size());
            this.glTF.getAccessors().add(normsAccessor);
            FloatBuffer normsBuffer = FloatBuffer.wrap(norms);
            BufferView normsBufferView = new BufferView();
            normsBufferView.setBuffer(0);
            normsBufferView.setByteLength(vertsBuffer.capacity() * 4);
            normsBufferView.setTarget(34962);
            this.glTF.getBufferViews().add(normsBufferView);

            FloatBuffer uvsBuffer = null;
            if (uvs.length > 0) {
                Accessor uvsAccessor = new Accessor();
                Map<String, Number[]> uvMinMax = getMinMax(uvs, 2);
                uvsAccessor.setName(meshName + "_" + index + "_uvs");
                uvsAccessor.setCount(uvs.length / 2);
                uvsAccessor.setComponentType(5126);
                uvsAccessor.setType("VEC2");
                uvsAccessor.setMin(uvMinMax.get("min"));
                uvsAccessor.setMax(uvMinMax.get("max"));
                uvsAccessor.setBufferView(this.glTF.getBufferViews().size());
                attributes.put("TEXCOORD_0", this.glTF.getAccessors().size());
                this.glTF.getAccessors().add(uvsAccessor);
                uvsBuffer = FloatBuffer.wrap(uvs);
                BufferView uvsBufferView = new BufferView();
                uvsBufferView.setBuffer(0);
                uvsBufferView.setByteLength(uvsBuffer.capacity() * 4);
                uvsBufferView.setTarget(34962);
                this.glTF.getBufferViews().add(normsBufferView);
            }
            primitive.setAttributes(attributes);
            primitive.setMaterial(0);
            mesh.getPrimitives().add(primitive);
            this.glTF.getMeshes().add(mesh);
            node.setMesh(index);
            this.glTF.getNodes().add(node);
            scene.getNodes().add(index);
            index++;
        }
//        glTF.getAccessors().add();
        System.out.println(this.glTF);
    }

    private ObjResourse read(URI objUri) throws IOException {
        List<Float> curVerts = new ArrayList<>();
        List<Float> curNorms = new ArrayList<>();
        List<Float> curUVs = new ArrayList<>();
        List<Integer> curIndices = new ArrayList<>();
        String currentGroupName = "_defaultGroup_";
        Integer currentGroupIndex = 0;
        Integer vertCount = 0;
        Map<String, Integer> hashindices = new HashMap<>();

        ObjResourse objResourse = new ObjResourse();
        String indexKey = currentGroupIndex.toString();
        objResourse.getVertsMap().put(indexKey, curVerts);
        objResourse.getNormalsMap().put(indexKey, curNorms);
        objResourse.getUvsMap().put(indexKey, curUVs);
        objResourse.getIndicesMap().put(indexKey, curIndices);
        // read start!
        InputStream inputStream = objUri.toURL().openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            List<String> elements = new ArrayList<>(Arrays.asList(line.split(" ")));
            elements.remove(0);
            if (line.contains("o ")) {
                name = line.substring(2).trim();
            } else if (line.contains("g ")) {
                currentGroupName = line.substring(2).trim();
                groupNames.add(currentGroupName);
                vertCount = 0;
                String indexKey2 = currentGroupIndex.toString();
                objResourse.getVertsMap().put(indexKey2, curVerts);
                objResourse.getNormalsMap().put(indexKey2, curNorms);
                objResourse.getUvsMap().put(indexKey2, curUVs);
                objResourse.getIndicesMap().put(indexKey2, curIndices);
                currentGroupIndex++;
            } else if (line.contains("v ")) {
                globalVerts.addAll(elements);
            } else if (line.contains("vn ")) {
                globalNorms.addAll(elements);
            } else if (line.contains("vt ")) {
                globalUVs.addAll(elements);
            } else if (line.contains("mtllib ")) {
                String mtllibLine = line.substring(7).trim();
                //// 引用外部的材质文件，暂不处理，因为此项目不用.mtl文件，由前端传入贴图
            } else if (line.contains("usemtl ")) {
                String materialName = line.substring(7).trim();
                materialNames.add(materialName);
            } else if (line.contains("f ")) {
                List<List<String>> triangles = triangulate(elements);
                for (List<String> triangle: triangles) {
                    for (int i = 0; i < triangle.size(); i++) {
                        String hash = triangle.get(i) + "," + currentGroupIndex;
                        if (hashindices.get(hash) == null) {
                            String[] split = triangle.get(i).split("/");

                            int normalIndex = split.length - 1;
                            // Vertex position
                            Float vX = Float.valueOf(globalVerts.get((Integer.valueOf(split[0]) - 1) * 3));
                            Float vY = Float.valueOf(globalVerts.get((Integer.valueOf(split[0]) - 1) * 3 + 1));
                            Float vZ = Float.valueOf(globalVerts.get((Integer.valueOf(split[0]) - 1) * 3 + 2));
                            curVerts.add(vX);
                            curVerts.add(vY);
                            curVerts.add(vZ);
                            // Vertex textures
                            if (globalUVs.size() > 0) {
                                Float tX = Float.valueOf(globalUVs.get((Integer.valueOf(split[1]) - 1) * 2));
                                Float tY = Float.valueOf(globalUVs.get((Integer.valueOf(split[1]) - 1) * 2 + 1));
                                curUVs.add(tX);
                                curUVs.add(tY);
                            }
                            // Vertex normals
                            Float nX = Float.valueOf(globalNorms.get((Integer.valueOf(split[normalIndex]) - 1) * 3));
                            Float nY = Float.valueOf(globalNorms.get((Integer.valueOf(split[normalIndex]) - 1) * 3 + 1));
                            Float nZ = Float.valueOf(globalNorms.get((Integer.valueOf(split[normalIndex]) - 1) * 3 + 2));
                            curNorms.add(nX);
                            curNorms.add(nY);
                            curNorms.add(nZ);
                            curIndices.add(vertCount++);
                            hashindices.put(hash, 1);
                        }
                    }
                }
            }
        }
        reader.close();
        return objResourse;
    }

    // 三角化
    private List<List<String>> triangulate(List<String> elements) {
        List<List<String>> lists = new ArrayList<>();
        if (elements.size() <= 3) {
            lists.add(elements);
        } else if (elements.size() == 4) {
            List<String> list1 = new ArrayList<>();
            list1.add(elements.get(0));
            list1.add(elements.get(1));
            list1.add(elements.get(2));
            List<String> list2 = new ArrayList<>();
            list2.add(elements.get(2));
            list2.add(elements.get(3));
            list2.add(elements.get(0));
            lists.add(list1);
            lists.add(list2);
        } else {
            for (int i = 1; i < elements.size() - 1; i++) {
                List<String> list = new ArrayList<>();
                list.add(elements.get(0));
                list.add(elements.get(i));
                list.add(elements.get(i + 1));
                lists.add(list);
            }
        }
        return lists;
    }
    private static String stripFileNameExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0) return fileName;
        return fileName.substring(0, lastDotIndex);
    }
    private Asset createAsset() {
        Asset asset = new Asset();
        asset.setGenerator("threedcger from https://3dcger.com");
        asset.setVersion("2.0");
        return asset;
    }
    private Map<String, Number[]> getMinMax(float[] array, Integer component) {
        Integer count = array.length / component;
        float[] min = new float[component], max = new float[component];
        for (int i = 0; i < count; i++) {
            float[] current = new float[component];
            for (int j = 0; j < component; j++) {
                current[j] = array[i + component + j];
                min[j] = Math.min(min[j], current[j]);
                max[j] = Math.max(max[j], current[j]);
            }
        }
        Map<String, Number[]> result = new HashMap<>();
        Number[] min1 = new Number[component];
        Number[] max1 = new Number[component];
        for (int i = 0; i < min.length; i++) {
            min1[i] = min[i];
            max1[i] = max[i];
        }
        result.put("min", min1);
        result.put("max", max1);
        return result;
    }
    private Map<String, Number[]> getMinMax(int[] array) {
        Number[] min = {0};
        Number[] max = {0};
        for (int i = 0; i < array.length; i++) {
            min[0] = Math.min(array[i], min[0].intValue());
            max[0] = Math.max(array[i], max[0].intValue());
        }
        Map<String, Number[]> result = new HashMap<>();
        result.put("min", min);
        result.put("max", max);
        return result;
    }

    @Data
    private class ObjResourse {
        private Map<String, List<Float>> vertsMap;
        private Map<String, List<Float>> normalsMap;
        private Map<String, List<Float>> uvsMap;
        private Map<String, List<Integer>> indicesMap;

        ObjResourse() {
            this.vertsMap = new HashMap<>();
            this.normalsMap = new HashMap<>();
            this.uvsMap = new HashMap<>();
            this.indicesMap = new HashMap<>();
        }
    }

    @Data
    private class BufferState {
        private FloatBuffer positionBuffers;
        private FloatBuffer normalBuffers;
        private FloatBuffer uvBuffers;
        private IntBuffer indexBuffers;
        private List<Integer> positionAccessors;
        private List<Integer> normalAccessors;
        private List<Integer> uvAccessors;
        private List<Integer> indexAccessors;
    }
}
