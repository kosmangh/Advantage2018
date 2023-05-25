/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;


import java.io.Serializable;

/**
 * @author Edwin
 */

public class GroundRentDetail implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String groundRentId;

    private Integer yearDue;
    private String counter;

    
    private String propertyUsage;

    private Double firstClassEstateAmountCharged;

    private Double secondClassEstateAmountCharged;

    private Double thirdClassEstateAmountCharged;

    public GroundRentDetail() {
    }

    public GroundRentDetail(String groundRentId) {
        this.groundRentId = groundRentId;
    }

    public String getGroundRentId() {
        return groundRentId;
    }


    public String getPropertyUsage()
    {
        return propertyUsage;
    }

    public String getCounter()
    {
        return counter;
    }

    public void setCounter(String counter)
    {
        this.counter = counter;
    }

    public void setPropertyUsage(String propertyUsage)
    {
        this.propertyUsage = propertyUsage;
    }

    public Integer getYearDue()
    {
        return yearDue;
    }

    public void setYearDue(Integer yearDue)
    {
        this.yearDue = yearDue;
    }

    

    public Double getFirstClassEstateAmountCharged()
    {
        return firstClassEstateAmountCharged;
    }

    public void setFirstClassEstateAmountCharged(Double firstClassEstateAmountCharged)
    {
        this.firstClassEstateAmountCharged = firstClassEstateAmountCharged;
    }

    public Double getSecondClassEstateAmountCharged()
    {
        return secondClassEstateAmountCharged;
    }

    public void setSecondClassEstateAmountCharged(Double secondClassEstateAmountCharged)
    {
        this.secondClassEstateAmountCharged = secondClassEstateAmountCharged;
    }

    public Double getThirdClassEstateAmountCharged()
    {
        return thirdClassEstateAmountCharged;
    }

    public void setThirdClassEstateAmountCharged(Double thirdClassEstateAmountCharged)
    {
        this.thirdClassEstateAmountCharged = thirdClassEstateAmountCharged;
    }

    @Override
    public String toString()
    {
        return yearDue + "";
    }





}
