package com.mycompany.istudy.xml.entities;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory {

    private final static QName _Student_QNAME = new QName("", "student");

    public ObjectFactory() {
    }

    public StudentType createStudentType() {
        return new StudentType();
    }

    public ModuleType createModuleType() {
        return new ModuleType();
    }

    public UniversityType createUniversityType() {
        return new UniversityType();
    }

    public ModulesType createModulesType() {
        return new ModulesType();
    }

    public InvestedType createInvestedType() {
        return new InvestedType();
    }

    public RecordType createRecordType() {
        return new RecordType();
    }

    public AcademicrecordsType createAcademicrecordsType() {
        return new AcademicrecordsType();
    }

    public WeekType createWeekType() {
        return new WeekType();
    }

    public SemestersType createSemestersType() {
        return new SemestersType();
    }

    public SemesterType createSemesterType() {
        return new SemesterType();
    }

    @XmlElementDecl(namespace = "", name = "student")
    public JAXBElement<StudentType> createStudent(StudentType value) {
        return new JAXBElement<>(_Student_QNAME, StudentType.class, null, value);
    }
}
