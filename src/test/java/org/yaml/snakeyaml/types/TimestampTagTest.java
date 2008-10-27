/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @see http://yaml.org/type/timestamp.html
 */
public class TimestampTagTest extends AbstractTest {

    public void testTimestamp() throws IOException {
        assertEquals("2001-12-15 at 3:59:43 (100) CET", getText(
                "canonical: 2001-12-15T02:59:43.1Z", "canonical", TimeZone
                        .getTimeZone("Europe/Amsterdam")));
        assertEquals("2001-12-14 at 21:59:43 (100) GMT-05:00", getText(
                "valid iso8601:    2001-12-14t21:59:43.10-05:00", "valid iso8601", TimeZone
                        .getTimeZone("GMT-5:00")));
        assertEquals("2001-12-15 at 3:59:43 (100) CET", getText(
                "valid iso8601:    2001-12-14t21:59:43.10-05:00", "valid iso8601", TimeZone
                        .getTimeZone("CET")));
        assertEquals("2001-12-14 at 21:59:43 (100) GMT-05:00", getText(
                "space separated:  2001-12-14 21:59:43.10 -5", "space separated", TimeZone
                        .getTimeZone("GMT-5:00")));
        assertEquals("Must be UTC time.", "2001-12-15 at 2:59:43 (100) UTC", getText(
                "no time zone (Z): 2001-12-15 2:59:43.10", "no time zone (Z)", TimeZone
                        .getTimeZone("UTC")));
        assertEquals(
                "If the time zone is omitted, the timestamp is assumed to be specified in UTC.",
                "2001-12-15 at 2:59:43 (100) UTC", getText(
                        "no time zone (Z): 2001-12-15 2:59:43.10", "no time zone (Z)", TimeZone
                                .getTimeZone("UTC")));
        assertEquals("2002-12-14 at 0:0:0 UTC", getText("date (00:00:00Z): 2002-12-14",
                "date (00:00:00Z)", TimeZone.getTimeZone("UTC")));

    }

    public void testTimestampShorthand() throws IOException {
        assertTrue(getMapValue("canonical: !!timestamp 2001-12-15T02:59:43.1Z", "canonical") instanceof Date);
    }

    public void testTimestampTag() throws IOException {
        assertTrue(getMapValue("canonical: !<tag:yaml.org,2002:timestamp> 2001-12-15T02:59:43.1Z",
                "canonical") instanceof Date);
    }

    public void testTimestampOut() throws IOException {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        cal.clear();
        cal.set(2008, 8, 23, 14, 35, 4);
        Date date = cal.getTime();
        Map<String, Date> map = new HashMap<String, Date>();
        map.put("canonical", date);
        String output = dump(map);
        assertEquals("--- \ncanonical: 2008-09-23T10:35:04Z\n", output);
        assertTrue("" + output, output.contains("canonical: 2008-09-23T10:35:04Z"));
    }

    private String getText(String yaml, String key, TimeZone timezone) {
        Date date = (Date) getMapValue(yaml, key);
        Calendar cal = Calendar.getInstance(timezone);
        cal.setTime(date);
        int years = cal.get(Calendar.YEAR);
        int months = cal.get(Calendar.MONTH) + 1; // 0..12
        int days = cal.get(Calendar.DAY_OF_MONTH); // 1..31
        int hour24 = cal.get(Calendar.HOUR_OF_DAY); // 0..24
        int minutes = cal.get(Calendar.MINUTE); // 0..59
        int seconds = cal.get(Calendar.SECOND); // 0..59
        int millis = cal.get(Calendar.MILLISECOND);
        String beginning = String.valueOf(years) + "-" + String.valueOf(months) + "-"
                + String.valueOf(days) + " at " + String.valueOf(hour24) + ":"
                + String.valueOf(minutes) + ":" + String.valueOf(seconds);
        if (millis > 0) {
            beginning += " (" + String.valueOf(millis) + ")";
        }
        return beginning + " " + timezone.getDisplayName(false, TimeZone.SHORT);
    }

    public void testTimestampReadWrite() throws IOException {
        Date date = (Date) getMapValue("Time: 2001-11-23 15:01:42 -5", "Time");
        Map<String, Date> map = new HashMap<String, Date>();
        map.put("canonical", date);
        String output = dump(map);
        assertEquals("--- \ncanonical: 2001-11-23T20:01:42Z\n", output);
    }
}
