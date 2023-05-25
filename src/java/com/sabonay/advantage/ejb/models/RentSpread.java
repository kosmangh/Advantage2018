/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;

/**
 *
 * @author Edwin
 */
public class RentSpread
{
    private EstateProperty estateProperty;
    private PropertyOccupant propertyOccupant;

    private double currentRent = 0;
    private double currentRentBalance = 0;

    public double getCurrentRent()
    {
        return currentRent;
    }

    public void setCurrentRent(double currentRent)
    {
        this.currentRent = currentRent;
    }

    public double getCurrentRentBalance()
    {
        return currentRentBalance;
    }

    public void setCurrentRentBalance(double currentRentBalance)
    {
        this.currentRentBalance = currentRentBalance;
    }

    public EstateProperty getEstateProperty()
    {
        return estateProperty;
    }

    public void setEstateProperty(EstateProperty estateProperty)
    {
        this.estateProperty = estateProperty;
    }

    public PropertyOccupant getPropertyOccupant()
    {
        return propertyOccupant;
    }

    public void setPropertyOccupant(PropertyOccupant propertyOccupant)
    {
        this.propertyOccupant = propertyOccupant;
    }

}
