/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.web.pagecontrollers;

import com.sabonay.advantage.common.utils.SettingsKey;
import com.sabonay.advantage.ejb.entities.Setting;
import com.sabonay.advantage.web.tablemodel.SettingTableModel;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.api.annotations.DataPanel;
import com.sabonay.modules.web.jsf.api.annotations.DataTableModelList;
import com.sabonay.modules.web.jsf.component.HtmlDataPanel;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Edwin
 */
@ManagedBean(name="settingsController")
@RequestScoped
public class SettingsController {


    private Setting setting = new Setting();

    private SettingTableModel settingTableModel = new SettingTableModel();

    @DataTableModelList(group="s")
    private List<Setting> settingList = new LinkedList<>();

    @DataPanel(group="s")
    private HtmlDataPanel<Setting> settingDataPanel;



    public SettingsController() 
    {

        settingDataPanel = settingTableModel.getDataPanel();

        settingDataPanel.setInitType(HtmlDataPanel.Init.NO_SEARCH_SELECTION);
        settingDataPanel.autoBindAndBuild(SettingsController.class,"s");


        String keys[] = SettingsKey.KEYS;

        for (int i = 0; i < keys.length; i++)
        {
            String settingsKey = keys[i];

//
            setting = ds.getCommonQry().settingFind(settingsKey);

            if(setting == null)
            {
                setting = new Setting();
                setting.setSettingsKey(settingsKey);
                ds.getCommonQry().settingUpdate(setting);

            }

            settingList.add(setting);
        }

        

    }





    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public Setting getSetting()
    {
        return setting;
    }


    public void setSetting(Setting setting)
    {
        this.setting = setting;
    }

    public List<Setting> getSettingList()
    {
        return settingList;
    }


    public void setSettingList(List<Setting> settingList)
    {
        this.settingList = settingList;
    }

    public HtmlDataPanel<Setting> getSettingDataPanel()
    {
        return settingDataPanel;
    }


    public void setSettingDataPanel(HtmlDataPanel<Setting> settingDataPanel)
    {
        this.settingDataPanel = settingDataPanel;
    }



    // </editor-fold>}


}
