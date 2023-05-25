/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.constants;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author edwin
 */
public enum OccupantType
{
    Lessee, Tenant;

    public static List<OccupantType> list()
    {
        List<OccupantType> occupantTypes = new LinkedList<OccupantType>();
        occupantTypes.add(Lessee);
        occupantTypes.add(Tenant);

        return occupantTypes;
    }

    public String getFullString()
    {
        return getClass().getCanonicalName()+"."+name();
    }
}
