package com.threedcger.lib.obj;

import lombok.Data;

import java.util.*;

@Data
public class Obj {
    private String name;
    private List<FloatTuple> vertices;
    private List<FloatTuple> texCoords;
    private List<FloatTuple> normals;
    private List<ObjFace> faces;
    private List<ObjGroup> groups;
    private List<ObjGroup> materialGroups;
    private Map<String, ObjGroup> groupMap;
    private Map<String, ObjGroup> materialGroupMap;
    private List<String> mtlFileNames;
    private Map<ObjFace, Set<String>> startedGroupNames;
    private Map<ObjFace, String> startedMaterialGroupNames;
    private Set<String> nextActiveGroupNames = null;
    private String nextActiveMaterialGroupName = null;
    private List<ObjGroup> activeGroups = null;
    private Set<String> activeGroupNames = null;
    private ObjGroup activeMaterialGroup = null;
    private String activeMaterialGroupName = null;

    public Obj() {
        vertices = new ArrayList<FloatTuple>();
        texCoords = new ArrayList<FloatTuple>();
        normals = new ArrayList<FloatTuple>();
        faces = new ArrayList<ObjFace>();

        groups = new ArrayList<ObjGroup>();
        materialGroups = new ArrayList<ObjGroup>();

        groupMap = new LinkedHashMap<String, ObjGroup>();
        materialGroupMap = new LinkedHashMap<String, ObjGroup>();

        startedGroupNames = new HashMap<ObjFace, Set<String>>();
        startedMaterialGroupNames = new HashMap<ObjFace, String>();

        setActiveGroupNames(Arrays.asList("default"));
        getGroupInternal("default");
    }

    public int getNumVertices()
    {
        return vertices.size();
    }

    public FloatTuple getVertex(int index)
    {
        return vertices.get(index);
    }

    public int getNumTexCoords()
    {
        return texCoords.size();
    }

    public FloatTuple getTexCoord(int index)
    {
        return texCoords.get(index);
    }

    public int getNumNormals()
    {
        return normals.size();
    }

    public FloatTuple getNormal(int index)
    {
        return normals.get(index);
    }

    public int getNumFaces()
    {
        return faces.size();
    }

    public ObjFace getFace(int index)
    {
        return faces.get(index);
    }

    public Set<String> getActivatedGroupNames(ObjFace face)
    {
        return startedGroupNames.get(face);
    }

    public String getActivatedMaterialGroupName(ObjFace face)
    {
        return startedMaterialGroupNames.get(face);
    }

    public int getNumGroups()
    {
        return groups.size();
    }

    public ObjGroup getGroup(int index)
    {
        return groups.get(index);
    }

    public ObjGroup getGroup(String name)
    {
        return groupMap.get(name);
    }

    public int getNumMaterialGroups()
    {
        return materialGroups.size();
    }

    public ObjGroup getMaterialGroup(int index)
    {
        return materialGroups.get(index);
    }

    public ObjGroup getMaterialGroup(String name)
    {
        return materialGroupMap.get(name);
    }

    public List<String> getMtlFileNames()
    {
        return mtlFileNames;
    }

    public void addVertex(FloatTuple vertex) {
        Objects.requireNonNull(vertex, "The vertex is null");
        vertices.add(vertex);
    }

    public void addVertex(float x, float y, float z)
    {
        vertices.add(new FloatTuple(x, y, z));
    }

    public void addTexCoord(FloatTuple texCoord) {
        Objects.requireNonNull(texCoord, "The texCoord is null");
        texCoords.add(texCoord);
    }

    public void addTexCoord(float x)
    {
        texCoords.add(new FloatTuple(x));
    }

    public void addTexCoord(float x, float y)
    {
        texCoords.add(new FloatTuple(x, y));
    }

    public void addTexCoord(float x, float y, float z)
    {
        texCoords.add(new FloatTuple(x, y, z));
    }

    public void addNormal(FloatTuple normal) {
        Objects.requireNonNull(normal, "The normal is null");
        normals.add(normal);
    }

