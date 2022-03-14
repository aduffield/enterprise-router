package com.hastings.router.route.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RouteConfigRequest {

    private String id;

    private String pattern;

    private String brand;

    private String arFlag;

    private String lob;

    private String appPath;

    private List<Integer> sum = new ArrayList<>();

    private Integer status;

    /**
     *
     */
    public RouteConfigRequest() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getArFlag() {
        return arFlag;
    }

    public void setArFlag(String arFlag) {
        this.arFlag = arFlag;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public List<Integer> getSum() {
        return sum;
    }

    public void setSum(List<Integer> sum) {
        this.sum = sum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String asJsonString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "RouteConfigRequest{" +
                "id='" + id + '\'' +
                ", pattern='" + pattern + '\'' +
                ", brand='" + brand + '\'' +
                ", arFlag='" + arFlag + '\'' +
                ", lob='" + lob + '\'' +
                ", appPath='" + appPath + '\'' +
                ", sum=" + sum +
                ", status=" + status +
                '}';
    }
}
