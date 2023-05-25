/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author crash
 */
@Entity
@Table(name = "smess_message")
@NamedQueries({
    @NamedQuery(name = "SmessMessage.findAll", query = "SELECT s FROM SmessMessage s")})
public class SmessMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "message_id")
    private Integer messageId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_people_to_send_to")
    private int numberOfPeopleToSendTo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_of_compose")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfCompose;
    @Column(name = "date_of_dispatch")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfDispatch;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "message_body")
    private String messageBody;
    @Size(max = 10)
    @Column(name = "status")
    private String status;
    @Column(name = "template")
    private Boolean template;
    @Column(name = "draft")
    private boolean draft;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name= "contact_group")
    private String contactGroup;
    @Column(name = "contact_group_value")
    private String contactGroupValue;

    public SmessMessage() {
    }

    public SmessMessage(Integer messageId) {
        this.messageId = messageId;
    }

    public SmessMessage(Integer messageId, int numberOfPeopleToSendTo, Date dateOfCompose, String title, String messageBody, boolean draft, boolean deleted
        , String contactGroup, String contactGroupValue) {
        this.messageId = messageId;
        this.numberOfPeopleToSendTo = numberOfPeopleToSendTo;
        this.dateOfCompose = dateOfCompose;
        this.title = title;
        this.messageBody = messageBody;
        this.draft = draft;
        this.deleted = deleted;
        this.contactGroup = contactGroup;
        this.contactGroupValue = contactGroupValue;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public int getNumberOfPeopleToSendTo() {
        return numberOfPeopleToSendTo;
    }

    public void setNumberOfPeopleToSendTo(int numberOfPeopleToSendTo) {
        this.numberOfPeopleToSendTo = numberOfPeopleToSendTo;
    }

    public Date getDateOfCompose() {
        return dateOfCompose;
    }

    public void setDateOfCompose(Date dateOfCompose) {
        this.dateOfCompose = dateOfCompose;
    }

    public Date getDateOfDispatch() {
        return dateOfDispatch;
    }

    public void setDateOfDispatch(Date dateOfDispatch) {
        this.dateOfDispatch = dateOfDispatch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getTemplate() {
        return template;
    }

    public void setTemplate(Boolean template) {
        this.template = template;
    }

    public boolean getDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(String contactGroup) {
        this.contactGroup = contactGroup;
    }

    public String getContactGroupValue() {
        return contactGroupValue;
    }

    public void setContactGroupValue(String contactGroupValue) {
        this.contactGroupValue = contactGroupValue;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageId != null ? messageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SmessMessage)) {
            return false;
        }
        SmessMessage other = (SmessMessage) object;
        if ((this.messageId == null && other.messageId != null) || (this.messageId != null && !this.messageId.equals(other.messageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.SmessMessage[ messageId=" + messageId + " ]";
    }
    
}
