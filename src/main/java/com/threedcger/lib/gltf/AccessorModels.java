package com.threedcger.lib.gltf;

import com.threedcger.utils.Utils;

/**
 * Utility methods related to {@link AccessorModel} instances
 */
public class AccessorModels {
    /**
     * Compute the number of bytes that the given {@link AccessorModel} data
     * has to be aligned to. 
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The alignment bytes
     */
    public static int computeAlignmentBytes(AccessorModel accessorModel)
    {
        return accessorModel.getComponentSizeInBytes();
    }
    
    /**
     * Compute the alignment for the given {@link AccessorModel} instances.
     * This is the least common multiple of the alignment of all instances.
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The alignment
     */
    public static int computeAlignmentBytes(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int alignmentBytes = 1;
        for (AccessorModel accessorModel : accessorModels)
        {
            alignmentBytes = Utils.computeLeastCommonMultiple(alignmentBytes, AccessorModels.computeAlignmentBytes(accessorModel));
        }
        return alignmentBytes;
    }
    
    /**
     * Compute the byte stride that is common for the given 
     * {@link AccessorModel} instances. This is the maximum of the
     * {@link AccessorModel#getElementSizeInBytes() element sizes}
     * of the given models.
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The common byte stride
     */
    public static int computeCommonByteStride(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int commonByteStride = 1;
        for (AccessorModel accessorModel : accessorModels)
        {
            int elementSize = accessorModel.getElementSizeInBytes();
            commonByteStride = Math.max(commonByteStride, elementSize);
        }
        return commonByteStride;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorModels()
    {
        // Private constructor to prevent instantiation
    }
    
    
}