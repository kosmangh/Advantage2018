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
@Table(name = "usertable")
@NamedQueries({
    @NamedQuery(name = "Usertable.findAll", query = "SELECT u FROM Usertable u"),
    @NamedQuery(name = "Usertable.findByUserid", query = "SELECT u FROM Usertable u WHERE u.userid = :userid"),
    @NamedQuery(name = "Usertable.findByUserpassword", query = "SELECT u FROM Usertable u WHERE u.userpassword = :userpassword")})
public class Usertable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "userid")
    private String userid;
    @Basic(optional = false)
    @Column(name = "userpassword")
    private String userpassword;

    public Usertable() {
    }

    public Usertable(String userid) {
        this.userid = userid;
    }

    public Usertable(String userid, String userpassword) {
        this.userid = userid;
        this.userpassword = userpassword;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usertable)) {
            return false;
        }
        Usertable other = (Usertable) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sabonay.advantage.ejb.entities.Usertable[userid=" + userid + "]";
    }

}
