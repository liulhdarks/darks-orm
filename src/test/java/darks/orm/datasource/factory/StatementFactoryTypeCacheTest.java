package darks.orm.datasource.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;

import darks.orm.datasource.factory.StatementFactory.StatementType;

/**
 * Verifies PreparedStatement cache keys are scoped by StatementType so a prior
 * Normal prepare cannot satisfy a later GenerateKey request for the same SQL.
 */
public class StatementFactoryTypeCacheTest
{

    @Test
    public void generateKeyDoesNotReuseNormalPreparedStatement()
        throws Exception
    {
        StatementFactory factory = new StatementFactory();
        String sql = "insert into item(name) values (?)";

        Method cacheKey = StatementFactory.class.getDeclaredMethod("statementCacheKey", String.class,
            StatementType.class);
        cacheKey.setAccessible(true);

        String normalKey = (String)cacheKey.invoke(null, sql, StatementType.Normal);
        String generateKey = (String)cacheKey.invoke(null, sql, StatementType.GenerateKey);
        assertTrue("Normal and GenerateKey cache keys must differ", !normalKey.equals(generateKey));

        Field mapField = StatementFactory.class.getDeclaredField("mapPS");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ConcurrentMap<String, PreparedStatement> mapPS =
            (ConcurrentMap<String, PreparedStatement>)mapField.get(factory);

        PreparedStatement normalStub = new MinimalPreparedStatement();
        PreparedStatement generateStub = new MinimalPreparedStatement();
        mapPS.put(normalKey, normalStub);

        // Simulate getPrepareStatementGenerateKey lookup: must miss the Normal entry.
        assertNull(mapPS.get(generateKey));

        mapPS.put(generateKey, generateStub);
        assertNotSame(mapPS.get(normalKey), mapPS.get(generateKey));
        assertEquals(normalStub, mapPS.get(normalKey));
        assertEquals(generateStub, mapPS.get(generateKey));
    }

    /**
     * Minimal stub - only identity matters for this cache-key test.
     */
    private static final class MinimalPreparedStatement implements PreparedStatement
    {
        public void close() {}
        public boolean isClosed() { return false; }
        public void clearParameters() {}
        public void setFetchSize(int rows) {}
        public int getFetchSize() { return 0; }
        public <T> T unwrap(Class<T> iface) { return null; }
        public boolean isWrapperFor(Class<?> iface) { return false; }
        public java.sql.ResultSet executeQuery(String sql) { return null; }
        public int executeUpdate(String sql) { return 0; }
        public boolean execute(String sql) { return false; }
        public java.sql.ResultSet executeQuery() { return null; }
        public int executeUpdate() { return 0; }
        public boolean execute() { return false; }
        public java.sql.ResultSet getResultSet() { return null; }
        public int getUpdateCount() { return 0; }
        public boolean getMoreResults() { return false; }
        public void setObject(int parameterIndex, Object x) {}
        public void setString(int parameterIndex, String x) {}
        public void setInt(int parameterIndex, int x) {}
        public void setLong(int parameterIndex, long x) {}
        public void setBoolean(int parameterIndex, boolean x) {}
        public void setByte(int parameterIndex, byte x) {}
        public void setShort(int parameterIndex, short x) {}
        public void setFloat(int parameterIndex, float x) {}
        public void setDouble(int parameterIndex, double x) {}
        public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) {}
        public void setBytes(int parameterIndex, byte[] x) {}
        public void setDate(int parameterIndex, java.sql.Date x) {}
        public void setTime(int parameterIndex, java.sql.Time x) {}
        public void setTimestamp(int parameterIndex, java.sql.Timestamp x) {}
        public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) {}
        @Deprecated public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) {}
        public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) {}
        public void setObject(int parameterIndex, Object x, int targetSqlType) {}
        public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) {}
        public void setRef(int parameterIndex, java.sql.Ref x) {}
        public void setBlob(int parameterIndex, java.sql.Blob x) {}
        public void setClob(int parameterIndex, java.sql.Clob x) {}
        public void setArray(int parameterIndex, java.sql.Array x) {}
        public java.sql.ResultSetMetaData getMetaData() { return null; }
        public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar cal) {}
        public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar cal) {}
        public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar cal) {}
        public void setNull(int parameterIndex, int sqlType) {}
        public void setNull(int parameterIndex, int sqlType, String typeName) {}
        public void setURL(int parameterIndex, java.net.URL x) {}
        public void setRowId(int parameterIndex, java.sql.RowId x) {}
        public void setNString(int parameterIndex, String value) {}
        public void setNCharacterStream(int parameterIndex, java.io.Reader value, long length) {}
        public void setNClob(int parameterIndex, java.sql.NClob value) {}
        public void setClob(int parameterIndex, java.io.Reader reader, long length) {}
        public void setBlob(int parameterIndex, java.io.InputStream inputStream, long length) {}
        public void setNClob(int parameterIndex, java.io.Reader reader, long length) {}
        public void setSQLXML(int parameterIndex, java.sql.SQLXML xmlObject) {}
        public void setAsciiStream(int parameterIndex, java.io.InputStream x, long length) {}
        public void setBinaryStream(int parameterIndex, java.io.InputStream x, long length) {}
        public void setCharacterStream(int parameterIndex, java.io.Reader reader, long length) {}
        public void setAsciiStream(int parameterIndex, java.io.InputStream x) {}
        public void setBinaryStream(int parameterIndex, java.io.InputStream x) {}
        public void setCharacterStream(int parameterIndex, java.io.Reader reader) {}
        public void setNCharacterStream(int parameterIndex, java.io.Reader value) {}
        public void setClob(int parameterIndex, java.io.Reader reader) {}
        public void setBlob(int parameterIndex, java.io.InputStream inputStream) {}
        public void setNClob(int parameterIndex, java.io.Reader reader) {}
        public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) {}
        public void addBatch() {}
        public void clearBatch() {}
        public int[] executeBatch() { return new int[0]; }
        public java.sql.Connection getConnection() { return null; }
        public boolean getMoreResults(int current) { return false; }
        public java.sql.ResultSet getGeneratedKeys() { return null; }
        public int executeUpdate(String sql, int autoGeneratedKeys) { return 0; }
        public int executeUpdate(String sql, int[] columnIndexes) { return 0; }
        public int executeUpdate(String sql, String[] columnNames) { return 0; }
        public boolean execute(String sql, int autoGeneratedKeys) { return false; }
        public boolean execute(String sql, int[] columnIndexes) { return false; }
        public boolean execute(String sql, String[] columnNames) { return false; }
        public int getResultSetHoldability() { return 0; }
        public void setCursorName(String name) {}
        public void setEscapeProcessing(boolean enable) {}
        public int getMaxFieldSize() { return 0; }
        public void setMaxFieldSize(int max) {}
        public int getMaxRows() { return 0; }
        public void setMaxRows(int max) {}
        public int getQueryTimeout() { return 0; }
        public void setQueryTimeout(int seconds) {}
        public void cancel() {}
        public java.sql.SQLWarning getWarnings() { return null; }
        public void clearWarnings() {}
        public void setFetchDirection(int direction) {}
        public int getFetchDirection() { return 0; }
        public int getResultSetConcurrency() { return 0; }
        public int getResultSetType() { return 0; }
        public void addBatch(String sql) {}
        public void setPoolable(boolean poolable) {}
        public boolean isPoolable() { return false; }
        public java.sql.ParameterMetaData getParameterMetaData() { return null; }
        public void closeOnCompletion() {}
        public boolean isCloseOnCompletion() { return false; }
    }
}
