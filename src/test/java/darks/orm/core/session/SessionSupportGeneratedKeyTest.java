package darks.orm.core.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import darks.orm.core.session.SqlSessionImpl;
import darks.orm.datasource.Transaction;
import darks.orm.datasource.factory.StatementFactory.StatementType;

/**
 * Verifies generated-key retrieval is not gated on auto-commit, and that SELECT
 * key feedback uses a separate statement from the INSERT.
 */
public class SessionSupportGeneratedKeyTest
{

    @Test
    public void executeUpdateGeneratedKeyReturnsKeyWhenAutoCommitDisabled()
        throws Exception
    {
        StubTransaction tx = new StubTransaction(false);
        tx.generatedKey = Long.valueOf(42L);

        SqlSessionImpl session = new SqlSessionImpl();
        injectTransaction(session, tx);

        Object key = session.executeUpdateGeneratedKey("insert into t(name) values(?)", "n");
        assertEquals(Long.valueOf(42L), key);
        assertTrue("insert must execute even when autoCommit is false", tx.executeUpdateCalls > 0);
    }

    @Test
    public void executeUpdateSelectKeyUsesSeparateStatement()
        throws Exception
    {
        StubTransaction tx = new StubTransaction(true);
        tx.selectKeyValue = Integer.valueOf(99);

        SqlSessionImpl session = new SqlSessionImpl();
        injectTransaction(session, tx);

        Method method = SessionSupport.class.getDeclaredMethod("executeUpdateSelectKey",
            String.class, String.class, Class.class, Object[].class);
        method.setAccessible(true);
        Object key = method.invoke(session, "insert into t(name) values(?)",
            "select last_insert_id()", Integer.class, new Object[] {"n"});

        assertEquals(Integer.valueOf(99), key);
        assertEquals("INSERT and key SQL must use distinct prepared statements", 2, tx.preparedSqls.size());
        assertEquals("insert into t(name) values(?)", tx.preparedSqls.get(0));
        assertEquals("select last_insert_id()", tx.preparedSqls.get(1));
        assertTrue("key SQL must be executed via executeQuery on its own statement",
            tx.keyExecuteQueryCalls.get() > 0);
        assertEquals("INSERT statement must not receive execute(String)", 0, tx.insertExecuteStringCalls.get());
    }

    private static void injectTransaction(SqlSessionImpl session, Transaction tx)
        throws Exception
    {
        Field txField = SessionSupport.class.getDeclaredField("tx");
        txField.setAccessible(true);
        txField.set(session, tx);
        Field initedField = SessionSupport.class.getDeclaredField("inited");
        initedField.setAccessible(true);
        initedField.set(session, Boolean.TRUE);
    }

    private static final class StubTransaction implements Transaction
    {
        private boolean autoCommit;

        Object generatedKey = Long.valueOf(1L);

        Object selectKeyValue = Integer.valueOf(1);

        int executeUpdateCalls;

        final List<String> preparedSqls = new ArrayList<String>();

        final AtomicInteger keyExecuteQueryCalls = new AtomicInteger();

        final AtomicInteger insertExecuteStringCalls = new AtomicInteger();

        StubTransaction(boolean autoCommit)
        {
            this.autoCommit = autoCommit;
        }

        public java.sql.Connection getConnection()
        {
            return null;
        }

        public java.sql.Connection getConnection(boolean isCreate)
        {
            return null;
        }

        public void setConnection(java.sql.Connection connection)
        {
        }

        public PreparedStatement getPreparedStatement(String sql)
        {
            return getPreparedStatement(sql, StatementType.Normal);
        }

        public PreparedStatement getPreparedStatement(String sql, StatementType type)
        {
            return getPreparedStatement(sql, type, false);
        }

        public PreparedStatement getPreparedStatement(String sql, StatementType type, boolean isDirect)
        {
            preparedSqls.add(sql);
            final boolean isKeySql = preparedSqls.size() > 1;
            final Object key = isKeySql ? selectKeyValue : generatedKey;
            return (PreparedStatement)Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
                new Class[] {PreparedStatement.class}, new InvocationHandler()
                {
                    public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable
                    {
                        String name = method.getName();
                        if ("executeUpdate".equals(name))
                        {
                            executeUpdateCalls++;
                            return Integer.valueOf(1);
                        }
                        if ("execute".equals(name))
                        {
                            if (args != null && args.length == 1 && args[0] instanceof String)
                            {
                                insertExecuteStringCalls.incrementAndGet();
                                return Boolean.TRUE;
                            }
                            return Boolean.FALSE;
                        }
                        if ("executeQuery".equals(name))
                        {
                            if (isKeySql)
                            {
                                keyExecuteQueryCalls.incrementAndGet();
                            }
                            return resultSet(key);
                        }
                        if ("getGeneratedKeys".equals(name))
                        {
                            return resultSet(generatedKey);
                        }
                        if ("getResultSet".equals(name))
                        {
                            return resultSet(key);
                        }
                        if ("setObject".equals(name) || "close".equals(name))
                        {
                            return null;
                        }
                        Class<?> rt = method.getReturnType();
                        if (rt == boolean.class)
                        {
                            return Boolean.FALSE;
                        }
                        if (rt == int.class)
                        {
                            return Integer.valueOf(0);
                        }
                        return null;
                    }
                });
        }

        public java.sql.CallableStatement getCallableStatement(String sql)
        {
            return null;
        }

        public void close()
        {
        }

        public boolean isClosed()
        {
            return false;
        }

        public boolean isAutoCommit()
        {
            return autoCommit;
        }

        public void setAutoCommit(boolean isAutoCommit)
            throws SQLException
        {
            this.autoCommit = isAutoCommit;
        }

        public void commit()
            throws SQLException
        {
        }

        public void rollback()
            throws SQLException
        {
        }

        public void rollback(java.sql.Savepoint point)
            throws SQLException
        {
        }

        public java.sql.Savepoint setSavepoint()
            throws SQLException
        {
            return null;
        }

        public java.sql.Savepoint setSavepoint(String name)
            throws SQLException
        {
            return null;
        }

        private static ResultSet resultSet(final Object value)
        {
            final boolean[] next = new boolean[] {true};
            return (ResultSet)Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                new Class[] {ResultSet.class}, new InvocationHandler()
                {
                    public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable
                    {
                        String name = method.getName();
                        if ("next".equals(name))
                        {
                            if (next[0])
                            {
                                next[0] = false;
                                return Boolean.TRUE;
                            }
                            return Boolean.FALSE;
                        }
                        if ("getObject".equals(name) || "getInt".equals(name) || "getLong".equals(name)
                            || "getString".equals(name))
                        {
                            return value;
                        }
                        if ("wasNull".equals(name))
                        {
                            return Boolean.FALSE;
                        }
                        if ("close".equals(name))
                        {
                            return null;
                        }
                        Class<?> rt = method.getReturnType();
                        if (rt == boolean.class)
                        {
                            return Boolean.FALSE;
                        }
                        if (rt == int.class)
                        {
                            return Integer.valueOf(0);
                        }
                        return null;
                    }
                });
        }
    }
}
