package cardRecognizer;

import processing.data.XML;

public class XMLWorker extends core.XmlHelper {

	public XML[] configXML = new XML[1];
	public XML[] urlsXML = new XML[1];

	static final String configFilePath = System.getProperty("user.dir") + "\\config\\config.xml";
	//this is here so that urls_private.xml can be .gitignored and contain URLs that are to be kept private
	static final String urlsFilePath_private = System.getProperty("user.dir") + "\\config\\urls_private.xml";
	static final String urlsFilePath = System.getProperty("user.dir") + "\\config\\urls.xml";


	//------------------------------------------------------------
	//CONSTRUCTOR
	//------------------------------------------------------------
	public XMLWorker() {
		configXML = GetXMLFromFile(configFilePath);
		urlsXML = GetXMLFromFile(urlsFilePath_private, urlsFilePath);
	}
	
	public int GetIntFromXml(String nodePath) {
		return GetIntFromXml(nodePath, configXML);
	}
	
	public String GetDataFromXml(String nodePath) {
		return GetDataFromXml(nodePath, configXML);
	}
	
	public String GetURLFromXml(String nodePath) {
		return GetDataFromXml(nodePath, urlsXML);
	}
}
