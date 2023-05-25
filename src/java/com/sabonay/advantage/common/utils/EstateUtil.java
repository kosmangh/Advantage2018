/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;

/**
 *
 * @author Edwin
 */
public class EstateUtil {

    private Estate estate;
    private EstateBlock estateBlock;
    private EstateProperty estateProperty;
    private String estateId;
    private String blockId;
    private String propertyId;
    private String propertyNumber;
    private String blockName;

    public EstateUtil(Estate estate, String blockName, String propertyNumber) {
        this.estate = estate;
        this.blockName = blockName;
        this.propertyNumber = propertyNumber;

        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessageAndRespond("Entered Estate Property  does not exit");
            return;
        }

    }

    public void process() {
        if (estate == null) {
            return;
        }

        if (blockName == null) {
            return;
        }

        if (blockName.isEmpty()) {
            return;
        }

        estateBlock = new EstateBlock();
        estateBlock.setEstate(estate);
        estateBlock.setBlockName(blockName);

        IDCreator.setEstateBlockId(estateBlock);
        estateBlock = ds.getCommonQry().estateBlockFind(estateBlock.getEstateBlockId());

        if (propertyNumber == null) {
            return;
        }

        if (propertyNumber.isEmpty()) {
            return;
        }

        estateProperty = new EstateProperty();
        estateProperty.setEstateBlock(estateBlock);
        estateProperty.setPropertyNumber(propertyNumber);

        IDCreator.setEstatePropertyId(estateProperty);

        estateProperty = ds.getCommonQry().estatePropertyFind(estateProperty.getEstatePropertyId());

    }

    public Estate getEstate() {
        return estate;
    }

    public EstateBlock getEstateBlock() {
        return estateBlock;
    }

    public EstateProperty getEstateProperty() {
        return estateProperty;
    }
}
