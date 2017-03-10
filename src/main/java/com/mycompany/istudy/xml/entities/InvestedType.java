package com.mycompany.istudy.xml.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "investedType", propOrder = {
    "week"
})
public class InvestedType {

    protected List<WeekType> week;
    
    @XmlAttribute(name = "svgPath")
    protected String svgPath;
   
    public List<WeekType> getWeek() {
        if (week == null) {
            week = new ArrayList<>();
        }
        return this.week;
    }

    public void setWeek(List<WeekType> week) {
        this.week = week;
    }

    public String getSvgPath() {
        return svgPath;
    }

    public void setSvgPath(String svgPath) {
        this.svgPath = svgPath;
    }
}
