package com.rr.grm;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class UtilFormatterTest {
    @Test
    public void isNotNullOrEmpty() throws Exception {
        assertEquals(false, UtilFormatter.isNotNullOrEmpty(" "));
    }

    @Test
    public void isNotNullOrEmptyPositive() throws Exception {
        assertEquals(true, UtilFormatter.isNotNullOrEmpty("31st Dec 2012"));
    }
    @Test
    public void isOnOrAfterDateSameDates() throws Exception {
        assertEquals(true, UtilFormatter.isOnOrAfterDate("31st Dec 2012", "31st Dec 2012"));
    }

    @Test
    public void isOnOrAfterDatePositive() throws Exception {
        assertEquals(true, UtilFormatter.isOnOrAfterDate("1st Dec 2012", "31st Dec 2012"));
    }

    @Test
    public void isOnOrAfterDateNegative() throws Exception {
        assertEquals(false, UtilFormatter.isOnOrAfterDate("1st Dec 2012", "21st Nov 2012"));
    }

    @Test
    public void isOnOrBeforeDatePositive() throws Exception {
        assertEquals(true, UtilFormatter.isOnOrBeforeDate("1st Oct 2011", "23rd Mar 2011"));
    }

    @Test
    public void isOnOrBeforeDateNegative() throws Exception {
        assertEquals(false, UtilFormatter.isOnOrBeforeDate( "23rd Mar 2011", "1st Oct 2011"));
    }
    @Test
    public void isOnOrBeforeDateSameDates() throws Exception {
        assertEquals(true, UtilFormatter.isOnOrBeforeDate("1st Oct 2011", "1st Oct 2011"));
    }
    @Test
    public void parseDate() throws Exception {

        DateTime dtf = DateTime.parse("01 Oct 2011", DateTimeFormat.forPattern("dd MMMM yyyy"));

        assertEquals(dtf, UtilFormatter.parseDate("1st Oct 2011"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMalformedDate() throws Exception {

        DateTime dtf = DateTime.parse("031 Oct 2011", DateTimeFormat.forPattern("dd MMMM yyyy"));

        assertEquals(dtf, UtilFormatter.parseDate("1st Oct 2011"));
    }
}