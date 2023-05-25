/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Edwin
 */
public class _DemandNotice implements Serializable
{
    private String propertyOccupantIssuedToId = "";
    private String demandNoticeIntroduction;
    private String propertyOccupantIssuedTo = "";
    private String dueDate;
    private String coverageDate;
    private String nextGroundRentReviewDate;


    private List<PropertyBill> propertyBillsList = new LinkedList<PropertyBill>();

    public boolean haveBillForProperty(String propertyID)
    {
        try
        {
            for (PropertyBill propertyBill : propertyBillsList)
        {
            if(propertyBill.getEstatePropertyId().equalsIgnoreCase(propertyID))
                return true;
        }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public String getCoverageDate()
    {
        return coverageDate;
    }

    public void setCoverageDate(String coverageDate)
    {
        this.coverageDate = coverageDate;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getPropertyOccupantIssuedTo()
    {
        return propertyOccupantIssuedTo;
    }

    public void setPropertyOccupantIssuedTo(String propertyOccupantIssuedTo)
    {
        this.propertyOccupantIssuedTo = propertyOccupantIssuedTo;
    }

    public String getNextGroundRentReviewDate()
    {
        return nextGroundRentReviewDate;
    }

    public void setNextGroundRentReviewDate(String nextGroundRentReviewDate)
    {
        this.nextGroundRentReviewDate = nextGroundRentReviewDate;
    }

    public List<PropertyBill> getPropertyBillsList()
    {
        return propertyBillsList;
    }

    public void setPropertyBillsList(List<PropertyBill> propertyBillsList)
    {
        this.propertyBillsList = propertyBillsList;
    }

    public String getDemandNoticeIntroduction()
    {
        return demandNoticeIntroduction;
    }

    public void setDemandNoticeIntroduction(String demandNoticeIntroduction)
    {
        this.demandNoticeIntroduction = demandNoticeIntroduction;
    }

    

    public String getPropertyOccupantIssuedToId()
    {
        return propertyOccupantIssuedToId;
    }

    public void setPropertyOccupantIssuedToId(String propertyOccupantIssuedToId)
    {
        this.propertyOccupantIssuedToId = propertyOccupantIssuedToId;
    }


    @Override
    public String toString()
    {
        return propertyOccupantIssuedTo;
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
        final _DemandNotice other = (_DemandNotice) obj;
        if ((this.propertyOccupantIssuedToId == null) ? (other.propertyOccupantIssuedToId != null) : !this.propertyOccupantIssuedToId.equals(other.propertyOccupantIssuedToId))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + (this.propertyOccupantIssuedToId != null ? this.propertyOccupantIssuedToId.hashCode() : 0);
        return hash;
    }



}
