/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sabonay.advantage.common.utils;

import com.routesms.smshttpapi.DeliveryReport;
import com.routesms.smshttpapi.HTTPTextMessage;
import com.routesms.smshttpapi.MessageType;
import com.sabonay.advantage.ejb.entities.BindProperties;
import com.sabonay.advantage.web.utils.ds;
import com.sabonay.modules.web.jsf.utilities.JsfUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author crash
 */
@Named
@RequestScoped
public class PrepHttpMessage {

    private String server;
    private int port;
    private String username;
    private String password;
    private static final String BEAN_NAME = "prepHttpMessage";
    private HTTPTextMessage textMessage;
    private List<BindProperties> bindPropsList;
    private static final Logger LOG = Logger.getLogger(PrepHttpMessage.class.getName());
    private Integer clientId;
    private String senderId;

    public PrepHttpMessage() {
    }

    public void initialseMessageParameters() {
        try {
            bindPropsList = ds.getCommonQry().bindPropertiesGetAll();
            username = bindPropsList.get(0).getSmppUser();
            password = bindPropsList.get(0).getSmppPwd();
            port = bindPropsList.get(0).getSmppPort();
            server = bindPropsList.get(0).getSmppHost();
            textMessage = new HTTPTextMessage(server, port, username, password, senderId, MessageType.PLAIN_TEXT_ISO_ENCODING, DeliveryReport.DLR_REQUIRED);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    public static PrepHttpMessage getInstance() {
        try {
            return (PrepHttpMessage) JsfUtil.getManagedBean(BEAN_NAME);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getter And Setter">

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BindProperties> getBindPropsList() {
        return bindPropsList;
    }

    public void setBindPropsList(List<BindProperties> bindPropsList) {
        this.bindPropsList = bindPropsList;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public HTTPTextMessage getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(HTTPTextMessage textMessage) {
        this.textMessage = textMessage;
    }
//</editor-fold>

}
