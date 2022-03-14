package com.hastings.router.route.model;

/**
 *
 */
public class AppRouteRequest {

    private String id;

    private String policyNumber;

    /**
     * Default constructor
     */
    public AppRouteRequest() {

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

    @Override
    public String toString() {
        return "AppRouteRequest{" +
                "id='" + id + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                '}';
    }
}
