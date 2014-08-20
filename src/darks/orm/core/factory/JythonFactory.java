package darks.orm.core.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.python.core.PyArray;
import org.python.core.PyFunction;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PySingleton;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

@SuppressWarnings("unchecked")
public class JythonFactory
{
    
    private static volatile JythonFactory instance = null;
    
    private static final ConcurrentMap<String, PythonInterpreter> piMap =
        new ConcurrentHashMap<String, PythonInterpreter>();
    
    // private static final ConcurrentMap<String, PythonInterpreter> psMap = new
    // ConcurrentHashMap<String, PythonInterpreter>();
    
    public static JythonFactory getInstance()
    {
        if (instance == null)
        {
            instance = new JythonFactory();
        }
        return instance;
    }
    
    public Object getJavaObjectFromJythonFile(String interfaceName, String pathToJythonModule)
    {
        Object javaObject = null;
        
        PythonInterpreter interpreter = cacheInterpreter(pathToJythonModule);
        
        String tempName = pathToJythonModule.substring(pathToJythonModule.lastIndexOf("/") + 1);
        tempName = tempName.substring(0, tempName.indexOf("."));
        System.out.println(tempName);
        String instanceName = tempName.toLowerCase();
        String javaClassName = tempName.substring(0, 1).toUpperCase() + tempName.substring(1);
        String objectDef = "=" + javaClassName + "()";
        interpreter.exec(instanceName + objectDef);
        try
        {
            Class JavaInterface = Class.forName(interfaceName);
            javaObject = interpreter.get(instanceName).__tojava__(JavaInterface);
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace(); // Add logging here36.
        }
        return javaObject;
    }
    
    public PyObject getPyObjectFromJythonFile(String typeName, String pathToJythonModule)
    {
        PyObject pyObject = null;
        PythonInterpreter interpreter = cacheInterpreter(pathToJythonModule);
        
        String instanceName = typeName.toLowerCase();
        String objectDef = "=" + typeName + "()";
        interpreter.exec(instanceName + objectDef);
        pyObject = interpreter.get(instanceName);
        return pyObject;
    }
    
    public PyFunction getPyFunctionFromJythonFile(String funcName, String pathToJythonModule)
    {
        PyFunction pyFunction = null;
        PythonInterpreter interpreter = cacheInterpreter(pathToJythonModule);
        pyFunction = (PyFunction)interpreter.get(funcName, PyFunction.class);
        return pyFunction;
    }
    
    private PythonInterpreter cacheInterpreter(String pathToJythonModule)
    {
        PythonInterpreter interpreter = null;
        if (piMap.get(pathToJythonModule) != null)
        {
            interpreter = piMap.get(pathToJythonModule);
        }
        else
        {
            interpreter = new PythonInterpreter();
            interpreter.execfile(pathToJythonModule);
            piMap.put(pathToJythonModule, interpreter);
        }
        return interpreter;
    }
    
    public Object[] pyObjectToObjects(PyObject pobj)
    {
        if (pobj == null)
            return null;
        if (pobj instanceof PyTuple)
        {
            PyTuple pt = (PyTuple)pobj;
            return pt.toArray();
        }
        else if (pobj instanceof PyList)
        {
            PyList pl = (PyList)pobj;
            return pl.toArray();
        }
        else if (pobj instanceof PyArray)
        {
            PyArray pa = (PyArray)pobj;
            return (Object[])pa.getArray();
        }
        else if (pobj instanceof PySingleton)
        {
            PySingleton ps = (PySingleton)pobj;
            Object obj = ps.__tojava__(Object.class);
            return new Object[] {obj};
        }
        else
        {
            Object obj = pobj.__tojava__(Object.class);
            return new Object[] {obj};
        }
    }
    
    public Object pyObjectToObject(PyObject pobj, Class c)
    {
        return pobj.__tojava__(c);
    }
    
    public PythonInterpreter getInterpreter()
    {
        return new PythonInterpreter();
    }
    
}
