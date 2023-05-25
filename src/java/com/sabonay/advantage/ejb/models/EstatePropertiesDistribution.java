/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class EstatePropertiesDistribution extends BlockDetail
{


//    private String blockName;
    private int residentialCount;

    private int mixedUseCount;
    private int commercialCount;
    private int psuCount;
    private int industrialCount;


//    public String getBlockName()
//    {
//        return blockName;
//    }
//
//    public void setBlockName(String blockName)
//    {
//        this.blockName = blockName;
//    }



    public int getMixedUseCount()
    {
        return mixedUseCount;
    }

    public void setMixedUseCount(int mixedUseCount)
    {
        this.mixedUseCount = mixedUseCount;
    }

    public int getResidentialCount()
    {
        return residentialCount;
    }

    public void setResidentialCount(int residentialCount)
    {
        this.residentialCount = residentialCount;
    }

    public int getCommercialCount()
    {
        return commercialCount;
    }

    public void setCommercialCount(int commercialCount)
    {
        this.commercialCount = commercialCount;
    }

    


    public int getIndustrialCount()
    {
        return industrialCount;
    }

    public void setIndustrialCount(int industrialCount)
    {
        this.industrialCount = industrialCount;
    }

    public int getPsuCount()
    {
        return psuCount;
    }

    public void setPsuCount(int psuCount)
    {
        this.psuCount = psuCount;
    }


    

}
