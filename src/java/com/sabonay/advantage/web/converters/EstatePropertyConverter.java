/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.converters;

import com.sabonay.advantage.ejb.entities.EstateProperty;
import com.sabonay.advantage.web.utils.ds;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = EstateProperty.class)
public class EstatePropertyConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        EstateProperty estateProperty = ds.getCommonQry().estatePropertyFind(value);
        return estateProperty;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        EstateProperty estateProperty = (EstateProperty) value;
        if (estateProperty != null) {
            if (component.getId().toLowerCase().contains("name")) {
                return estateProperty.toString();
            }

            return estateProperty.getEstatePropertyId();
        }
        return null;
    }
}