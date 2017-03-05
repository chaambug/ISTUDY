package com.mycompany.istudy.xml.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "studentType", propOrder = {
    "university"
})
@XmlRootElement
public class StudentType {

    @XmlElement(required = true)
    protected UniversityType university;
    @XmlAttribute(name = "matnr")
    protected String matnr;
    @XmlAttribute(name = "firstname")
    protected String firstname;
    @XmlAttribute(name = "lastname")
    protected String lastname;

    public UniversityType getUniversity() {
        return university;
    }

    public void setUniversity(UniversityType value) {
        this.university = value;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String value) {
        this.matnr = value;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String value) {
        this.firstname = value;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String value) {
        this.lastname = value;
    }
}
