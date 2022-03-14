package com.hastings.router.route.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class AppRoute {

    @Id
    private String id;

    private String policyNumber;

    private String statusCode;

    private String statusText;

    private String appPath;

    public AppRoute() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    @Override
    public String toString() {
        return "AppRoute{" +
                "id='" + id + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusText='" + statusText + '\'' +
                ", appPath='" + appPath + '\'' +
                '}';
    }
}