    public void addNormal(float x, float y, float z)
    {
        normals.add(new FloatTuple(x, y, z));
    }

    public void setActiveGroupNames(Collection<? extends String> groupNames) {
        if (groupNames == null) return;
        if (groupNames.size() == 0) {
            groupNames = Arrays.asList("default");
        } else if (groupNames.contains(null)) {
            throw new NullPointerException("The groupNames contains null");
        }
        nextActiveGroupNames = Collections.unmodifiableSet(new LinkedHashSet<String>(groupNames));
    }

    public void setActiveMaterialGroupName(String materialGroupName) {
        if (materialGroupName == null) return;
        nextActiveMaterialGroupName = materialGroupName;
    }

    public void addFace(ObjFace face) {
        if (face == null) throw new NullPointerException("The face is null");
        if (nextActiveGroupNames != null) {
            activeGroups = getGroupsInternal(nextActiveGroupNames);
            if (!nextActiveGroupNames.equals(activeGroupNames)) {
                startedGroupNames.put(face, nextActiveGroupNames);
            }
            activeGroupNames = nextActiveGroupNames;
            nextActiveGroupNames = null;
        }
        if (nextActiveMaterialGroupName != null) {
            activeMaterialGroup = getMaterialGroupInternal(nextActiveMaterialGroupName);
            if (!nextActiveMaterialGroupName.equals(activeMaterialGroupName)) {
                startedMaterialGroupNames.put(face, nextActiveMaterialGroupName);
            }
            activeMaterialGroupName = nextActiveMaterialGroupName;
            nextActiveMaterialGroupName = null;
        }
        faces.add(face);
        if (activeMaterialGroup != null) {
            activeMaterialGroup.addFace(face);
        }
        for (ObjGroup group : activeGroups) {
            group.addFace(face);
        }
    }

    public void addFace(int ... v) { addFace(v, null, null); }

    public void addFaceWithTexCoords(int... v)
    {
        addFace(v, v, null);
    }

    public void addFaceWithNormals(int... v)
    {
        addFace(v, null, v);
    }

    public void addFaceWithAll(int... v)
    {
        addFace(v, v, v);
    }

    public void addFace(int[] v, int[] vt, int[] vn) {
        Objects.requireNonNull(v, "The vertex indices are null");
        checkIndices(v, getNumVertices(), "Vertex");
        checkIndices(vt, getNumTexCoords(), "TexCoord");
        checkIndices(vn, getNumNormals(), "Normal");
        ObjFace face = new ObjFace(v, vt, vn);
        addFace(face);
    }

    public void setMtlFileNames(Collection<? extends String> mtlFileNames) {
        this.mtlFileNames = Collections.unmodifiableList(new ArrayList<String>(mtlFileNames));
    }

    private List<ObjGroup> getGroupsInternal(Collection<? extends String> groupNames) {
        List<ObjGroup> groups = new ArrayList<ObjGroup>(groupNames.size());
        for (String groupName : groupNames) {
            ObjGroup group = getGroupInternal(groupName);
            groups.add(group);
        }
        return groups;
    }

    private ObjGroup getGroupInternal(String groupName) {
        ObjGroup group = groupMap.get(groupName);
        if (group == null) {
            group = new ObjGroup(groupName);
            groupMap.put(groupName, group);
            groups.add(group);
        }
        return group;
    }

    private ObjGroup getMaterialGroupInternal(String materialGroupName) {
        ObjGroup group = materialGroupMap.get(materialGroupName);
        if (group == null) {
            group = new ObjGroup(materialGroupName);
            materialGroupMap.put(materialGroupName, group);
            materialGroups.add(group);
        }
        return group;
    }

    private static void checkIndices(int indices[], int max, String name) {
        if (indices == null) return;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] < 0) {
                throw new IllegalArgumentException(name+" index is negative: "+indices[i]);
            }
            if (indices[i] >= max) {
                throw new IllegalArgumentException(name+" index is "+indices[i]+ ", but must be smaller than "+max);
            }
        }
    }
}
