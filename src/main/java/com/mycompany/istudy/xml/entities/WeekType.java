package com.mycompany.istudy.xml.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "weekType", propOrder = {
    "value"
})
public class WeekType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "nrofweek")
    protected String nrofweek;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNrofweek() {
        return nrofweek;
    }

    public void setNrofweek(String value) {
        this.nrofweek = value;
    }
}
