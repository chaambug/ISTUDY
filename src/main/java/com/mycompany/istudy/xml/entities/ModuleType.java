package com.mycompany.istudy.xml.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "moduleType", propOrder = {
    "invested",
    "academicrecords"
})
public class ModuleType {

    @XmlElement(required = true)
    protected InvestedType invested;
    @XmlElement(required = true)
    protected AcademicrecordsType academicrecords;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "etcpostrings")
    protected String etcpostrings;
    @XmlAttribute(name = "studyhours")
    protected String studyhours;
    @XmlAttribute(name = "needStudyHoursPerWeek")
    protected String needStudyHoursPerWeek;
    @XmlAttribute(name = "studyWeeks")
    protected String studyWeeks;

   
    public InvestedType getInvested() {
        return invested;
    }

    public void setInvested(InvestedType value) {
        this.invested = value;
    }

    public AcademicrecordsType getAcademicrecords() {
        return academicrecords;
    }

    public void setAcademicrecords(AcademicrecordsType value) {
        this.academicrecords = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getEtcpostrings() {
        return etcpostrings;
    }

    public void setEtcpostrings(String value) {
        this.etcpostrings = value;
    }

    public String getStudyhours() {
        return studyhours;
    }

    public void setStudyhours(String value) {
        this.studyhours = value;
    }

    public String getNeedStudyHoursPerWeek() {
        return needStudyHoursPerWeek;
    }


    public void setNeedStudyHoursPerWeek(String value) {
        this.needStudyHoursPerWeek = value;
    }

    public String getStudyWeeks() {
        return studyWeeks;
    }

    public void setStudyWeeks(String studyWeeks) {
        this.studyWeeks = studyWeeks;
    }
}
