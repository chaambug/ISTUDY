/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.principalservices;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * JaxBUtility for reading and writing xml files.
 * @author Cham
 */
public class JaxBUtility {
    
    /**
     * Creates a xml file
     * @param inXmlPath path for xml file, that is to create
     * @param inObject the xml object
     * @throws JAXBException
     */
    public static void writeXml(String inXmlPath, Object inObject) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(inObject.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(inObject, new File(inXmlPath));
        //jaxbMarshaller.marshal(inObject, System.out);
    }


    /**
     * Reads the XML-File
     * @param inXmlPath path to the XML-File
     * @param theClass
     * @return the unmarshaled xml root object
     * @throws JAXBException
     */
    public static Object readXML(String inXmlPath, Class<?> theClass) throws JAXBException {
        File file = new File(inXmlPath);
        JAXBContext jaxbContext = JAXBContext.newInstance(theClass);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(file);
    }
}
