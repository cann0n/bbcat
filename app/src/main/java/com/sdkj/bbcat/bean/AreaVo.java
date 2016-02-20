package com.sdkj.bbcat.bean;

import com.huaxi100.networkapp.adapter.ParentVo;

import java.io.Serializable;

/**
 * Created by ${Rhino} on 2015/10/27 11:24
 */
public class AreaVo extends ParentVo  implements Serializable{

    private String id;
    private String name;
    
    private boolean isChecked=false;
    
    private boolean isShown=false;

    public boolean isShown() {
        return isShown;
    }

    public void setIsShown(boolean isShown) {
        this.isShown = isShown;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
