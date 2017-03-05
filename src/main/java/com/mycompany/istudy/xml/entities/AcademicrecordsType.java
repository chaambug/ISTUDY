package com.mycompany.istudy.xml.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "academicrecordsType", propOrder = {
    "record"
})
public class AcademicrecordsType {

    protected List<RecordType> record;

    public List<RecordType> getRecord() {
        if (record == null) {
            record = new ArrayList<RecordType>();
        }
        return this.record;
    }

    public void setRecord(List<RecordType> record) {
        this.record = record;
    }
}
