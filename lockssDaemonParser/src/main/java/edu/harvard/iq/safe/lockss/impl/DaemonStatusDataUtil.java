/*
 *  Copyright 2010 President and fellows of Harvard University.
 *                 (Author: Akio Sone)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package edu.harvard.iq.safe.lockss.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 *
 * @author Akio Sone
 */
public class DaemonStatusDataUtil {

    static final Logger logger =
            Logger.getLogger(DaemonStatusDataUtil.class.getName());

    /**
     *
     */
    protected static final String sizeRegex = "^([0-9\\.]+)(TB|GB|MB|KB)$";
    /**
     *
     */
    protected static Pattern pss = null;
    /**
     *
     */
    protected static final Map<String, Double> pwrTbl =
            new HashMap<String, Double>();

    /**
     *
     */
    protected static final Map<String, Long> durationUnits =
            new LinkedHashMap<String, Long>();


    static final String IpAdRegex =
            "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
            + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    static Pattern p = null;
    
    
    static final String FORMAT_PATTERN_FOR_HDR_TMSTMP = "dd MMM yyyy HH:mm:ss Z";
    static final String FORMAT_PATTERN_FOR_LOCKSSBOX_TMSTMP = "HH:mm:ss MM/dd/yy";
    static final String TIMEZONE_GMT0 = "+0000";
    

    static {

        pss = Pattern.compile(sizeRegex);

        durationUnits.put("ms", TimeUnit.MILLISECONDS.toMillis(1));
        durationUnits.put("s", TimeUnit.SECONDS.toMillis(1));
        durationUnits.put("m", TimeUnit.MINUTES.toMillis(1));
        durationUnits.put("h", TimeUnit.HOURS.toMillis(1));
        durationUnits.put("d", TimeUnit.DAYS.toMillis(1));
        durationUnits.put("w", 7L*TimeUnit.DAYS.toMillis(1));

        pwrTbl.put("PB", 5D);
        pwrTbl.put("TB", 4D);
        pwrTbl.put("GB", 3D);
        pwrTbl.put("MB", 2D);
        pwrTbl.put("KB", 1D);

        p = Pattern.compile(IpAdRegex);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String getStdFrmtdTime(Date date) {
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss zz",
                TimeZone.getDefault()).format(date);
    }

    public static String getStdFrmtdTime(Date date, String timezoneId) {
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss zz",
                TimeZone.getTimeZone(timezoneId)).format(date);
    }

    public static String getStdFrmtdTime(Date date, String timestampPattern, String timezoneId) {
        return FastDateFormat.getInstance(timestampPattern,
                TimeZone.getTimeZone(timezoneId)).format(date);
    }
