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

package darks.orm.datasource.factory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.core.config.ResultSetConfig;
import darks.orm.datasource.ProxyConnection;
import darks.orm.datasource.ProxyPrepareStatement;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

/**
 * 
 * 
 * <p>
 * <h1>StatementFactory.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public class StatementFactory
{
    
    private static final Logger logger = LoggerFactory.getLogger(StatementFactory.class);
    
    private ConcurrentMap<String, PreparedStatement> mapPS = new ConcurrentHashMap<String, PreparedStatement>();
    
    private ConcurrentMap<String, CallableStatement> mapCS = new ConcurrentHashMap<String, CallableStatement>();
    
    public enum StatementType
    {
        Normal, Scorllable, GenerateKey
    }
    
    public void removePreparedStatement(String sql)
    {
        mapPS.remove(sql);
    }
    
    public void removeCallableStatement(String sql)
    {
        mapCS.remove(sql);
    }
    
    public CallableStatement getCallableStatement(ProxyConnection conn, String sql)
        throws SQLException
    {
        DataSourceConfiguration config = ConnectionFactory.getInstance().getCurrentDataSourceConfig();
        CallableStatement cstmt = null;
        
        cstmt = mapCS.get(sql);
        if (cstmt != null)
        {
            if (cstmt.isClosed())
            {
                mapCS.remove(sql);
                cstmt = conn.prepareCall(sql);
                mapCS.put(sql, cstmt);
            }
        }
        else
        {
            cstmt = conn.prepareCall(sql);
            mapCS.put(sql, cstmt);
        }
        if (cstmt != null)
        {
            cstmt.clearParameters();
            cstmt.setFetchSize(config.getFetchSize());
        }
        return cstmt;
    }
    
    /**
     * 工厂获得PrepareStatement
     * 
     * @param sql SQL语句
     * @param conn 数据库连接
     * @param stateType PrepareStatement类型
     * @return PrepareStatement
     * @throws Exception
     */
    public PreparedStatement getPrepareStatement(String sql, ProxyConnection conn, StatementType stateType)
        throws SQLException
    {
        DataSourceConfiguration config = ConnectionFactory.getInstance().getCurrentDataSourceConfig();
        PreparedStatement pstmt = null;
        if (stateType == StatementType.Scorllable)
        {
            try
            {
                pstmt = getPrepareStatementScorllable(sql, conn);
            }
            catch (SQLException e)
            {
                pstmt = getPrepareStatementNormal(sql, conn);
            }
        }
        else if (stateType == StatementType.GenerateKey)
        {
            pstmt = getPrepareStatementGenerateKey(sql, conn);
        }
        else
        {
            pstmt = getPrepareStatementNormal(sql, conn);
        }
        if (pstmt != null)
        {
            pstmt.clearParameters();
            pstmt.setFetchSize(config.getFetchSize());
        }
        return pstmt;
    }
    
    /**
     * 获得可以返回主键值的PrepareStatement
     * 
     * @param sql SQL语句
     * @param conn 数据库连接
     * @return PrepareStatement
     * @throws Exception
     */
    private PreparedStatement getPrepareStatementGenerateKey(String sql, ProxyConnection conn)
        throws SQLException
    {
        PreparedStatement pstmt = mapPS.get(sql);
        if (pstmt != null)
        {
            if (pstmt.isClosed())
            {
                mapPS.remove(sql);
                pstmt = getPrepareStatementByType(sql, conn, StatementType.GenerateKey);
                mapPS.put(sql, pstmt);
            }
        }
        else
        {
            pstmt = getPrepareStatementByType(sql, conn, StatementType.GenerateKey);
            mapPS.put(sql, pstmt);
        }
        return pstmt;
    }
    
    /**
     * 获得可滑动的PrepareStatement
     * 
     * @param sql SQL语句
     * @param conn 数据库连接
     * @return PrepareStatement
     * @throws Exception
     */
    private PreparedStatement getPrepareStatementScorllable(String sql, ProxyConnection conn)
        throws SQLException
    {
        PreparedStatement pstmt = mapPS.get(sql);
        if (pstmt != null)
        {
            if (pstmt.isClosed())
            {
                mapPS.remove(sql);
                pstmt = getPrepareStatementByType(sql, conn, StatementType.Scorllable);
                mapPS.put(sql, pstmt);
            }
        }
        else
        {
            pstmt = getPrepareStatementByType(sql, conn, StatementType.Scorllable);
            mapPS.put(sql, pstmt);
        }
        return pstmt;
    }
    
    /**
     * 获得普通PrepareStatement
     * 
     * @param sql SQL语句
     * @param conn 数据库连接
     * @return PrepareStatement
     * @throws Exception
     */
    private PreparedStatement getPrepareStatementNormal(String sql, ProxyConnection conn)
        throws SQLException
    {
        PreparedStatement pstmt = mapPS.get(sql);
        if (pstmt != null)
        {
            if (pstmt.isClosed())
            {
                mapPS.remove(sql);
                pstmt = getPrepareStatementByType(sql, conn, StatementType.Normal);
                mapPS.put(sql, pstmt);
            }
        }
        else
        {
            pstmt = getPrepareStatementByType(sql, conn, StatementType.Normal);
            mapPS.put(sql, pstmt);
        }
        return pstmt;
    }
    
    /**
     * 获得PrepareStatement
     * 
     * @param sql SQL语句
     * @param conn 连接
     * @param stateType 类型
     * @return PrepareStatement
     * @throws Exception
     */
    private PreparedStatement getPrepareStatementByType(String sql, ProxyConnection conn, StatementType stateType)
        throws SQLException
    {
        DataSourceConfiguration config = ConnectionFactory.getInstance().getCurrentDataSourceConfig();
        ResultSetConfig rsConfig = config.getResultSetConfig();
        PreparedStatement pstmt = null;
        if (stateType == StatementType.Scorllable)
        {
            pstmt = conn.prepareStatement(sql, rsConfig.getType(), rsConfig.getConcurrency());
        }
        else if (stateType == StatementType.GenerateKey)
        {
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        }
        else
        {
            pstmt = conn.prepareStatement(sql);
        }
        pstmt = new ProxyPrepareStatement(sql, conn, pstmt);
        return pstmt;
    }
    
    /**
     * 关闭PreparedStatement
     */
    public void close()
    {
        if (mapPS.size() > 0)
        {
            for (PreparedStatement stmt : mapPS.values())
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    logger.debug("StatementFactory close PreparedStatement " + stmt + " error.", e);
                }
            }
            mapPS.clear();
        }
        if (mapCS.size() > 0)
        {
            for (CallableStatement stmt : mapCS.values())
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException e)
                {
                    logger.debug("StatementFactory close CallableStatement " + stmt + " error.", e);
                }
            }
            mapCS.clear();
        }
    }
}