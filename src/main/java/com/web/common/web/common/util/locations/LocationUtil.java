package com.web.common.web.common.util.locations;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.web.common.web.common.util.FileUtils;

/**
 * 地理位置工具类.
 *
 * @author royguo@rongxintx.com
 */
public class LocationUtil {
    private static final String filePath = "cities.json";

    // 缓存
    private static final List<Province> provinces = new ArrayList<>(50);
    private static final List<City> cities = new ArrayList<>();
    private static final Map<String, List<District>> cityDistricts =
        new HashMap<String, List<District>>();
    private static final Map<String, List<BusinessArea>> districtAreas =
        new HashMap<String, List<BusinessArea>>();

    // 遍历工具
    private static final List<Item> i1s = new ArrayList<>();
    private static final List<Item> i2s = new ArrayList<>();
    private static final List<Item> i3s = new ArrayList<>();
    private static final List<Item> i4s = new ArrayList<>();

    private static Iterator<Item> i1sIT;
    private static Iterator<Item> i2sIT;
    private static Iterator<Item> i3sIT;
    private static Iterator<Item> i4sIT;

    // JSON直接映射的对象.
    private static List<Item> items = new ArrayList<>();

    //暂时关闭，需要时在打开
    static {
        if (provinces.isEmpty()) {
            initData();
        }
    }

    /**
     * 获得所有的省信息.
     *
     * @return
     */
    public static List<Province> getAllProvinces() {
        if (provinces.isEmpty()) {
            initData();
        }
        return provinces;
    }

    public static String getCityIDByName(String name) {
        for (City city : cities) {
            if (city.name.equalsIgnoreCase(name)) {
                return city.id;
            }
        }
        return null;
    }

    public static String getProvinceIDByName(String name) {
        for (Province p : provinces) {
            if (p.name.equalsIgnoreCase(name)) {
                return p.id;
            }
        }
        return null;
    }

    /**
     * 获取省份下的所有城市
     *
     * @param provinceID
     * @return
     */
    public static List<City> getCitiesByProvince(String provinceID) {
        for (Province p : provinces) {
            if (p.id.equals(provinceID)) {
                return p.cities;
            }
        }
        return new ArrayList<>();
    }

    public static List<District> getDistrictsByCity(String cityID) {
        return cityDistricts.get(cityID);
    }

    /**
     * @param cityID
     * @param districtID
     * @return
     */
    public static String getDistrictNameByID(String cityID, String districtID) {
        List<District> districtList = getDistrictsByCity(cityID);
        if (districtList == null) {
            return "";
        }
        for (District district : districtList) {
            if (district.id.equals(districtID)) {
                return district.name;
            }
        }
        return "";
    }

    /**
     * 返回城市信息
     *
     * @param cityID 编号
     * @return 失败返回null
     */
    public static City getCityByID(String cityID) {
        for (City city : cities) {
            if (city.id.equals(cityID)) {
                return city;
            }
        }
        return null;
    }


    public static List<BusinessArea> getAreasByDistrict(String districtID) {
        return districtAreas.get(districtID);
    }

    /**
     * @param districtID
     * @param areaID
     * @return
     */
    public static String getAreaNameID(String districtID, String areaID) {
        List<BusinessArea> areaList = getAreasByDistrict(districtID);
        for (BusinessArea bArea : areaList) {
            if (bArea.id.equals(areaID)) {
                return bArea.name;
            }
        }
        return "";
    }

    /**
     * 返回城市名称
     *
     * @param cityID
     * @return
     */
    public static String getCityNameByID(String cityID) {
        City city = getCityByID(cityID);
        if (city != null) {
            return city.name;
        }
        return "";
    }

    /**
     * 返回省的信息
     *
     * @param provinceID 编号
     * @return 失败返回null
     */
    public static Province getProvinceByID(String provinceID) {
        for (Province province : provinces) {
            if (province.id.equals(provinceID)) {
                return province;
            }
        }
        return null;
    }

    /**
     * 返回省的名称
     *
     * @param provinceID
     * @return
     */
    public static String getProvinceNameByID(String provinceID) {
        Province province = getProvinceByID(provinceID);
        if (province != null) {
            return province.name;
        }
        return "";
    }

    /**
     * 根据城市获得所在省.
     *
     * @param cityID
     * @return
     */
    public static Province getProvinceByCity(String cityID) {
        for (City city : cities) {
            if (city.id.equals(cityID)) {
                return city.province;
            }
        }
        return null;
    }

    /**
     * 特例，全国的编号是0.
     */
    public static String getCountryCode() {
        return "0";
    }

    /**
     * @param provinceID
     * @return
     */
    public static String getLocation(String provinceID) {
        return getLocation(provinceID, null, null, null);
    }

