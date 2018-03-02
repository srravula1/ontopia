/*
 * #!
 * Ontopia Engine
 * #-
 * Copyright (C) 2001 - 2013 The Ontopia Project
 * #-
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * !#
 */

package net.ontopia.persistence.jdbcspy;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * INTERNAL: 
 */

public class SpyPreparedStatement extends SpyStatement implements PreparedStatement {

  protected PreparedStatement pstm;
  protected String sql;

  public SpyPreparedStatement(SpyConnection conn, SpyStats stats, 
			      String sql, PreparedStatement pstm) {
    super(conn, stats, pstm);
    this.sql = sql;
    this.pstm = pstm;
  }

  @Override
  public int executeUpdate()
    throws SQLException {
    long st = System.currentTimeMillis();
    int r = pstm.executeUpdate();
    stats.preparedExecuteUpdate(this, r, st, System.currentTimeMillis());
    return r;
  }

  @Override
  public void addBatch()
    throws SQLException {
    pstm.addBatch();
  }

  @Override
  public int[] executeBatch()
    throws SQLException {
    // overrided from SpyStatement because now we know the actual sql statements
    long st = System.currentTimeMillis();
    int[] r = stm.executeBatch();
    stats.statementExecuteBatch(this, sql, r.length, st, System.currentTimeMillis());
    return r;
  }

  @Override
  public void clearParameters()
    throws SQLException {
    pstm.clearParameters();
  }

  @Override
  public boolean execute()
    throws SQLException {
    long st = System.currentTimeMillis();
    boolean r = pstm.execute();
    stats.preparedExecute(this, st, System.currentTimeMillis());
    return r;
  }

  @Override
  public void setByte(int parameterIndex, byte x)
    throws SQLException {
    pstm.setByte(parameterIndex, x);
  }

  @Override
  public void setDouble(int parameterIndex, double x)
    throws SQLException {
    pstm.setDouble(parameterIndex, x);
  }

  @Override
  public void setFloat(int parameterIndex, float x)
    throws SQLException {
    pstm.setFloat(parameterIndex, x);
  }

  @Override
  public void setInt(int parameterIndex, int x)
    throws SQLException {
    pstm.setInt(parameterIndex, x);
  }

  @Override
  public void setNull(int parameterIndex, int sqlType)
    throws SQLException {
    pstm.setNull(parameterIndex, sqlType);
  }

  @Override
  public void setLong(int parameterIndex, long x)
    throws SQLException {
    pstm.setLong(parameterIndex, x);
  }

  @Override
  public void setShort(int parameterIndex, short x)
    throws SQLException {
    pstm.setShort(parameterIndex, x);
  }

  @Override
  public void setBoolean(int parameterIndex, boolean x)
    throws SQLException {
    pstm.setBoolean(parameterIndex, x);
  }

  @Override
  public void setBytes(int parameterIndex, byte[] x)
    throws SQLException {
    pstm.setBytes(parameterIndex, x);
  }

  @Override
  public void setAsciiStream(int parameterIndex,InputStream x, int length)
    throws SQLException {
    pstm.setAsciiStream(parameterIndex, x, length);
  }
  
  @Override
  public void setBinaryStream(int parameterIndex, InputStream x, int length)
    throws SQLException {
    pstm.setBinaryStream(parameterIndex, x, length);
  }
  
  @Override
  public void setUnicodeStream(int parameterIndex, InputStream x, int length)
    throws SQLException {
    pstm.setUnicodeStream(parameterIndex, x, length);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader x, int length)
    throws SQLException {
    pstm.setCharacterStream(parameterIndex, x, length);
  }

  @Override
  public void setObject(int parameterIndex, Object x)
    throws SQLException {
    pstm.setObject(parameterIndex, x);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType)
    throws SQLException {
    pstm.setObject(parameterIndex, x, targetSqlType);
  }

  @Override
  public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
    throws SQLException {
    pstm.setObject(parameterIndex, x, targetSqlType, scale);
  }

  @Override
  public void setNull(int parameterIndex, int sqlType, String typeName)
    throws SQLException {
    pstm.setNull(parameterIndex, sqlType, typeName);
  }

  @Override
  public void setString(int parameterIndex, String x)
    throws SQLException {
    pstm.setString(parameterIndex, x);
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal x)
    throws SQLException {
    pstm.setBigDecimal(parameterIndex, x);
  }

