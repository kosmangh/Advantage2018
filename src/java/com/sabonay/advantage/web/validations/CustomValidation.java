/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author wahab
 */



// i wrote this

public class CustomValidation implements javax.faces.validator.Validator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String ESTATE_NAME = "^[a-zA-Z]$";

    public CustomValidation(){
        pattern = Pattern.compile(ESTATE_NAME);
    }

    public boolean validate(final String estatename){
        matcher = pattern.matcher(estatename);
		  return matcher.matches();
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException{

    }

}
