/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

import com.sabonay.advantage.ejb.entities.ArreasRecord;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.web.utils.ds;

/**
 *
 * @author Edwin
 */
public class EstatePropertyOccupantDetail extends EstatePropertyDetail
{

    private Boolean verified;
    private String propertyOccupantId;
    private String propertyOccupant;
    private int arrreasFrom;

    public int getArrreasFrom()
    {
        return arrreasFrom;
    }

    public void setArrreasFrom(int arrreasFrom)
    {
        this.arrreasFrom = arrreasFrom;
    }

    public String getPropertyOccupant()
    {
        return propertyOccupant;
    }

    public void setPropertyOccupant(String propertyOccupant)
    {
        this.propertyOccupant = propertyOccupant;
    }


    public String getPropertyOccupantId()
    {
        return propertyOccupantId;
    }

    public void setPropertyOccupantId(String propertyOccupantId)
    {
        this.propertyOccupantId = propertyOccupantId;
    }




    public Boolean getVerified()
    {
        return verified;
    }

    public void setVerified(Boolean verified)
    {
        this.verified = verified;
    }

   

    @Override
    public String toString()
    {
        return getPropertyName() + " " + verified ;
    }




    public void updateLeesse()
    {
        PropertyOccupant po = ds.getCommonQry().propertyOccupantFind(propertyOccupantId);

        if(propertyOccupant == null)
            return;
        
        po.setOccupantName(propertyOccupant);

        ds.getCommonQry().propertyOccupantUpdate(po);
    }

    public void updateArreasRecord()
    {


        ArreasRecord record = new ArreasRecord();
        record.setArreasRecordId(getEstatePropertyId());
        record.setYearArreasStartedFrom(arrreasFrom);
        record.setEstateProperty(ds.getCommonQry().estatePropertyFind(getEstatePropertyId()));
        record.setVerified(verified);

        ds.getCommonQry().arreasRecordUpdate(record);
    }



}
