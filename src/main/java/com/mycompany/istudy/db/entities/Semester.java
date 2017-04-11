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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Cham
 */
@Entity
@Table(name = "semester", catalog = "istudy", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Semester.findAll", query = "SELECT s FROM Semester s")
    , @NamedQuery(name = "Semester.findById", query = "SELECT s FROM Semester s WHERE s.id = :id")
    , @NamedQuery(name = "Semester.findActiveSemester", query = "SELECT s FROM Semester s WHERE s.semesterstatus = true")
    , @NamedQuery(name = "Semester.findActiveSemesterAndStudent", query = "SELECT s FROM Semester s WHERE s.semesterstatus = true AND s.userid = :student")
    , @NamedQuery(name = "Semester.findSemesterByStudent", query = "SELECT s FROM Semester s WHERE s.userid = :student")
    , @NamedQuery(name = "Semester.findByNumber", query = "SELECT s FROM Semester s WHERE s.number = :number")})
public class Semester implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semesternummer")
    private List<Modul> modulList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterid")
    private List<Investedhoursperweekformodule> investedhoursperweekformoduleList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "number")
    private int number;
    @Basic(optional = false)
    @Lob
    @Column(name = "semesterStart")
    private String semesterStart;
    @Basic(optional = false)
    @Lob
    @Column(name = "examinationStart")
    private String examinationStart;
    @Basic(optional = false)
    @Column(name = "semesterstatus")
    private boolean semesterstatus;
    @JoinColumn(name = "userid", referencedColumnName = "matrikelnummer")
    @ManyToOne(optional = false)
    private Student userid;

    public Semester() {
    }

    public Semester(Integer id) {
        this.id = id;
    }

    public Semester(Integer id, int number, String semesterStart, String examinationStart, boolean semesterstatus) {
        this.id = id;
        this.number = number;
        this.semesterStart = semesterStart;
        this.examinationStart = examinationStart;
        this.semesterstatus = semesterstatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSemesterStart() {
        return semesterStart;
    }

    public void setSemesterStart(String semesterStart) {
        this.semesterStart = semesterStart;
    }

    public String getExaminationStart() {
        return examinationStart;
    }

    public void setExaminationStart(String examinationStart) {
        this.examinationStart = examinationStart;
    }

    public boolean getSemesterstatus() {
        return semesterstatus;
    }

    public void setSemesterstatus(boolean semesterstatus) {
        this.semesterstatus = semesterstatus;
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
        if (!(object instanceof Semester)) {
            return false;
        }
        Semester other = (Semester) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.mycompany.istudy.db.entities.Semester[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Modul> getModulList() {
        return modulList;
    }

    public void setModulList(List<Modul> modulList) {
        this.modulList = modulList;
    }

    @XmlTransient
    public List<Investedhoursperweekformodule> getInvestedhoursperweekformoduleList() {
        return investedhoursperweekformoduleList;
    }

    public void setInvestedhoursperweekformoduleList(List<Investedhoursperweekformodule> investedhoursperweekformoduleList) {
        this.investedhoursperweekformoduleList = investedhoursperweekformoduleList;
    }
    
}
