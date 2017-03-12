/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Varuni
 */
@Entity
@Table(name = "modul", catalog = "istudy", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modul.findAll", query = "SELECT m FROM Modul m")
    , @NamedQuery(name = "Modul.findByModulid", query = "SELECT m FROM Modul m WHERE m.modulid = :modulid")
    , @NamedQuery(name = "Modul.findByModulname", query = "SELECT m FROM Modul m WHERE m.modulname = :modulname")
    , @NamedQuery(name = "Modul.findAllModulesByStudentAndSemester", query = "SELECT m FROM Modul m WHERE m.semesternummer = :semester AND m.matrikelnummer = :student")
    , @NamedQuery(name = "Modul.findByEctspunkte", query = "SELECT m FROM Modul m WHERE m.ectspunkte = :ectspunkte")
    , @NamedQuery(name = "Modul.findByStatus", query = "SELECT m FROM Modul m WHERE m.modulestatus = :value")
    , @NamedQuery(name = "Modul.findByStudent", query = "SELECT m FROM Modul m WHERE m.matrikelnummer = :student")
    , @NamedQuery(name = "Modul.findByStatusAndStudent", query = "SELECT m FROM Modul m WHERE m.modulestatus = :value AND m.matrikelnummer = :student")
    , @NamedQuery(name = "Modul.findByStudyhours", query = "SELECT m FROM Modul m WHERE m.studyhours = :studyhours")})
public class Modul implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "moduleid")
    private List<Academicrecords> academicrecordsList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "moduleId")
    private List<Investedhoursperweekformodule> investedhoursperweekformoduleList;

    @Basic(optional = false)
    @Column(name = "modulestatus")
    private boolean modulestatus;
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "modulid")
    private Integer modulid;
    @Basic(optional = false)
    @Column(name = "modulname")
    private String modulname;
    @Basic(optional = false)
    @Column(name = "ectspunkte")
    private int ectspunkte;
    @Basic(optional = false)
    @Column(name = "studyhours")
    private double studyhours;
    @JoinColumn(name = "matrikelnummer", referencedColumnName = "matrikelnummer")
    @ManyToOne(optional = false)
    private Student matrikelnummer;
    @JoinColumn(name = "semesternummer", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semesternummer;

    public Modul() {
    }

    public Modul(Integer modulid) {
        this.modulid = modulid;
    }

    public Modul(Integer modulid, String modulname, int ectspunkte, double studyhours, boolean modulestatus) {
        this.modulid = modulid;
        this.modulname = modulname;
        this.ectspunkte = ectspunkte;
        this.studyhours = studyhours;
        this.modulestatus = modulestatus;
                
    }

    public Integer getModulid() {
        return modulid;
    }

    public void setModulid(Integer modulid) {
        this.modulid = modulid;
    }

    public String getModulname() {
        return modulname;
    }

    public void setModulname(String modulname) {
        this.modulname = modulname;
    }

    public int getEctspunkte() {
        return ectspunkte;
    }

    public void setEctspunkte(int ectspunkte) {
        this.ectspunkte = ectspunkte;
    }

    public double getStudyhours() {
        return studyhours;
    }

    public void setStudyhours(double studyhours) {
        this.studyhours = studyhours;
    }

    public Student getMatrikelnummer() {
        return matrikelnummer;
    }

    public void setMatrikelnummer(Student matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public Semester getSemesternummer() {
        return semesternummer;
    }

    public void setSemesternummer(Semester semesternummer) {
        this.semesternummer = semesternummer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modulid != null ? modulid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modul)) {
            return false;
        }
        Modul other = (Modul) object;
        return !((this.modulid == null && other.modulid != null) || (this.modulid != null && !this.modulid.equals(other.modulid)));
    }

    @Override
    public String toString() {
        return "com.mycompany.istudy.db.entities.Modul[ modulid=" + modulid + " ]";
    }

    public boolean getModulestatus() {
        return modulestatus;
    }

    public void setModulestatus(boolean modulestatus) {
        this.modulestatus = modulestatus;
    }

    @XmlTransient
    public List<Investedhoursperweekformodule> getInvestedhoursperweekformoduleList() {
        return investedhoursperweekformoduleList;
    }

    public void setInvestedhoursperweekformoduleList(List<Investedhoursperweekformodule> investedhoursperweekformoduleList) {
        this.investedhoursperweekformoduleList = investedhoursperweekformoduleList;
    }

    @XmlTransient
    public List<Academicrecords> getAcademicrecordsList() {
        return academicrecordsList;
    }

    public void setAcademicrecordsList(List<Academicrecords> academicrecordsList) {
        this.academicrecordsList = academicrecordsList;
    }
    
}
