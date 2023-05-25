/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.refactoring;

import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.Occupancy;
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.ejb.models.OccupiedProperty;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Edwin / Ritchid
 */
public class PropertyOccupation
{

    public static OccupiedProperty transOccupied(Occupancy lp)
    {
        OccupiedProperty op = new OccupiedProperty();

            PropertyOccupant propertyOccupant = lp.getPropertyOccupant();
            EstateProperty ep = lp.getEstateProperty();

            op.setPropertyOccupantId(propertyOccupant.getPropertyOccupantId());
            op.setOccupantName(propertyOccupant.getOccupantName());
            op.setDateOfBirth(propertyOccupant.getDateOfBirth());
            op.setGender(propertyOccupant.getGender());
            op.setOccupation(propertyOccupant.getOccupation());
            op.setHomeTown(propertyOccupant.getHomeTown());
            op.setLocalAddress(propertyOccupant.getLocalAddress());
            op.setNationality(propertyOccupant.getNationality());
            op.setTelephoneNumber(propertyOccupant.getTelephoneNumber());
            op.setEmailAddress(propertyOccupant.getEmailAddress());
            op.setNationalIdType(propertyOccupant.getNationalIdType());
            op.setNationalId(propertyOccupant.getNationalId());
            op.setNextOfKin(propertyOccupant.getNextOfKin());
            op.setRelationship(propertyOccupant.getRelationship());

            op.setFirstDateOfOccupancy(lp.getFirstDateOfOccupancy());
            op.setLastDateOfOccupancy(lp.getLastDateOfOccupancy());

            op.setEstatePropertyId(ep.getEstatePropertyId());
            op.setEstateName(ep.getPropertyEstate().getEstateName());
            op.setPropertyName(ep.getPropertyName());
            op.setEstateName(ep.getPropertyEstate().getEstateName());
            op.setPropertyUsage(ep.getPropertyUsage().getUsageName());
            op.setPropertyCategory(ep.getPropertyCategory());
            op.setPropertyOccupiedAs(lp.getOccupationType().toString());

        return op;
    }

    public static List<OccupiedProperty> transOccupied(List<Occupancy> occupantPropertyList)
    {
        List<OccupiedProperty> list = new LinkedList<OccupiedProperty>();

        for (Occupancy lp : occupantPropertyList)
        {
            OccupiedProperty op = transOccupied(lp);
            list.add(op);
        }

        return list;
    }

}
