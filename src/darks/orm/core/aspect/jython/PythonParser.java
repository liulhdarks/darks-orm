package darks.orm.core.aspect.jython;

import darks.orm.app.QueryEnumType;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.core.factory.JythonFactory;
import darks.orm.exceptions.JythonAspectException;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class PythonParser
{
    
    public static String JY_ESPECT_DATA = "__DATA";
    
    /**
     * 解析XML脚本
     * 
     * @param queryData 查询数据
     * @param smEspectData 注入数据
     * @param methodType 解析方法类型 JY_ESPECT_BEFORE or JY_ESPECT_AFTER
     * @return 是否继续执行
     */
    public boolean parse(AspectData aspectData, SimpleAspectWrapper simpleWrapper, QueryEnumType queryEnumType,
        String methodType)
        throws JythonAspectException
    {
        if (aspectData == null)
            return true;
        PythonInterpreter pyInter = initPythonInterpreter(simpleWrapper);
        if (aspectData.getAspectType() == AspectType.JYTHON)
        {
            String content = PythonBuilder.buildJython(aspectData, aspectData.getClassName(), aspectData.getContent());
            try
            {
                pyInter.exec(content);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new JythonAspectException(e);
            }
        }
        else if (aspectData.getAspectType() == AspectType.PYFILE)
        {
            pyInter.exec(PythonBuilder.getPythonHeader().toString());
            pyInter.execfile(aspectData.getContent());
            pyInter.exec(PythonBuilder.buildJythonTail(aspectData.getClassName()));
        }
        else
        {
            return true;
        }
        PyObject espectObj = pyInter.get(PythonBuilder.JY_ESPECT_CLASS);
        PyObject retObj = espectObj.invoke(methodType);
        
        if (retObj == null || retObj instanceof PyNone)
        {
            return false;
        }
        else
        {
            Integer ret = (Integer)JythonFactory.getInstance().pyObjectToObject(retObj, Integer.class);
            if (ret == 0)
                return false;
            else
                return true;
        }
        
    }
    
    /**
     * 初始化Python引擎并获得PythonInterpreter
     * 
     * @param queryData 查询数据
     * @param sql SQL语句
     * @param values 选择值
     * @param params 参数
     * @param page 当前页
     * @param pageSize 分页大小
     * @return PythonInterpreter 实例
     */
    public PythonInterpreter initPythonInterpreter(SimpleAspectWrapper simpleWrapper)
    {
        PythonInterpreter pyInter = JythonFactory.getInstance().getInterpreter();
        if (simpleWrapper instanceof QueryAspectWrapper)
        {
            pyInter.set(JY_ESPECT_DATA, (QueryAspectWrapper)simpleWrapper);
        }
        else
        {
            pyInter.set(JY_ESPECT_DATA, simpleWrapper);
        }
        return pyInter;
    }
    
}
