package com.apolloframework.dataaccess.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.joda.time.DateTime;

/**
 * Hibernate {@link UserType} for Joda {@link DateTime} objects
 * @author amarenco
 *
 */
public class DateTimeType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.TIMESTAMP };
    }

    @Override
    public Class<DateTime> returnedClass() {
        return DateTime.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public DateTime nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        Timestamp value = rs.getTimestamp(names[0]);
        return value != null ? new DateTime(value.getTime()) : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        
        if((value == null) || (!(value instanceof DateTime))) {
            st.setNull(index, Types.TIMESTAMP);
        } else {
            DateTime date = (DateTime)value;
            st.setTimestamp(index, new Timestamp(date.toDate().getTime()));
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
