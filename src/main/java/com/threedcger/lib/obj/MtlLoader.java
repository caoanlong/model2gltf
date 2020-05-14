package com.threedcger.lib.obj;

import com.threedcger.lib.obj.model.*;
import com.threedcger.utils.CMath;

import java.io.*;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MtlLoader {
    private Options options;
    private Material material;

    private String mtlDirectory;
    private List<Material> materials = new ArrayList<>();

    private String overridingSpecularTexture;
    private String overridingSpecularShininessTexture;
    private String overridingAmbientTexture;
    private String overridingNormalTexture;
    private String overridingDiffuseTexture;
    private String overridingEmissiveTexture;
    private String overridingAlphaTexture;

    private TextureOptions decodeOptions = new TextureOptions();
    private TextureOptions diffuseTextureOptions = new TextureOptions();

    private TextureOptions ambientTextureOptions;
    private TextureOptions specularTextureOptions;
    private TextureOptions specularShinessTextureOptions;
    private TextureOptions emissiveTextureOptions = new TextureOptions();
    private TextureOptions normalTextureOptions = new TextureOptions();
    private TextureOptions alphaTextureOptions = new TextureOptions();

    private IntBuffer scratchResizeChannel;


    public MtlLoader(Options options) {
        this.options = options;
        OverridingTextures overridingTextures = options.getOverridingTextures();
        this.overridingSpecularTexture = overridingTextures.getMetallicRoughnessOcclusionTexture() != null ? overridingTextures.getMetallicRoughnessOcclusionTexture() : overridingTextures.getSpecularGlossinessTexture();
        this.overridingSpecularShininessTexture = overridingTextures.getMetallicRoughnessOcclusionTexture() != null ? overridingTextures.getMetallicRoughnessOcclusionTexture() : overridingTextures.getSpecularGlossinessTexture();
        this.overridingAmbientTexture = overridingTextures.getMetallicRoughnessOcclusionTexture() != null ? overridingTextures.getMetallicRoughnessOcclusionTexture() : overridingTextures.getOcclusionTexture();
        this.overridingNormalTexture = overridingTextures.getNormalTexture();
        this.overridingDiffuseTexture = overridingTextures.getBaseColorTexture();
        this.overridingEmissiveTexture = overridingTextures.getEmissiveTexture();
        this.overridingAlphaTexture = overridingTextures.getAlphaTexture();

        this.diffuseTextureOptions.setCheckTransparency(options.getCheckTransparency());

        this.ambientTextureOptions = overridingAmbientTexture != null ? null : (options.getPackOcclusion() ? decodeOptions : null);
        this.specularTextureOptions = overridingSpecularTexture != null ? null : decodeOptions;
        this.specularShinessTextureOptions = overridingSpecularShininessTexture != null ? null : decodeOptions;
    }
    public List<Mtl> load(String mtlPath) throws IOException {
        File file = new File(mtlPath);
        this.mtlDirectory = file.getParent();
        readLines(mtlPath);
        return convertMaterials(materials, options);
    }
    private void readLines(String path) throws IOException {
        File file = new File(path);
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            this.parseLine(line);
        }
        bufferedReader.close();
        for (Material material: materials) {
            loadMaterialTextures(material);
        }
    }
    private void parseLine(String line) {
        line = line.trim();
        if (line.matches("(?i)^newmtl\\s+[\\s\\S]*")) {
            String name = line.substring(7).trim();
            createMaterial(name);
        } else if (line.matches("(?i)^ka\\s+[\\s\\S]*")) {
            String[] split = line.substring(3).trim().split(" ");
            List<Double> ambientColors = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                ambientColors.add(Double.valueOf(split[i]));
            }
            ambientColors.add(1.0);
            material.setAmbientColor(ambientColors);
        } else if (line.matches("(?i)^Ke\\s+[\\s\\S]*")) {
            String[] split = line.substring(3).trim().split(" ");
            List<Double> emissiveColors = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                emissiveColors.add(Double.valueOf(split[i]));
            }
            emissiveColors.add(1.0);
            material.setEmissiveColor(emissiveColors);
        } else if (line.matches("(?i)^Kd\\s+[\\s\\S]*")) {
            String[] split = line.substring(3).trim().split(" ");
            List<Double> diffuseColors = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                diffuseColors.add(Double.valueOf(split[i]));
            }
            diffuseColors.add(1.0);
            material.setDiffuseColor(diffuseColors);
        } else if (line.matches("(?i)^Ks\\s+[\\s\\S]*")) {
            String[] split = line.substring(3).trim().split(" ");
            List<Double> specularColors = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                specularColors.add(Double.valueOf(split[i]));
            }
            specularColors.add(1.0);
            material.setSpecularColor(specularColors);
        } else if (line.matches("(?i)^Ns\\s+[\\s\\S]*")) {
            Double specularShininess = Double.valueOf(line.substring(3).trim());
            material.setSpecularShininess(specularShininess);
        } else if (line.matches("(?i)^d\\s+[\\s\\S]*")) {
            Double alpha = Double.valueOf(line.substring(3).trim());
            material.setAlpha(correctAlpha(alpha));
        } else if (line.matches("(?i)^Tr\\s+[\\s\\S]*")) {
            Double alpha = Double.valueOf(line.substring(3).trim());
            material.setAlpha(correctAlpha(1.0 - alpha));
        } else if (line.matches("(?i)^map_Ka\\s+[\\s\\S]*")) {
            if (overridingAmbientTexture == null) {
                String ambientTexture = normalizeTexturePath(line.substring(7).trim(), mtlDirectory);
                material.setAmbientTexturePath(ambientTexture);
            }
        } else if (line.matches("(?i)^map_Ke\\s+[\\s\\S]*")) {
            if (overridingEmissiveTexture == null) {
                String emissiveTexture = normalizeTexturePath(line.substring(7).trim(), mtlDirectory);
                material.setEmissiveTexturePath(emissiveTexture);
            }
        } else if (line.matches("(?i)^map_Kd\\s+[\\s\\S]*")) {
            if (overridingDiffuseTexture == null) {
                String diffuseTexture = normalizeTexturePath(line.substring(7).trim(), mtlDirectory);
                material.setDiffuseTexturePath(diffuseTexture);
            }
        } else if (line.matches("(?i)^map_Ks\\s+[\\s\\S]*")) {
            if (overridingSpecularTexture == null) {
                String specularTexture = normalizeTexturePath(line.substring(7).trim(), mtlDirectory);
                material.setSpecularTexturePath(specularTexture);
            }
        } else if (line.matches("(?i)^map_Ns\\s+[\\s\\S]*")) {
            if (overridingSpecularShininessTexture == null) {
                String specularShininessTexture = normalizeTexturePath(line.substring(7).trim(), mtlDirectory);
                material.setSpecularShininessTexturePath(specularShininessTexture);
            }
        } else if (line.matches("(?i)^map_Bump\\s+[\\s\\S]*")) {
            if (overridingNormalTexture == null) {
                String normalTexture = normalizeTexturePath(line.substring(9).trim(), mtlDirectory);
                material.setNormalTexturePath(normalTexture);
            }
        } else if (line.matches("(?i)^map_d\\s+[\\s\\S]*")) {
            if (overridingAlphaTexture == null) {
                String alphaTexture = normalizeTexturePath(line.substring(6).trim(), mtlDirectory);
                material.setAlphaTexturePath(alphaTexture);
            }
        }
    }
    private String normalizeTexturePath(String texturePath, String mtlDirectory) {
        texturePath = texturePath.replaceAll("\\\\", "/");
        File file = new File(texturePath);
        return mtlDirectory + "/" + file.getName();
    }
    private Double correctAlpha(Double alpha) {
        // An alpha of 0.0 usually implies a problem in the export, change to 1.0 instead
        return alpha == 0.0 ? 1.0 : alpha;
    }
    private void createMaterial(String name) {
        material = new Material();
        material.setName(name);
        material.setSpecularShininess(options.getMetallicRoughness() ? 1.0 : 0.0);
        material.setSpecularTexturePath(overridingSpecularTexture);
        material.setSpecularShininessTexturePath(overridingSpecularShininessTexture);
        material.setDiffuseTexturePath(overridingDiffuseTexture);
        material.setAmbientTexturePath(overridingAmbientTexture);
        material.setNormalTexturePath(overridingNormalTexture);
        material.setEmissiveTexturePath(overridingEmissiveTexture);
        material.setAlphaTexturePath(overridingAlphaTexture);
        materials.add(material);
    }
    private void loadMaterialTextures(Material material) {
        // If an alpha texture is present the diffuse texture needs to be decoded so they can be packed together
        TextureOptions diffuseAlphaTextureOptions = material.getAlphaTexturePath() != null ? alphaTextureOptions : diffuseTextureOptions;
        if (material.getDiffuseTexturePath() != null && material.getDiffuseTexturePath().equals(material.getAmbientTexturePath())) {
            // OBJ models are often exported with the same texture in the diffuse and ambient slots but this is typically not desirable, particularly
            // when saving with PBR materials where the ambient texture is treated as the occlusion texture.
            material.setAmbientTexturePath(null);
        }
        List<String> textureNames = Arrays.asList("diffuseTexture", "ambientTexture", "emissiveTexture", "specularTexture", "specularShininessTexture", "normalTexture", "alphaTexture");
        List<TextureOptions> textureOptions = Arrays.asList(diffuseAlphaTextureOptions, ambientTextureOptions, emissiveTextureOptions, specularTextureOptions, specularShinessTextureOptions, normalTextureOptions, alphaTextureOptions);
        HashMap<String, TextureOptions> sharedOptions = new HashMap<>();
        for (int i = 0; i < textureNames.size(); i++) {
            String texturePath = material.getTexturePath(textureNames.get(i));
            TextureOptions originalOptions = textureOptions.get(i);
            if (texturePath != null && originalOptions != null) {
                if (sharedOptions.get(texturePath) == null) {
                    TextureOptions cloneOriginalOptions = new TextureOptions();
                    cloneOriginalOptions.setCheckTransparency(originalOptions.getCheckTransparency());
                    cloneOriginalOptions.setDecode(originalOptions.getDecode());
                    cloneOriginalOptions.setKeepSource(originalOptions.getKeepSource());
                    sharedOptions.put(texturePath, cloneOriginalOptions);
                }
                TextureOptions opt = sharedOptions.get(texturePath);
                opt.setCheckTransparency(opt.getCheckTransparency() || originalOptions.getCheckTransparency());
                opt.setDecode(opt.getDecode() || originalOptions.getDecode());
                opt.setKeepSource(opt.getKeepSource() || !originalOptions.getDecode() || !originalOptions.getCheckTransparency());
            }
        }
        for (int j = 0; j < textureNames.size(); j++) {
            String texturePath = material.getTexturePath(textureNames.get(j));
            if (texturePath != null) {
                try {
                    loadMaterialTexture(material, textureNames.get(j), sharedOptions.get(texturePath), mtlDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void loadMaterialTexture(Material material, String name, TextureOptions textureOptions, String mtlDirectory) throws IOException {
        String texturePath = material.getTexturePath(name);
        if (texturePath == null) return;
        String shallowPath = normalizeTexturePath(texturePath, mtlDirectory);
        TextureLoader textureLoader = new TextureLoader(textureOptions);
        Texture texture = textureLoader.load(shallowPath);
        material.setTexture(name, texture);
    }
    private List<Mtl> convertMaterials(List<Material> materials, Options options) {
        List<Mtl> mtls = new ArrayList<>();
        for (Material material: materials) {
            Mtl mtl = convertMaterial(material, options);
            mtls.add(mtl);
        }
        return mtls;
    }
    private Mtl convertMaterial(Material material, Options options) {
        if (options.getSpecularGlossiness()) {
            return createSpecularGlossinessMaterial(material, options);
        } else if (options.getMetallicRoughness()) {
            return createMetallicRoughnessMaterial(material, options);
        }
        // No material type specified, convert the material to metallic roughness
        convertTraditionalToMetallicRoughness(material);
        return createMetallicRoughnessMaterial(material, options);
    }
    private Mtl createMetallicRoughnessMaterial(Material material, Options options) {
        Texture emissiveTexture = material.getEmissiveTexture();
        Texture normalTexture = material.getNormalTexture();
        Texture occlusionTexture = material.getAmbientTexture();
        Texture baseColorTexture = material.getDiffuseTexture();
        Texture alphaTexture = material.getAlphaTexture();
        Texture metallicTexture = material.getSpecularTexture();
        Texture roughnessTexture = material.getSpecularShininessTexture();
        Texture metallicRoughnessTexture = createMetallicRoughnessTexture(metallicTexture, roughnessTexture, occlusionTexture, options);
        Texture diffuseAlphaTexture = createDiffuseAlphaTexture(baseColorTexture, alphaTexture, options);

        if (options.getPackOcclusion()) occlusionTexture = metallicRoughnessTexture;

        List<Double> emissiveFactor = material.getEmissiveColor().subList(0, 3);
        List<Double> baseColorFactor = material.getDiffuseColor();
        Double metallicFactor = material.getSpecularColor().get(0);
        Double roughnessFactor = material.getSpecularShininess();

        if (emissiveTexture != null) emissiveFactor = Arrays.asList(1.0, 1.0, 1.0);
        if (baseColorTexture != null) baseColorFactor = Arrays.asList(1.0, 1.0, 1.0, 1.0);
        if (metallicTexture != null) metallicFactor = 1.0;
        if (roughnessTexture != null) roughnessFactor = 1.0;

        Boolean transparent = false;
        if (alphaTexture != null) {
            transparent = true;
        } else {
            baseColorFactor.set(3, material.getAlpha());
            transparent = material.getAlpha() < 1.0;
        }

        if (baseColorTexture != null) transparent = transparent || baseColorTexture.getTransparent();

        Boolean doubleSided = transparent;
        String alphaMode = transparent ? "BLEND" : "OPAQUE";

        PbrMetallicRoughness pbrMetallicRoughness = new PbrMetallicRoughness();
        pbrMetallicRoughness.setBaseColorTexture(diffuseAlphaTexture);
        pbrMetallicRoughness.setMetallicRoughnessTexture(metallicRoughnessTexture);
        pbrMetallicRoughness.setBaseColorFactor(baseColorFactor);
        pbrMetallicRoughness.setMetallicFactor(metallicFactor);
        pbrMetallicRoughness.setRoughnessFactor(roughnessFactor);

        Mtl mtl = new Mtl();
        mtl.setName(material.getName());
        mtl.setPbrMetallicRoughness(pbrMetallicRoughness);
        mtl.setEmissiveTexture(emissiveTexture);
        mtl.setNormalTexture(normalTexture);
        mtl.setOcclusionTexture(occlusionTexture);
        mtl.setEmissiveFactor(emissiveFactor);
        mtl.setAlphaMode(alphaMode);
        mtl.setDoubleSided(doubleSided);
        return mtl;
    }
    private Mtl createSpecularGlossinessMaterial(Material material, Options options) {
        Texture emissiveTexture = material.getEmissiveTexture();
        Texture normalTexture = material.getNormalTexture();
        Texture occlusionTexture = material.getAmbientTexture();
        Texture diffuseTexture = material.getDiffuseTexture();
        Texture alphaTexture = material.getAlphaTexture();
        Texture specularTexture = material.getSpecularTexture();
        Texture glossinessTexture = material.getSpecularShininessTexture();
        Texture specularGlossinessTexture = createSpecularGlossinessTexture(specularTexture, glossinessTexture, options);
        Texture diffuseAlphaTexture = createDiffuseAlphaTexture(diffuseTexture, alphaTexture, options);

        List<Double> emissiveFactor = material.getEmissiveColor().subList(0, 3);
        List<Double> diffuseFactor = material.getDiffuseColor();
        List<Double> specularFactor = material.getSpecularColor().subList(0, 3);
        Double glossinessFactor = material.getSpecularShininess();

        if (emissiveTexture != null) emissiveFactor = Arrays.asList(1.0, 1.0, 1.0);
        if (diffuseTexture != null) diffuseFactor = Arrays.asList(1.0, 1.0, 1.0, 1.0);
        if (specularTexture != null) specularFactor = Arrays.asList(1.0, 1.0, 1.0);
        if (glossinessTexture != null) glossinessFactor = 1.0;

        Boolean transparent = false;
        if (alphaTexture != null) {
            transparent = true;
        } else {
            diffuseFactor.set(3, material.getAlpha());
            transparent = material.getAlpha() < 1.0;
        }

        if (diffuseTexture != null) transparent = transparent || diffuseTexture.getTransparent();

        Boolean doubleSided = transparent;
        String alphaMode = transparent ? "BLEND" : "OPAQUE";

        KHR_materials_pbrSpecularGlossiness khr_materials_pbrSpecularGlossiness = new KHR_materials_pbrSpecularGlossiness();
        khr_materials_pbrSpecularGlossiness.setDiffuseTexture(diffuseAlphaTexture);
        khr_materials_pbrSpecularGlossiness.setSpecularGlossinessTexture(specularGlossinessTexture);
        khr_materials_pbrSpecularGlossiness.setDiffuseFactor(diffuseFactor);
        khr_materials_pbrSpecularGlossiness.setSpecularFactor(specularFactor);
        khr_materials_pbrSpecularGlossiness.setGlossinessFactor(glossinessFactor);

        Extensions extensions = new Extensions();
        extensions.setKhr_materials_pbrSpecularGlossiness(khr_materials_pbrSpecularGlossiness);

        Mtl mtl = new Mtl();
        mtl.setName(material.getName());
        mtl.setExtensions(extensions);
        mtl.setEmissiveTexture(emissiveTexture);
        mtl.setNormalTexture(normalTexture);
        mtl.setOcclusionTexture(occlusionTexture);
        mtl.setEmissiveFactor(emissiveFactor);
        mtl.setAlphaMode(alphaMode);
        mtl.setDoubleSided(doubleSided);
        return mtl;
    }
    private Texture createMetallicRoughnessTexture(Texture metallicTexture, Texture roughnessTexture, Texture occlusionTexture, Options options) {
        if (options.getOverridingTextures().getMetallicRoughnessOcclusionTexture() != null) return metallicTexture;

        Boolean packMetallic = metallicTexture != null;
        Boolean packRoughness = roughnessTexture != null;
        Boolean packOcclusion = occlusionTexture != null && options.getPackOcclusion();

        if (!packMetallic && !packRoughness) return null;

        if (packMetallic && metallicTexture.getPixels() == null) {
            System.out.println("Could not get decoded texture data for " + metallicTexture.getPath() + ". The material will be created without a metallicRoughness texture.");
            return null;
        }
        if (packRoughness && roughnessTexture.getPixels() == null) {
            System.out.println("Could not get decoded texture data for " + roughnessTexture.getPath() + ". The material will be created without a metallicRoughness texture.");
            return null;
        }
        if (packOcclusion && occlusionTexture.getPixels() == null) {
            System.out.println("Could not get decoded texture data for " + occlusionTexture.getPath() + ". The occlusion texture will not be packed in the metallicRoughness texture.");
            return null;
        }
        List<Texture> packedTextures = new ArrayList<>();
        packedTextures.add(metallicTexture);
        packedTextures.add(roughnessTexture);
        packedTextures.add(occlusionTexture);
        packedTextures = packedTextures.stream().filter(texture -> texture != null && texture.getPixels() != null).collect(Collectors.toList());

        Integer[] dimensions = getMinimumDimensions(packedTextures, options);
        Integer width = dimensions[0];
        Integer height = dimensions[1];
        Integer pixelsLength = width * height;
        int[] pixelsArray = new int[pixelsLength * 4];
        Arrays.fill(pixelsArray, 0xFF);
        int[] scratchChannelArray = new int[pixelsLength];
        IntBuffer pixels = IntBuffer.wrap(pixelsArray);
        IntBuffer scratchChannel = IntBuffer.wrap(scratchChannelArray);

        if (packMetallic) {
            // Write into the B channel
            IntBuffer metallicChannel = getTextureChannel(metallicTexture, 0, width, height, scratchChannel);
            writeChannel(pixels, metallicChannel, 2);
        }
        if (packRoughness) {
            // Write into the G channel
            IntBuffer roughnessChannel = getTextureChannel(roughnessTexture, 0, width, height, scratchChannel);
            writeChannel(pixels, roughnessChannel, 1);
        }
        if (packOcclusion) {
            // Write into the R channel
            IntBuffer occlusionChannel = getTextureChannel(occlusionTexture, 0, width, height, scratchChannel);
            writeChannel(pixels, occlusionChannel, 0);
        }

        Integer length = packedTextures.size();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            names.add(packedTextures.get(i).getName());
        }
        String name = String.join("-", names);

        Texture texture = new Texture();
        texture.setName(name);
        texture.setExtension("png");
        texture.setPixels(pixels);
        texture.setWidth(width);
        texture.setHeight(height);
        return texture;
    }
    private Texture createSpecularGlossinessTexture(Texture specularTexture, Texture glossinessTexture, Options options) {
        if (options.getOverridingTextures().getSpecularGlossinessTexture() != null) {
            return specularTexture;
        }
        Boolean packSpecular = specularTexture != null;
        Boolean packGlossiness = glossinessTexture != null;
        if (!packSpecular && !packGlossiness) return null;
        if (packSpecular && specularTexture.getPixels() == null) {
            System.out.println("Could not get decoded texture data for " + specularTexture.getPath() + ". The material will be created without a specularGlossiness texture.");
            return null;
        }
        if (packGlossiness && glossinessTexture.getPixels() == null) {
            System.out.println("Could not get decoded texture data for " + glossinessTexture.getPath() + ". The material will be created without a specularGlossiness texture.");
            return null;
        }
        List<Texture> packedTextures = new ArrayList<>();
        packedTextures.add(specularTexture);
        packedTextures.add(glossinessTexture);
        packedTextures = packedTextures.stream().filter(texture -> texture != null && texture.getPixels() != null).collect(Collectors.toList());

        Integer[] dimensions = getMinimumDimensions(packedTextures, options);
        Integer width = dimensions[0];
        Integer height = dimensions[1];
        Integer pixelsLength = width * height;
        int[] pixelsArray = new int[pixelsLength * 4];
        Arrays.fill(pixelsArray, 0xFF);
        int[] scratchChannelArray = new int[pixelsLength];
        IntBuffer pixels = IntBuffer.wrap(pixelsArray);
        IntBuffer scratchChannel = IntBuffer.wrap(scratchChannelArray);

        if (packSpecular) {
            // Write into the R, G, B channels
            IntBuffer redChannel = getTextureChannel(specularTexture, 0, width, height, scratchChannel);
            writeChannel(pixels, redChannel, 0);
            IntBuffer greenChannel = getTextureChannel(specularTexture, 1, width, height, scratchChannel);
            writeChannel(pixels, greenChannel, 1);
            IntBuffer blueChannel = getTextureChannel(specularTexture, 2, width, height, scratchChannel);
            writeChannel(pixels, blueChannel, 2);
        }

        if (packGlossiness) {
            // Write into the A channel
            IntBuffer glossinessChannel = getTextureChannel(glossinessTexture, 0, width, height, scratchChannel);
            writeChannel(pixels, glossinessChannel, 3);
        }

        Integer length = packedTextures.size();
        List<String> names = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            names.add(packedTextures.get(i).getName());
        }
        String name = String.join("-", names);

        Texture texture = new Texture();
        texture.setName(name);
        texture.setExtension("png");
        texture.setPixels(pixels);
        texture.setWidth(width);
        texture.setHeight(height);
        return texture;

    }
    private Texture createDiffuseAlphaTexture(Texture diffuseTexture, Texture alphaTexture, Options options) {
        if (diffuseTexture == null) return null;
        if (alphaTexture == null) return diffuseTexture;

        if (diffuseTexture == alphaTexture) return diffuseTexture;
        if (diffuseTexture.getPixels() == null || alphaTexture.getPixels() == null) {
            System.out.println("Could not get decoded texture data for " + diffuseTexture.getPath() + " or " + alphaTexture.getPath() + ". The material will be created without an alpha texture.");
            return diffuseTexture;
        }

        List<Texture> packedTextures = new ArrayList<>();
        packedTextures.add(diffuseTexture);
        packedTextures.add(alphaTexture);
        Integer[] dimensions = getMinimumDimensions(packedTextures, options);
        Integer width = dimensions[0];
        Integer height = dimensions[1];
        Integer pixelsLength = width * height;
        int[] pixelsArray = new int[pixelsLength * 4];
        Arrays.fill(pixelsArray, 0xFF);
        int[] scratchChannelArray = new int[pixelsLength];
        IntBuffer pixels = IntBuffer.wrap(pixelsArray);
        IntBuffer scratchChannel = IntBuffer.wrap(scratchChannelArray);

        // Write into the R, G, B channels
        IntBuffer redChannel = getTextureChannel(diffuseTexture, 0, width, height, scratchChannel);
        writeChannel(pixels, redChannel, 0);
        IntBuffer greenChannel = getTextureChannel(diffuseTexture, 1, width, height, scratchChannel);
        writeChannel(pixels, greenChannel, 1);
        IntBuffer blueChannel = getTextureChannel(diffuseTexture, 2, width, height, scratchChannel);
        writeChannel(pixels, blueChannel, 2);

        // First try reading the alpha component from the alpha channel, but if it is a single color read from the red channel instead.
        IntBuffer alphaChannel = getTextureChannel(alphaTexture, 3, width, height, scratchChannel);
        if (isChannelSingleColor(alphaChannel)) {
            alphaChannel = getTextureChannel(alphaTexture, 0, width, height, scratchChannel);
        }
        writeChannel(pixels, alphaChannel, 3);

        Texture texture = new Texture();
        texture.setName(diffuseTexture.getName());
        texture.setExtension("png");
        texture.setPixels(pixels);
        texture.setWidth(width);
        texture.setHeight(height);
        texture.setTransparent(true);
        return texture;
    }
    private Integer[] getMinimumDimensions(List<Texture> textures, Options options) {
        Double width = Double.POSITIVE_INFINITY;
        Double height = Double.POSITIVE_INFINITY;

        for (int i = 0; i < textures.size(); i++) {
            width = Math.min(textures.get(i).getWidth(), width);
            height = Math.min(textures.get(i).getHeight(), height);
        }
        for (int i = 0; i < textures.size(); i++) {
            if (textures.get(i).getWidth().doubleValue() != width || textures.get(i).getHeight().doubleValue() != height) {
                System.out.println("Texture " + textures.get(i).getPath() + " will be scaled from " + textures.get(i).getWidth() + "x" + textures.get(i).getHeight() + " to " + width + "x" + height + ".");
            }
        }
        return new Integer[]{width.intValue(), height.intValue()};
    }
    private IntBuffer getTextureChannel(Texture texture, Integer index, Integer targetWidth, Integer targetHeight, IntBuffer targetChannel) {
        IntBuffer pixels = texture.getPixels();
        Integer sourceWidth = texture.getWidth();
        Integer sourceHeight = texture.getHeight();
        Integer sourcePixelsLength = sourceWidth * sourceHeight;
        Integer targetPixelsLength = targetWidth * targetHeight;

        // Allocate the scratchResizeChannel on demand if the texture needs to be resized
        IntBuffer sourceChannel = targetChannel;
        if (sourcePixelsLength > targetPixelsLength) {
            if (scratchResizeChannel == null || sourcePixelsLength > scratchResizeChannel.capacity()) {
                scratchResizeChannel = IntBuffer.allocate(sourcePixelsLength);
            }
            sourceChannel = scratchResizeChannel;
        }
        for (int i = 0; i < sourcePixelsLength; i++) {
            Integer value = pixels.get(i * 4 + index);
            sourceChannel.put(i, value);
            // TODO
            // 此处put进去的应该是byte，但是pixels中取的是int
        }

        if (sourcePixelsLength > targetPixelsLength) {
            resizeChannel(sourceChannel, sourceWidth, sourceHeight, targetChannel, targetWidth, targetHeight);
        }
        return targetChannel;
    }
    private IntBuffer resizeChannel(IntBuffer sourcePixels, Integer sourceWidth, Integer sourceHeight, IntBuffer targetPixels, Integer targetWidth, Integer targetHeight) {
        // Nearest neighbor sampling
        Float widthRatio = Float.valueOf(sourceWidth / targetWidth);
        Float heightRatio = Float.valueOf(sourceHeight / targetHeight);
        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                Integer targetIndex = y * targetWidth + x;
                Integer sourceY = Math.round(y * heightRatio);
                Integer sourceX = Math.round(x * widthRatio);
                Integer sourceIndex = sourceY * sourceWidth + sourceX;
                int sourceValue = sourcePixels.get(sourceIndex);
                targetPixels.put(targetIndex, sourceValue);
            }
        }
        return targetPixels;
    }
    private void writeChannel(IntBuffer pixels, IntBuffer channel, Integer index) {
        int pixelsLength = pixels.capacity() / 4;
        for (int i = 0; i < pixelsLength; i++) {
            int value = channel.get(i);
            pixels.put(i * 4 + index, value);
        }
    }
    private Boolean isChannelSingleColor(IntBuffer buffer) {
        int first = buffer.get(0);
        int length = buffer.capacity();
        for (int i = 1; i < length; i++) {
            if (buffer.get(i) != first) {
                return false;
            }
        }
        return true;
    }
    private void convertTraditionalToMetallicRoughness(Material material) {
        // Translate the blinn-phong model to the pbr metallic-roughness model
        // Roughness factor is a combination of specular intensity and shininess
        // Metallic factor is 0.0
        // Textures are not converted for now
        Double specularIntensity = luminance(material.getSpecularColor());

        // Transform from 0-1000 range to 0-1 range. Then invert.
        Double roughnessFactor = material.getSpecularShininess();
        roughnessFactor = roughnessFactor / 1000.0;
        roughnessFactor = 1.0 - roughnessFactor;
        roughnessFactor = CMath.clamp(roughnessFactor, 0.0, 1.0);

        // Low specular intensity values should produce a rough material even if shininess is high.
        if (specularIntensity < 0.1) {
            roughnessFactor *= (1.0 - specularIntensity);
        }

        Double metallicFactor = 0.0;
        material.setSpecularColor(Arrays.asList(metallicFactor, metallicFactor, metallicFactor, 1.0));
        material.setSpecularShininess(roughnessFactor);
    }
    private Double luminance(List<Double> color) {
        return color.get(0) * 0.2125 + color.get(1) * 0.7154 + color.get(2) * 0.0721;
    }
}
