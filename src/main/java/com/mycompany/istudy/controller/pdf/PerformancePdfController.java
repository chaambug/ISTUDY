/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller.pdf;

import com.mycompany.istudy.db.services.impl.StudentManager;
import com.mycompany.istudy.db.services.impl.AcademicrecordsManager;
import com.mycompany.istudy.db.services.impl.InvestedHoursPerWeekForModuleManager;
import com.mycompany.istudy.db.services.impl.IStudyDefaultJFreeSvgService;
import com.mycompany.istudy.db.services.impl.SemesterManager;
import com.mycompany.istudy.db.services.impl.ModulManager;
import com.mycompany.istudy.db.entities.*;
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.GuiServices;
import com.mycompany.istudy.principalservices.JaxBUtility;
import com.mycompany.istudy.xml.entities.*;
import java.io.BufferedWriter;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.util.MimeConstants;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Varuni
 */
public class PerformancePdfController extends IStudyPdfGenerator {

    private final static Logger logger = Logger.getLogger(PerformancePdfController.class);

    private String xmlFilePath;
    private String pdfFilePath;
    private String svgDirPath;
    private String fopXconfPath;
    private Student student;
    private List<Semester> allSemesterOfStudent;
    private Properties props;

    public PerformancePdfController() {
        initAttributes();
    }

    public void generatePDF(String outputDir, UserWin instance) {
        this.instance = instance;
        this.outputDir = outputDir;
        //start thread to generate pdf
        startGenerating(this);
    }

    @Override
    void generate() throws Exception {
        initAttributes();
        createXML();
        createPDF();
    }

    private void createXML() throws Exception {
        List<SemesterType> semesterList = createSemesterList();
        SemestersType semestersType = createSemestersType(semesterList);
        UniversityType universityType = createUniversityType(semestersType);
        StudentType studentType = createStudent(universityType);
        createLineCharts(studentType);
        //generate XML
        JaxBUtility.writeXml(xmlFilePath, studentType);
    }

    //1
    private StudentType createStudent(UniversityType universityType) {
        StudentType studentType = new StudentType();
        studentType.setFirstname(student.getVorname());
        studentType.setLastname(student.getNachname());
        studentType.setMatnr(String.valueOf(student.getMatrikelnummer()));
        studentType.setUniversity(universityType);
        return studentType;
    }

    //2
    private UniversityType createUniversityType(SemestersType semestersType) {
        UniversityType universityType = new UniversityType();
        universityType.setFaculty(student.getNameOfFac());
        universityType.setUniversity(student.getNameOfUni());
        universityType.setSubjectstream(student.getSubjectStream());
        universityType.setSemesters(semestersType);
        return universityType;
    }

    //3
    private SemestersType createSemestersType(List<SemesterType> semesterList) {
        SemestersType semestersType = new SemestersType();
        semestersType.setSemester(semesterList);
        return semestersType;
    }

    //4
    private List<SemesterType> createSemesterList() {
        List<SemesterType> result = new ArrayList<>();
        for (Semester semester : allSemesterOfStudent) {
            result.add(createSemesterType(semester));
        }
        return result;
    }

    //5
    private SemesterType createSemesterType(Semester semester) {
        SemesterType semesterType = new SemesterType();
        semesterType.setEnd(semester.getExaminationStart());
        semesterType.setStart(semester.getSemesterStart());
        semesterType.setNr(String.valueOf(semester.getNumber()));
        ModulesType modulesType = createModulesType(semester);
        semesterType.setModules(modulesType);
        return semesterType;
    }

    //6
    private ModulesType createModulesType(Semester semester) {
        ModulesType modulesType = new ModulesType();
        modulesType.setModule(createModuleTypeList(semester));
        return modulesType;
    }

    //7
    private List<ModuleType> createModuleTypeList(Semester semester) {
        List<ModuleType> result = new ArrayList<>();
        Iterator<Modul> iterator = ModulManager.getInstance().getAllModule(semester, student).iterator();
        while (iterator.hasNext()) {
            result.add(createModuleType(semester, iterator.next()));
        }
        return result;
    }

