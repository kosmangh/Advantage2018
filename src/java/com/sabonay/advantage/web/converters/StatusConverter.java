/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.converters;

import com.sabonay.advantage.ejb.entities.Status;
import com.sabonay.advantage.web.utils.ds;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author sethbilly
 */
@FacesConverter(forClass = Status.class)
public class StatusConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Status status = ds.getCommonQry().statusFind(Boolean.parseBoolean(value));
        return status;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Status status = (Status) value;
        return status.getId().toString();
    }
    
}
