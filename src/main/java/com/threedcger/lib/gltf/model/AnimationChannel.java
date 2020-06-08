/*
 * glTF JSON model
 * 
 * Do not modify this class. It is automatically generated
 * with JsonModelGen (https://github.com/javagl/JsonModelGen)
 * Copyright (c) 2016 Marco Hutter - http://www.javagl.de
 */

package com.threedcger.lib.gltf.model;


import lombok.Data;

import java.util.Map;

/**
 * Targets an animation's sampler at a node's property.
 *
 * Auto-generated for animation.channel.schema.json
 *
 */
@Data
public class AnimationChannel {

    private Integer sampler;
    private AnimationChannelTarget target;

    private Map<String, Object> extensions;
    private Object extras;

}
