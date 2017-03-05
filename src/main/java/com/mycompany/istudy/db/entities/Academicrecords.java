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
@Table(name = "academicrecords", catalog = "istudy", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Academicrecords.findAll", query = "SELECT a FROM Academicrecords a")
    , @NamedQuery(name = "Academicrecords.findById", query = "SELECT a FROM Academicrecords a WHERE a.id = :id")
    , @NamedQuery(name = "Academicrecords.findAllRecords", query = "SELECT a FROM Academicrecords a WHERE a.studentid = :student AND a.moduleid = :module")    
    , @NamedQuery(name = "Academicrecords.findByGrade", query = "SELECT a FROM Academicrecords a WHERE a.grade = :grade")})
public class Academicrecords implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "examinationdate")
    private String examinationdate;
    @Basic(optional = false)
    @Column(name = "grade")
    private double grade;
    @JoinColumn(name = "studentid", referencedColumnName = "matrikelnummer")
    @ManyToOne(optional = false)
    private Student studentid;
    @JoinColumn(name = "moduleid", referencedColumnName = "modulid")
    @ManyToOne(optional = false)
    private Modul moduleid;

    public Academicrecords() {
        examinationdate = "";
    }

    public Academicrecords(Integer id) {
        this.id = id;
    }

    public Academicrecords(Integer id, String examinationdate, double grade) {
        this.id = id;
        this.examinationdate = examinationdate;
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExaminationdate() {
        return examinationdate;
    }

    public void setExaminationdate(String examinationdate) {
        this.examinationdate = examinationdate;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Student getStudentid() {
        return studentid;
    }

    public void setStudentid(Student studentid) {
        this.studentid = studentid;
    }

    public Modul getModuleid() {
        return moduleid;
    }

    public void setModuleid(Modul moduleid) {
        this.moduleid = moduleid;
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
        if (!(object instanceof Academicrecords)) {
            return false;
        }
        Academicrecords other = (Academicrecords) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.istudy.db.entities.Academicrecords[ id=" + id + " ]";
    }
    
}
