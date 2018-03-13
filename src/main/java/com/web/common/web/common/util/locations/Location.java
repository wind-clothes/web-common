package com.web.common.web.common.util.locations;

/**
 * 地址的数据，包含：省市区（县）商圈
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2015年10月29日 上午11:51:26
 */
public class Location {

    private Long cityId;
    private Long provinceId;
    private Long districtId;
    private Long businessAreaceId;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getBusinessAreaceId() {
        return businessAreaceId;
    }

    public void setBusinessAreaceId(Long businessAreaceId) {
        this.businessAreaceId = businessAreaceId;
    }

}
