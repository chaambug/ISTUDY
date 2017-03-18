<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE xsl:stylesheet [
  <!ENTITY newline "&#xa;">
]>
<xsl:stylesheet version="2.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:date="http://exslt.org/dates-and-times" 
                extension-element-prefixes="date">

    <!-- performance pdf document -->
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

            <fo:layout-master-set>
                <fo:simple-page-master master-name="main"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       font-family="sans-serif"
                                       margin-top="0cm"
                                       margin-bottom="0cm"
                                       margin-left="2cm"
                                       margin-right="2cm">
                    <fo:region-body margin-top="2.0cm" margin-bottom="1cm"/>
                    <fo:region-before extent="1.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="main">
                <fo:flow flow-name="xsl-region-body">
                    <!-- generated time - head of document -->
                    <fo:block>
                        Created at : <xsl:value-of  select="date:date-time()"/>
                    </fo:block>
                    <!-- specific istudy information header -->
                    <xsl:apply-templates select="studentType"/>
                    <!-- common information -->
                    <xsl:apply-templates select="studentType/university"/>
                    <!-- semester and module information -->
                    <xsl:apply-templates select="studentType/university/semesters"/>
                    <!-- performance graphics at end of pdf -->
                    <fo:block>
                        <xsl:apply-templates select="studentType/university/semesters/semester/modules/module/invested"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <!-- header -->
    <xsl:template match="studentType">
        <fo:table border-collapse="separate" table-layout="fixed" width="100%" display-align="center">
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt" border-right-style="solid"/>
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt" border-right-style="solid"/>
            <fo:table-body>
                <!-- header -->
                <fo:table-row background-color="#d5d5c3">
                    <fo:table-cell>
                        <fo:block text-align="left" font-weight="bold" font-size="12pt">
                            <xsl:text>Student details</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align= "left" font-weight="bold" font-size="12pt">
                            <xsl:text>University details</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!-- Content -->
                <fo:table-row background-color="#ffffff">
                    <fo:table-cell>
                        <fo:block font-size="11" >
                            <xsl:value-of select="@firstname"/>
                            <xsl:text>&#xA0;</xsl:text>
                            <xsl:value-of select="@lastname"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="university/@university"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row background-color="#ffffff">
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="@matnr"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="university/@faculty"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row background-color="#ffffff">
                    <fo:table-cell>
                        <fo:block>
                            
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="university/@subjectstream"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>

    <!-- common information -->
    <xsl:template match="studentType/university">
        <fo:block linefeed-treatment="preserve">
            <xsl:text>&#xA;</xsl:text>
            <xsl:text>&#xA;</xsl:text>
        </fo:block>
        <fo:table border-collapse="separate" table-layout="fixed" width="100%" display-align="center">
            <fo:table-column column-width="50%" border-right-style="none"/>
            <fo:table-column column-width="50%" border-right-style="none"/>
            <fo:table-body>
                <!-- header -->
                <fo:table-row background-color="#d5d5c3">
                    <fo:table-cell>
                        <fo:block text-align="left" font-weight="bold" font-size="12pt">
                            <xsl:text>Common information</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align= "left" font-weight="bold" font-size="12pt">
                            <xsl:text></xsl:text>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:text>Average</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:value-of select="average"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:text>Active semester</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:value-of select="activeSemester"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:text>Active modules</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:value-of select="activeModules"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:text>Saved credit points</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:value-of select="cpSaved"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:text>Credit points to be achieved</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:value-of select="cpToAchieve"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:text>Failed exams</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:value-of select="failedExams"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:text>Passed exams</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="left" font-size="11">
                            <xsl:value-of select="passedExames"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>

    <!-- semester and module information -->
    <xsl:template match="studentType/university/semesters">
        <fo:block linefeed-treatment="preserve">
            <xsl:text>&#xA;</xsl:text>
            <xsl:text>&#xA;</xsl:text>
        </fo:block>
        <xsl:for-each select="semester">
            <fo:table border-collapse="separate" table-layout="fixed" width="100%" display-align="center">
                <fo:table-column column-width="40%" border-right-style="none"/>
                <fo:table-column column-width="20%" border-right-style="none"/>
                <fo:table-column column-width="20%" border-right-style="none"/>
                <fo:table-column column-width="20%" border-right-style="none"/>
                <fo:table-body>
                    <!-- header -->
                    <fo:table-row background-color="#d5d5c3">
                        <fo:table-cell>
                            <fo:block text-align="left" font-weight="bold" font-size="12pt">
                                <xsl:text>Module&#160;</xsl:text>
                                <xsl:text>(Semester </xsl:text>
                                <xsl:value-of select="@nr"/>
                                <xsl:text>)</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align= "left" font-weight="bold" font-size="12pt">
                                <xsl:text>Creditpoints</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align= "left" font-weight="bold" font-size="12pt">
                                <xsl:text>Grade</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align= "left" font-weight="bold" font-size="12pt">
                                <xsl:text>Attempt</xsl:text>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <xsl:for-each select="modules/module">
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="11">
                                    <xsl:value-of select="@name"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="11">
                                    <xsl:value-of select="@etcpostrings"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="11">
                                    <xsl:for-each select="academicrecords/record">
                                        <xsl:if test="not(. = 0.0) and not(. = 5.0)">
                                            <xsl:value-of select="."/>
                                        </xsl:if>
                                    </xsl:for-each>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align="left" font-size="11">
                                    <xsl:value-of select="@attempt"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </xsl:for-each>
                </fo:table-body>
            </fo:table>
        </xsl:for-each>
    </xsl:template>

    <!-- performance graphics at end of pdf-->
    <xsl:template match="studentType/university/semesters/semester/modules/module/invested">
        <xsl:variable name="pathof">
            <xsl:value-of select="@chartPath"/>
        </xsl:variable>
        <fo:external-graphic src="url('{$pathof}')"  content-width="160mm"/>
        <xsl:text>&#x0A;</xsl:text>
        <xsl:text>&#x0A;</xsl:text>
    </xsl:template>
</xsl:stylesheet>