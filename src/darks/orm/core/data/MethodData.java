package darks.orm.core.data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MethodData implements Serializable
{
    
    private static final long serialVersionUID = -9219147812448315504L;
    
    public enum MethodEnumType
    {
        None, Query, OneToOne, ManyToOne, OneToMany
    }
    
    private String methodName;
    
    private String methodSimpleName;
    
    private ConcurrentMap<String, Integer> argumentsMap;
    
    private transient Method method;
    
    private MethodEnumType methodEnumType;
    
    public MethodData()
    {
        argumentsMap = new ConcurrentHashMap<String, Integer>();
    }
    
    public int getArgumentIndex(String argName)
    {
        if (argumentsMap.containsKey(argName))
        {
            return argumentsMap.get(argName);
        }
        return -1;
    }
    
    public void addArgument(int index, String argName)
    {
        argumentsMap.put(argName, index);
    }
    
    public String getMethodName()
    {
        return methodName;
    }
    
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
    
    public ConcurrentMap<String, Integer> getArgumentsMap()
    {
        return argumentsMap;
    }
    
    public void setArgumentsMap(ConcurrentMap<String, Integer> argumentsMap)
    {
        this.argumentsMap = argumentsMap;
    }
    
    public Method getMethod()
    {
        return method;
    }
    
    public void setMethod(Method method)
    {
        this.method = method;
    }
    
    public String getMethodSimpleName()
    {
        return methodSimpleName;
    }
    
    public void setMethodSimpleName(String methodSimpleName)
    {
        this.methodSimpleName = methodSimpleName;
    }
    
    public MethodEnumType getMethodEnumType()
    {
        return methodEnumType;
    }
    
    public void setMethodEnumType(MethodEnumType methodEnumType)
    {
        this.methodEnumType = methodEnumType;
    }
    
}
