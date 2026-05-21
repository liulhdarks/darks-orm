package darks.orm.core.config;

import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class SessionConfigFactoryTest
{
    
    @Test
    public void getConfigurationAcceptsLegacyEntitysElement()
        throws Exception
    {
        String xml =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<!DOCTYPE darks PUBLIC \"-//darks//DTD darks 3.0//EN\" \"darks.dtd\">"
                + "<darks>"
                + "<dataSource type=\"jdbc\" id=\"jdbc\" main=\"true\">"
                + "<property name=\"driver\" value=\"java.lang.String\"/>"
                + "</dataSource>"
                + "<entitys>"
                + "<entity alias=\"StringAlias\" class=\"java.lang.String\"/>"
                + "</entitys>"
                + "</darks>";
        
        Configuration configuration =
            SessionConfigFactory.getConfiguration(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        
        assertSame(String.class, configuration.getEntityConfig().getEntity("StringAlias"));
    }
}
