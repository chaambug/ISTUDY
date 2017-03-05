<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:date="http://exslt.org/dates-and-times" 
                extension-element-prefixes="date">

    <!-- Page layout information -->

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
                    <fo:block>
                        Created at : <xsl:value-of  select="date:date-time()"/>
                    </fo:block>
                    <xsl:apply-templates select="studentType"/>
                    <fo:block space-before="70pt">
                        <xsl:apply-templates select="studentType/university/semesters/semester[nr='1']/modules"/>
                    </fo:block>
                    <fo:block>
                        <xsl:apply-templates select="studentType/university/semesters/semester/modules/module/invested"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

   <xsl:template match="studentType/university/semesters/semester/modules/module/invested">
        <xsl:variable name="pathof">
            <xsl:value-of select="@svgPath"/>
        </xsl:variable>
        <fo:external-graphic src="url('{$pathof}')"  content-width="160mm"/>
        <xsl:text>&#x0A;</xsl:text>
        <xsl:text>&#x0A;</xsl:text>
    </xsl:template>
    
    <xsl:template match="studentType">
        <fo:table border-collapse="separate" table-layout="fixed" width="100%" display-align="center">
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt" border-right-style="solid"/>
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt" border-right-style="solid"/>
            <fo:table-body>
                <!-- header -->
                <fo:table-row background-color="#d5d5c3">
                     <fo:table-cell>
                         <fo:block text-align="left" font-weight="bold" font-size="14pt">
                            <xsl:text>Student details</xsl:text>
                        </fo:block>
                     </fo:table-cell>
                     <fo:table-cell>
                        <fo:block text-align= "left" font-weight="bold" font-size="14pt">
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
    
    <xsl:template match="studentType/university/semesters/semester[@nr='1']/modules">
        <fo:table border-collapse="collapse" table-layout="fixed" width="100%" display-align="center">
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt"
                             border-right-style="solid"/>
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt"
                             border-right-style="solid"/>
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt"
                             border-right-style="solid"/>
            <fo:table-column column-width="50%" border-right-color="white" border-right-width="2pt"
                             border-right-style="solid"/>
            <fo:table-body>
                <!-- header -->
                <fo:table-row background-color="##d5d5c3">
                    <fo:table-cell>
                        <fo:block text-align="center" font-weight="bold" font-size="14pt">
                            <xsl:text>Module</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="center" font-weight="bold" font-size="14pt">>
                            <xsl:text>Expected study hours</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="center" font-weight="bold" font-size="14pt">>
                            <xsl:text>ETCS points</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block text-align="center" font-weight="bold" font-size="14pt">>
                            <xsl:text>Expected Performance (Hrs/Week)</xsl:text>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>

                <!-- Content -->
                <fo:table-row background-color="#ffffff">
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="module/@name"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="module/@studyhours"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="module/@etcpostrings"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="11">
                            <xsl:value-of select="module/@needStudyHoursPerWeek"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    
</xsl:stylesheet>