    //8
    private ModuleType createModuleType(Semester semester, Modul modul) {
        ModuleType moduleType = new ModuleType();
        moduleType.setEtcpostrings(String.valueOf(modul.getEctspunkte()));
        moduleType.setName(modul.getModulname());
        moduleType.setStudyhours(String.valueOf(modul.getStudyhours()));
        moduleType.setNeedStudyHoursPerWeek(getNeedStudyHoursPerWeek(semester, modul));
        moduleType.setStudyWeeks(String.valueOf(getKwForModule(semester)));
        moduleType.setInvested(createInvestedType(semester, modul));
        moduleType.setAcademicrecords(createAcademicrecordsType(modul));
        return moduleType;
    }

    //9
    private InvestedType createInvestedType(Semester semester, Modul modul) {
        InvestedType investedType = new InvestedType();
        investedType.setWeek(createWeekTypeList(semester, modul));
        return investedType;
    }

    //10
    private List<WeekType> createWeekTypeList(Semester semester, Modul modul) {
        List<WeekType> result = new ArrayList<>();
        List<Investedhoursperweekformodule> allEntriesForModule = InvestedHoursPerWeekForModuleManager.getInstance().getAllEntriesForModule(modul, semester);
        for (Investedhoursperweekformodule investedhoursperweekformodule : allEntriesForModule) {
            result.add(createWeekType(investedhoursperweekformodule));
        }
        return result;
    }

    //11
    private WeekType createWeekType(Investedhoursperweekformodule investedhoursperweekformodule) {
        WeekType weekType = new WeekType();
        weekType.setNrofweek(String.valueOf(investedhoursperweekformodule.getWeek()));
        weekType.setValue(String.valueOf(investedhoursperweekformodule.getInvestedHours()));
        return weekType;
    }

    //12
    private AcademicrecordsType createAcademicrecordsType(Modul modul) {
        AcademicrecordsType academicrecordsType = new AcademicrecordsType();
        academicrecordsType.setRecord(createRecordTypeList(modul));
        return academicrecordsType;
    }

    //13
    private List<RecordType> createRecordTypeList(Modul modul) {
        List<RecordType> result = new ArrayList<>();
        List<Academicrecords> allRecords = AcademicrecordsManager.getInstance().getAllRecords(student, modul);
        int i = 1;
        for (Academicrecords academicrecords : allRecords) {
            result.add(createRecordType(academicrecords, i));
            i++;
        }
        return result;
    }

    //14
    private RecordType createRecordType(Academicrecords academicrecords, int tryNo) {
        RecordType recordType = new RecordType();
        recordType.setDate(academicrecords.getExaminationdate());
        recordType.setNr(String.valueOf(tryNo));
        recordType.setValue(String.valueOf(academicrecords.getGrade()));
        return recordType;
    }

    private void createPDF() throws Exception {
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File(xmlFilePath));
        // create an instance of fop factory

        ImageContext fopFactory = (FopFactory) FopFactory.newInstance(new File(fopXconfPath));

        // a user agent is needed for transformation
        FOUserAgent foUserAgent = ((FopFactory) fopFactory).newFOUserAgent();
        // Setup output
        OutputStream out;

        out = new java.io.FileOutputStream(pdfFilePath);
        try {
            // Construct fop with desired output format
            Fop fop = ((FopFactory) fopFactory).newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();

            String xslFile = props.getProperty("xslFile");
            InputStream is = getClass().getClassLoader().getResourceAsStream(xslFile);
            Transformer transformer = factory.newTransformer(new StreamSource(is));
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }

    private String getNeedStudyHoursPerWeek(Semester semester, Modul modul) {
        try {
            final long kw = getKwForModule(semester);
            return String.valueOf(modul.getStudyhours() / kw);
        } catch (Exception e) {
            logger.error("System error", e);
        }
        return "";
    }

