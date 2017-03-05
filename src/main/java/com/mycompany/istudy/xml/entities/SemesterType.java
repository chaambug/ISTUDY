package com.mycompany.istudy.xml.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "semesterType", propOrder = {
    "modules"
})
public class SemesterType {

    @XmlElement(required = true)
    protected ModulesType modules;
    @XmlAttribute(name = "nr")
    protected String nr;
    @XmlAttribute(name = "start")
    protected String start;
    @XmlAttribute(name = "end")
    protected String end;

    public ModulesType getModules() {
        return modules;
    }

    public void setModules(ModulesType value) {
        this.modules = value;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String value) {
        this.nr = value;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String value) {
        this.start = value;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String value) {
        this.end = value;
    }

}
