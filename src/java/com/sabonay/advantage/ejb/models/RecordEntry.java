/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class RecordEntry
{
    private String propertyName;
    private String propertyNumber;
    private String propertyId;

    public String getPropertyOccupantName() {
        return occupantName;
    }

    public void setPropertyOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

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
    private String occupantName;



}
