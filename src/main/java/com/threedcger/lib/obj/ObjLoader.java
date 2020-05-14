package com.threedcger.lib.obj;

import com.threedcger.lib.obj.model.*;
import com.threedcger.utils.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ObjLoader {
    private List<Node> nodes = new ArrayList<Node>();
    private Node node;
    private Mesh mesh;
    private Primitive primitive;
    private String activeMaterial;
    private Options options;

    private Map<String, Integer> vertexCache = new HashMap<String, Integer>();
    private final Integer vertexCacheLimit = 0;
    private Integer vertexCacheCount = 0;
    private Integer vertexCount = 0;

    private List<String> mtlPaths = new ArrayList<String>();

    private String lineBuffer = "";

    private List<String> faceVertices = new ArrayList<String>();
    private List<String> facePositions = new ArrayList<String>();
    private List<String> faceUvs = new ArrayList<String>();
    private List<String> faceNormals = new ArrayList<String>();

    private Pattern vertexPattern = Pattern.compile("v( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)");
    private Pattern normalPattern = Pattern.compile("vn( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)");
    private Pattern uvPattern = Pattern.compile("vt( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)");
//    private Pattern facePattern = Pattern.compile("(-?\\d+)\\/?(-?\\d*)\\/?(-?\\d*)");

    private Cartesian3 scratchCartesian = new Cartesian3();

    private Matrix4 axisTransform = null;
    // Global store of vertex attributes listed in the obj file
    private List<Float> globalPositions = new ArrayList<Float>();
    private List<Float> globalNormals = new ArrayList<Float>();
    private List<Float> globalUvs = new ArrayList<Float>();

    private Cartesian3 scratch1 = new Cartesian3();
    private Cartesian3 scratch2 = new Cartesian3();
    private Cartesian3 scratch3 = new Cartesian3();
    private Cartesian3 scratch4 = new Cartesian3();
    private Cartesian3 scratch5 = new Cartesian3();
    private Cartesian3 scratchCenter = new Cartesian3();
    private Cartesian3 scratchAxis1 = new Cartesian3();
    private Cartesian3 scratchAxis2 = new Cartesian3();
    private Cartesian3 scratchNormal = new Cartesian3();
    private List<Cartesian3> scratchPositions = new ArrayList<Cartesian3>();
    private List<Integer> scratchVertexIndices = new ArrayList<Integer>();
    private List<Cartesian3> scratchPoints = new ArrayList<Cartesian3>();

    public ObjLoader(Options options) {
        this.options = options;
        this.axisTransform = this.getAxisTransform(options.getInputUpAxis(), options.getOutputUpAxis());
        for (int i = 0; i < 4; i++) {
            scratchPositions.add(new Cartesian3());
        }
    }

    public ObjData load(String objPath) throws IOException {
        this.addNode("");
        return this.readLines(objPath);
    }
    private ObjData readLines(String path) throws IOException {
        File file = new File(path);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            this.parseLine(line);
        }
        bufferedReader.close();
        // Unload resources
        globalPositions.clear();
        globalNormals.clear();
        globalUvs.clear();
        return finishLoading(nodes, mtlPaths, path, activeMaterial != null, options);
    }
    private void parseLine(String line) {
        line = line.trim();
        Matcher result = null;

        if (line.length() == 0 || '#' == line.charAt(0)) {
            // Don't process empty lines or comments
        } else if (line.matches("(?i)^o\\s+[\\s\\S]*")) {
            String objectName = line.substring(2).trim();
            this.addNode(objectName);
        } else if (line.matches("(?i)^g\\s+[\\s\\S]*")) {
            String groupName = line.substring(2).trim();
            this.addMesh(groupName);
        } else if (line.matches("(?i)^usemtl\\s+[\\s\\S]*")) {
            String materialName = line.substring(7).trim();
            this.useMaterial(materialName);
        } else if (line.matches("(?i)^mtllib\\s+[\\s\\S]*")) {
            String mtllibLine = line.substring(7).trim();
            this.mtlPaths.addAll(this.getMtlPaths(mtllibLine));
        } else if ((result = vertexPattern.matcher(line)).find()) {
            Cartesian3 position = scratchCartesian;
            position.setX(Float.valueOf(result.group(1)));
            position.setY(Float.valueOf(result.group(2)));
            position.setZ(Float.valueOf(result.group(3)));
            if (axisTransform != null) {
                Matrix4.multiplyByPoint(axisTransform, position, position);
            }
            globalPositions.add(position.getX());
            globalPositions.add(position.getY());
            globalPositions.add(position.getZ());
        } else if ((result = normalPattern.matcher(line)).find()) {
            Cartesian3 normal = Cartesian3.fromElements(Float.valueOf(result.group(1)), Float.valueOf(result.group(2)), Float.valueOf(result.group(3)), scratchNormal);
            if (Cartesian3.equals(normal, Cartesian3.ZERO)) {
                Cartesian3.clone(Cartesian3.UNIT_Z, normal);
            } else {
                Cartesian3.normalize(normal, normal);
            }
            if (axisTransform != null) {
                Matrix4.multiplyByPointAsVector(axisTransform, normal, normal);
            }
            globalNormals.add(normal.getX());
            globalNormals.add(normal.getY());
            globalNormals.add(normal.getZ());
        } else if ((result = uvPattern.matcher(line)).find()) {
            globalUvs.add(Float.valueOf(result.group(1)));
            globalUvs.add(1.0f - Float.valueOf(result.group(2)));
        } else {
            if ("\\".equals(line.substring(line.length()-1))) {
                lineBuffer += line.substring(0, line.length()-1);
                return;
            }
            lineBuffer += line;
            if ("f ".equals(lineBuffer.substring(0, 2))) {
                String[] group = lineBuffer.replaceAll("f ", "").split(" ");
                for (int i = 0; i < group.length; i++) {
                    faceVertices.add(group[i]);
                    String[] items = group[i].split("/");
                    facePositions.add(items[0]);
                    faceUvs.add(items[1]);
                    faceNormals.add(items[2]);
                }
                if (faceVertices.size() > 2) {
                    addFace(faceVertices, facePositions, faceUvs, faceNormals);
                }
                faceVertices.clear();
                facePositions.clear();
                faceUvs.clear();
                faceNormals.clear();
            }
            lineBuffer = "";
        }
    }
    private ObjData finishLoading(List<Node> nodes, List<String> mtlPaths, String objPath, Boolean usesMaterials, Options options) {
        nodes = cleanNodes(nodes);
        if (nodes.size() == 0) {
            throw new RuntimeException(objPath + " does not have any geometry data");
        }
        File file = new File(objPath);
        String fullName = file.getName();
        String objDirectory = file.getParent();
        Integer dotIndex = fullName.lastIndexOf('.');
        String name = (dotIndex == -1) ? fullName : fullName.substring(0, dotIndex);
        try {
            List<Mtl> materials = loadMtls(mtlPaths, objDirectory, options);
            if (materials.size() > 0 && !usesMaterials) {
                assignDefaultMaterial(nodes, materials);
            }
            assignUnnamedMaterial(nodes, materials);

            ObjData objData = new ObjData();
            objData.setName(name);
            objData.setNodes(nodes);
            objData.setMtls(materials);
            return objData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void assignDefaultMaterial(List<Node> nodes, List<Mtl> materials) {
        String defaultMaterial = materials.get(0).getName();
        int nodesLength = nodes.size();
        for (int i = 0; i < nodesLength; ++i) {
            List<Mesh> meshes = nodes.get(i).getMeshes();
            int meshesLength = meshes.size();
            for (int j = 0; j < meshesLength; ++j) {
                List<Primitive> primitives = meshes.get(j).getPrimitives();
                int primitivesLength = primitives.size();
                for (int k = 0; k < primitivesLength; ++k) {
                    Primitive primitive = primitives.get(k);
                    primitive.setMaterial(primitive.getMaterial() != null ? primitive.getMaterial() : defaultMaterial);
                }
            }
        }
    }
    private void assignUnnamedMaterial(List<Node> nodes, List<Mtl> materials) {
        Mtl unnamedMaterial = null;
        for (Mtl material: materials) {
            if (material.getName() == null || material.getName().length() == 0) {
                unnamedMaterial = material;
            }
        }
        if (unnamedMaterial == null) return;

        int nodesLength = nodes.size();
        for (int i = 0; i < nodesLength; ++i) {
            List<Mesh> meshes = nodes.get(i).getMeshes();
            int meshesLength = meshes.size();
            for (int j = 0; j < meshesLength; ++j) {
                List<Primitive> primitives = meshes.get(j).getPrimitives();
                int primitivesLength = primitives.size();
                for (int k = 0; k < primitivesLength; ++k) {
                    Primitive primitive = primitives.get(k);
                    if (primitive.getMaterial() == null) {
                        primitive.setMaterial(unnamedMaterial.getName());
                    }
                }
            }
        }
    }
    private String normalizeMtlPath(String mtlPath, String objDirectory) {
        mtlPath = mtlPath.replaceAll("\\\\", "/");
        return objDirectory + "/" + mtlPath;
    }
    private List<Mtl> loadMtls(List<String> mtlPaths, String objDirectory, Options options) throws IOException {
        // Remove duplicates
        LinkedHashSet<String> set = new LinkedHashSet<String>(mtlPaths.size());
        set.addAll(mtlPaths);
        mtlPaths.clear();
        mtlPaths.addAll(set);

        List<Mtl> list = new ArrayList<Mtl>();
        for (String mtlPath: mtlPaths) {
            mtlPath = normalizeMtlPath(mtlPath, objDirectory);
            MtlLoader mtlLoader = new MtlLoader(options);
            List<Mtl> mtls = mtlLoader.load(mtlPath);
            list.addAll(mtls);
        }
        return list;
    }
    private void clearVertexCache() {
        this.vertexCache.clear();
        this.vertexCacheCount = 0;
    }
    private void addNode(String name) {
        this.node = new Node();
        this.node.setName(name);
        this.nodes.add(this.node);
        this.addMesh("");
    }
    private void addMesh(String name) {
        this.mesh = new Mesh();
        this.mesh.setName(name);
        this.node.getMeshes().add(this.mesh);
        this.addPrimitive();
    }
    private void addPrimitive() {
        this.primitive = new Primitive();
        this.primitive.setMaterial(this.activeMaterial);
        this.mesh.getPrimitives().add(this.primitive);
        // Clear the vertex cache for each new primitive
        this.clearVertexCache();
        this.vertexCount = 0;
    }
    private Primitive reusePrimitive() {
        List<Primitive> primitives = this.mesh.getPrimitives();
        for (Primitive primitive: primitives) {
            if (primitive.getMaterial() == this.activeMaterial) {
                this.primitive = primitive;
                this.clearVertexCache();
                this.vertexCount = primitive.getPositions().size() / 3;
                return primitive;
            }
        }
        this.addPrimitive();
        return null;
    }
    private void useMaterial(String name) {
        this.activeMaterial = name;
        this.reusePrimitive();
    }
    private Boolean faceAndPrimitiveMatch(List<String> uvs, List<String> normals, Primitive primitive) {
        Boolean faceHasUvs = uvs.get(0) != null;
        Boolean faceHasNormals = normals.get(0) != null;
        Boolean primitiveHasUvs = primitive.getUvs().size() > 0;
        Boolean primitiveHasNormals = primitive.getNormals().size() > 0;
        return primitiveHasUvs == faceHasUvs && primitiveHasNormals == faceHasNormals;
    }
    private void checkPrimitive(List<String> uvs, List<String> normals) {
        Boolean firstFace = primitive.getIndices().size() == 0;
        if (!firstFace && !faceAndPrimitiveMatch(uvs, normals, primitive)) {
            Primitive primitive1 = reusePrimitive();
            faceAndPrimitiveMatch(uvs, normals, primitive1);
        }
    }
    private Integer getIndexFromStart(String index, List<Float> attributeData, Integer components) {
        Integer i = Integer.valueOf(index);
        if (i < 0) {
            return attributeData.size() / components + i;
        }
        return i - 1;
    }
    private void correctAttributeIndices(List<String> attributeIndices, List<Float> attributeData, Integer components) {
        for (int i = 0; i < attributeIndices.size(); i++) {
            if (attributeIndices.get(i).length() == 0) {
                attributeIndices.set(i, null);
            } else {
                attributeIndices.set(i, String.valueOf(getIndexFromStart(attributeIndices.get(i), attributeData, components)));
            }
        }
    }
    private void correctVertices(List<String> vertices, List<String> positions, List<String> uvs, List<String> normals) {
        for (int i = 0; i < vertices.size(); i++) {
            String position = positions.get(i) != null ? positions.get(i) : "";
            String uv = uvs.get(i) != null ? uvs.get(i) : "";
            String normal = normals.get(i) != null ? normals.get(i) : "";
            vertices.set(i, position + "/" + uv + "/" + normal);
        }
    }
    private void createVertex(String p, String u, String n) throws Exception {
        // Positions
        if (p != null && globalPositions.size() > 0) {
            Integer intP = Integer.valueOf(p);
            if (intP * 3 >= globalPositions.size()) {
                throw new Exception("Position index " + p + " is out of bounds");
            }
            Float px = globalPositions.get(intP * 3);
            Float py = globalPositions.get(intP * 3 + 1);
            Float pz = globalPositions.get(intP * 3 + 2);
            primitive.getPositions().add(px);
            primitive.getPositions().add(py);
            primitive.getPositions().add(pz);
        }
        // Normals
        if (n != null && globalNormals.size() > 0) {
            Integer intN = Integer.valueOf(n);
            if (intN * 3 >= globalNormals.size()) {
                throw new Exception("Normal index " + n + " is out of bounds");
            }
            Float nx = globalNormals.get(intN * 3);
            Float ny = globalNormals.get(intN * 3 + 1);
            Float nz = globalNormals.get(intN * 3 + 2);
            primitive.getNormals().add(nx);
            primitive.getNormals().add(ny);
            primitive.getNormals().add(nz);
        }
        // UVs
        if (u != null && globalUvs.size() > 0) {
            Integer intU = Integer.valueOf(u);
            if (intU * 2 >= globalUvs.size()) {
                throw new Exception("UV index " + u + " is out of bounds");
            }
            Float ux = globalUvs.get(intU * 2);
            Float uy = globalUvs.get(intU * 2 + 1);
            primitive.getUvs().add(ux);
            primitive.getUvs().add(uy);
        }
    }
    private Integer addVertex(String v, String p, String u, String n) {
        Integer index = vertexCache.get(v);
        if (index == null) {
            index = vertexCount++;
            vertexCache.put(v, index);
            try {
                createVertex(p, u, n);
                // Prevent the vertex cache from growing too large. As a result of clearing the cache there
                // may be some duplicate vertices.
                vertexCacheCount++;
                if (vertexCacheCount > vertexCacheLimit) {
                    clearVertexCache();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return index;
    }
    private Cartesian3 getPosition(String index, Cartesian3 result) {
        Float px = globalPositions.get(Integer.valueOf(index) * 3);
        Float py = globalPositions.get(Integer.valueOf(index) * 3 + 1);
        Float pz = globalPositions.get(Integer.valueOf(index) * 3 + 2);
        return Cartesian3.fromElements(px, py, pz, result);
    }
    private Cartesian3 getNormal(String index, Cartesian3 result) {
        Float nx = globalNormals.get(Integer.valueOf(index) * 3);
        Float ny = globalNormals.get(Integer.valueOf(index) * 3 + 1);
        Float nz = globalNormals.get(Integer.valueOf(index) * 3 + 2);
        return Cartesian3.fromElements(nx, ny, nz, result);
    }
    private Boolean checkWindingCorrect(String positionIndex1, String positionIndex2, String positionIndex3, String normalIndex) {
        if (normalIndex == null || "".equals(normalIndex)) {
            // If no face normal, we have to assume the winding is correct.
            return true;
        }
        Cartesian3 normal = getNormal(normalIndex, scratchNormal);
        Cartesian3 A = getPosition(positionIndex1, scratch1);
        Cartesian3 B = getPosition(positionIndex2, scratch2);
        Cartesian3 C = getPosition(positionIndex3, scratch3);

        Cartesian3 BA = Cartesian3.subtract(B, A, scratch4);
        Cartesian3 CA = Cartesian3.subtract(C, A, scratch5);
        Cartesian3 cross = Cartesian3.cross(BA, CA, scratch3);
        return Cartesian3.dot(normal, cross) >= 0;
    }
    private void addTriangle(Integer index1, Integer index2, Integer index3, Boolean correctWinding) {
        if (correctWinding) {
            primitive.getIndices().add(index1);
            primitive.getIndices().add(index2);
            primitive.getIndices().add(index3);
        } else {
            primitive.getIndices().add(index1);
            primitive.getIndices().add(index3);
            primitive.getIndices().add(index2);
        }
    }
    private void addFace(List<String> vertices, List<String> positions, List<String> uvs, List<String> normals) {
        correctAttributeIndices(positions, globalPositions, 3);
        correctAttributeIndices(normals, globalNormals, 3);
        correctAttributeIndices(uvs, globalUvs, 2);
        correctVertices(vertices, positions, uvs, normals);

        checkPrimitive(uvs, faceNormals);

        if (vertices.size() == 3) {
            Boolean isWindingCorrect = checkWindingCorrect(positions.get(0), positions.get(1), positions.get(2), normals.get(0));
            Integer index1 = addVertex(vertices.get(0), positions.get(0), uvs.get(0), normals.get(0));
            Integer index2 = addVertex(vertices.get(1), positions.get(1), uvs.get(1), normals.get(1));
            Integer index3 = addVertex(vertices.get(2), positions.get(2), uvs.get(2), normals.get(2));
            addTriangle(index1, index2, index3, isWindingCorrect);
        } else {  // Triangulate if the face is not a triangle
            List<Cartesian3> points = this.scratchPoints;
            List<Integer> vertexIndices = this.scratchVertexIndices;
            points.clear();
            vertexIndices.clear();
            for (int i = 0; i < vertices.size(); i++) {
                Integer index = addVertex(vertices.get(i), positions.get(i), uvs.get(i), normals.get(i));
                vertexIndices.add(index);
                if (i == scratchPositions.size()) {
                    scratchPositions.add(new Cartesian3());
                }
                points.add(getPosition(positions.get(i), scratchPositions.get(i)));
            }
            Boolean validGeometry = CoplanarPolygonGeometryLibrary.computeProjectTo2DArguments(points, scratchCenter, scratchAxis1, scratchAxis2);
            if (!validGeometry) return;
            List<Cartesian2> points2D = CoplanarPolygonGeometryLibrary.createProjectPointsTo2DFunction(scratchCenter, scratchAxis1, scratchAxis2, points);
            List<Integer> indices = PolygonPipeline.triangulate(points2D, null);
            Boolean isWindingCorrect = PolygonPipeline.computeWindingOrder2D(points2D) != WindingOrder.CLOCKWISE;
            for (int i = 0; i < indices.size() - 2; i += 3) {
                addTriangle(vertexIndices.get(indices.get(i)), vertexIndices.get(indices.get(i+1)), vertexIndices.get(indices.get(i+2)), isWindingCorrect);
            }
        }
    }
    private List<String> getMtlPaths(String mtllibLine) {
        List<String> mtlPaths = new ArrayList<String>();
        String[] splits = mtllibLine.split(" ");
        Integer startIndex = 0;
        for (int i = 0; i < splits.length; i++) {
            String suffix = splits[i].substring(splits[i].lastIndexOf("."), splits[i].length());
            if (".mtl".equals(suffix)) {
                String[] strings = Arrays.copyOfRange(splits, startIndex, i + 1);
                String mtlPath = String.join(" ", strings);
                startIndex = i + 1;
                mtlPaths.add(mtlPath);
            }
        }
        return mtlPaths;
    }
    private List<Mesh> removeEmptyMeshes(List<Mesh> meshes) {
        return meshes.stream().filter((Mesh m) -> {
            List<Primitive> primitives = m.getPrimitives().stream().filter((Primitive p) -> p.getIndices().size() > 0 && p.getPositions().size() > 0).collect(Collectors.toList());
            m.setPrimitives(primitives);
            return m.getPrimitives().size() > 0;
        }).collect(Collectors.toList());
    }
    private Boolean meshesHaveNames(List<Mesh> meshes) {
        Integer meshesLength = meshes.size();
        for (int i = 0; i < meshesLength; i++) {
            if (meshes.get(i).getName() != null) return true;
        }
        return false;
    }
    private List<Node> removeEmptyNodes(List<Node> nodes) {
        List<Node> finals = new ArrayList<>();
        Integer nodesLength = nodes.size();
        for (int i = 0; i < nodesLength; i++) {
            Node node = nodes.get(i);
            List<Mesh> meshes = removeEmptyMeshes(node.getMeshes());
            if (meshes.size() == 0) continue;
            node.setMeshes(meshes);
            if (node.getName() == null && meshesHaveNames(meshes)) {
                Integer meshesLength = meshes.size();
                for (int j = 0; j < meshesLength; j++) {
                    Node convertedNode = new Node();
                    convertedNode.setName(meshes.get(j).getName());
                    convertedNode.setMeshes(Arrays.asList(meshes.get(j)));
                    finals.add(convertedNode);
                }
            } else {
                finals.add(node);
            }
        }
        return finals;
    }
    private void setDefaults(List<Node> nodes) {
        HashMap<String, Integer> usedNames = new HashMap<>();
        Integer nodesLength = nodes.size();
        for (int i = 0; i < nodesLength; i++) {
            Node node = nodes.get(i);
            String name = node.getName() != null ? node.getName() : "Node";
            Integer occurrences = usedNames.get(name);
            if (occurrences != null) {
                usedNames.put(name, usedNames.get(name) + 1);
                name = name + "_" + occurrences;
            } else {
                usedNames.put(name, 1);
            }
            node.setName(name);
        }
        for (int j = 0; j < nodesLength; j++) {
            Node node = nodes.get(j);
            List<Mesh> meshes = node.getMeshes();
            Integer meshLength = meshes.size();
            for (int x = 0; x < meshLength; x++) {
                Mesh mesh = meshes.get(x);
                String meshName = mesh.getName() != null ? mesh.getName() : node.getName() + "-Mesh";
                Integer meshOccurrences = usedNames.get(meshName);
                if (meshOccurrences != null) {
                    usedNames.put(meshName, usedNames.get(meshName) + 1);
                    meshName = meshName + "-" + meshOccurrences;
                } else {
                    usedNames.put(meshName, 1);
                }
                mesh.setName(meshName);
            }
        }
    }
    private List<Node> cleanNodes(List<Node> nodes) {
        nodes = removeEmptyNodes(nodes);
        setDefaults(nodes);
        return nodes;
    }
    private Matrix4 getAxisTransform(String inputUpAxis, String outputUpAxis) {
        if ("X".equals(inputUpAxis) && "Y".equals(outputUpAxis)) {
            return Axis.X_UP_TO_Y_UP;
        } else if ("X".equals(inputUpAxis) && "Z".equals(outputUpAxis)) {
            return Axis.X_UP_TO_Z_UP;
        } else if ("Y".equals(inputUpAxis) && "X".equals(outputUpAxis)) {
            return Axis.Y_UP_TO_X_UP;
        } else if ("Y".equals(inputUpAxis) && "Z".equals(outputUpAxis)) {
            return Axis.Y_UP_TO_Z_UP;
        } else if ("Z".equals(inputUpAxis) && "X".equals(outputUpAxis)) {
            return Axis.Z_UP_TO_X_UP;
        } else if ("Z".equals(inputUpAxis) && "Y".equals(outputUpAxis)) {
            return Axis.Z_UP_TO_Y_UP;
        }
        return null;
    }
}