//------------------------------------------------------------------------------

    /**
     *
     * @param s
     * @return
     */
    public static long auSizeConverion(String s) {
        String numberOnly = null;
        if (s.contains(",")) {
            numberOnly = s.replaceAll(",", "");
        } else {
            numberOnly = s;
        }
        return Long.parseLong(numberOnly);
    }

    /**
     *
     * @param s
     * @return
     */
    public static double diskUsageConverion(String s) {
        // 2011-05-18: After the meeting of 05-16,
        // the unit of this variable is set to byte rather than Mb
        return Math.pow(2, 20)*Double.parseDouble(s);
    }

    /**
     *
     * @param rawDaemonVersion
     * @return
     */
    public static String getDaemonVersion(String rawDaemonVersion) {
        String daemonVersion = null;
        String dmnVerRegex = "Daemon\\s+(\\d+\\.\\d+\\.\\d+)\\s+built";
        Pattern pdv = Pattern.compile(dmnVerRegex);
        Matcher m = pdv.matcher(rawDaemonVersion);
        if (m.find()) {
            logger.log(Level.FINE, "LOCKSS-daemon version found={0}", m.group(1));
            daemonVersion = "" + m.group(1);
        } else {
            logger.log(Level.WARNING, "LOCKSS-daemon version is not found");
        }
        return daemonVersion;
    }

    /**
     *
     * @param rawData
     * @return
     */
    public static double getSpaceSize(String rawData) {
        String unit = null;
        String size = null;
        double spaceSize = 0D;

        String data = null;
        if (rawData.contains(",")) {
            data = rawData.replaceAll(",", "");
        } else {
            data = rawData;
        }
//        logger.log(Level.INFO, "token="+data);
        Matcher m = pss.matcher(data);

        if (m.find()) {
//            logger.log(Level.INFO, "size="+m.group(1));
//            logger.log(Level.INFO, "unit="+m.group(2));
            size = m.group(1);
            unit = m.group(2);
        }
        if ((unit != null) && (size != null)) {
            spaceSize = Double.parseDouble(size) * Math.pow(1024D, pwrTbl.get(unit));
//            logger.log(Level.INFO, "raw string("+data+")\tsize="+spaceSize +" bytes");
        }
        return spaceSize;
    }

    /**
     *
     * @param timeStamp
     * @return
     */
    /*
    @Deprecated
    public static long stringToEpocTime(String timeStamp) {
        long epocTime = 0L;
        DateFormat formater = null;
        if (timeStamp.equals("never") || StringUtils.isEmpty(timeStamp)) {
            return epocTime;
        }
        try {
            formater = new SimpleDateFormat("HH:mm:ss MM/dd/yy");
            
            //logger.log(Level.INFO, "timeStamp=" + timeStamp);
            epocTime = ((Date) formater.parse(timeStamp)).getTime();//date.getTime();
            //logger.log(Level.INFO, "AuLastCrawl in long=" + epocTime);
        } catch (java.text.ParseException ex) {
            epocTime = -1L;
            logger.log(Level.SEVERE, "date/time-string parsing error", ex);
        } catch (Exception ex) {
            epocTime = -1L;
            logger.log(Level.SEVERE, "non-parsing error", ex);
        }

        return epocTime;
    }
     */

    public static long getEpocTimeFromString(String timestamp, String timezoneOffset) {
        long epocTime = 0L;
        String timestampWithTimezone=null;
        //FastDateFormat formater = null;
        if (StringUtils.isEmpty(timestamp) || timestamp.equals("never")) {
            return epocTime;
        }
        
        try {
            // timezoneOffset is externally given, box-wise
            // attach its timezone offset token (e.g., -0500)
            // to a timestamp to be evaluated so that it is correctly
            // converted at its correct timezone.
        if (StringUtils.isBlank(timezoneOffset)){
            timestampWithTimezone = timestamp + getJVMTimezoneOffset();
            logger.log(Level.WARNING, 
                    "timezoneOffset is not available: use the JVM timezone={0}", timestampWithTimezone);
        } else {
            timestampWithTimezone = timestamp + timezoneOffset;
        }
            
            logger.log(Level.FINEST, 
                    "After the timezone offset is attached to the timestamp={0}", 
                    timestampWithTimezone);
            // attach the Z format-token to 
            // the default lockssbox timestamp pattern
            String formatPatternForLockssTmstmpWTz =
                    FORMAT_PATTERN_FOR_LOCKSSBOX_TMSTMP + " Z";
            
            epocTime = DateUtils.parseDateStrictly(timestampWithTimezone,
                    formatPatternForLockssTmstmpWTz).getTime();
            
            logger.log(Level.FINEST, "epoch time={0}", epocTime);
        } catch (java.text.ParseException ex) {
            epocTime = -1L;
            logger.log(Level.SEVERE, "date/time-string parsing error", ex);
        } catch (Exception ex) {
            epocTime = -1L;
            logger.log(Level.SEVERE, "non-parsing error", ex);
        }

        return epocTime;
    }

    /**
     *
     * @param duration
     * @return
     */
    public static long durationStringToMilliSeconds(String duration) {
        long milliSeconds = 0L;
        DateFormat formater = null;

        if (StringUtils.isEmpty(duration)) {
            return milliSeconds;
        }

        Map<String, Long> result = new LinkedHashMap<String, Long>();
        List<String> tmps = new ArrayList<String>();
        List<Long> tmpl = new ArrayList<Long>();
        char[] chrs = duration.toCharArray();

        //logger.log(Level.INFO, "duration token=" + duration);
        //out.println("chrs length=" + chrs.length);
        StringBuilder sba = new StringBuilder("");
        StringBuilder sbd = new StringBuilder("");
        boolean isLastOneNumeric = false;
        // frist character is always numeric
        for (int i = 0; i < chrs.length; i++) {
            //out.println(i + "th current token=" + chrs[i]);
            //out.println(tmps);
            if (((int) chrs[i] >= 48) && ((int) chrs[i] <= 57)) {
                // digit
                //out.println(i + "th current token=(" + chrs[i] + ") is a digit");
                // digit
                if (!isLastOneNumeric) {
                    // the last token was a unit string
                    // save this unit string and clear the builder
                    if (!StringUtils.isEmpty(sba.toString())) {
                        tmps.add(sba.toString());
                        sba.setLength(0);
                    }
                }
                //out.println("added to digit");
                sbd.append(chrs[i]);
                isLastOneNumeric = true;
            } else {
                // alphabet
                //out.println(i + "th current token=(" + chrs[i] + ") is an alphabet");
                if (isLastOneNumeric) {
                    // hit the beginning of a new unit
                    sba.append(chrs[i]);
                    // save the digits that have kept so far
                    if (i != 0) {
                        tmpl.add(Long.parseLong(sbd.toString()));
                        sbd.setLength(0);
                    }
                } else {
                    // the last token was a unit string, "m" of ms
                    // save this unit string
                    sba.append(chrs[i]);
                }
                if (i == (chrs.length - 1)) {
                    // last element
                    //out.println("last token");
                    tmps.add(sba.toString());
                    sba.setLength(0);
                }
                isLastOneNumeric = false;
            }
            //out.println(i + "th tmps=" + tmps);
            //out.println(i + "th tmpl=" + tmpl);
        }
        if (tmps.size() != tmpl.size()) {
            // parsing error
            milliSeconds = -1L;
        } else {
            for (int i = 0; i < tmps.size(); i++) {
                milliSeconds += durationUnits.get(tmps.get(i)) * tmpl.get(i);
            }
            //logger.log(Level.INFO, "duration in ms="+milliSeconds);
        }
        return milliSeconds;
    }

    public static boolean isOlderDaemonVersion(String underQuestion,
            String baseline) {
        LockssDaemonVersion uq = new LockssDaemonVersion(underQuestion);
        LockssDaemonVersion bl = new LockssDaemonVersion(baseline);

        return (uq.compareTo(bl) < 0);
    }

    public static String escapeHtml(String rawUrl){
        String sanitizedUrl = null;
        logger.log(Level.FINEST, "received={0}", rawUrl);
        sanitizedUrl = rawUrl.replace("%", "%25");
        sanitizedUrl = sanitizedUrl.replace("~", "%7E");
        sanitizedUrl = sanitizedUrl.replace("|", "%7C");
        sanitizedUrl = sanitizedUrl.replace("&", "%26");
        logger.log(Level.FINEST, "final={0}", sanitizedUrl);
        return sanitizedUrl;
    }

    public static String getPeerIpAddress(String rawIp) {
        if (rawIp == null) {
            return null;
        }
        Matcher m = p.matcher(rawIp);
        if (m.find()) {
            return m.group(0);
        } else {
            return null;
        }
    }

    public static String calculateTimezoneOffset(String hdrTmstmpRaw,
            String tmstmpCurrentTime) {

        String timezoneOffset = null;
        try {
            // working on the timestamp taken from the http response header
            // usually formatted liks this "01 Mar 2012 21:12:51 GMT";
            logger.log(Level.FINE, "Timestamp (timezone GMT) in Http Header={0}",
                    hdrTmstmpRaw);
            // Since GMT is not recognized by DateUtils.parseDate() as GMT+0000,
            // replace it with +0000
            String hdrTmstmp = hdrTmstmpRaw.replace("GMT", TIMEZONE_GMT0);

            Date hdrGMTDate = DateUtils.parseDateStrictly(hdrTmstmp,
                    FORMAT_PATTERN_FOR_HDR_TMSTMP);

            long hdrMillSec = hdrGMTDate.getTime();
            logger.log(Level.FINE, "timestamp in the header in UTC milliseconds={0}",
                    hdrMillSec);


            // tmstmpCurrentTime, i.e., current-time timestamp taken from the 
            // PlatformStatus page is formatted like: "16:12:51 03/01/12";

            logger.log(Level.FINE, "local-time timestamp {0} by the lockss box=",
                    tmstmpCurrentTime);

            // append the timezone token to the format pattern to make sure
            // the parser uses the GMT+0000 timezone
            String formatPatternForLockssTmstmpWTz =
                    FORMAT_PATTERN_FOR_LOCKSSBOX_TMSTMP + " Z";


            Date currentLocalTime = DateUtils.parseDate(
                    tmstmpCurrentTime + " " + TIMEZONE_GMT0,
                    formatPatternForLockssTmstmpWTz);

            Long currentLocalTimeMillSec = currentLocalTime.getTime();
            logger.log(Level.FINE, "Resulting time in UTC milliseconds={0}",
                    currentLocalTimeMillSec);

            // take the difference betwen header and curret-time timestamp
            Long offsetToGMTMillSec = currentLocalTimeMillSec - hdrMillSec;
            logger.log(Level.FINE, "diff in millis={0}", offsetToGMTMillSec);

            Long offsetToGMTmin = TimeUnit.MILLISECONDS.toMinutes(offsetToGMTMillSec);
            logger.log(Level.FINE, "offset To GMT in min={0}", offsetToGMTmin);

            Long hPart = 0L;
            Long mPart = 0L;
            // remove the sign from the min part to avoid a case like -04-30
            mPart = Math.abs(offsetToGMTmin % 60L);
            hPart = offsetToGMTmin / 60L;
            timezoneOffset = getUTCOffsetString(hPart, mPart);
            logger.log(Level.FINE, "timezone offset to be used for this lockss box={0}",
                    timezoneOffset);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, "failed to parse the givne timestamp", ex);
        } finally {
            if (StringUtils.isBlank(timezoneOffset)) {
                timezoneOffset = getJVMTimezoneOffset();
            }
        }
        return timezoneOffset;
    }

    public static String getUTCOffsetString(long hour, long min) {
        long lhour = hour;

        if (Math.abs(hour) >= 0 && Math.abs(hour) <= 23) {
            // within the normal range
            // do nothing
        } else if (Math.abs(hour) == 24) {
            lhour = 0L;
        } else {
            // set to 0
            lhour = 0L;
        }

        long lmin = Math.abs(min);
        if (lmin >= 0 && lmin <= 59) {
            // within the normal range
            // however, there might be some second-level difference between 
            // the header time and the lockss-page rendering one 
            // such as -4:59 instead of -05:00
            if (lmin <= 2){
                // set back to zero
                // no hour change
                lmin = 0L;
                
            } else if (lmin >=28L && lmin <=32L) {
                // min set to 30; no hour change
                lmin =30L;
                
            } else if (lmin >= 58){
                // hour +1, min set to 0
                lmin=0L;
                if (lhour < 0){
                    lhour--;
                } else {
                    lhour++;
                }
            }
            
        } else {
            // out-of-the normal range
            // set to 0
            lmin = 0L;
        }
        
        logger.log(Level.FINEST, "hour={0}=>lhour{1}", 
                new Object[]{hour, lhour});
        logger.log(Level.FINEST, " min={0}=>lmin={1}", 
                new Object[]{min, lmin});
        
        return String.format(" %+03d%02d", lhour, lmin);
    }

    public static String getJVMTimezoneOffset() {
        String jvmTzOffset = null;
        
        Long jvmOffsetMins =
                TimeUnit.MILLISECONDS.toMinutes(TimeZone.getDefault().getRawOffset());
        logger.log(Level.FINE, "This JVM's  timezone offset in min={0}",
                jvmOffsetMins);

        Long jvmHourPart = jvmOffsetMins / 60L;
        // remove the sign from the min part to avoid a case like -04-30
        Long jvmMinPart = Math.abs(jvmOffsetMins % 60L);


        logger.log(Level.FINE, "jvmHourPart={0}", jvmHourPart);
        logger.log(Level.FINE, "jvmMinPart={0}", jvmMinPart);

        jvmTzOffset = getUTCOffsetString(jvmHourPart, jvmMinPart);
        if (StringUtils.isBlank(jvmTzOffset)) {
            logger.log(Level.WARNING, 
                    "JVM's timezone is NOT available: using the GMT+0000");
            jvmTzOffset = TIMEZONE_GMT0;
        }
        logger.log(Level.FINE, "This JVM's  timezone offset={0}", jvmTzOffset);
        return jvmTzOffset;
    }


}
