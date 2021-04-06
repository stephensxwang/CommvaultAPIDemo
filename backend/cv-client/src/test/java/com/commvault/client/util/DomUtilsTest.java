package com.commvault.client.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class DomUtilsTest {
    private Document document;

    @Before
    public void setup() throws Exception{
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        String input = "<App_Test><empty/><content>abc</content></App_Test>";

        document = builder.parse(new InputSource(new StringReader(input)));
    }

    @Test
    public void testDocumentToString(){
        Assert.assertTrue(DomUtils.documentToString(document).contains("<App_Test><empty/><content>abc</content></App_Test>"));
    }

    @Test
    public void testRemoveEmptyNodes(){
        DomUtils.removeEmptyNodes(document);
        Assert.assertTrue(DomUtils.documentToString(document).contains("<App_Test><content>abc</content></App_Test>"));
    }

    @Test
    public void testSelectSingleNode(){
        Node property = DomUtils.selectSingleNode(document.getDocumentElement(), "content");
        Assert.assertEquals("abc", property.getTextContent());
    }
}
