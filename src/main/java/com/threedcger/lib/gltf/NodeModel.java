package com.threedcger.lib.gltf;

import com.threedcger.utils.MathUtils;
import com.threedcger.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of a {@link NodeModel} 
 */
public final class NodeModel {
    private String name;

    /**
     * A thread-local, temporary 16-element matrix
     */
    private static final ThreadLocal<float[]> TEMP_MATRIX_4x4_IN_LOCAL =
        ThreadLocal.withInitial(() -> new float[16]);

    /**
     * A thread-local, temporary 16-element matrix
     */
    private static final ThreadLocal<float[]> TEMP_MATRIX_4x4_IN_GLOBAL =
        ThreadLocal.withInitial(() -> new float[16]);

    /**
     * The parent of this node. This is <code>null</code> for the root node.
     */
    private NodeModel parent;

    /**
     * The children of this node
     */
    private final List<NodeModel> children;

    /**
     * The {@link MeshModel} objects that are attached to this node
     */
    private final List<MeshModel> meshModels;


    /**
     * The local transform matrix
     */
    private float matrix[];

    /**
     * The translation
     */
    private float translation[];

    /**
     * The rotation
     */
    private float rotation[];

    /**
     * The scale
     */
    private float scale[];

    /**
     * The weights
     */
    private float weights[];

    /**
     * Creates a new instance
     */
    public NodeModel()
    {
        this.children = new ArrayList<NodeModel>();
        this.meshModels = new ArrayList<MeshModel>();
    }
    
    /**
     * Add the given child to this node
     * 
     * @param child The child
     */
    public void addChild(NodeModel child) {
        Objects.requireNonNull(child, "The child may not be null");
        children.add(child);
        child.parent = this;
    }
    
    /**
     * Add the given {@link MeshModel} to this node
     * 
     * @param meshModel The {@link MeshModel}
     */
    public void addMeshModel(MeshModel meshModel) {
        Objects.requireNonNull(meshModel, "The meshModel may not be null");
        meshModels.add(meshModel);
    }

    public NodeModel getParent()
    {
        return parent;
    }

    public List<NodeModel> getChildren()
    {
        return Collections.unmodifiableList(children);
    }

    public List<MeshModel> getMeshModels()
    {
        return Collections.unmodifiableList(meshModels);
    }

    public void setMatrix(float[] matrix)
    {
        this.matrix = check(matrix, 16);
    }

    public float[] getMatrix()
    {
        return matrix;
    }

    public void setTranslation(float[] translation)
    {
        this.translation = check(translation, 3);
    }

    public float[] getTranslation()
    {
        return translation;
    }

    public void setRotation(float[] rotation)
    {
        this.rotation = check(rotation, 4);
    }

    public float[] getRotation()
    {
        return rotation;
    }

    public void setScale(float[] scale)
    {
        this.scale = check(scale, 3);
    }

    public float[] getScale()
    {
        return scale;
    }

    public void setWeights(float[] weights)
    {
        this.weights = weights;
    }

    public float[] getWeights()
    {
        return weights;
    }

    public float[] computeLocalTransform(float result[]) {
        return computeLocalTransform(this, result);
    }

    public float[] computeGlobalTransform(float result[]) {
        return computeGlobalTransform(this, result);
    }

    public Supplier<float[]> createGlobalTransformSupplier() {
//        return Suppliers.createTransformSupplier(this, NodeModel::computeGlobalTransform);
        return Suppliers.createTransformSupplier(this, (c, t) -> NodeModel.computeGlobalTransform(c, t));
    }

    public Supplier<float[]> createLocalTransformSupplier() {
//        return Suppliers.createTransformSupplier(this, NodeModel::computeLocalTransform);
        return Suppliers.createTransformSupplier(this, (c, t) -> NodeModel.computeLocalTransform(c, t));
    }

    /**
     * Compute the local transform of the given node. The transform
     * is either taken from the {@link #getMatrix()} (if it is not
     * <code>null</code>), or computed from the {@link #getTranslation()}, 
     * {@link #getRotation()} and {@link #getScale()}, if they
     * are not <code>null</code>, respectively.<br>
     * <br>
     * The result will be written to the given array, as a 4x4 matrix in 
     * column major order. If the given array is <code>null</code> or does
     * not have a length of 16, then a new array with length 16 will be 
     * created and returned. 
     * 
     * @param nodeModel The node. May not be <code>null</code>.
     * @param result The result array
     * @return The result array
     */
    public static float[] computeLocalTransform(NodeModel nodeModel, float result[]) {
        float localResult[] = Utils.validate(result, 16);
        if (nodeModel.getMatrix() != null) {
            float m[] = nodeModel.getMatrix();
            System.arraycopy(m, 0, localResult, 0, m.length);
            return localResult;
        }
        
        MathUtils.setIdentity4x4(localResult);
        if (nodeModel.getTranslation() != null) {
            float t[] = nodeModel.getTranslation();
            localResult[12] = t[0]; 
            localResult[13] = t[1]; 
            localResult[14] = t[2]; 
        }
        if (nodeModel.getRotation() != null) {
            float q[] = nodeModel.getRotation();
            float m[] = TEMP_MATRIX_4x4_IN_LOCAL.get();
            MathUtils.quaternionToMatrix4x4(q, m);
            MathUtils.mul4x4(localResult, m, localResult);
        }
        if (nodeModel.getScale() != null) {
            float s[] = nodeModel.getScale();
            float m[] = TEMP_MATRIX_4x4_IN_LOCAL.get();
            MathUtils.setIdentity4x4(m);
            m[ 0] = s[0];
            m[ 5] = s[1];
            m[10] = s[2];
            m[15] = 1.0f;
            MathUtils.mul4x4(localResult, m, localResult);
        }
        return localResult;
    }
    
    /**
     * Compute the global transform for the given {@link NodeModel},
     * and store it in the given result. If the given result is 
     * <code>null</code> or does not have a length of 16, then 
     * a new array will be created and returned.
     * 
     * @param nodeModel The {@link NodeModel}
     * @param result The result
     * @return The result
     */
    private static float[] computeGlobalTransform(NodeModel nodeModel, float result[]) {
        float localResult[] = Utils.validate(result, 16);
        float tempLocalTransform[] = TEMP_MATRIX_4x4_IN_GLOBAL.get();
        NodeModel currentNode = nodeModel;
        MathUtils.setIdentity4x4(localResult);
        while (currentNode != null) {
            currentNode.computeLocalTransform(tempLocalTransform);
            MathUtils.mul4x4(tempLocalTransform, localResult, localResult);
            currentNode = currentNode.getParent();
        }
        return localResult;
    }
    
    /**
     * Check whether the given array has the expected length, and return
     * the given array. If the given source array is <code>null</code>, then 
     * <code>null</code> will be returned. If the given source array does not 
     * have the expected length, then an <code>IllegalArgumentException</code>
     * will be thrown. 
     * 
     * @param array The array
     * @param expectedLength The expected length
     * @return The array
     * @throws IllegalArgumentException If the given array does not have
     * the expected length
     */
    private static float[] check(float array[], int expectedLength) {
        if (array == null) {
            return null;
        }
        if (array.length != expectedLength) {
            throw new IllegalArgumentException("Expected " + expectedLength + " array elements, but found " + array.length);
        }
        return array;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
