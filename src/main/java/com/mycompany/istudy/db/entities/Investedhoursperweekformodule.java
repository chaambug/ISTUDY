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
@Table(name = "investedhoursperweekformodule", catalog = "istudy", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Investedhoursperweekformodule.findAll", query = "SELECT i FROM Investedhoursperweekformodule i")
    , @NamedQuery(name = "Investedhoursperweekformodule.findById", query = "SELECT i FROM Investedhoursperweekformodule i WHERE i.id = :id")
    , @NamedQuery(name = "Investedhoursperweekformodule.findAllByModuleAndSemester", query = "SELECT i FROM Investedhoursperweekformodule i WHERE i.moduleId = :module AND i.semesterid = :semester")
    , @NamedQuery(name = "Investedhoursperweekformodule.findByModuleAndWeekAndSemester", query = "SELECT i FROM Investedhoursperweekformodule i WHERE i.moduleId = :module AND i.week = :week AND i.semesterid = :semester")
    , @NamedQuery(name = "Investedhoursperweekformodule.findByWeek", query = "SELECT i FROM Investedhoursperweekformodule i WHERE i.week = :week")
    , @NamedQuery(name = "Investedhoursperweekformodule.findByInvestedHours", query = "SELECT i FROM Investedhoursperweekformodule i WHERE i.investedHours = :investedHours")})
public class Investedhoursperweekformodule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "week")
    private int week;
    @Basic(optional = false)
    @Column(name = "investedHours")
    private double investedHours;
    @JoinColumn(name = "moduleId", referencedColumnName = "modulid")
    @ManyToOne(optional = false)
    private Modul moduleId;
    @JoinColumn(name = "semesterid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semesterid;

    public Investedhoursperweekformodule() {
    }

    public Investedhoursperweekformodule(Integer id) {
        this.id = id;
    }

    public Investedhoursperweekformodule(Integer id, int week, double investedHours) {
        this.id = id;
        this.week = week;
        this.investedHours = investedHours;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public double getInvestedHours() {
        return investedHours;
    }

    public void setInvestedHours(double investedHours) {
        this.investedHours = investedHours;
    }

    public Modul getModuleId() {
        return moduleId;
    }

    public void setModuleId(Modul moduleId) {
        this.moduleId = moduleId;
    }

    public Semester getSemesterid() {
        return semesterid;
    }

    public void setSemesterid(Semester semesterid) {
        this.semesterid = semesterid;
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
        if (!(object instanceof Investedhoursperweekformodule)) {
            return false;
        }
        Investedhoursperweekformodule other = (Investedhoursperweekformodule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.istudy.db.entities.Investedhoursperweekformodule[ id=" + id + " ]";
    }
    
}
