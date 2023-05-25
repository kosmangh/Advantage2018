/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

import java.io.Serializable;

/**
 *
 * @author Edwin
 */
public class PropertyBill implements Serializable
{
    private String propertyUsage;
    private String propertyNumber;
    private String propertyLocation;
    private String amountPayable;
    private String estatePropertyId;



    
    public String getPropertyUsage()
    {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage)
    {
        this.propertyUsage = propertyUsage;
    }

    public String getPropertyNumber()
    {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber)
    {
        this.propertyNumber = propertyNumber;
    }

    

    @Override
    public String toString()
    {
        return propertyNumber + "("+propertyUsage + ")";
    }

    public String getAmountPayable()
    {
        return amountPayable;
    }

    public void setAmountPayable(String amountPayable)
    {
        this.amountPayable = amountPayable;
    }

    public String getPropertyLocation()
    {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation)
    {
        this.propertyLocation = propertyLocation;
    }

    public String getEstatePropertyId()
    {
        return estatePropertyId;
    }

    public void setEstatePropertyId(String estatePropertyId)
    {
        this.estatePropertyId = estatePropertyId;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final PropertyBill other = (PropertyBill) obj;
        if ((this.estatePropertyId == null) ? (other.estatePropertyId != null) : !this.estatePropertyId.equals(other.estatePropertyId))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + (this.estatePropertyId != null ? this.estatePropertyId.hashCode() : 0);
        return hash;
    }



}
