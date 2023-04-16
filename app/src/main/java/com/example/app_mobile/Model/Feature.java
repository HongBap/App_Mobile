package com.example.app_mobile.Model;

import java.io.Serializable;

public class Feature implements Serializable {
    private int featureFeatureId;
    private int featureTypeId;
    private String featureSpecific;
    private int featurePoint;

    public Feature(int featureFeatureId, int featureTypeId, String featureSpecific) {
        this.featureFeatureId = featureFeatureId;
        this.featureTypeId = featureTypeId;
        this.featureSpecific = featureSpecific;
    }

    public int getFeatureFeatureId() {
        return featureFeatureId;
    }

    public void setFeatureFeatureId(int featureFeatureId) {
        this.featureFeatureId = featureFeatureId;
    }

    public int getFeatureTypeId() {
        return featureTypeId;
    }

    public void setFeatureTypeId(int featureTypeId) {
        this.featureTypeId = featureTypeId;
    }

    public String getFeatureSpecific() {
        return featureSpecific;
    }

    public void setFeatureSpecific(String featureSpecific) {
        this.featureSpecific = featureSpecific;
    }
}
