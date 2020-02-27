package com.zga.dazhong.common;

public enum MaterialEnums {
    IMAGE(0, "image"),
    VOICE(1, "voice"),
    VIDEO(2, "video"),
    ;
    private Integer materialType;
    private String materialDesc;

    MaterialEnums(Integer materialType, String materialDesc) {
        this.materialType = materialType;
        this.materialDesc = materialDesc;
    }

    public static MaterialEnums getByType(Integer materialType) {
        for (MaterialEnums enums : values()) {
            if (enums.getMaterialType().equals(materialType)) {
                return enums;
            }
        }
        return null;
    }
    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }
}
