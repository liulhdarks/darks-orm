package darks.orm.test;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

import darks.orm.core.config.Configuration;
import darks.orm.core.config.SessionConfigFactory;
import darks.orm.test.model.User;

public class SessionConfigFactoryTest
{
    
    @Test
    public void testLegacyEntitysTagIsAccepted()
        throws Exception
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<!DOCTYPE darks PUBLIC \"-//darks//DTD darks 3.0//EN\" \"darks.dtd\">"
            + "<darks><entitys><entity class=\"darks.orm.test.model.User\" alias=\"legacyUser\"/></entitys></darks>";
        
        Configuration cfg = SessionConfigFactory.getConfiguration(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        
        Assert.assertEquals(User.class, cfg.getEntityConfig().getEntity("legacyUser"));
    }
}
