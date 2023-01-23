/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.converters;

import com.sabonay.advantage.ejb.entities.Estate;
import com.sabonay.advantage.web.utils.ds;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Edwin
 */
@FacesConverter(forClass = Estate.class)
public class EstateConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Estate selectedEstate = ds.getCommonQry().estateFind(value);
        return selectedEstate;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Estate selectedEstate = (Estate) value;
        if (selectedEstate != null) {
            if (component.getId().toLowerCase().contains("name")) {
                return selectedEstate.toString();
            } else {
                return selectedEstate.getEstateId();
            }
        }

        return null;
    }
}
