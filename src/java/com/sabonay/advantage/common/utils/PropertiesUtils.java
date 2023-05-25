/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.ejb.entities.ArreasRecord;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.EstatePropertyOccupantDetail;
import com.sabonay.advantage.web.utils.ds;

/**
 *
 * @author Edwin
 */
public class PropertiesUtils
{

    public static EstatePropertyOccupantDetail getPropertyDetail(String estatePropertyId)
    {
        EstateProperty estateProperty = ds.getCommonQry().estatePropertyFind(estatePropertyId);

        EstatePropertyOccupantDetail estatePropertyDetail = new EstatePropertyOccupantDetail();

        estatePropertyDetail.setEstateName(estateProperty.getEstateBlock().getEstate().getEstateName());
        estatePropertyDetail.setBlockName(estateProperty.getEstateBlock().getBlockName());
        estatePropertyDetail.setPropertyName(estateProperty.getPropertyName());
        estatePropertyDetail.setPropertyNumber(estateProperty.getPropertyNumber());
        estatePropertyDetail.setEstatePropertyId(estateProperty.getEstatePropertyId());

        PropertyOccupant propertyOccupant = ds.getCustomQry().estatePropertyCurrentOccupant(estateProperty);

        if(propertyOccupant != null)
        {
            estatePropertyDetail.setPropertyOccupant(propertyOccupant.getOccupantName());
            estatePropertyDetail.setPropertyOccupantId(propertyOccupant.getPropertyOccupantId());
        }
        

        ArreasRecord arreasRecord = new ArreasRecord();
        arreasRecord.setEstateProperty(estateProperty);
        IDCreator.setArreasRecordId(arreasRecord);

        System.out.println("going to find arreas ..... " + arreasRecord);
        arreasRecord = ds.getCommonQry().arreasRecordFind(arreasRecord.getArreasRecordId());

        System.out.println("arreas loadded is  " + arreasRecord);

        if(arreasRecord != null)
        {
            try
            {
                estatePropertyDetail.setArrreasFrom(arreasRecord.getYearArreasStartedFrom());
                estatePropertyDetail.setVerified(arreasRecord.getVerified());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        return estatePropertyDetail;
    }

}
