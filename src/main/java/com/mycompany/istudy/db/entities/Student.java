/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Varuni
 */
@Entity
@Table(name = "student", catalog = "istudy", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s")
    , @NamedQuery(name = "Student.findByMatrikelnummer", query = "SELECT s FROM Student s WHERE s.matrikelnummer = :matrikelnummer")
    , @NamedQuery(name = "Student.findByVorname", query = "SELECT s FROM Student s WHERE s.vorname = :vorname")
    , @NamedQuery(name = "Student.findByNachname", query = "SELECT s FROM Student s WHERE s.nachname = :nachname")
    , @NamedQuery(name = "Student.findByEmailaddress", query = "SELECT s FROM Student s WHERE s.emailaddress = :emailaddress")
    , @NamedQuery(name = "Student.findByPasswort", query = "SELECT s FROM Student s WHERE s.passwort = :passwort")
    , @NamedQuery(name = "Student.findByBenutzername", query = "SELECT s FROM Student s WHERE s.benutzername = :benutzername")
    , @NamedQuery(name = "Student.findByStreetNr", query = "SELECT s FROM Student s WHERE s.streetNr = :streetNr")
    , @NamedQuery(name = "Student.findByCityAndZip", query = "SELECT s FROM Student s WHERE s.cityAndZip = :cityAndZip")
    , @NamedQuery(name = "Student.isValid", query = "SELECT s FROM Student s WHERE s.benutzername = :username AND s.passwort = :password")
    , @NamedQuery(name = "Student.findByGeburtsdatum", query = "SELECT s FROM Student s WHERE s.geburtsdatum = :geburtsdatum")})
public class Student implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentid")
    private List<Academicrecords> academicrecordsList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "matrikelnummer")
    private Integer matrikelnummer;
    @Basic(optional = false)
    @Column(name = "vorname")
    private String vorname;
    @Basic(optional = false)
    @Column(name = "nachname")
    private String nachname;
    @Basic(optional = false)
    @Column(name = "Emailaddress")
    private String emailaddress;
    @Basic(optional = false)
    @Column(name = "passwort")
    private String passwort;
    @Basic(optional = false)
    @Column(name = "benutzername")
    private String benutzername;
    @Column(name = "streetNr")
    private String streetNr;
    @Column(name = "cityAndZip")
    private String cityAndZip;
    @Lob
    @Column(name = "telefonnummer")
    private String telefonnummer;
    @Column(name = "geburtsdatum")
    @Temporal(TemporalType.DATE)
    private Date geburtsdatum;
    @Basic(optional = false)
    @Lob
    @Column(name = "cityOfUniversity")
    private String cityOfUniversity;
    @Basic(optional = false)
    @Lob
    @Column(name = "nameOfUni")
    private String nameOfUni;
    @Basic(optional = false)
    @Lob
    @Column(name = "nameOfFac")
    private String nameOfFac;
    @Basic(optional = false)
    @Lob
    @Column(name = "subjectStream")
    private String subjectStream;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Userloginentries> userloginentriesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matrikelnummer")
    private List<Modul> modulList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private List<Semester> semesterList;

    public Student() {
    }

    public Student(Integer matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public Student(Integer matrikelnummer, String vorname, String nachname, String emailaddress, String passwort, String benutzername, String cityOfUniversity, String nameOfUni, String nameOfFac, String subjectStream) {
        this.matrikelnummer = matrikelnummer;
        this.vorname = vorname;
        this.nachname = nachname;
        this.emailaddress = emailaddress;
        this.passwort = passwort;
        this.benutzername = benutzername;
        this.cityOfUniversity = cityOfUniversity;
        this.nameOfUni = nameOfUni;
        this.nameOfFac = nameOfFac;
        this.subjectStream = subjectStream;
    }

    public Integer getMatrikelnummer() {
        return matrikelnummer;
    }

    public void setMatrikelnummer(Integer matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getStreetNr() {
        return streetNr;
    }

    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    public String getCityAndZip() {
        return cityAndZip;
    }

    public void setCityAndZip(String cityAndZip) {
        this.cityAndZip = cityAndZip;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getCityOfUniversity() {
        return cityOfUniversity;
    }

    public void setCityOfUniversity(String cityOfUniversity) {
        this.cityOfUniversity = cityOfUniversity;
    }

    public String getNameOfUni() {
        return nameOfUni;
    }

    public void setNameOfUni(String nameOfUni) {
        this.nameOfUni = nameOfUni;
    }

    public String getNameOfFac() {
        return nameOfFac;
    }

    public void setNameOfFac(String nameOfFac) {
        this.nameOfFac = nameOfFac;
    }

    public String getSubjectStream() {
        return subjectStream;
    }

    public void setSubjectStream(String subjectStream) {
        this.subjectStream = subjectStream;
    }

    @XmlTransient
    public List<Userloginentries> getUserloginentriesList() {
        return userloginentriesList;
    }

    public void setUserloginentriesList(List<Userloginentries> userloginentriesList) {
        this.userloginentriesList = userloginentriesList;
    }

    @XmlTransient
    public List<Modul> getModulList() {
        return modulList;
    }

    public void setModulList(List<Modul> modulList) {
        this.modulList = modulList;
    }

    @XmlTransient
    public List<Semester> getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(List<Semester> semesterList) {
        this.semesterList = semesterList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matrikelnummer != null ? matrikelnummer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        return !((this.matrikelnummer == null && other.matrikelnummer != null) || (this.matrikelnummer != null && !this.matrikelnummer.equals(other.matrikelnummer)));
    }

    @Override
    public String toString() {
        return "com.mycompany.istudy.db.entities.Student[ matrikelnummer=" + matrikelnummer + " ]";
    }

    @XmlTransient
    public List<Academicrecords> getAcademicrecordsList() {
        return academicrecordsList;
    }

    public void setAcademicrecordsList(List<Academicrecords> academicrecordsList) {
        this.academicrecordsList = academicrecordsList;
    }
    
}
