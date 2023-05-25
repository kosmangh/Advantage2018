/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.constants;
import com.sabonay.common.api.EnumCommon;

/**
 *
 * @author Edwin / Ritchid
 */
public enum PropertyUsage implements EnumCommon {
    Commercial("Commercial","COM"),
    Institutional("Institutional","INS"),
    Mix_Use("Mixed Use","MIX"),
    Residential("Residential","RES"),
    ALL("All", "ALL");

    private final String usageInitials;
    private final String usageName;

    PropertyUsage(String usageInitials, String usageName) {
        this.usageInitials = usageInitials;
        this.usageName = usageName;
    }

    public String getUsageInitials() {
        return usageInitials;
    }
    
    public String getUsageName() {
        return usageName;
    }


    @Override
    public String getFullString() {
        return getClass().getCanonicalName()+"."+name();
    }



}
