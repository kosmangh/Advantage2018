/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author wahab
 */
@Entity
@Table(name = "usergrouptable")
@NamedQueries({
    @NamedQuery(name = "Usergrouptable.findAll", query = "SELECT u FROM Usergrouptable u"),
    @NamedQuery(name = "Usergrouptable.findByUserid", query = "SELECT u FROM Usergrouptable u WHERE u.usergrouptablePK.userid = :userid"),
    @NamedQuery(name = "Usergrouptable.findByGroupid", query = "SELECT u FROM Usergrouptable u WHERE u.usergrouptablePK.groupid = :groupid")})
public class Usergrouptable implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsergrouptablePK usergrouptablePK;

    public Usergrouptable() {
    }

    public Usergrouptable(UsergrouptablePK usergrouptablePK) {
        this.usergrouptablePK = usergrouptablePK;
    }

    public Usergrouptable(String userid, String groupid) {
        this.usergrouptablePK = new UsergrouptablePK(userid, groupid);
    }

    public UsergrouptablePK getUsergrouptablePK() {
        return usergrouptablePK;
    }

    public void setUsergrouptablePK(UsergrouptablePK usergrouptablePK) {
        this.usergrouptablePK = usergrouptablePK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usergrouptablePK != null ? usergrouptablePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usergrouptable)) {
            return false;
        }
        Usergrouptable other = (Usergrouptable) object;
        if ((this.usergrouptablePK == null && other.usergrouptablePK != null) || (this.usergrouptablePK != null && !this.usergrouptablePK.equals(other.usergrouptablePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.Usergrouptable[usergrouptablePK=" + usergrouptablePK + "]";
    }

}
