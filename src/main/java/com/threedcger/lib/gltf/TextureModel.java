package com.threedcger.lib.gltf;

public class TextureModel {
    private String name;

    /**
     * The magnification filter constant
     */
    private final Integer magFilter;

    /**
     * The minification filter constant
     */
    private final Integer minFilter;

    /**
     * The wrapping constant for the S-direction
     */
    private final int wrapS;

    /**
     * The wrapping constant for the T-direction
     */
    private final int wrapT;

    /**
     * The {@link ImageModel}
     */
    private ImageModel imageModel;

    public TextureModel(Integer magFilter, Integer minFilter, int wrapS, int wrapT) {
        this.magFilter = magFilter;
        this.minFilter = minFilter;
        this.wrapS = wrapS;
        this.wrapT = wrapT;
    }

    /**
     * Set the {@link ImageModel}
     *
     * @param imageModel The {@link ImageModel}
     */
    public void setImageModel(ImageModel imageModel)
    {
        this.imageModel = imageModel;
    }

    public Integer getMagFilter()
    {
        return magFilter;
    }

    public Integer getMinFilter()
    {
        return minFilter;
    }

    public int getWrapS()
    {
        return wrapS;
    }

    public int getWrapT()
    {
        return wrapT;
    }

    public ImageModel getImageModel()
    {
        return imageModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
