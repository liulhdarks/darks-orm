package darks.orm.core.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import darks.orm.annotation.Entity;
import darks.orm.annotation.Id.GenerateKeyType;
import darks.orm.core.data.EntityData;
import darks.orm.core.data.FieldData;
import darks.orm.core.data.FieldData.FieldFlag;
import darks.orm.core.data.PrimaryKeyData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.exceptions.PersistenceException;
import darks.orm.exceptions.SessionException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.DataTypeHelper;

/**
 * BEAN支持类 作者:DarkShadow 版权:归夜影所有 时间:2011-11-10 版本:1.0.0
 */
@SuppressWarnings("unchecked")
public abstract class PersistSqlBuilder
{
    
    private static final Logger logger = LoggerFactory.getLogger(PersistSqlBuilder.class);
    
    private PersistSqlBuilder()
    {
        
    }
    
    /**
     * 保存实体类
     * 
     * @param c 类
     * @return 字段连接字符串
     */
    public static <T> List<Object> buildSaveSql(Class<T> c, T ent, String tableName, boolean pkIsNull, Object pkval)
        throws SessionException
    {
        boolean flag = c.isAnnotationPresent(Entity.class);
        if (!flag)
            return null;
        Entity classType = (Entity)c.getAnnotation(Entity.class);
        if (classType == null)
            return null;
        EntityData entityData = ClassFactory.parseClass(c);
        StringBuffer buf = new StringBuffer(128);
        StringBuffer buf2 = new StringBuffer(128);
        List<Object> list = new ArrayList<Object>();
        List<Object> ret = new ArrayList<Object>();
        int num = 0;
        buf.append("insert into ");
        buf.append(tableName);
        buf.append('(');
        buf2.append(" values(");
        for (Entry<String, FieldData> entry : entityData.getMapFields().entrySet())
        {
            String key = entry.getKey();
            FieldData fdata = entry.getValue();
            if (!fdata.isInsertable())
                continue;
            Object o = fdata.getValue(ent);
            
            if (fdata.isPrimaryKey())
            {
                
                if (o == null || pkIsNull)
                {
                    PrimaryKeyData pkdata = fdata.getPkData();
                    if (pkdata.getType() == GenerateKeyType.AUTO)
                    {
                        continue;
                    }
                    else if (pkdata.getType() == GenerateKeyType.SEQUENCE)
                    {
                        String seq = pkdata.getSeq();
                        if ("".equals(seq))
                            continue;
                        // seq+=".nextval";
                        buf.append(key);
                        buf.append(',');
                        buf2.append(seq);
                        buf2.append(',');
                        continue;
                    }
                    else if (pkdata.getType() == GenerateKeyType.SELECT)
                    {
                        o = pkval;
                    }
                }
            }
            
            if (fdata.isNullable())
            {
                if (o == null)
                    continue;
            }
            if (o != null)
            {
                if (fdata.getFieldFlag() == FieldFlag.FkEntity)
                {
                    o = ClassFactory.getPrimaryKeyValue(fdata.getFkClass(), o);
                }
            }
            list.add(o);
            buf.append(key);
            buf.append(',');
            buf2.append("?,");
            num++;
        }
        if (num == 0)
            return null;
        buf.deleteCharAt(buf.length() - 1);
        buf2.deleteCharAt(buf2.length() - 1);
        buf.append(')');
        buf2.append(')');
        buf.append(buf2);
        Object[] objs = list.toArray();
        ret.add(buf.toString());
        ret.add(objs);
        return ret;
    }
    
    /**
     * 更新实体类
     * 
     * @param c 类
     * @param isNullable 是否允许为空
     * @return 字段连接字符串
     */
    public static <T> List<Object> buildUpdateSql(Class<T> c, T ent, String tableName, boolean isNullable)
        throws Exception
    {
        boolean flag = c.isAnnotationPresent(Entity.class);
        if (!flag)
            return null;
        Entity classType = (Entity)c.getAnnotation(Entity.class);
        if (classType == null)
            return null;
        EntityData entityData = ClassFactory.parseClass(c);
        StringBuffer buf = new StringBuffer(128);
        List<Object> list = new ArrayList<Object>();
        List<Object> ret = new ArrayList<Object>();
        String pk = ClassFactory.getPrimaryKeyName(c);
        int num = 0;
        buf.append("update ");
        buf.append(tableName);
        buf.append(" set ");
        Object pkv = null;
        for (Entry<String, FieldData> entry : entityData.getMapFields().entrySet())
        {
            String key = entry.getKey();
            
            FieldData fdata = entry.getValue();
            if (!fdata.isUpdatable())
                continue;
            Object o = fdata.getValue(ent);
            if (key.equals(pk))
            {
                pkv = o;
                continue;
            }
            
            if (!fdata.isNullable() || isNullable == true)
            {
                if (DataTypeHelper.checkValueIsNull(fdata.getFieldClass(), o))
                    continue;
            }
            
            if (o != null)
            {
                if (fdata.getFieldFlag() == FieldFlag.FkEntity)
                {
                    Class fclass = fdata.getFkClass();
                    o = ClassFactory.getPrimaryKeyValue(fclass, o);
                }
            }
            list.add(o);
            buf.append(key);
            buf.append(" = ?,");
            num++;
        }
        if (num == 0)
            return null;
        buf.deleteCharAt(buf.length() - 1);
        buf.append(" where ");
        buf.append(pk);
        buf.append(" = ?");
        list.add(pkv);
        Object[] objs = list.toArray();
        ret.add(buf.toString());
        ret.add(objs);
        return ret;
    }
    
    /**
     * 删除实体
     * 
     * @param c 实体类
     * @param id 实体主键值
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public static <T> String buildDeleteSql(Class<T> c, int id)
        throws SessionException
    {
        
        StringBuffer buf = new StringBuffer(256);
        String tn = ClassFactory.getTableName(c);
        if (tn == null)
            return null;
        String pk = ClassFactory.getPrimaryKeyName(c);
        if (pk == null)
        {
            throw new PersistenceException("delete '" + c.getName() + "' does not has primary key");
        }
        
        buf.append("delete from ");
        buf.append(tn);
        buf.append(" where ");
        buf.append(pk);
        buf.append(" = ?");
        return buf.toString();
    }
    
    /**
     * 通过主键获得实体
     * 
     * @param c 实体类
     * @param id 主键值
     * @return SQL语句
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public static <T> String buildGetSql(Class<T> c, int id)
        throws PersistenceException, ClassNotFoundException
    {
        StringBuffer buf = new StringBuffer(256);
        String fs = ClassFactory.getFieldNames(c);
        if (fs == null || "".equals(fs))
            fs = "*";
        String tn = ClassFactory.getTableName(c);
        if (tn == null)
            return null;
        String pk = ClassFactory.getPrimaryKeyName(c);
        if (pk == null)
        {
            throw new PersistenceException("get '" + c.getName() + "' does not has primary key");
        }
        
        buf.append("select ");
        buf.append(fs);
        buf.append(" from ");
        buf.append(tn);
        buf.append(" where ");
        buf.append(pk);
        buf.append(" = ?");
        return buf.toString();
    }
    
}
