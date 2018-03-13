package com.web.common.web.common.util.locations;


/**
 * 商圈.
 */
public class BusinessArea {
    public String id;
    public String name;
    public District district;

    public BusinessArea() {
    }

    public BusinessArea(Item item, District district) {
        this.id = item.cityId;
        this.name = item.cityName;
        this.district = district;
    }
}
