package com.sdkj.bbcat.bean;

/**
 * Created by ${Rhino} on 2016/1/27 22:35
 */
public class RoomVo {
    private String id;
    private String name;
    private String owner;
    private String affiliations_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAffiliations_count() {
        return affiliations_count;
    }

    public void setAffiliations_count(String affiliations_count) {
        this.affiliations_count = affiliations_count;
    }
}
