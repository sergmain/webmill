/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.riverock.common.tools;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class RsetTools {
    private final static Logger log = Logger.getLogger( RsetTools.class );

    public static void setString( final PreparedStatement ps, final int index, final String data)
        throws SQLException
    {
        if (data!=null)
            ps.setString(index, data);
        else
            ps.setNull(index, Types.VARCHAR);
    }

    public static void setLong( final PreparedStatement ps, final int index, final Long data)
        throws SQLException
    {
        if (data!=null)
            ps.setLong(index, data.longValue());
        else
            ps.setNull(index, Types.NUMERIC);
    }

    public static void setInt( final PreparedStatement ps, final int index, final Integer data)
        throws SQLException
    {
        if (data!=null)
            ps.setInt(index, data.intValue());
        else
            ps.setNull(index, Types.NUMERIC);
    }

    public static void setDouble( final PreparedStatement ps, final int index, final Double data)
        throws SQLException
    {
        if (data!=null)
            ps.setDouble(index, data.doubleValue());
        else
            ps.setNull(index, Types.NUMERIC);
    }

    public static void setFloat( final PreparedStatement ps, final int index, final Float data)
        throws SQLException
    {
        if (data!=null)
            ps.setFloat(index, data.floatValue());
        else
            ps.setNull(index, Types.NUMERIC);
    }

    public static void setTimestamp( final PreparedStatement ps, final int index, final Timestamp data)
        throws SQLException
    {
        if (data!=null)
            ps.setTimestamp(index, data);
        else
            ps.setNull(index, Types.DATE);
    }

    public static String getColumnName( final ResultSet rs, final int columnNumber )
        throws SQLException
    {
        ResultSetMetaData rsmd = rs.getMetaData();
        return rsmd.getColumnName( columnNumber );
    }

    public static String getStringDate( final ResultSet rs, final String f,
        final String mask, final String def, final Locale loc )
        throws SQLException
    {
        String s = DateTools.getStringDate( getCalendar( rs, f, null ), mask, loc );

        if ( s==null )
            return def;

        return s;
    }

    /**
     * Возвращает int значение из текущей записи. Если поле не найдено, возвращает 0
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * </blockquote>
     */
    public static Calendar getCalendar( final ResultSet rs, final String f )
        throws SQLException
    {
        return getCalendar( rs, f, null );
    }

    /**
     * Возвращает int значение из текущей записи. Если поле не найдено, возвращает 0
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * int def - возвращаемое значение, если поле не найдено
     * </blockquote>
     */
    public static Calendar getCalendar( final ResultSet rs, final String f, final Calendar def )
        throws SQLException
    {
        if ( rs==null || f==null )
            return null;

//        Object obj = null;
        try
        {
            java.util.Date date = rs.getTimestamp( f );
            if (rs.wasNull())
                return def;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis( date.getTime() );
            return calendar;
/*
            obj = rs.getObject( f );
            if ( obj==null )
                return def;

            Calendar c = new GregorianCalendar();
            c.setTime( rs.getTime( f ) );

            int hour = c.get( Calendar.HOUR_OF_DAY );
            int min = c.get( Calendar.MINUTE );
            int sec = c.get( Calendar.SECOND );
            int millsec = c.get( Calendar.MILLISECOND );

            c.setTime( rs.getDate( f ) );

            c.add( Calendar.HOUR_OF_DAY, hour );
            c.add( Calendar.MINUTE, min );
            c.add( Calendar.SECOND, sec );
            c.add( Calendar.MILLISECOND, millsec );
            return c;
*/
        }
        catch (SQLException e)
        {
            log.error( "Error get Calendar value from field '"+f+"' ", e );
            throw e;
        }
    }

    public static Timestamp getTimestamp( final ResultSet rs, final String f )
        throws SQLException
    {
        if ( rs==null || f==null )
            return null;

        Timestamp stamp = null;
        try
        {
            stamp = rs.getTimestamp( f );
            if (rs.wasNull())
                return null;
        }
        catch (SQLException exc)
        {
            log.error( "Error get timestamp field '"+f+"'", exc );
            throw exc;
        }

        return stamp;
    }

    /**
     * Возвращает int значение из текущей записи. Если поле не найдено, возвращает 0
     * @param rs ResultSet с текущей записью выборки
     * @param f имя поля
     * @return java.util.Date если значение есть, иначе null
     * @throws SQLException
     */
    public static java.util.Date getDate( final ResultSet rs, final String f )
        throws SQLException
    {
        if ( rs==null || f==null )
            return null;

        Calendar cal = getCalendar( rs, f, null );
        if ( cal==null )
            return null;

        return cal.getTime();
    }

    /**
     * Возвращает int значение из текущей записи. Если поле не найдено, возвращает 0
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * </blockquote>
     */
    public static Integer getInt( final ResultSet rs, final String f )
        throws SQLException
    {
        return getInt( rs, f, null );
    }

    /**
     * Возвращает int значение из текущей записи. Если поле не найдено, возвращает 0
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * int def - возвращаемое значение, если поле не найдено
     * </blockquote>
     */
    public static Integer getInt( final ResultSet rs, final String f, final Integer def )
        throws SQLException
    {
        if ( rs==null || f==null )
            return def;

        try
        {
            int temp = rs.getInt(f);
            if (rs.wasNull())
                return def;

            return new Integer( temp );
        }
        catch (SQLException exc)
        {
            log.error( "Error get Integer field '"+f+"' from ResultSet", exc );
            throw exc;
        }
    }

    /**
     * Возвращает long значение из текущей записи. Если поле не найдено, возвращает 0
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * </blockquote>
     */
    public static Long getLong( final ResultSet rs, final String f )
        throws SQLException
    {
        return getLong( rs, f, null );
    }

    /**
     * Возвращает long значение из текущей записи. Если поле не найдено, возвращает 0
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * long def - возвращаемое значение, если поле не найдено
     * </blockquote>
     */
    public static Long getLong( final ResultSet rs, final String f, final Long def )
        throws SQLException
    {

        if ( rs==null || f==null )
            return def;

        try
        {
            long temp = rs.getLong( f );
            if (rs.wasNull())
                return def;

            return new Long(temp);
        }
        catch (SQLException exc)
        {
            log.error( "Error get Long field '"+f+"'", exc );
            throw exc;
        }

    }

    /**
     * Возвращает float значение из текущей записи. Если поле не найдено, возвращает 0
     * @param rs - ResultSet с текущей записью выборки
     * @param f - String, имя поля
     * @return float значение из текущей записи
     */
    public static Float getFloat( final ResultSet rs, final String f )
        throws SQLException
    {
        return getFloat( rs, f, null );
    }

    /**
     Возвращает float значение из текущей записи. Если поле не найдено, возвращает 0
     * @param rs - ResultSet с текущей записью выборки
     * @param f - String, имя поля
     * @param def - возвращаемое значение по умолчанию, если поле не найдено
     * @return float значение из текущей записи
     */
    public static Float getFloat( final ResultSet rs, final String f, final Float def )
        throws SQLException
    {
        if ( rs==null || f==null )
            return def;

        try
        {
            float temp = rs.getLong( f );
            if (rs.wasNull())
                return def;

            return new Float(temp);
        }
        catch (SQLException exc)
        {
            log.error( "Error get Float field '"+f+"' from ResultSet", exc );
            throw exc;
        }
    }

    /**
     Возвращает double значение из текущей записи. Если поле не найдено, возвращает 0.0
     * @param rs - ResultSet с текущей записью выборки
     * @param f - String, имя поля
     * @return double значение из текущей записи
     */
    public static Double getDouble( final ResultSet rs, final String f )
        throws SQLException
    {
        return getDouble( rs, f, null );
    }

    /**
     Возвращает double значение из текущей записи. Если поле не найдено, возвращает 0.0
     * @param rs - ResultSet с текущей записью выборки
     * @param f - String, имя поля
     * @param def - возвращаемое значение по умолчанию, если поле не найдено
     * @return double значение из текущей записи
     */
    public static Double getDouble( final ResultSet rs, final String f, final Double def )
        throws SQLException
    {
        if ( rs==null || f==null )
            return def;

        Double i = def;

        try
        {
            i = ( rs.getObject( f )!=null ) ? new Double(rs.getDouble( f )) : def;
        }
        catch (SQLException exc)
        {
            log.error( "Error get Double field '"+f+"' from ResultSet", exc );
            throw exc;
        }

        return i;
    }

    public static BigDecimal getBigDecimal( final ResultSet rs, final String f )
        throws SQLException
    {
        return getBigDecimal( rs, f, null );
    }

    /**
     * Возвращает BigDecimal значение из текущей записи. Если поле не найдено, возвращает 0.0
     * @param rs - ResultSet с текущей записью выборки
     * @param f - String, имя поля
     * @param def - возвращаемое значение по умолчанию, если поле не найдено
     * @return double значение из текущей записи
     */
    public static BigDecimal getBigDecimal( final ResultSet rs, final String f, final BigDecimal def )
        throws SQLException
    {
        if ( rs==null || f==null )
            return def;

        BigDecimal i = def;

        try
        {
            i = ( rs.getObject( f )!=null ) ? rs.getBigDecimal( f ) : def;
        }
        catch (SQLException exc)
        {
            log.error( "Error get BigDecimal field '"+f+"' from ResultSet", exc );
            throw exc;
        }

        return i;
    }

    /**
     * Возвращает String значение из текущей записи. Если поле не найдено, возвращает пустую строку
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * </blockquote>
     */
    public static String getString( final ResultSet rs, final String f )
        throws SQLException
    {
        return getString( rs, f, null );
    }

    /**
     * Возвращает String значение из текущей записи. Если поле не найдено, возвращает пустую строку
     * Параметры:
     * <blockquote>
     * ResultSet rs - ResultSet с текущей записью выборки
     * String f - имя поля
     * String def - возвращаемое значение, если поле не найдено
     * </blockquote>
     */
    public static String getString( final ResultSet rs, final String f, final String def )
        throws SQLException
    {
        if ( rs==null || f==null )
            return def;

        try
        {
            Object obj = rs.getObject( f );
            if (rs.wasNull())
                return def;

            return obj.toString();

        }
        catch (SQLException exc)
        {
            log.error( "Error get String field '"+f+"' from ResultSet, sql error code ", exc );
            throw exc;
        }

    }

}