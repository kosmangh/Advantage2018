/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.converters;

import com.sabonay.advantage.ejb.entities.UserRole;
import com.sabonay.advantage.web.utils.ds;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author crash
 */
@FacesConverter(forClass = UserRole.class)
public class UserRoleConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        UserRole userRole = ds.getCommonQry().userRoleFind(Integer.parseInt(value));
        return userRole;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        UserRole userRole = (UserRole)value;
        return userRole.getId().toString();
    }
    
}
