package darks.orm.core.factory;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.config.sqlmap.SqlMapConfiguration;
import darks.orm.core.data.xml.DDLData;
import darks.orm.core.data.xml.DDLData.DDLType;
import darks.orm.datasource.factory.ConnectionFactory;
import darks.orm.exceptions.DataSourceException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

public class TableGeneratorFactory
{
    
    private static final Logger logger = LoggerFactory.getLogger(TableGeneratorFactory.class);
    
    private static final ConcurrentMap<String, String> tableMap = new ConcurrentHashMap<String, String>();
    
    /**
     * 初始化数据库信息
     * 
     * @param conn 数据库连接
     * @return 是否成功初始化
     * @throws Exception
     */
    private static boolean initDataMateData(Connection conn)
        throws Exception
    {
        SqlMapConfiguration smcfg = SqlMapSingletonFactory.getInstance().getSqlmapconfig();
        if (smcfg == null)
            return false;
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet rs = dbmd.getTables(smcfg.getCatalog(), smcfg.getSchema(), "%", new String[] {"TABLE", "VIEW"});
        while (rs.next())
        {
            String tableName = rs.getString("TABLE_NAME");
            tableName = tableName.toUpperCase();
            tableMap.put(tableName, tableName);
        }
        rs.close();
        return true;
    }
    
    /**
     * 创建或更新表
     * 
     * @param ddlMap DDL集合
     * @return
     */
    public static boolean mergeTable(ConcurrentMap<String, DDLData> ddlMap)
    {
        if (ddlMap.size() == 0)
            return true;
        Connection conn = null;
        Statement stmt = null;
        try
        {
            conn = ConnectionFactory.getInstance().getConnection();
            if (conn == null)
            {
                throw new DataSourceException("the connection is null");
            }
            else
            {
                initDataMateData(conn);
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                for (Entry<String, DDLData> entry : ddlMap.entrySet())
                {
                    DDLData ddlData = entry.getValue();
                    if (!ddlData.isAutoRunable())
                        continue;
                    if (ddlData.isCheckTable())
                    {
                        String tbName = ddlData.getTableName();
                        if (tbName == null || "".equals(tbName))
                            continue;
                        tbName = tbName.toUpperCase();
                        if (!checkTableExist(tbName))
                        {
                            if (ddlData.getType() == DDLType.Alter)
                                continue;
                        }
                        else
                        {
                            if (ddlData.getType() == DDLType.Create)
                                continue;
                        }
                    }
                    String sql = ddlData.getSql();
                    if (sql == null || "".equals(sql))
                        continue;
                    logger.debug("[SQL]" + sql);
                    stmt.addBatch(sql);
                }
                stmt.executeBatch();
                conn.commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                if (conn != null)
                {
                    conn.rollback();
                }
            }
            catch (Exception err)
            {
                err.printStackTrace();
            }
        }
        finally
        {
            try
            {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
                Thread thread = Thread.currentThread();
                logger.debug("TableGeneratorFactory merge table close connection");
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
        return true;
    }
    
    /**
     * 检查表是否存在
     * 
     * @param tbName 表名
     * @return true存在 false不存在
     * @throws RemoteException
     */
    public static boolean checkTableExist(String tbName)
        throws RemoteException
    {
        if (tableMap.containsKey(tbName))
            return true;
        return false;
    }
    
}
