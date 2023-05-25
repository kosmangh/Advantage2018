/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author wahab
 */
@Embeddable
public class UsergrouptablePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "userid")
    private String userid;
    @Basic(optional = false)
    @Column(name = "groupid")
    private String groupid;

    public UsergrouptablePK() {
    }

    public UsergrouptablePK(String userid, String groupid) {
        this.userid = userid;
        this.groupid = groupid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        hash += (groupid != null ? groupid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsergrouptablePK)) {
            return false;
        }
        UsergrouptablePK other = (UsergrouptablePK) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        if ((this.groupid == null && other.groupid != null) || (this.groupid != null && !this.groupid.equals(other.groupid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.UsergrouptablePK[userid=" + userid + ", groupid=" + groupid + "]";
    }

}
