/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class EstatePropertyDetail extends BlockDetail {

    private String estatePropertyId;
    private String propertyName;
    private String propertyNumber;
    private String propertyUsage;
    private String propertyCategory;
    private String esatePropertyType;
    private String propertyOccupiedAs;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getEstatePropertyId() {
        return estatePropertyId;
    }

    public void setEstatePropertyId(String estatePropertyId) {
        this.estatePropertyId = estatePropertyId;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getEsatePropertyType() {
        return esatePropertyType;
    }

    public void setEsatePropertyType(String esatePropertyType) {
        this.esatePropertyType = esatePropertyType;
    }

    public String getPropertyOccupiedAs() {
        return propertyOccupiedAs;
    }

    public void setPropertyOccupiedAs(String propertyOccupiedAs) {
        this.propertyOccupiedAs = propertyOccupiedAs;
    }

}
