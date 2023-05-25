/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.ejb.models;

/**
 *
 * @author ABDULMUMIN
 */
public class RentalOccupantDetail {
    
    private String propertyNumber;
    private String propertyOccupant;
    private double oldRent;
    private double newRent;

    public double getNewRent() {
        return newRent;
    }

    public void setNewRent(double newRent) {
        this.newRent = newRent;
    }

    public double getOldRent() {
        return oldRent;
    }

    public void setOldRent(double oldRent) {
        this.oldRent = oldRent;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getPropertyOccupant() {
        return propertyOccupant;
    }

    public void setPropertyOccupant(String propertyOccupant) {
        this.propertyOccupant = propertyOccupant;
    }
    
    
}
