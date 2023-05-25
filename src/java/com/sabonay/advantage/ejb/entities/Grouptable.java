/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sabonay.advantage.ejb.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author wahab
 */
@Entity
@Table(name = "grouptable")
@NamedQueries({
    @NamedQuery(name = "Grouptable.findAll", query = "SELECT g FROM Grouptable g"),
    @NamedQuery(name = "Grouptable.findByGroupid", query = "SELECT g FROM Grouptable g WHERE g.groupid = :groupid"),
    @NamedQuery(name = "Grouptable.findByGroupdesc", query = "SELECT g FROM Grouptable g WHERE g.groupdesc = :groupdesc")})
public class Grouptable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "groupid")
    private String groupid;
    @Column(name = "groupdesc")
    private String groupdesc;

    public Grouptable() {
    }

    public Grouptable(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupdesc() {
        return groupdesc;
    }

    public void setGroupdesc(String groupdesc) {
        this.groupdesc = groupdesc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupid != null ? groupid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grouptable)) {
            return false;
        }
        Grouptable other = (Grouptable) object;
        if ((this.groupid == null && other.groupid != null) || (this.groupid != null && !this.groupid.equals(other.groupid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.Grouptable[groupid=" + groupid + "]";
    }

}
