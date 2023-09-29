package org.example;

public class ParentModel {
    String key_store,alias;
    String name,organ_unit,unit,city,state,code;
    String apk_path;

    public ParentModel(String key_store, String alias, String name, String organ_unit, String unit, String city, String state, String code) {
        this.key_store = key_store;
        this.alias = alias;
        this.name = name;
        this.organ_unit = organ_unit;
        this.unit = unit;
        this.city = city;
        this.state = state;
        this.code = code;
    }

    public ParentModel(String alias,String key_store, String apk_path) {
        this.key_store = key_store;
        this.alias = alias;
        this.apk_path = apk_path;
    }

    public String getKey_store() {
        return key_store;
    }

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public String getOrgan_unit() {
        return organ_unit;
    }

    public String getUnit() {
        return unit;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCode() {
        return code;
    }

    public String getApk_path() {
        return apk_path;
    }
}
