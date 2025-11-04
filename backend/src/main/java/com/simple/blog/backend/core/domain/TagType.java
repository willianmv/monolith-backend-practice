package com.simple.blog.backend.core.domain;

import java.util.Arrays;

public enum TagType {

    TECHNOLOGY,
    FOOD,
    TRAVEL,
    NEWS,
    ENTERTAINMENT,
    SPORTS,
    FUN,
    HEALTH,
    EDUCATION,
    BUSINESS,
    CULTURE;

    public static TagType fromString(String name){
        for(TagType tag : TagType.values()){
            if(name.equalsIgnoreCase(tag.name())) return tag;
        }

        throw new IllegalArgumentException("Invalid tag: "+name);
    }

    public static boolean exists(String name){
        return Arrays.stream(TagType.values())
                .anyMatch(tag -> tag.name().equalsIgnoreCase(name));
    }

}
