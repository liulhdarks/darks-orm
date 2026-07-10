package darks.orm.core.aspect.js;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Assume;
import org.junit.Test;

import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.exceptions.JsAspectException;

public class JsParserTest
{

    @Test
    public void parseFailsClosedWhenJavascriptMethodIsMissing()
    {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        Assume.assumeTrue(engine instanceof Invocable);

        AspectData aspectData = new AspectData();
        aspectData.setAspectType(AspectType.JAVASCRIPT);
        aspectData.setContent("function after(wrapper) { return true; }");

        try
        {
            new JsParser().parse(aspectData, new SimpleAspectWrapper(), null, "before");
            fail("Expected missing JavaScript before hook to fail closed");
        }
        catch (JsAspectException expected)
        {
            // Expected.
        }
    }

    @Test
    public void parseFailsClearlyWhenJavascriptEngineIsUnavailable()
        throws Exception
    {
        Field managerField = JsParser.class.getDeclaredField("manager");
        managerField.setAccessible(true);
        Object originalManager = managerField.get(null);
        managerField.set(null, new NoJavascriptEngineManager());

        AspectData aspectData = new AspectData();
        aspectData.setAspectType(AspectType.JAVASCRIPT);
        aspectData.setContent("function before(wrapper) { return true; }");

        try
        {
            new JsParser().parse(aspectData, new SimpleAspectWrapper(), null, "before");
            fail("Expected unavailable JavaScript engine to fail closed");
        }
        catch (JsAspectException expected)
        {
            // Expected.
        }
        finally
        {
            managerField.set(null, originalManager);
        }
    }

    private static class NoJavascriptEngineManager extends ScriptEngineManager
    {
        @Override
        public ScriptEngine getEngineByName(String shortName)
        {
            return null;
        }
    }
}
