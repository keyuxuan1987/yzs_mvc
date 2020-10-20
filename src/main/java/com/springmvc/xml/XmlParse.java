package com.springmvc.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @ClassName XmlParse
 * @Description: <p>xml解析类</p>
 * @Author Kelvin
 * @Date 2020/10/20
 * @Version V1.0
 **/

public class XmlParse {

    public static String getBasePackage(String xml) {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = XmlParse.class.getClassLoader().getResourceAsStream(xml);
        try {
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            Element componentScan = rootElement.element("component-san");
            Attribute attribute = componentScan.attribute("base-package");
            String basePackage = attribute.getText();
            return basePackage;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }
}
