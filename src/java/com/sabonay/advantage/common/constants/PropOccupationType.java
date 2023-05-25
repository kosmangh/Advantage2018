/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.constants;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author edwin / Ritchid
 */
public enum PropOccupationType {


    Leasehold, Rental, NONE;


    public static List<PropOccupationType> list() {
        List<PropOccupationType> occupationTypes = new LinkedList<>();
        occupationTypes.add(Leasehold);
        occupationTypes.add(Rental);

        return occupationTypes;
    }



    public String getFullString()
    {
        return getClass().getCanonicalName()+"."+name();
    }

}