    private long getKwForModule(Semester semester) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date s = sdf.parse(semester.getSemesterStart());
            Date e = sdf.parse(semester.getExaminationStart());
            return GuiServices.getCalendarWeeks(s, e);
        } catch (Exception e) {
            logger.error("System error", e);
        }
        return 0;
    }

    private void initAttributes() {
        student = StudentManager.getInstance().getStudent();
        allSemesterOfStudent = SemesterManager.getInstance().getAllSemesterOfStudent(student);

        final String pattern = "yyyyMMdd_hhmm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            final String propertyName = "config.properties";
            final File currentDirPath = new File(System.getProperty("user.dir"));

            props = new Properties();
            InputStream is = classLoader.getResourceAsStream(propertyName);
            props.load(is);

            String inputDirValue = (String) props.get("inputDir");
            String chartsDirValue = (String) props.get("chartsDir");
            String xmlDirValue = (String) props.get("xmlDir");
            String xmlFileValue = (String) props.get("xmlFile");
            String pdfOutputValue = (String) props.get("pdfOutput");

            File input = new File(currentDirPath + inputDirValue);
            if (!input.exists()) {
                input.mkdirs();
            }

            File chartsDirFile = new File(currentDirPath + chartsDirValue);
            if (!chartsDirFile.exists()) {
                chartsDirFile.mkdirs();
            }

            File pdfOutputDirFile = new File(currentDirPath + pdfOutputValue);
            if (!pdfOutputDirFile.exists()) {
                pdfOutputDirFile.mkdirs();
            }

            File xmlDirFile = new File(currentDirPath + xmlDirValue);
            if (!xmlDirFile.exists()) {
                xmlDirFile.mkdirs();
            }

            File xmlFile = new File(currentDirPath + xmlFileValue);
            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
            }

            //copy fopXconf to working dir
            final String fopConfFileName = "fop.xconf";
            File dest = new File(currentDirPath + File.separator + fopConfFileName);
            if (!dest.exists()) {
                final String content = getFile(fopConfFileName);
                FileOutputStream fileOutputStream = new FileOutputStream(dest.getAbsolutePath());
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
                Writer writer = new BufferedWriter(outputStreamWriter);
                writer.write(content);
            }
            fopXconfPath = dest.getAbsolutePath();

            xmlFilePath = xmlFile.getAbsolutePath();
            svgDirPath = chartsDirFile.getAbsolutePath();
            pdfFilePath = outputDir == null
                    ? String.format("%s%sresult.pdf", pdfOutputDirFile.getAbsolutePath(), File.separator)
                    : String.format("%s%sgenerated_performance_%s.pdf",
                            outputDir,
                            File.separator,
                            sdf.format(new Date()));

        } catch (Exception e) {
            logger.error("System error", e);
        }
    }

    private String getFile(String fileName) throws Exception {
        InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
        StringWriter writer = new StringWriter();
        IOUtils.copy(in, writer, "utf-8");
        return writer.toString();
    }

    private void createLineCharts(StudentType studentType) {
        try {
            List<SemesterType> semester = studentType
                    .getUniversity()
                    .getSemesters()
                    .getSemester();

            for (SemesterType sem : semester) {
                for (ModuleType module : sem.getModules().getModule()) {
                    String generatedChartCsvFilePath = generateSvg(module);
                    module.getInvested().setSvgPath(generatedChartCsvFilePath);
                }
            }
        } catch (Exception e) {
            logger.error("System error", e);
        }
    }

    private String generateSvg(ModuleType module) {
        /**
         * 1) module name 2) current time
         */
        String svgChartFileNamePattern
                = File.separator + "%s_%s_autoGenerated.png";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String currTime = sdf.format(new Date());

        IStudyDefaultJFreeSvgService iStudyDefaultJFreeSvgService
                = new IStudyDefaultJFreeSvgService();

        Map<String, String> invested = new HashMap<>();
        invested.put("0", "0");
        double actual = 0;
        for (WeekType week : module.getInvested().getWeek()) {
            double weekHour = Double.valueOf(week.getValue());
            actual += weekHour;
            invested.put(week.getNrofweek(), String.valueOf(actual));
        }

        Map<String, String> expected = new HashMap<>();
        double hourPerWeek = Double.valueOf(module.getNeedStudyHoursPerWeek());
        expected.put("0", "0");
        for (int i = 1; i <= Integer.valueOf(module.getStudyWeeks()); i++) {
            expected.put(String.valueOf(i), String.valueOf(hourPerWeek * i));
        }
        final String fileName = String.format(svgChartFileNamePattern,
                module.getName().replaceAll("\\s+", ""),
                currTime);
        final String path = svgDirPath + fileName;
        iStudyDefaultJFreeSvgService.create2DLineChart(
                path,
                module.getName(),
                "Weeks",
                "Hours",
                invested,
                expected);

        String result = (String) props.get("chartsDirXml") + fileName;
        return result.replace("\\", "");
    }
}