  @Override
  public void setArray(int parameterIndex, Array x)
    throws SQLException {
    pstm.setArray(parameterIndex, x);
  }

  @Override
  public void setBlob(int parameterIndex, Blob x)
    throws SQLException {
    pstm.setBlob(parameterIndex, x);
  }

  @Override
  public void setClob(int parameterIndex, Clob x)
    throws SQLException {
    pstm.setClob(parameterIndex, x);
  }

  @Override
  public void setDate(int parameterIndex, Date x)
    throws SQLException {
    pstm.setDate(parameterIndex, x);
  }

  @Override
  public void setRef(int parameterIndex, Ref x)
    throws SQLException {
    pstm.setRef(parameterIndex, x);
  }

  @Override
  public ResultSet executeQuery()
    throws SQLException {
    long st = System.currentTimeMillis();
    ResultSet r = new SpyResultSet(this, stats, pstm.executeQuery());
    stats.preparedExecuteQuery(this, st, System.currentTimeMillis());
    return r;
  }

  @Override
  public ResultSetMetaData getMetaData()
    throws SQLException {
    return pstm.getMetaData();
  }

  @Override
  public void setTime(int parameterIndex, Time x)
    throws SQLException {
    pstm.setTime(parameterIndex, x);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x)
    throws SQLException {
    pstm.setTimestamp(parameterIndex, x);
  }

  @Override
  public void setDate(int parameterIndex, Date x, Calendar cal)
    throws SQLException {
    pstm.setDate(parameterIndex, x, cal);
  }

  @Override
  public void setTime(int parameterIndex, Time x, Calendar cal)
    throws SQLException {
    pstm.setTime(parameterIndex, x, cal);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
    throws SQLException {
    pstm.setTimestamp(parameterIndex, x, cal);
  }

  // J2EE 1.4

  @Override
  public ParameterMetaData getParameterMetaData()
    throws SQLException {
    return pstm.getParameterMetaData();
  }

  @Override
  public void setURL(int parameterIndex, URL x)
    throws SQLException {
    pstm.setURL(parameterIndex, x);
  }

  // J2EE 1.6 specifics - comment out remainder of methods if you have to use java 1.5

  @Override
  public void setAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
    pstm.setAsciiStream(i, inputStream, l);
  }

  @Override
  public void setBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
    pstm.setBinaryStream(i, inputStream, l);
  }

  @Override
  public void setCharacterStream(int i, Reader reader, long l) throws SQLException {
    pstm.setCharacterStream(i, reader, l);
  }

  @Override
  public void setAsciiStream(int i, InputStream inputStream) throws SQLException {
    pstm.setAsciiStream(i, inputStream);
  }

  @Override
  public void setBinaryStream(int i, InputStream inputStream) throws SQLException {
    pstm.setBinaryStream(i, inputStream);
  }

  @Override
  public void setCharacterStream(int i, Reader reader) throws SQLException {
    pstm.setCharacterStream(i, reader);
  }

  @Override
  public void setNCharacterStream(int i, Reader reader) throws SQLException {
    pstm.setNCharacterStream(i, reader);
  }

  @Override
  public void setClob(int i, Reader reader) throws SQLException {
    pstm.setClob(i, reader);
  }

  @Override
  public void setBlob(int i, InputStream inputStream) throws SQLException {
    pstm.setBlob(i, inputStream);
  }

  @Override
  public void setNClob(int i, Reader reader) throws SQLException {
    pstm.setNClob(i, reader);
  }

  @Override
  public void setRowId(int i, RowId rowId) throws SQLException {
    pstm.setRowId(i, rowId);
  }

  @Override
  public void setNString(int i, String s) throws SQLException {
    pstm.setNString(i, s);
  }

  @Override
  public void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
    pstm.setNCharacterStream(i, reader, l);
  }

  @Override
  public void setNClob(int i, NClob nClob) throws SQLException {
    pstm.setNClob(i, nClob);
  }

  @Override
  public void setClob(int i, Reader reader, long l) throws SQLException {
    pstm.setClob(i, reader, l);
  }

  @Override
  public void setBlob(int i, InputStream inputStream, long l) throws SQLException {
    pstm.setBlob(i, inputStream, l);
  }

  @Override
  public void setNClob(int i, Reader reader, long l) throws SQLException {
    pstm.setNClob(i, reader, l);
  }

  @Override
  public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
    pstm.setSQLXML(i, sqlxml);
  }

}
