package darks.orm.util;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

/**
 * XML helper
 * 
 * @author darks
 * 
 */
public final class XmlHelper
{

	private static Logger log = LoggerFactory.getLogger(XmlHelper.class);

	private static final int ERROR_COUNT_LIMIT = 20;

	private XmlHelper()
	{

	}

	/**
	 * Validate xml file by DTD file
	 * 
	 * @param publicId Public id
	 * @param dtdPath DTD path
	 * @param xmlIns XML inputstream
	 * @return Whether valid
	 */
	public static Document getXMLDocument(String publicId, final String dtdPath, InputStream xmlIns)
	{
		try
		{
			EntityResolver resolver = new EntityResolver()
			{
				public InputSource resolveEntity(String publicId, String systemId)
				{
					if (publicId.equals(publicId))
					{
						InputStream in = getClass().getResourceAsStream(dtdPath);
						InputSource ins = new InputSource(in);
						ins.setPublicId(publicId);
						ins.setSystemId(systemId);
						return ins;
					}
					return null;
				}
			};
			XMLValidationErrorHandler errorHandler = new XMLValidationErrorHandler();
			SAXReader reader = new SAXReader();
			reader.setValidation(true);
			reader.setErrorHandler(errorHandler);
			reader.setEntityResolver( resolver );
			Document doc = reader.read(xmlIns);
			if (errorHandler.isError())
			{
				return null;
			}
			return doc;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return null;
	}

	static class XMLValidationErrorHandler extends DefaultHandler
	{
		private int errorCount = 0;

		public void error(SAXParseException e) throws SAXException
		{
			if (errorCount >= ERROR_COUNT_LIMIT)
			{
				return;
			}
			else if (errorCount == 0)
			{
				System.err.println("Occured XML parse exception.");
			}
			String systemId = e.getSystemId();
			if (systemId == null)
			{
				systemId = "null";
			}
			String message = "Error: URI=" + systemId + " Line=" + e.getLineNumber() + ": "
					+ e.getMessage();
			System.err.println(message);
			errorCount++;
		}

		public boolean isError()
		{
			return errorCount > 0;
		}
	}
}
