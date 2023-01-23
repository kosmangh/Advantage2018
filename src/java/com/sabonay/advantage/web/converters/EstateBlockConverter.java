/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.converters;

import com.sabonay.advantage.ejb.entities.EstateBlock;
import com.sabonay.advantage.web.utils.ds;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Edwin
 */
@FacesConverter(forClass = EstateBlock.class)
public class EstateBlockConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        EstateBlock selectedEstateBlock = ds.getCommonQry().estateBlockFind(value);
        return selectedEstateBlock;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        EstateBlock selectedEstateBlock = (EstateBlock) value;
        if (selectedEstateBlock != null) {
            return selectedEstateBlock.getEstateBlockId();
        }
        return null;
    }
}
