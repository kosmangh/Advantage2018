/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.web.utils;

/**
 *
 * @author crash
 */
public class MenuItemActions {

    private MenuItem itemId;
    private boolean editable;
    private String edit_page_name;

    public MenuItemActions(MenuItem itemId, String edit_page_name, boolean editable) {
        this.itemId = itemId;
        this.edit_page_name = edit_page_name;
        this.editable = editable;
    }

    public MenuItem getItemId() {
        return this.itemId;
    }

    public void setItemId(MenuItem itemId) {
        this.itemId = itemId;
    }

    public String getEdit_page_name() {
        return this.edit_page_name;
    }

    public void setEdit_page_name(String edit_page_name) {
        this.edit_page_name = edit_page_name;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String toString() {
        return "MenuItemActions{itemId=" + this.itemId + ", editable=" + this.editable + ", edit_page_name=" + this.edit_page_name + '}';
    }
}
