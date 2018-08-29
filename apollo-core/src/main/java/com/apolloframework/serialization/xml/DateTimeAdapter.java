package com.apolloframework.serialization.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * XML Adapter for ISO Date Time (no milliseconds)
 * @author amarenco
 *
 */
public class DateTimeAdapter extends XmlAdapter<String, DateTime> {

    @Override
    public DateTime unmarshal(String v) throws Exception {
        return ISODateTimeFormat.dateTimeNoMillis().parseDateTime(v);
    }

    @Override
    public String marshal(DateTime v) throws Exception {
        return ISODateTimeFormat.dateTimeNoMillis().print(v);
    }

}
