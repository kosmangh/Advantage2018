/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.converters;

/**
 *
 * @author Edwin
 */
import com.sabonay.advantage.ejb.entities.PropertyOccupant;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = PropertyOccupant.class)
public class PropertyOccupantConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        PropertyOccupant propertyOccupant = ds.getCommonQry().propertyOccupantFind(value);
        return propertyOccupant;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {

        try {
            PropertyOccupant propertyOccupant = (PropertyOccupant) value;

            if (propertyOccupant != null) {
                if (component.getId().toLowerCase().contains("name".toLowerCase())) {
                    return propertyOccupant.toString();
                } else {
                    return propertyOccupant.getPropertyOccupantId();
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error ocuured in lesee convertor");
            e.printStackTrace();
        }
        return null;
    }
}
