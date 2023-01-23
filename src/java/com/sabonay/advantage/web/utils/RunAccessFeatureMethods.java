/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author crash
 */
public class RunAccessFeatureMethods {

    public void runMethod(Object obj, String methodname, boolean editable) {
        if ((methodname == null) || ((methodname != null) && (methodname.length() == 0))) {
            return;
        }

        try {
            Class clz = obj.getClass();

            Method curMethod = clz.getMethod(methodname, new Class[]{Boolean.TYPE});

            curMethod.invoke(obj, new Object[]{Boolean.valueOf(editable)});
        } catch (NoSuchMethodException e) {
            
        } catch (SecurityException e) {
            
        } catch (IllegalAccessException e) {
            
        } catch (IllegalArgumentException e) {
            
        } catch (InvocationTargetException e) {
           
        } catch (Exception e) {
           
        }
    }

    public boolean runMethod(Object obj, String methodname) {
        if ((methodname == null) || ((methodname != null) && (methodname.length() == 0))) {
            return false;
        }

        try {
            Class clz = obj.getClass();

            Method curMethod = clz.getMethod(methodname, new Class[0]);

            return ((Boolean) curMethod.invoke(obj, new Object[0])).booleanValue();
            
        } catch (NoSuchMethodException e) {
            
        } catch (SecurityException e) {
            
        } catch (IllegalAccessException e) {
            
        } catch (IllegalArgumentException e) {
           
        } catch (InvocationTargetException e) {
            
        } catch (Exception e) {
            
        }

        return false;
    }

//    public static void main(String[] args) {
//        String editmethodname = "stdMrks_Write";
//        RunAccessFeatureMethods uarreflectedmethods = new RunAccessFeatureMethods();
//
//        boolean isedit = true;
//
//        String editmethod = SentenceCases.stringToSetMethodName(editmethodname);
//        System.out.println("LoadUserPages::loadPages() editmethod " + editmethod);
//        uarreflectedmethods.runMethod(uarreflectedmethods, editmethod, isedit);
//
//        editmethod = SentenceCases.stringToGetMethodName(editmethodname);
//        System.out.println("LoadUserPagesByRole::main() editmethodname " + editmethodname);
//        System.out.println("LoadUserPagesByRole::main() editmethod " + editmethod);
//        System.out.println("LoadUserPagesByRole::main() editval " + uarreflectedmethods.runMethod(uarreflectedmethods, editmethod));
//    }
}
