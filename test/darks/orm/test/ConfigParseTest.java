package darks.orm.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import darks.orm.core.config.Configuration;
import darks.orm.core.config.SessionConfigFactory;
import darks.orm.test.model.User;

public class ConfigParseTest
{
    
    @Test
    public void parseLegacyEntitysTag()
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<!DOCTYPE darks PUBLIC \"-//darks//DTD darks 3.0//EN\" \"darks.dtd\">"
            + "<darks>"
            + "<dataSource type=\"jdbc\" main=\"true\">"
            + "<property name=\"driver\" value=\"driver\"/>"
            + "<property name=\"url\" value=\"url\"/>"
            + "<property name=\"username\" value=\"username\"/>"
            + "<property name=\"password\" value=\"password\"/>"
            + "</dataSource>"
            + "<entitys>"
            + "<entity alias=\"User\" class=\"darks.orm.test.model.User\"/>"
            + "</entitys>"
            + "</darks>";
        InputStream input = new ByteArrayInputStream(xml.getBytes());
        Configuration config = SessionConfigFactory.getConfiguration(input);
        
        Assert.assertEquals(User.class, config.getEntityConfig().getEntity("User"));
    }
}
