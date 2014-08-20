package darks.orm.core.data.xml;

public class AspectData implements Cloneable
{
    
    public enum AspectType
    {
        JYTHON, PYFILE, JAVACLASS, JAVASCRIPT, JSFILE
    }
    
    private AspectType aspectType;
    
    private String className;
    
    private String content;
    
    public AspectData()
    {
        
    }
    
    public AspectType getAspectType()
    {
        return aspectType;
    }
    
    public void setAspectType(AspectType aspectType)
    {
        this.aspectType = aspectType;
    }
    
    public String getClassName()
    {
        return className;
    }
    
    public void setClassName(String className)
    {
        this.className = className;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public AspectData clone()
    {
        try
        {
            return (AspectData)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
}
