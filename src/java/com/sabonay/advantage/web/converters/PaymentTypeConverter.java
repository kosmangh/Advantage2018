/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.convertors;

/**
 *
 * @author Edwin
 */
import com.sabonay.advantage.common.constants.PaymentType;
import com.sabonay.advantage.web.utils.ds;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = PaymentType.class)
public class PaymentTypeConverter implements Converter
{

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value)
    {
        String paymentType = value;
        return paymentType;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value)
    {
        PaymentType paymentType = (PaymentType) value;
        if (paymentType != null)
        {
            if (component.getId().toLowerCase().contains("string"))
            {
                return paymentType.toString();
            }

            return paymentType.getInitials();
        }
        return null;
    }
}
