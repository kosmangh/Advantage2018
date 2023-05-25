/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.models;

/**
 *
 * @author Edwin
 */
public class BlockDetail extends EstateDetail
{

    private String blockName;
    private String estateBlockId;

    private int blockPropertiesCount;

    public String getBlockName()
    {
        return blockName;
    }

    public void setBlockName(String blockName)
    {
        this.blockName = blockName;
    }

    public int getBlockPropertiesCount()
    {
        return blockPropertiesCount;
    }

    public void setBlockPropertiesCount(int blockPropertiesCount)
    {
        this.blockPropertiesCount = blockPropertiesCount;
    }

    public String getEstateBlockId()
    {
        return estateBlockId;
    }

    public void setEstateBlockId(String estateBlockId)
    {
        this.estateBlockId = estateBlockId;
    }


    

}
