/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.orm.core.session;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.annotation.Id.FeedBackKeyType;
import darks.orm.annotation.Id.GenerateKeyType;
import darks.orm.app.Page;
import darks.orm.app.SqlSession;
import darks.orm.core.cache.CacheContext;
import darks.orm.core.config.ResultSetConfig;
import darks.orm.core.data.FieldData;
import darks.orm.core.data.PrimaryKeyData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.factory.TransformFactory;
import darks.orm.datasource.Transaction;
import darks.orm.datasource.factory.ConnectionFactory;
import darks.orm.datasource.factory.StatementFactory.StatementType;
import darks.orm.datasource.factory.TransactionFactory;
import darks.orm.exceptions.PersistenceException;
import darks.orm.exceptions.SessionException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.DataTypeHelper;
import darks.orm.util.JdbcHelper;
import darks.orm.util.ReflectHelper;

/**
 * 
 * 
 * <p>
 * <h1>SessionSupport.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public abstract class SessionSupport implements Serializable, SqlSession
{
    
    private static final Logger logger = LoggerFactory.getLogger(SessionSupport.class);
    
    private static final long serialVersionUID = 9127842587231565250L;
    
    private static ConcurrentMap<String, String> getMap = new ConcurrentHashMap<String, String>();
    
    private static ConcurrentMap<String, String> deleteMap = new ConcurrentHashMap<String, String>();
    
    // 是否自动处理事务
    private Transaction tx;
    
    private StatementType stmtType;
    
    private volatile boolean inited = false;
    
    public SessionSupport()
    {
        
    }
    
    /**
     * Initialize sql session
     */
    private void initialize()
    {
        if (!inited || tx == null)
        {
            tx = TransactionFactory.getTransaction();
            ResultSetConfig cfg = ConnectionFactory.getInstance().getCurrentResultSetConfig();
            if (cfg != null)
            {
                stmtType = cfg.getStatementType();
            }
            else
            {
            	stmtType = StatementType.Scorllable;
            }
            inited = true;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isInited()
    {
        return inited;
    }
    
    /**
     * {@inheritDoc}
     */
    public Connection getConnection()
    {
        return tx.getConnection();
    }
    
    /**
     * {@inheritDoc}
     */
    public Connection getConnection(boolean isCreate)
    {
        return tx.getConnection(isCreate);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setConnection(Connection conn)
    {
        tx.setConnection(conn);
    }
    
    /**
     * {@inheritDoc}
     */
    public void close()
    {
        if (tx == null)
            return;
        tx.close();
    }
    
    /**
     * {@inheritDoc}
     */
    public void shutDown()
    {
        close();
        SessionContext.destroy();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isClosed()
    {
        if (tx == null)
            return true;
        return tx.isClosed();
    }
    
    /**
     * {@inheritDoc}
     */
    public int executeField(String sql, Object... params)
    {
        ResultSet rs = executeQuery(sql, params);
        int num = 0;
        try
        {
            if (rs.next())
            {
                num = rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            throw new SessionException(e.getMessage(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
        return num;
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(String sql, Object... param)
    {
        return executeQuery(sql, param, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(String sql, Object param[], StatementType stateType)
        throws SessionException
    {
        sql = sql.toLowerCase();
        logger.debug("[SQL]" + sql);
        PreparedStatement pstmt = null;
        
        initialize();
        try
        {
            if (stateType != null)
                pstmt = tx.getPreparedStatement(sql, stateType);
            else
                pstmt = tx.getPreparedStatement(sql);
            if (param != null)
            {
                for (int i = 0; i < param.length; i++)
                {
                    pstmt.setObject(i + 1, param[i]);
                }
            }
            return pstmt.executeQuery();
        }
        catch (SQLException e)
        {
            throw new SessionException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public CallableStatement prepareCall(String sql)
    {
        initialize();
        return tx.getCallableStatement(sql);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryList(Class<T> c, String sql, Object... params)
    {
        initialize();
        ResultSet rs = null;
        try
        {
            rs = executeQuery(sql, params);
            if (rs == null)
                return null;
            return TransformFactory.getInstance().ResultToList(c, sql, rs);
        }
        catch (Exception e)
        {
            throw new SessionException(e.getMessage(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryList(Class<T> c, ResultSet rs, String sql)
    {
        try
        {
            initialize();
            if (rs == null)
                return null;
            logger.debug("[SQL]" + sql);
            return TransformFactory.getInstance().ResultToList(c, sql, rs);
        }
        catch (Exception e)
        {
            throw new SessionException(e.getMessage(), e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryCascadeObject(Class<T> c, String sql, String[] entityName, String[] alias, Object... param)
    {
        ResultSet rs = null;
        Map<String, String> map = new HashMap<String, String>();
        StringBuffer buf = new StringBuffer("[MAP]");
        if (entityName != null && alias != null)
        {
            if (entityName.length != alias.length)
                return null;
            for (int i = 0; i < entityName.length; i++)
            {
                map.put(entityName[i], alias[i]);
                buf.append(entityName[i]);
                buf.append("#");
                buf.append(alias[i]);
                buf.append("@");
            }
        }
        try
        {
            initialize();
            sql = TransformFactory.getInstance().transformSQLToCascade(sql, c, map, buf);
            rs = executeQuery(sql, param);
            if (rs == null)
                return null;
            return TransformFactory.getInstance().cascadeResultToBean(c, sql, rs, map, true);
        }
        catch (Exception e)
        {
            throw new SessionException(e.getMessage(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryCascadeList(Class<T> c, String sql, String[] entityName, String[] alias, Object... param)
    {
        ResultSet rs = null;
        Map<String, String> map = new HashMap<String, String>();
        StringBuffer buf = new StringBuffer("[MAP]");
        if (entityName != null && alias != null)
        {
            if (entityName.length != alias.length)
                return null;
            for (int i = 0; i < entityName.length; i++)
            {
                map.put(entityName[i], alias[i]);
                buf.append(entityName[i]);
                buf.append("#");
                buf.append(alias[i]);
                buf.append("@");
            }
        }
        try
        {
            initialize();
            sql = TransformFactory.getInstance().transformSQLToCascade(sql, c, map, buf);
            rs = executeQuery(sql, param);
            if (rs == null)
                return null;
            return TransformFactory.getInstance().cascadeResultToList(c, sql, rs, map);
        }
        catch (Exception e)
        {
            throw new SessionException(e.getMessage(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> Page<T> queryCascadePageList(Class<T> c, String sql, int page, int pageSize, String[] entityName,
        String[] alias, Object... param)
    {
        ResultSet rs = null;
        Map<String, String> map = new HashMap<String, String>();
        StringBuffer buf = new StringBuffer("[MAP]");
        if (entityName != null && alias != null)
        {
            if (entityName.length != alias.length)
                return null;
            for (int i = 0; i < entityName.length; i++)
            {
                map.put(entityName[i], alias[i]);
                buf.append(entityName[i]);
                buf.append("#");
                buf.append(alias[i]);
                buf.append("@");
            }
        }
        try
        {
            initialize();
            sql = TransformFactory.getInstance().transformSQLToCascade(sql, c, map, buf);
            rs = executeQuery(sql, param);
            if (rs == null)
                return null;
            List<T> list = null;
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            if (count > 0)
            {
                list = TransformFactory.getInstance().cascadeResultToPage(c, sql, rs, page, pageSize, map);
            }
            else
            {
                list = new ArrayList<T>();
            }
            return new Page<T>(list, count);
        }
        catch (Exception e)
        {
            throw new SessionException(e.getMessage(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryById(Class<T> c, int id)
    {
        String sql = null;
        if (getMap.containsKey(c.getName()))
        {
            sql = getMap.get(c.getName());
        }
        else
        {
            try
            {
                sql = PersistSqlBuilder.buildGetSql(c, id);
            }
            catch (ClassNotFoundException e)
            {
                throw new SessionException("SessionSupport::queryById ClassNotFoundException " + e.toString(), e);
            }
            getMap.put(c.getName(), sql);
        }
        initialize();
        ResultSet rs = executeQuery(sql, id);
        if (rs == null)
            return null;
        try
        {
            return TransformFactory.getInstance().ResultToBean(c, sql, rs, true);
        }
        catch (Exception e)
        {
            throw new SessionException("SessionSupport::queryById " + e.toString(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryBySQL(Class<T> c, String sql, Object... param)
    {
        initialize();
        ResultSet rs = executeQuery(sql, param);
        if (rs == null)
            return null;
        try
        {
            return TransformFactory.getInstance().ResultToBean(c, sql, rs, true);
        }
        catch (Exception e)
        {
            throw new SessionException("SessionSupport::queryBySQL " + e.toString(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryBySQL(Class<T> c, ResultSet rs, String sql)
    {
        initialize();
        if (rs == null)
            return null;
        logger.debug("[SQL]" + sql);
        try
        {
            return TransformFactory.getInstance().ResultToBean(c, sql, rs, true);
        }
        catch (Exception e)
        {
            throw new SessionException("queryBySQL " + e.toString(), e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> Page<T> queryPageList(Class<T> c, String sql, int page, int pageSize, Object... param)
    {
        
        ResultSet rs = null;
        initialize();
        try
        {
            rs = executeQuery(sql, param);
            if (rs == null)
                return null;
            int count = 0;
            List<T> list = null;
            if (stmtType == StatementType.Scorllable)
            {
                rs.last();
                count = rs.getRow();
                rs.beforeFirst();
                if (count > 0)
                {
                    list = TransformFactory.getInstance().ResultToPageScroll(c, sql, rs, page, pageSize);
                }
                else
                {
                    list = new ArrayList<T>();
                }
            }
            else
            {
                String countsql = TransformFactory.getInstance().transformSqlToCount(sql);
                count = executeField(countsql, param);
                if (count > 0)
                {
                    list = TransformFactory.getInstance().ResultToPageForward(c, sql, rs, page, pageSize);
                }
                else
                {
                    list = new ArrayList<T>();
                }
            }
            return new Page<T>(list, count);
        }
        catch (Exception e)
        {
            throw new SessionException("SessionSupport::queryPageList " + e.toString(), e);
        }
        finally
        {
            JdbcHelper.closeResultSet(rs);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> int save(T entity)
        throws SQLException
    {
        return (Integer)save(entity, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> Object save(T entity, boolean isReturnPk)
        throws SQLException
    {
        initialize();
        Class<T> c = null;
        if (entity == null)
        {
            throw new PersistenceException("the entity saved is null");
        }
        else
        {
            c = (Class<T>)entity.getClass();
        }
        String tn = ClassFactory.getTableName(c);
        if (tn == null)
            return -1;
        FieldData fdata = ClassFactory.getPrimaryKey(c);
        PrimaryKeyData pkdata = fdata.getPkData();
        Class<?> pkClass = fdata.getFieldClass();
        Object pkval = fdata.getValue(entity);
        boolean isNull = DataTypeHelper.checkValueIsNull(pkClass, pkval);
        if (isNull)
        {
            if (pkdata.getType() == GenerateKeyType.SELECT)
            {
                String sql = pkdata.getSelect();
                ResultSet rs = executeQuery(sql, null, StatementType.Normal);
                if (rs != null && rs.next())
                {
                    pkval = ReflectHelper.getResultSetValue(rs, pkClass, 1);
                    rs.close();
                }
                isNull = DataTypeHelper.checkValueIsNull(pkClass, pkval);
            }
        }
        
        List<Object> list = PersistSqlBuilder.buildSaveSql(c, entity, tn, isNull, pkval);
        if (list == null)
            return -1;
        String sql = (String)list.get(0);
        Object[] objs = (Object[])list.get(1);
        if (isReturnPk)
        {
            Object id = pkval;
            if (id == null || isNull)
            {
                if (pkdata.getFeedBackKey() == FeedBackKeyType.SELECT)
                {
                    id = executeUpdateSelectKey(sql, pkdata.getSelect(), fdata.getFieldClass(), objs);
                }
                else
                {
                    id = executeUpdateGeneratedKey(sql, fdata.getFieldClass(), objs);
                }
            }
            else
            {
                executeUpdate(sql, objs);
            }
            if (!id.getClass().equals(pkClass))
            {
                id = DataTypeHelper.longToOther(id, pkClass);
            }
            fdata.setValue(entity, id);
            list.clear();
            list = null;
            return id;
        }
        int ret = executeUpdate(sql, objs);
        if (pkval != null && !isNull)
        {
            fdata.setValue(entity, pkval);
        }
        list.clear();
        list = null;
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> void update(T entity)
    {
        update(entity, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> void update(T entity, boolean isNullable)
    {
        initialize();
        Class<T> c = null;
        if (entity == null)
        {
            throw new PersistenceException("update entity is null");
        }
        else
        {
            c = (Class<T>)entity.getClass();
        }
        try
        {
            String tn = ClassFactory.getTableName(c);
            if (tn == null)
                return;
            List<Object> list = PersistSqlBuilder.buildUpdateSql(c, entity, tn, isNullable);
            if (list == null)
                return;
            String sql = (String)list.get(0);
            Object[] objs = (Object[])list.get(1);
            executeUpdate(sql, objs);
        }
        catch (Exception e)
        {
            throw new PersistenceException("update exception " + e.toString());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> void delete(T entity)
    {
        Class<?> c = null;
        if (entity == null)
        {
            throw new PersistenceException("delete entity is null");
        }
        else
        {
            c = entity.getClass();
        }
        if (c != null)
        {
            int val = (Integer)ClassFactory.getPrimaryKeyValue(c, entity);
            delete(c, val);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> void delete(Class<T> c, int id)
    {
        initialize();
        String sql = deleteMap.get(c.getName());
        ;
        if (sql == null)
        {
            sql = PersistSqlBuilder.buildDeleteSql(c, id);
            if (sql == null)
                return;
            deleteMap.put(c.getName(), sql);
        }
        executeUpdate(sql, id);
    }
    
    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql, Object... params)
    {
        initialize();
        sql = sql.toLowerCase();
        logger.debug("[SQL]" + sql);
        int Count = -1;
        PreparedStatement pstmt = null;
        boolean isAutoCommit = tx.isAutoCommit();
        try
        {
            pstmt = tx.getPreparedStatement(sql, StatementType.Normal);
            tx.setAutoCommit(false);
            
            if (params != null)
            {
                for (int i = 0; i < params.length; i++)
                {
                    JdbcHelper.setObject(pstmt, i + 1, params[i]);
                }
            }
            if (pstmt != null)
            {
                Count = pstmt.executeUpdate();
                if (isAutoCommit)
                {
                    tx.commit();
                }
            }
        }
        catch (SQLException ex)
        {
            if (isAutoCommit)
            {
                JdbcHelper.rollback(tx);
            }
            throw new SessionException("SessionSupport::executeUpdate sql exception " + ex.toString(), ex);
        }
        finally
        {
            try
            {
                tx.setAutoCommit(isAutoCommit);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            JdbcHelper.closeStatement(pstmt);
            CacheContext ctx = SessionContext.getCacheContext();
            if (SessionContext.isUseCache() && ctx != null)
            {
                ctx.flushAll();
            }
        }
        return Count;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object executeUpdateGeneratedKey(String sql, Object... params)
    {
        initialize();
        sql = sql.toLowerCase();
        logger.debug("[SQL]" + sql);
        PreparedStatement pstmt = null;
        boolean isAutoCommit = tx.isAutoCommit();
        try
        {
            pstmt = tx.getPreparedStatement(sql, StatementType.GenerateKey);
            tx.setAutoCommit(false);
            
            if (params != null)
            {
                for (int i = 0; i < params.length; i++)
                {
                    JdbcHelper.setObject(pstmt, i + 1, params[i]);
                }
            }
            pstmt.executeUpdate();
            if (isAutoCommit)
            {
                tx.commit();
                Object autoIncKeyFromApi = -1;
                ResultSet rs = null;
                try
                {
                    rs = pstmt.getGeneratedKeys();
                    if (rs != null && rs.next())
                    {
                        autoIncKeyFromApi = rs.getObject(1);
                    }
                }
                finally
                {
                    JdbcHelper.closeResultSet(rs);
                }
                return autoIncKeyFromApi;
            }
            else
            {
                return 0;
            }
        }
        catch (SQLException ex)
        {
            if (isAutoCommit)
            {
                JdbcHelper.rollback(tx);
            }
            throw new SessionException("SessionSupport::executeUpdateGeneratedKey sql exception " + ex.toString(), ex);
        }
        finally
        {
            try
            {
                tx.setAutoCommit(isAutoCommit);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            JdbcHelper.closeStatement(pstmt);
            CacheContext ctx = SessionContext.getCacheContext();
            if (SessionContext.isUseCache() && ctx != null)
            {
                ctx.flushAll();
            }
        }
    }
    
    /**
     * 执行更新，可返回主键值
     * 
     * @param sql SQL语句
     * @param param 注入数组参数
     * @return 主键值
     */
    private Object executeUpdateGeneratedKey(String sql, Class<?> keyClass, Object[] params)
        throws SessionException
    {
        initialize();
        sql = sql.toLowerCase();
        logger.debug("[SQL]" + sql);
        PreparedStatement pstmt = null;
        boolean isAutoCommit = tx.isAutoCommit();
        try
        {
            pstmt = tx.getPreparedStatement(sql, StatementType.GenerateKey);
            tx.setAutoCommit(false);
            
            if (params != null)
            {
                for (int i = 0; i < params.length; i++)
                {
                    JdbcHelper.setObject(pstmt, i + 1, params[i]);
                }
            }
            pstmt.execute();
            Object autoIncKeyFromApi = -1;
            ResultSet rs = null;
            try
            {
                rs = pstmt.getGeneratedKeys();
                if (rs != null && rs.next())
                {
                    autoIncKeyFromApi = rs.getObject(1);
                }
            }
            finally
            {
                JdbcHelper.closeResultSet(rs);
            }
            if (isAutoCommit)
            {
                tx.commit();
            }
            return autoIncKeyFromApi;
        }
        catch (SQLException ex)
        {
            if (isAutoCommit)
            {
                JdbcHelper.rollback(tx);
            }
            throw new SessionException("SessionSupport::executeUpdateGeneratedKey sql exception " + ex.toString(), ex);
        }
        finally
        {
            try
            {
                tx.setAutoCommit(isAutoCommit);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            JdbcHelper.closeStatement(pstmt);
            CacheContext ctx = SessionContext.getCacheContext();
            if (SessionContext.isUseCache() && ctx != null)
            {
                ctx.flushAll();
            }
        }
    }
    
    /**
     * 执行更新，可返回主键值
     * 
     * @param sql SQL语句
     * @param param 注入数组参数
     * @return 主键值
     */
    private Object executeUpdateSelectKey(String sql, String keysql, Class<?> keyClass, Object[] param)
        throws SessionException
    {
        initialize();
        sql = sql.toLowerCase();
        logger.debug("[SQL]" + sql);
        PreparedStatement pstmt = null;
        boolean isAutoCommit = tx.isAutoCommit();
        try
        {
            pstmt = tx.getPreparedStatement(sql, StatementType.Normal);
            tx.setAutoCommit(false);
            
            if (param != null)
            {
                for (int i = 0; i < param.length; i++)
                {
                    JdbcHelper.setObject(pstmt, i + 1, param[i]);
                }
            }
            pstmt.execute();
            Object autoIncKeyFromApi = null;
            if (pstmt.execute(keysql))
            {
                ResultSet rs = null;
                try
                {
                    rs = pstmt.getResultSet();
                    if (rs != null && rs.next())
                    {
                        autoIncKeyFromApi = ReflectHelper.getResultSetValue(rs, keyClass, 1);
                    }
                }
                finally
                {
                    JdbcHelper.closeResultSet(rs);
                }
            }
            if (isAutoCommit)
            {
                tx.commit();
            }
            return autoIncKeyFromApi;
        }
        catch (SQLException ex)
        {
            if (isAutoCommit)
            {
                JdbcHelper.rollback(tx);
            }
            throw new SessionException("SessionSupport::executeUpdateSelectKey sql exception " + ex.toString(), ex);
        }
        finally
        {
            try
            {
                tx.setAutoCommit(isAutoCommit);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            JdbcHelper.closeStatement(pstmt);
            CacheContext ctx = SessionContext.getCacheContext();
            if (SessionContext.isUseCache() && ctx != null)
            {
                ctx.flushAll();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isAutoCommit()
    {
        initialize();
        return tx.isAutoCommit();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setAutoCommit(boolean isAutoCommit)
        throws SQLException
    {
        initialize();
        tx.setAutoCommit(isAutoCommit);
    }
    
    /**
     * {@inheritDoc}
     */
    public void commit()
        throws SQLException
    {
        initialize();
        tx.commit();
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && ctx != null)
        {
            ctx.flushAll();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void rollback()
    {
        initialize();
        JdbcHelper.rollback(tx);
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && ctx != null)
        {
            ctx.flushAll();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void rollback(Savepoint point)
    {
        initialize();
        JdbcHelper.rollback(tx, point);
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && ctx != null)
        {
            ctx.flushAll();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Savepoint setSavepoint()
        throws SQLException
    {
        initialize();
        return tx.setSavepoint();
    }
    
    /**
     * {@inheritDoc}
     */
    public Savepoint setSavepoint(String name)
        throws SQLException
    {
        initialize();
        return tx.setSavepoint(name);
    }
    
}