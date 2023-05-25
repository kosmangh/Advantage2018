/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.common.utils.IDCreator;
import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.GroundRent;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;



public class InitDataUtils
{
    public static EstateProperty createEstateProperty(String houseNumber,double assetLandSize,String estateAssetCategory, EstateBlock estateBlock)
    {
        EstateProperty estateProperty = new EstateProperty();
        estateProperty.setPropertyNumber(houseNumber);
        estateProperty.setPropertyCategory(estateAssetCategory);
        estateProperty.setEstateBlock(estateBlock);
        estateProperty.setPropertyLandSize(assetLandSize);
        IDCreator.setEstatePropertyId(estateProperty);
        boolean houseId = ds.getCommonQry().estatePropertyUpdate(estateProperty);

        return estateProperty;
    }

    public static EstateBlock createEstateBlock(String estateBlockName, Estate selectedEstate)
    {
        EstateBlock esateBlock = new EstateBlock();
        esateBlock.setBlockName(estateBlockName.toUpperCase());
        esateBlock.setEstate(selectedEstate);
        IDCreator.setEstateBlockId(esateBlock);
        boolean blockId = ds.getCommonQry().estateBlockUpdate(esateBlock);
        
        return esateBlock;
    }

    
}
