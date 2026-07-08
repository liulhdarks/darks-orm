package darks.orm.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import darks.orm.core.config.Configuration;
import darks.orm.core.config.SessionConfigFactory;
import darks.orm.test.model.User;

public class ConfigCompatibilityTest
{
    
    @Test
    public void parsesCurrentEntitiesTag()
        throws Exception
    {
        Configuration cfg = SessionConfigFactory.getConfiguration(config("entities", "CurrentUser"));
        
        Assert.assertEquals(User.class, cfg.getEntityConfig().getEntity("CurrentUser"));
    }
    
    @Test
    public void parsesLegacyEntitysTag()
        throws Exception
    {
        Configuration cfg = SessionConfigFactory.getConfiguration(config("entitys", "LegacyUser"));
        
        Assert.assertEquals(User.class, cfg.getEntityConfig().getEntity("LegacyUser"));
    }
    
    private InputStream config(String entityTag, String alias)
        throws Exception
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<!DOCTYPE darks PUBLIC \"-//darks//DTD darks 3.0//EN\" \"darks.dtd\">"
            + "<darks>"
            + "<dataSource type=\"jdbc\" id=\"jdbc\" main=\"true\">"
            + "<property name=\"driver\" value=\"com.mysql.jdbc.Driver\"></property>"
            + "<property name=\"url\" value=\"jdbc:mysql://localhost:3306/test\"></property>"
            + "<property name=\"username\" value=\"root\"></property>"
            + "<property name=\"password\" value=\"1234\"></property>"
            + "<property name=\"fetchSize\" value=\"0\"></property>"
            + "<property name=\"autoCommit\" value=\"true\"></property>"
            + "</dataSource>"
            + "<" + entityTag + ">"
            + "<entity alias=\"" + alias + "\" class=\"darks.orm.test.model.User\"></entity>"
            + "</" + entityTag + ">"
            + "</darks>";
        return new ByteArrayInputStream(xml.getBytes("UTF-8"));
    }
}
