/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller.pdf;

import com.mycompany.istudy.controller.HomeController;
import com.mycompany.istudy.db.services.impl.StudentManager;
import com.mycompany.istudy.db.services.impl.AcademicrecordsManager;
import com.mycompany.istudy.db.services.impl.InvestedHoursPerWeekForModuleManager;
import com.mycompany.istudy.principalservices.IStudyDefaultJFreeService;
import com.mycompany.istudy.db.services.impl.SemesterManager;
import com.mycompany.istudy.db.services.impl.ModulManager;
import com.mycompany.istudy.db.entities.*;
import com.mycompany.istudy.gui.UserWin;
import com.mycompany.istudy.principalservices.GuiServices;
import com.mycompany.istudy.principalservices.IstudyProperties;
import com.mycompany.istudy.principalservices.JaxBUtility;
import com.mycompany.istudy.xml.entities.*;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Varuni
 */
public class PerformancePdfController extends IStudyPdfGenerator {

    private final static Logger LOGGER = Logger.getLogger(PerformancePdfController.class);

    private String xmlFilePath;
    private String pdfFilePath;
    private String chartsDirPath;
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
        HomeController homeController = new HomeController(null);
        List<String> info = homeController.getInfo();
        universityType.setActiveModules(info.get(0));
        universityType.setActiveSemester(info.get(2));
        universityType.setAverage(info.get(3));
        universityType.setCpSaved(info.get(4));
        universityType.setCpToAchieve(info.get(5));
        universityType.setFailedExams(info.get(6));
        universityType.setPassedExames(info.get(7));
        return universityType;
    }

    //3
    private SemestersType createSemestersType(List<SemesterType> semesterList) {
        SemestersType semestersType = new SemestersType();
        semestersType.getSemester().addAll(semesterList);
        return semestersType;
    }

    //4
    private List<SemesterType> createSemesterList() {
        List<SemesterType> result = new ArrayList<>();
        allSemesterOfStudent.stream().forEach((semester) -> {
            result.add(createSemesterType(semester));
        });
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
        modulesType.getModule().addAll(createModuleTypeList(semester));
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
        moduleType.setAttempt(String.valueOf(AcademicrecordsManager.getInstance().getAcademicrecordGrad5(student, modul).size() + 1));
        moduleType.setAcademicrecords(createAcademicrecordsType(modul));
        return moduleType;
    }

    //9
    private InvestedType createInvestedType(Semester semester, Modul modul) {
        InvestedType investedType = new InvestedType();
        investedType.getWeek().addAll(createWeekTypeList(semester, modul));
        return investedType;
    }

    //10
    private List<WeekType> createWeekTypeList(Semester semester, Modul modul) {
        List<WeekType> result = new ArrayList<>();
        List<Investedhoursperweekformodule> allEntriesForModule = InvestedHoursPerWeekForModuleManager.getInstance().getAllEntriesForModule(modul, semester);
        allEntriesForModule.stream().forEach((investedhoursperweekformodule) -> {
            result.add(createWeekType(investedhoursperweekformodule));
        });
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
        academicrecordsType.getRecord().addAll(createRecordTypeList(modul));
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
            LOGGER.error("System error", e);
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
            LOGGER.error("System error", e);
        }
        return 0;
    }

    /**
     * 
     * All paths are loaded from properties and configured for generating process
     */
    private void initAttributes() {
        student = StudentManager.getInstance().getStudent();
        allSemesterOfStudent = SemesterManager.getInstance().getAllSemesterOfStudent(student);

        final String pattern = "yyyyMMdd_hhmm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            final File currentDirPath = new File(System.getProperty("user.dir"));

            IstudyProperties istudyProperties = new IstudyProperties();
            props = istudyProperties.getConfig();

            String inputDirValue = props.getProperty(istudyProperties.INPUT_DIR);
            String chartsDirValue = props.getProperty(istudyProperties.CHARTS_DIR);
            String xmlDirValue = props.getProperty(istudyProperties.XML_DIR);
            String xmlFileValue = props.getProperty(istudyProperties.XML_FILE);
            String pdfOutputValue = props.getProperty(istudyProperties.PDF_OUTPUT_DIR);

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
                FileUtils.writeByteArrayToFile(dest, content.getBytes());
            }
            fopXconfPath = dest.getAbsolutePath();

            xmlFilePath = xmlFile.getAbsolutePath();
            chartsDirPath = chartsDirFile.getAbsolutePath();
            pdfFilePath = outputDir == null
                    ? String.format("%s%sresult.pdf", pdfOutputDirFile.getAbsolutePath(), File.separator)
                    : String.format("%s%sgenerated_performance_%s.pdf",
                            outputDir,
                            File.separator,
                            sdf.format(new Date()));

        } catch (Exception e) {
            LOGGER.error("System error", e);
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

            semester.stream().forEach((SemesterType sem) -> {
                sem.getModules().getModule().stream().forEach((module) -> {
                    String generatedChartFilePath = generateChart(module);
                    module.getInvested().setChartPath(generatedChartFilePath);
                });
            });
        } catch (Exception e) {
            LOGGER.error("System error", e);
        }
    }

    private String generateChart(ModuleType module) {
        /**
         * 1) module name 2) current time
         */
        String svgChartFileNamePattern
                = File.separator + "%s_%s_autoGenerated.png";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String currTime = sdf.format(new Date());

        IStudyDefaultJFreeService iStudyDefaultJFreeSvgService
                = new IStudyDefaultJFreeService();

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
        final String path = chartsDirPath + fileName;
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
