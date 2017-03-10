/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Varuni
 */
@Entity
@Table(name = "userloginentries", catalog = "istudy", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Userloginentries.findAll", query = "SELECT u FROM Userloginentries u")
    , @NamedQuery(name = "Userloginentries.findByStudent", query = "SELECT u FROM Userloginentries u WHERE u.userid = :student")
    , @NamedQuery(name = "Userloginentries.findById", query = "SELECT u FROM Userloginentries u WHERE u.id = :id")})
public class Userloginentries implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Lob
    @Column(name = "logindate")
    private String logindate;
    @JoinColumn(name = "userid", referencedColumnName = "matrikelnummer")
    @ManyToOne(optional = false)
    private Student userid;

    public Userloginentries() {
    }

    public Userloginentries(Long id) {
        this.id = id;
    }

    public Userloginentries(Long id, String logindate) {
        this.id = id;
        this.logindate = logindate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public Student getUserid() {
        return userid;
    }

    public void setUserid(Student userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userloginentries)) {
            return false;
        }
        Userloginentries other = (Userloginentries) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.mycompany.istudy.db.entities.Userloginentries[ id=" + id + " ]";
    }
    
}
