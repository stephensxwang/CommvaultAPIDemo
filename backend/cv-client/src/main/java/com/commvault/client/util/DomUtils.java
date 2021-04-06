package com.commvault.client.util;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomUtils {
	
	private static Logger logger = LoggerFactory.getLogger(DomUtils.class);

	public static String documentToString(Document doc) {		
		logger.debug("Transform document to string, source: {}", doc);
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer tf;
		try {
			tf = TransformerFactory.newInstance().newTransformer();
			tf.transform(domSource, result);
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			logger.error("Fail to init the Document Transformer.", e);
		} catch (TransformerException e) {
			logger.error("Fail to transform the xml object to string.", e);
		}
		
		return writer.toString();
	}
	
	public static Node selectSingleNode(Element src, String tagName) {
		logger.debug("Get a node from {} with xpath {}", src, tagName);
		Node result = null;
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			result = (Node) xpath.evaluate(tagName, src, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			logger.error("Invalid xpath.", e);
		}
		
		return result;
	}
	
	public static void removeEmptyNodes(Node node) {
		logger.debug("Remove empty nodes, source: {}", node);
		NodeList list = node.getChildNodes();
		List<Node> nodesToCheck = new LinkedList<>();
		
		for(int i = 0; i < list.getLength(); i++) {
			nodesToCheck.add(list.item(i));
		}
		
		for(Node tempNode : nodesToCheck) {
			removeEmptyNodes(tempNode);
		}
		boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
		
		boolean emptyText = node.getNodeType() == Node.TEXT_NODE && StringUtils.isBlank(node.getNodeValue());
		
		if(emptyElement || emptyText) {
			if(!node.hasAttributes()) {
				node.getParentNode().removeChild(node);
			}
		}
	}
}
