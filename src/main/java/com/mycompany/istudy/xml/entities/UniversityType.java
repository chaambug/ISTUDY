package com.mycompany.istudy.xml.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "universityType", propOrder = {
    "semesters"
})
public class UniversityType {

    @XmlElement(required = true)
    protected SemestersType semesters;
    @XmlAttribute(name = "university")
    protected String university;
    @XmlAttribute(name = "faculty")
    protected String faculty;
    @XmlAttribute(name = "subjectstream")
    protected String subjectstream;

    public SemestersType getSemesters() {
        return semesters;
    }

    public void setSemesters(SemestersType value) {
        this.semesters = value;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String value) {
        this.university = value;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String value) {
        this.faculty = value;
    }

    public String getSubjectstream() {
        return subjectstream;
    }

    public void setSubjectstream(String value) {
        this.subjectstream = value;
    }
}
