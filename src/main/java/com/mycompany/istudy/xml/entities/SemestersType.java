package com.mycompany.istudy.xml.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "semestersType", propOrder = {
    "semester"
})
public class SemestersType {

    protected List<SemesterType> semester;

    public List<SemesterType> getSemester() {
        if (semester == null) {
            semester = new ArrayList<>();
        }
        return this.semester;
    }

    public void setSemester(List<SemesterType> semester) {
        this.semester = semester;
    }
}
