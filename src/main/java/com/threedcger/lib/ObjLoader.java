package com.threedcger.lib;

import com.threedcger.lib.gltf.GltfAsset;
import com.threedcger.lib.gltf.GltfModel;
import com.threedcger.lib.gltf.GltfModelWriter;
import com.threedcger.lib.obj.*;
import com.threedcger.utils.IO;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjLoader {
    private Pattern vertexPattern = Pattern.compile("v( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)");
    private Pattern normalPattern = Pattern.compile("vn( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)");
    private Pattern uvPattern = Pattern.compile("vt( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)");
    private int vertexCounter = 0;
    private int texCoordCounter = 0;
    private int normalCounter = 0;
    private Obj obj;
    private ObjFaceParser objFaceParser;
    private String lineBuffer = "";

    public void load(MtlDto mtlDto) throws IOException {
        URI objUri = Paths.get(mtlDto.getObjUrl()).toUri();
        URI baseUri = IO.getParent(objUri);
        String objFileName = IO.extractFileName(objUri);
        String baseName = stripFileNameExtension(objFileName);
        obj = new Obj();
        objFaceParser = new ObjFaceParser();
        obj = read(objUri);
        GltfAsset gltfAsset = new Convert().start(obj, baseName, baseUri, mtlDto);
        GltfModel gltfModel = new GltfModel(gltfAsset);

        GltfModelWriter gltfModelWriter = new GltfModelWriter();

        String outUrl;
        if (mtlDto.getGltfUrl() != null) {
            outUrl = mtlDto.getGltfUrl();
        } else {
            outUrl = baseUri + baseName + ".gltf";
        }
        File outputFile = new File(outUrl);

        File parentFile = outputFile.getParentFile();
        if (parentFile != null) parentFile.mkdirs();

        gltfModelWriter.write(gltfModel, outputFile);

    }
    private Obj read(URI objUri) throws IOException {
        InputStream inputStream = objUri.toURL().openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
        reader.close();
        return ObjUtils.convertToRenderable(obj);
    }
    private void parseLine(String line) throws IOException {
        line = line.trim();
        Matcher result = null;

        if (line.length() == 0 || '#' == line.charAt(0)) {
            // Don't process empty lines or comments
        } else if (line.matches("(?i)^o\\s+[\\s\\S]*")) {
            String objectName = line.substring(2).trim();
            obj.setName(objectName);
        } else if (line.matches("(?i)^g\\s+[\\s\\S]*")) {
            String s = line.substring(2).trim();
            String[] groupNames = readStrings(s);
            obj.setActiveGroupNames(Arrays.asList(groupNames));
        } else if (line.matches("(?i)^usemtl\\s+[\\s\\S]*")) {
            String materialName = line.substring(7).trim();
            obj.setActiveMaterialGroupName(materialName);
        } else if (line.matches("(?i)^mtllib\\s+[\\s\\S]*")) {
            String mtllibLine = line.substring(7).trim();
            obj.setMtlFileNames(Collections.singleton(mtllibLine));
        } else if ((result = vertexPattern.matcher(line)).find()) {
            float x = Float.valueOf(result.group(1));
            float y = Float.valueOf(result.group(2));
            float z = Float.valueOf(result.group(3));
            obj.addVertex(x, y, z);
            vertexCounter++;
        } else if ((result = normalPattern.matcher(line)).find()) {
            float x = Float.valueOf(result.group(1));
            float y = Float.valueOf(result.group(2));
            float z = Float.valueOf(result.group(3));
            obj.addNormal(x, y, z);
            normalCounter++;
        } else if ((result = uvPattern.matcher(line)).find()) {
            float x = Float.valueOf(result.group(1));
            float y = Float.valueOf(result.group(2));
            obj.addTexCoord(x, y);
            texCoordCounter++;
        } else {
            if ("\\".equals(line.substring(line.length()-1))) {
                lineBuffer += line.substring(0, line.length()-1);
                return;
            }
            lineBuffer += line;
            if ("f ".equals(lineBuffer.substring(0, 2))) {
                objFaceParser.parse(line);
                int[] v = objFaceParser.getVertexIndices();
                int[] vt = objFaceParser.getTexCoordIndices();
                int[] vn = objFaceParser.getNormalIndices();
                makeIndicesAbsolute(v, vertexCounter);
                makeIndicesAbsolute(vt, texCoordCounter);
                makeIndicesAbsolute(vn, normalCounter);
                obj.addFace(new ObjFace(v, vt, vn));
            }
            lineBuffer = "";
        }
    }

    private static void makeIndicesAbsolute(int[] array, int count) {
        if (array == null) return;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 0) {
                array[i] = count + array[i];
            } else {
                array[i]--;
            }
        }
    }

    private static String[] readStrings(String input) {
        StringTokenizer st = new StringTokenizer(input);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        return tokens.toArray(new String[tokens.size()]);
    }
    private static String stripFileNameExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0) return fileName;
        return fileName.substring(0, lastDotIndex);
    }
}