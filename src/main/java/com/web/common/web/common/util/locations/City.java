package com.web.common.web.common.util.locations;

import java.util.ArrayList;
import java.util.List;

public class City {
    public String id;
    public String name;
    public Province province;
    public List<District> districts = new ArrayList<>();

    public City() {
    }

    public City(Item item, Province province) {
        this.id = item.cityId;
        this.name = item.cityName;
        this.province = province;
    }
}
