package com.web.common.web.common.util.locations;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域
 *
 * @author royguo@rongxintx.com
 */
public class District {
    public String id;
    public String name;
    public City city;
    public List<BusinessArea> areas = new ArrayList<BusinessArea>();

    public District() {
    }

    public District(Item item, City city) {
        this.id = item.cityId;
        this.name = item.cityName;
        this.city = city;
    }
}
