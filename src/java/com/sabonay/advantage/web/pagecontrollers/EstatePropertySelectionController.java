/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.ejb.entities.EstateProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Edwin
 */
@Named(value = "estatePropertySelectionController")
@ViewScoped
public class EstatePropertySelectionController {

    private Estate selectedEstate;
    private EstateBlock selectedEstateBlock;
    private EstateProperty selectedEstateProperty;

    private static final String BEAN_NAME = "estatePropertySelectionController";

    public EstatePropertySelectionController() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Estate getSelectedEstate() {
        return selectedEstate;
    }

    public void setSelectedEstate(Estate selectedEstate) {
        this.selectedEstate = selectedEstate;
    }

    public EstateBlock getSelectedEstateBlock() {
        return selectedEstateBlock;
    }

    public void setSelectedEstateBlock(EstateBlock selectedEstateBlock) {
        this.selectedEstateBlock = selectedEstateBlock;
    }

    public EstateProperty getSelectedEstateProperty() {
        return selectedEstateProperty;
    }

    public void setSelectedEstateProperty(EstateProperty selectedEstateProperty) {
        this.selectedEstateProperty = selectedEstateProperty;
    }

    // </editor-fold>
}
