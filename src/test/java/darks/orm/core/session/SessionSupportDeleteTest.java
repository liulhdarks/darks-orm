package darks.orm.core.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import darks.orm.core.session.model.LongIdEntity;
import darks.orm.datasource.Transaction;
import darks.orm.datasource.factory.StatementFactory.StatementType;

public class SessionSupportDeleteTest
{

    @Test
    public void deleteEntitySupportsNonIntegerPrimaryKey()
        throws Exception
    {
        CapturingTransaction tx = new CapturingTransaction();
        SqlSessionImpl session = new SqlSessionImpl();
        injectTransaction(session, tx);

        LongIdEntity entity = new LongIdEntity();
        entity.setId(Long.valueOf(9007199254740993L));
        entity.setName("row");

        session.delete(entity);

        assertEquals(1, tx.boundParams.size());
        assertEquals(Long.valueOf(9007199254740993L), tx.boundParams.get(0));
        assertTrue(tx.lastSql.toLowerCase().contains("delete from"));
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

    private static final class CapturingTransaction implements Transaction
    {
        String lastSql;

        final List<Object> boundParams = new ArrayList<Object>();

        private boolean autoCommit = true;

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
            lastSql = sql;
            return (PreparedStatement)Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
                new Class[] {PreparedStatement.class}, new InvocationHandler()
                {
                    public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable
                    {
                        if ("setObject".equals(method.getName()) && args != null && args.length >= 2)
                        {
                            boundParams.add(args[1]);
                            return null;
                        }
                        if ("executeUpdate".equals(method.getName()))
                        {
                            return Integer.valueOf(1);
                        }
                        if ("close".equals(method.getName()))
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
    }
}