    /**
     * @param provinceID
     * @param cityID
     * @return
     */
    public static String getLocation(String provinceID, String cityID) {
        return getLocation(provinceID, cityID, null, null);
    }

    /**
     * @param provinceID
     * @param cityID
     * @param districtID
     * @return
     */
    public static String getLocation(String provinceID, String cityID, String districtID) {
        return getLocation(provinceID, cityID, districtID, null);
    }

    public static String getLocation(Long provinceID, Long cityID, Long districtID) {

        if (provinceID == null) {
            return "";
        }

        if (provinceID != null && cityID == null) {
            return getLocation(provinceID.toString(), null, null, null);
        }

        if (provinceID != null && cityID != null && districtID == null) {
            return getLocation(provinceID.toString(), cityID.toString(), null, null);
        }

        if (provinceID != null && cityID != null && districtID != null) {
            return getLocation(provinceID.toString(), cityID.toString(), districtID.toString(),
                null);
        }

        return "";
    }

    /**
     * @param provinceID
     * @param cityID
     * @param districtID
     * @param areaID
     * @return
     */
    public static String getLocation(String provinceID, String cityID, String districtID,
        String areaID) {

        if (StringUtils.isEmpty(provinceID)) {
            return "";
        }

        if (StringUtils.isEmpty(cityID)) {
            return getProvinceNameByID(provinceID);
        }

        if (StringUtils.isEmpty(districtID)) {
            return getProvinceNameByID(provinceID) + getCityNameByID(cityID);
        }

        if (StringUtils.isEmpty(areaID)) {
            return getProvinceNameByID(provinceID) + getCityNameByID(cityID) + getDistrictNameByID(
                cityID, districtID);
        }

        return getProvinceNameByID(provinceID) + getCityNameByID(cityID) + getDistrictNameByID(
            cityID, districtID) + getAreaNameID(districtID, areaID);

    }

    /**
     * 初始化数据.
     */
    private synchronized static void initData() {
        if (!provinces.isEmpty()) {
            return;
        }
        try {
            InputStream is =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            String json = FileUtils.readStreamToString(is);
            @SuppressWarnings("unchecked") Map<String, List<JSONObject>> map =
                JSON.parseObject(json, Map.class);
            for (String key : map.keySet()) {
                List<JSONObject> objs = map.get(key);
                for (JSONObject obj : objs) {
                    Item item = JSON.parseObject(obj.toJSONString(), Item.class);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 先读取所有数据缓存到对应的数组里，方便后面遍历.
        for (Item i : items) {
            if (i.cityType.equals("1")) {
                i1s.add(i);
            } else if (i.cityType.equals("2")) {
                i2s.add(i);
            } else if (i.cityType.equals("3")) {
                i3s.add(i);
            } else if (i.cityType.equals("4")) {
                i4s.add(i);
            }
        }

        // 开始填充数据
        i1sIT = i1s.iterator();
        while (i1sIT.hasNext()) {
            Item i1 = i1sIT.next();
            Province p = new Province(i1);
            fillCities(p);
            provinces.add(p);
            i1sIT.remove();
        }
    }

    // 为省添加市数据
    private static void fillCities(Province p) {
        i2sIT = i2s.iterator();

        while (i2sIT.hasNext()) {
            Item i2 = i2sIT.next();
            if (!i2.parentId.equals(p.id)) {
                continue;
            }
            City city = new City(i2, p);
            fillDistricts(city);
            ;

            // 将City添加到全局集合中.
            cities.add(city);
            // 缓存城市下的所有区域
            cityDistricts.put(city.id, city.districts);

            p.cities.add(city);
            i2sIT.remove();
        }
    }

    // 为城市添加区信息.
    private static void fillDistricts(City city) {
        i3sIT = i3s.iterator();
        while (i3sIT.hasNext()) {
            Item i3 = i3sIT.next();
            if (!i3.parentId.equals(city.id))
                continue;
            District district = new District(i3, city);
            fillBusinessAreas(district);
            districtAreas.put(district.id, district.areas);
            city.districts.add(district);
            i3sIT.remove();

        }
    }

    // 为区添加商圈信息
    private static void fillBusinessAreas(District district) {
        i4sIT = i4s.iterator();
        while (i4sIT.hasNext()) {
            Item i4 = i4sIT.next();
            if (!i4.parentId.equals(district.id))
                continue;
            BusinessArea area = new BusinessArea(i4, district);
            district.areas.add(area);
            i4sIT.remove();
        }
    }
}


/**
 * 与数据源的条目映射, 字段名称不代表实际意义, cityId在数据库中以整型存储.
 *
 * @author royguo@rongxintx.com
 */
class Item {
    public String cityId;
    public String cityName;
    public String cityType;
    public String parentId;

    public Item() {
    }
}
