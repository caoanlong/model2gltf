package com.threedcger.lib.gltf.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    private String name;
    private Integer mesh;
    private Integer camera;
    private Integer skin;
    private List<Integer> children;
    private float[] matrix;
    private float[] rotation;
    private float[] scale;
    private float[] translation;
    private List<Float> weights;

    public void setCamera(Integer camera) {
        if (camera == null) {
            this.camera = camera;
            return ;
        }
        this.camera = camera;
    }

    /**
     * The index of the camera referenced by this node. (optional)
     *
     * @return The camera
     *
     */
    public Integer getCamera() {
        return this.camera;
    }

    /**
     * The indices of this node's children. (optional)<br>
     * Minimum number of items: 1<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0 (inclusive)
     *
     * @param children The children to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     *
     */
    public void setChildren(List<Integer> children) {
        if (children == null) {
            this.children = children;
            return ;
        }
        if (children.size()< 1) {
            throw new IllegalArgumentException("Number of children elements is < 1");
        }
        for (Integer childrenElement: children) {
            if (childrenElement< 0) {
                throw new IllegalArgumentException("childrenElement < 0");
            }
        }
        this.children = children;
    }

    /**
     * The indices of this node's children. (optional)<br>
     * Minimum number of items: 1<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: 0 (inclusive)
     *
     * @return The children
     *
     */
    public List<Integer> getChildren() {
        return this.children;
    }

    /**
     * Add the given children. The children of this instance will be replaced
     * with a list that contains all previous elements, and additionally the
     * new element.
     *
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     *
     */
    public void addChildren(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.children;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.children = newList;
    }

    /**
     * Remove the given children. The children of this instance will be
     * replaced with a list that contains all previous elements, except for
     * the removed one.<br>
     * If this new list would be empty, then it will be set to
     * <code>null</code>.
     *
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     *
     */
    public void removeChildren(Integer element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Integer> oldList = this.children;
        List<Integer> newList = new ArrayList<Integer>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.children = null;
        } else {
            this.children = newList;
        }
    }

    /**
     * The index of the skin referenced by this node. (optional)
     *
     * @param skin The skin to set
     *
     */
    public void setSkin(Integer skin) {
        if (skin == null) {
            this.skin = skin;
            return ;
        }
        this.skin = skin;
    }

    /**
     * The index of the skin referenced by this node. (optional)
     *
     * @return The skin
     *
     */
    public Integer getSkin() {
        return this.skin;
    }

    /**
     * A floating-point 4x4 transformation matrix stored in column-major
     * order. (optional)<br>
     * Default:
     * [1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0]<br>
     * Number of items: 16<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @param matrix The matrix to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     *
     */
    public void setMatrix(float[] matrix) {
        if (matrix == null) {
            this.matrix = matrix;
            return ;
        }
        if (matrix.length< 16) {
            throw new IllegalArgumentException("Number of matrix elements is < 16");
        }
        if (matrix.length > 16) {
            throw new IllegalArgumentException("Number of matrix elements is > 16");
        }
        this.matrix = matrix;
    }

    /**
     * A floating-point 4x4 transformation matrix stored in column-major
     * order. (optional)<br>
     * Default:
     * [1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0]<br>
     * Number of items: 16<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @return The matrix
     *
     */
    public float[] getMatrix() {
        return this.matrix;
    }

    /**
     * Returns the default value of the matrix<br>
     * @see #getMatrix
     *
     * @return The default matrix
     *
     */
    public float[] defaultMatrix() {
        return new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F };
    }

    /**
     * The index of the mesh in this node. (optional)
     *
     * @param mesh The mesh to set
     *
     */
    public void setMesh(Integer mesh) {
        if (mesh == null) {
            this.mesh = mesh;
            return ;
        }
        this.mesh = mesh;
    }

    /**
     * The index of the mesh in this node. (optional)
     *
     * @return The mesh
     *
     */
    public Integer getMesh() {
        return this.mesh;
    }

    /**
     * The node's unit quaternion rotation in the order (x, y, z, w), where w
     * is the scalar. (optional)<br>
     * Default: [0.0,0.0,0.0,1.0]<br>
     * Number of items: 4<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: -1.0 (inclusive)<br>
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive)
     *
     * @param rotation The rotation to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     *
     */
    public void setRotation(float[] rotation) {
        if (rotation == null) {
            this.rotation = rotation;
            return ;
        }
        if (rotation.length< 4) {
            throw new IllegalArgumentException("Number of rotation elements is < 4");
        }
        if (rotation.length > 4) {
            throw new IllegalArgumentException("Number of rotation elements is > 4");
        }
        for (float rotationElement: rotation) {
            if (rotationElement > 1.0D) {
                throw new IllegalArgumentException("rotationElement > 1.0");
            }
            if (rotationElement<-1.0D) {
                throw new IllegalArgumentException("rotationElement < -1.0");
            }
        }
        this.rotation = rotation;
    }

    /**
     * The node's unit quaternion rotation in the order (x, y, z, w), where w
     * is the scalar. (optional)<br>
     * Default: [0.0,0.0,0.0,1.0]<br>
     * Number of items: 4<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)<br>
     * &nbsp;&nbsp;Minimum: -1.0 (inclusive)<br>
     * &nbsp;&nbsp;Maximum: 1.0 (inclusive)
     *
     * @return The rotation
     *
     */
    public float[] getRotation() {
        return this.rotation;
    }

    /**
     * Returns the default value of the rotation<br>
     * @see #getRotation
     *
     * @return The default rotation
     *
     */
    public float[] defaultRotation() {
        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
    }

    /**
     * The node's non-uniform scale. (optional)<br>
     * Default: [1.0,1.0,1.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @param scale The scale to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     *
     */
    public void setScale(float[] scale) {
        if (scale == null) {
            this.scale = scale;
            return ;
        }
        if (scale.length< 3) {
            throw new IllegalArgumentException("Number of scale elements is < 3");
        }
        if (scale.length > 3) {
            throw new IllegalArgumentException("Number of scale elements is > 3");
        }
        this.scale = scale;
    }

    /**
     * The node's non-uniform scale. (optional)<br>
     * Default: [1.0,1.0,1.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @return The scale
     *
     */
    public float[] getScale() {
        return this.scale;
    }

    /**
     * Returns the default value of the scale<br>
     * @see #getScale
     *
     * @return The default scale
     *
     */
    public float[] defaultScale() {
        return new float[] { 1.0F, 1.0F, 1.0F };
    }

    /**
     * The node's translation. (optional)<br>
     * Default: [0.0,0.0,0.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @param translation The translation to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     *
     */
    public void setTranslation(float[] translation) {
        if (translation == null) {
            this.translation = translation;
            return ;
        }
        if (translation.length< 3) {
            throw new IllegalArgumentException("Number of translation elements is < 3");
        }
        if (translation.length > 3) {
            throw new IllegalArgumentException("Number of translation elements is > 3");
        }
        this.translation = translation;
    }

    /**
     * The node's translation. (optional)<br>
     * Default: [0.0,0.0,0.0]<br>
     * Number of items: 3<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @return The translation
     *
     */
    public float[] getTranslation() {
        return this.translation;
    }

    /**
     * Returns the default value of the translation<br>
     * @see #getTranslation
     *
     * @return The default translation
     *
     */
    public float[] defaultTranslation() {
        return new float[] { 0.0F, 0.0F, 0.0F };
    }

    /**
     * The weights of the instantiated Morph Target. Number of elements must
     * match number of Morph Targets of used mesh. (optional)<br>
     * Minimum number of items: 1<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @param weights The weights to set
     * @throws IllegalArgumentException If the given value does not meet
     * the given constraints
     *
     */
    public void setWeights(List<Float> weights) {
        if (weights == null) {
            this.weights = weights;
            return ;
        }
        if (weights.size()< 1) {
            throw new IllegalArgumentException("Number of weights elements is < 1");
        }
        this.weights = weights;
    }

    /**
     * The weights of the instantiated Morph Target. Number of elements must
     * match number of Morph Targets of used mesh. (optional)<br>
     * Minimum number of items: 1<br>
     * Array elements:<br>
     * &nbsp;&nbsp;The elements of this array (optional)
     *
     * @return The weights
     *
     */
    public List<Float> getWeights() {
        return this.weights;
    }

    /**
     * Add the given weights. The weights of this instance will be replaced
     * with a list that contains all previous elements, and additionally the
     * new element.
     *
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     *
     */
    public void addWeights(Float element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Float> oldList = this.weights;
        List<Float> newList = new ArrayList<Float>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.add(element);
        this.weights = newList;
    }

    /**
     * Remove the given weights. The weights of this instance will be
     * replaced with a list that contains all previous elements, except for
     * the removed one.<br>
     * If this new list would be empty, then it will be set to
     * <code>null</code>.
     *
     * @param element The element
     * @throws NullPointerException If the given element is <code>null</code>
     *
     */
    public void removeWeights(Float element) {
        if (element == null) {
            throw new NullPointerException("The element may not be null");
        }
        List<Float> oldList = this.weights;
        List<Float> newList = new ArrayList<Float>();
        if (oldList!= null) {
            newList.addAll(oldList);
        }
        newList.remove(element);
        if (newList.isEmpty()) {
            this.weights = null;
        } else {
            this.weights = newList;
        }
    }
}
