/*
 * www.javagl.de - Obj
 *
 * Copyright (c) 2008-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.threedcger.lib.obj;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ObjUtils {
    public static Obj convertToRenderable(Obj input) {
        return convertToRenderable(input, new Obj());
    }
    public static Obj convertToRenderable(Obj input, Obj output) {
        Obj obj = triangulate(input);
        obj = makeTexCoordsUnique(obj);
        obj = makeNormalsUnique(obj);
        return makeVertexIndexed(obj, output);
    }

    public static Obj triangulate(Obj input) {
        return triangulate(input, new Obj());
    }

    public static Obj triangulate(Obj input, Obj output) {
        output.setMtlFileNames(input.getMtlFileNames());
        addAll(input, output);
        for (int i = 0; i < input.getNumFaces(); i++) {
            ObjFace face = input.getFace(i);
            activateGroups(input, face, output);
            if (face.getNumVertices() == 3) {
                output.addFace(face);
            } else {
                for(int j = 0; j < face.getNumVertices() - 2; j++) {
                    ObjFace triangle = ObjFaces.create(face, 0, j + 1, j + 2);
                    output.addFace(triangle);
                }
            }
        }
        return output;
    }

    public static Obj groupToObj(Obj input, ObjGroup inputGroup, List<Integer> vertexIndexMapping) {
        return groupToObj(input, inputGroup, vertexIndexMapping, new Obj());
    }

    public static Obj groupToObj(Obj input, ObjGroup inputGroup, List<Integer> vertexIndexMapping, Obj output) {
        output.setMtlFileNames(input.getMtlFileNames());

        // vertexIndexMap[i] contains the index that vertex i of the 
        // original Obj will have in the output
        int vertexIndexMap[] = new int[input.getNumVertices()];
        int texCoordIndexMap[] = new int[input.getNumTexCoords()];
        int normalIndexMap[] = new int[input.getNumNormals()];

        Arrays.fill(vertexIndexMap, -1);
        Arrays.fill(texCoordIndexMap, -1);
        Arrays.fill(normalIndexMap, -1);

        int vertexCounter = 0;
        int texCoordCounter = 0;
        int normalCounter = 0;
        for(int i = 0; i < inputGroup.getNumFaces(); i++) {
            // Clone the face info from the input
            ObjFace face = inputGroup.getFace(i);

            ObjFace resultFace = ObjFaces.create(face);
            
            activateGroups(input, face, output);
            
            // The indices of the cloned face have to be adjusted, 
            // so that they point to the correct vertices in the output
            for(int j = 0; j < face.getNumVertices(); j++)
            {
                int vertexIndex = face.getVertexIndex(j);
                if(vertexIndexMap[vertexIndex] == -1)
                {
                    vertexIndexMap[vertexIndex] = vertexCounter;
                    output.addVertex(input.getVertex(vertexIndex));
                    vertexCounter++;
                }
                resultFace.setVertexIndex(j, vertexIndexMap[vertexIndex]);
            }

            if(face.containsTexCoordIndices())
            {
                for(int j = 0; j < face.getNumVertices(); j++)
                {
                    int texCoordIndex = face.getTexCoordIndex(j);
                    if(texCoordIndexMap[texCoordIndex] == -1)
                    {
                        texCoordIndexMap[texCoordIndex] = texCoordCounter;
                        output.addTexCoord(input.getTexCoord(texCoordIndex));
                        texCoordCounter++;
                    }
                    resultFace.setTexCoordIndex(
                        j, texCoordIndexMap[texCoordIndex]);
                }
            }


            if(face.containsNormalIndices())
            {
                for(int j = 0; j < face.getNumVertices(); j++)
                {
                    int normalIndex = face.getNormalIndex(j);
                    if(normalIndexMap[normalIndex] == -1)
                    {
                        normalIndexMap[normalIndex] = normalCounter;
                        output.addNormal(input.getNormal(normalIndex));
                        normalCounter++;
                    }
                    resultFace.setNormalIndex(j, normalIndexMap[normalIndex]);
                }
            }

            // Add the cloned face with the adjusted indices to the output
            output.addFace(resultFace);
        }

        // Compute the vertexIndexMapping, so that vertexIndexMapping.get(i) 
        // afterwards stores the index that vertex i had in the input 
        if(vertexIndexMapping != null) {
            for(int i = 0; i < vertexCounter; i++)
            {
                vertexIndexMapping.add(-1);
            }
            for(int i = 0; i < input.getNumVertices(); i++)
            {
                if(vertexIndexMap[i] != -1)
                {
                    vertexIndexMapping.set(vertexIndexMap[i], i);
                }
            }
        }
        
        return output;
    }
    
    

    private interface PropertyIndexAccessor {

        int getPropertyIndex(Obj input, ObjFace face, int vertexNumber);
        
        /**
         * Returns whether the given face has indices for the property
         * that may be queries with this accessor
         * 
         * @param face The face
         * @return Whether the face has the property 
         */
        boolean hasProperty(ObjFace face);
    }
    

    public static Obj makeTexCoordsUnique(Obj input) {

        return makeTexCoordsUnique(input, null, new Obj());
    }

    public static Obj makeTexCoordsUnique(Obj input, List<Integer> indexMapping, Obj output) {
        PropertyIndexAccessor accessor = new PropertyIndexAccessor() {
            @Override
            public int getPropertyIndex(
                Obj input, ObjFace face, int vertexNumber)
            {
                return face.getTexCoordIndex(vertexNumber);
            }

            @Override
            public boolean hasProperty(ObjFace face)
            {
                return face.containsTexCoordIndices();
            }
        };
        makePropertiesUnique(input, accessor, indexMapping, output);
        return output;
    }

    public static Obj makeNormalsUnique(Obj input) {

        return makeNormalsUnique(input, null, new Obj());
    }

    public static Obj makeNormalsUnique(Obj input, List<Integer> indexMapping, Obj output) {
        PropertyIndexAccessor accessor = new PropertyIndexAccessor() {
            @Override
            public int getPropertyIndex(
                Obj input, ObjFace face, int vertexNumber)
            {
                return face.getNormalIndex(vertexNumber);
            }
            
            @Override
            public boolean hasProperty(ObjFace face)
            {
                return face.containsNormalIndices();
            }
        };
        makePropertiesUnique(input, accessor, indexMapping, output);
        return output;
    }

    private static void makePropertiesUnique(
        Obj input, PropertyIndexAccessor propertyIndexAccessor,
        List<Integer> indexMapping, Obj output) {
        output.setMtlFileNames(input.getMtlFileNames());
        addAll(input, output);
        
        int usedPropertyIndices[] = new int[input.getNumVertices()];
        Arrays.fill(usedPropertyIndices, -1);
        List<FloatTuple> extendedVertices = new ArrayList<FloatTuple>();

        for(int i = 0; i < input.getNumFaces(); i++) {
            ObjFace inputFace = input.getFace(i);
            
            activateGroups(input, inputFace, output);
            
            ObjFace outputFace = inputFace;
            
            if (propertyIndexAccessor.hasProperty(inputFace))
            {
                ObjFace extendedOutputFace = null;
                
                for(int j = 0; j < outputFace.getNumVertices(); j++)
                {
                    int vertexIndex = outputFace.getVertexIndex(j);
                    int propertyIndex = 
                        propertyIndexAccessor.getPropertyIndex(
                            input, outputFace, j);
    
                    // Check if the property of the vertex with the current
                    // index already has been used, and it is not equal to
                    // the property that it has in the current face
                    if(usedPropertyIndices[vertexIndex] != -1 &&
                       usedPropertyIndices[vertexIndex] != propertyIndex)  
                    {
                        FloatTuple vertex = input.getVertex(vertexIndex);
    
                        // Add the vertex which has multiple properties once
                        // more to the output, and update all indices that 
                        // now have to point to the "new" vertex
                        int extendedVertexIndex = 
                            input.getNumVertices() + extendedVertices.size();
                        extendedVertices.add(vertex);
                        output.addVertex(vertex);
                        
                        if (extendedOutputFace == null)
                        {
                            extendedOutputFace = ObjFaces.create(inputFace);
                        }
                        extendedOutputFace.setVertexIndex(
                            j, extendedVertexIndex);
    
                        if(indexMapping != null)
                        {
                            int indexInObj = indexMapping.get(vertexIndex);
                            indexMapping.add(indexInObj);
                        }
                    }
                    else
                    {
                        usedPropertyIndices[vertexIndex] = propertyIndex;
                    }
                }
                if (extendedOutputFace != null)
                {
                    outputFace = extendedOutputFace;
                }
            }
            output.addFace(outputFace);
        }
    }
    

    private static void addAll(Obj input, Obj output) {
        for (int i=0; i<input.getNumVertices(); i++)
        {
            output.addVertex(input.getVertex(i));
        }
        for (int i=0; i<input.getNumTexCoords(); i++)
        {
            output.addTexCoord(input.getTexCoord(i));
        }
        for (int i=0; i<input.getNumNormals(); i++)
        {
            output.addNormal(input.getNormal(i));
        }
    }

    public static void add(Obj input, Obj output) {
        int verticesOffset = output.getNumVertices();
        for (int i=0; i<input.getNumVertices(); i++) {
            output.addVertex(input.getVertex(i));
        }
        
        int texCoordsOffset = output.getNumTexCoords();
        for (int i=0; i<input.getNumTexCoords(); i++) {
            output.addTexCoord(input.getTexCoord(i));
        }
        
        int normalsOffset = output.getNumNormals();
        for (int i=0; i<input.getNumNormals(); i++) {
            output.addNormal(input.getNormal(i));
        }
        
        for(int i = 0; i < input.getNumFaces(); i++) {
            ObjFace inputFace = input.getFace(i);
            
            activateGroups(input, inputFace, output);
            
            ObjFace outputFace = ObjFaces.createWithOffsets(
                inputFace, verticesOffset, texCoordsOffset, normalsOffset);
            output.addFace(outputFace);
        }
    }

    public static Obj makeVertexIndexed(Obj input) {

        return makeVertexIndexed(input, new Obj());
    }

    public static Obj makeVertexIndexed(Obj input, Obj output) {
        output.setMtlFileNames(input.getMtlFileNames());
        for (int i=0; i<input.getNumVertices(); i++) {
            output.addVertex(input.getVertex(i));
        }
        
        boolean foundTexCoords = false;
        boolean foundNormals = false;
        int texCoordIndicesForVertexIndices[] = new int[input.getNumVertices()];
        int normalIndicesForVertexIndices[] = new int[input.getNumVertices()];
        for(int i = 0; i < input.getNumFaces(); i++) {
            ObjFace inputFace = input.getFace(i);
            for(int j = 0; j < inputFace.getNumVertices(); j++)
            {
                int vertexIndex = inputFace.getVertexIndex(j);
                if (inputFace.containsTexCoordIndices())
                {
                    int texCoordIndex = inputFace.getTexCoordIndex(j);
                    texCoordIndicesForVertexIndices[vertexIndex] =
                        texCoordIndex;
                    foundTexCoords = true;
                }
                if (inputFace.containsNormalIndices())
                {
                    int normalIndex = inputFace.getNormalIndex(j);
                    normalIndicesForVertexIndices[vertexIndex] =
                        normalIndex;
                    foundNormals = true;
                }
            }
        }
        
        if (foundTexCoords)
        {
            for (int i=0; i<input.getNumVertices(); i++)
            {
                int texCoordIndex = texCoordIndicesForVertexIndices[i];
                FloatTuple texCoord = input.getTexCoord(texCoordIndex);
                output.addTexCoord(texCoord);
            }
        }
        
        if (foundNormals)
        {
            for (int i=0; i<input.getNumVertices(); i++)
            {
                int normalIndex = normalIndicesForVertexIndices[i];
                FloatTuple normal = input.getNormal(normalIndex);
                output.addNormal(normal);
            }
        }
        
        for(int i = 0; i < input.getNumFaces(); i++)
        {
            ObjFace inputFace = input.getFace(i);
            
            activateGroups(input, inputFace, output);
            
            ObjFace outputFace = ObjFaces.create(inputFace);
            if (inputFace.containsTexCoordIndices())
            {
                for (int j=0; j<inputFace.getNumVertices(); j++)
                {
                    outputFace.setTexCoordIndex(j, 
                        outputFace.getVertexIndex(j));
                }
            }
            if (inputFace.containsNormalIndices())
            {
                for (int j=0; j<inputFace.getNumVertices(); j++)
                {
                    outputFace.setNormalIndex(j, 
                        outputFace.getVertexIndex(j));
                }
            }
            output.addFace(outputFace);
        }
        return output;
    }
    

    private static void activateGroups(Obj input, ObjFace face, Obj output) {
        Set<String> activatedGroupNames = 
            input.getActivatedGroupNames(face);
        if (activatedGroupNames != null) {
            output.setActiveGroupNames(activatedGroupNames);
        }
        String activatedMaterialGroupName = input.getActivatedMaterialGroupName(face);
        if (activatedMaterialGroupName != null) {
            output.setActiveMaterialGroupName(activatedMaterialGroupName);
        }
    }
    

    public static String createInfoString(Obj obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("Obj:"+"\n");
        sb.append("    mtlFileNames     : "+obj.getMtlFileNames()+"\n");
        sb.append("    numVertices      : "+obj.getNumVertices()+"\n");
        sb.append("    numTexCoords     : "+obj.getNumTexCoords()+"\n");
        sb.append("    numNormals       : "+obj.getNumNormals()+"\n");
        sb.append("    numFaces         : "+obj.getNumFaces()+"\n");
        sb.append("    numGroups        : "+obj.getNumGroups()+"\n");
        for (int i=0; i<obj.getNumGroups(); i++) {
            ObjGroup objGroup = obj.getGroup(i);
            sb.append("        Group "+i+":"+"\n");
            sb.append("            name    : "+objGroup.getName()+"\n");
            sb.append("            numFaces: "+objGroup.getNumFaces()+"\n");
        }
        sb.append("    numMaterialGroups: "+obj.getNumMaterialGroups()+"\n");
        for (int i=0; i<obj.getNumMaterialGroups(); i++) {
            ObjGroup objGroup = obj.getMaterialGroup(i);
            sb.append("        MaterialGroup "+i+":"+"\n");
            sb.append("            name    : "+objGroup.getName()+"\n");
            sb.append("            numFaces: "+objGroup.getNumFaces()+"\n");
        }
        return sb.toString();
    }



    /**
     * Private constructor to prevent instantiation.
     */
    private ObjUtils()
    {
        // Private constructor to prevent instantiation.
    }
}
