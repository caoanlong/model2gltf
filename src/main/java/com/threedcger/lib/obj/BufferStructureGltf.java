package com.threedcger.lib.obj;

import com.threedcger.lib.gltf.*;
import com.threedcger.lib.gltf.model.Accessor;
import com.threedcger.lib.gltf.model.Buffer;
import com.threedcger.lib.gltf.model.BufferView;

import java.util.ArrayList;
import java.util.List;

public class BufferStructureGltf {
    public static List<Accessor> createAccessors(BufferStructure bufferStructure) {
        List<BufferViewModel> bufferViewModels = bufferStructure.getBufferViewModels();
        List<AccessorModel> accessorModels = bufferStructure.getAccessorModels();
        List<Accessor> accessors = new ArrayList<Accessor>();
        for (AccessorModel accessorModel : accessorModels) {
            BufferViewModel bufferViewModel = accessorModel.getBufferViewModel();
            int bufferViewIndex = bufferViewModels.indexOf(bufferViewModel);
            accessors.add(createAccessor(accessorModel, bufferViewIndex));
        }
        return accessors;
    }

    private static Accessor createAccessor(AccessorModel accessorModel, int bufferViewIndex) {
        Accessor accessor = new Accessor();

        accessor.setBufferView(bufferViewIndex);

        accessor.setByteOffset(accessorModel.getByteOffset());
        accessor.setComponentType(accessorModel.getComponentType());
        accessor.setCount(accessorModel.getCount());
        accessor.setType(accessorModel.getElementType().toString());

        AccessorData accessorData = accessorModel.getAccessorData();
        accessor.setMax(AccessorDatas.computeMax(accessorData));
        accessor.setMin(AccessorDatas.computeMin(accessorData));

        return accessor;
    }

    public static List<BufferView> createBufferViews(BufferStructure bufferStructure) {
        List<BufferModel> bufferModels = bufferStructure.getBufferModels();

        List<BufferViewModel> bufferViewModels =
                bufferStructure.getBufferViewModels();
        List<BufferView> bufferViews = new ArrayList<BufferView>();
        for (BufferViewModel bufferViewModel : bufferViewModels) {
            BufferModel bufferModel = bufferViewModel.getBufferModel();
            int bufferIndex = bufferModels.indexOf(bufferModel);
            bufferViews.add(createBufferView(bufferViewModel, bufferIndex));
        }
        return bufferViews;
    }

    private static BufferView createBufferView(BufferViewModel bufferViewModel, int bufferIndex) {
        BufferView bufferView = new BufferView();

        bufferView.setBuffer(bufferIndex);
        bufferView.setByteOffset(bufferViewModel.getByteOffset());
        bufferView.setByteLength(bufferViewModel.getByteLength());
        bufferView.setByteStride(bufferViewModel.getByteStride());
        bufferView.setTarget(bufferViewModel.getTarget());

        return bufferView;
    }

    public static List<Buffer> createBuffers(BufferStructure bufferStructure) {
        List<BufferModel> bufferModels = bufferStructure.getBufferModels();
        List<Buffer> buffers = new ArrayList<Buffer>();
        for (BufferModel bufferModel : bufferModels) {
            buffers.add(createBuffer(bufferModel));
        }
        return buffers;
    }

    private static Buffer createBuffer(BufferModel bufferModel) {
        Buffer buffer = new Buffer();
        buffer.setUri(bufferModel.getUri());
        buffer.setByteLength(bufferModel.getByteLength());
        return buffer;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private BufferStructureGltf() {
        // Private constructor to prevent instantiation
    }
}
