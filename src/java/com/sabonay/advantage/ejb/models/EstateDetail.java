/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class EstateDetail {

    private String estateInitials;
    private String estateName;

    public String getEstateInitials()
    {
        return estateInitials;
    }

    public void setEstateInitials(String estateInitials)
    {
        this.estateInitials = estateInitials;
    }

    public String getEstateName()
    {
        return estateName;
    }

    public void setEstateName(String estateName)
    {
        this.estateName = estateName;
    }
}
