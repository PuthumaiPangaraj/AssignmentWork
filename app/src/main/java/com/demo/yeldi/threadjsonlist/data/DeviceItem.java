package com.demo.yeldi.threadjsonlist.data;

/**
 * Created by pudumai on 7/8/2016.
 */
public class DeviceItem {

    public String DevicType;
    public String Model;
    public String Name;
    public Boolean selected = false;


    public Boolean getIsChecked(){
        return selected;
    }
    public void setIsChecked(Boolean selected){
        selected= selected;
    }

    public String getDevicType() {
        return DevicType;
    }

    public String getModel() {
        return Model;
    }

    public String getName() {
        return Name;
    }

    public void setDevicType(String devicType) {
        DevicType = devicType;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setModel(String model) {
        Model = model;
    }
}
