/*
 *  Copyright 2012 President and fellows of Harvard University.
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


import edu.harvard.iq.safe.lockss.api.LOCKSSPlatformStatusParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Akio Sone
 */
public class LOCKSSPlatformStatusHtmlParser implements LOCKSSPlatformStatusParser {

    private static final Logger logger =
            Logger.getLogger(LOCKSSPlatformStatusHtmlParser.class.getName());

    /**
     *
     */
    protected String tableId = "PlatformStatus";

    /**
     * 1st-line items go to boxInfoMap
     * host=
     * time=
     * up=
     * version=
     * platform=
     */
    protected Map<String, String> boxInfoMap = new LinkedHashMap<String, String>();
    /**
     * 3rd-line items go to summaryInfoMap
     * table=
     * Hostname=
     * IP Address=
     * Group=usdocspln,
     * V3 Identity=
     * Mail Relay=
     * Admin Email=
     * Disks=
     * Current Time=
     * Uptime=
     * Daemon Version=
     * Platform=
     * Cwd=
     * Props=
     *
     */
    protected Map<String, String> summaryInfoMap = new LinkedHashMap<String, String>();


    static Set<String> fieldNameSet = new LinkedHashSet<String>();

    static Pattern currentTimeStampPattern =
            Pattern.compile("\\)\\s*(\\d{2}:\\d{2}:\\d{2} \\d{2}/\\d{2}/\\d{2}),\\s*up\\s+(\\w+)");

    static String[] FIELD_NAME_SET = new String[]{
        "Hostname",
        "IP Address",
        "Group",
        "V3 Identity",
        "Admin Email",
        "Disks",
        "Current Time",
        "Uptime",
        "Daemon Version",
        "Platform",
        "Cwd",
        "Props"
    };

    static String[] FIELD_NAME_SET_BEFEORE_1_53_3 = new String[]{
        "Hostname",
        "IP Address",
        "Group",
        "V3 Identity",
        "Admin Email",
        "Disks",
        "Cwd",
        "Props"
    };

    static String[] BOX_INFO_FIELD_NAME_SET = new String[]{
        "host",
        "time",
        "up",
        "version",
        "platform"
    };

    static Matcher tmstmpMatcher = null;

    static {
        for (String tagName : FIELD_NAME_SET) {
            fieldNameSet.add(tagName);
        }
    }

    /**
     *
     * @param is
     */
    @Override
    public void getPlatformStatusData(InputStream is) {

        try {

            Document doc = DataUtil.load(is, "UTF-8", "");
            Element body = doc.body();

            // most of the target items are sandwitched by <b> tag
            // this can be used to reach each target item.
            String tmpCurrentTime = null;
            String tmpUpTime = null;
            String currentTime = null;
            Elements tags = body.getElementsByTag("b");

            for (Element tag : tags) {

                // get the current-time string: for 1.52.3 or older daemons
                // this is the ony place to get it.
                String tagText = tag.text();
                logger.log(Level.FINE, "working on tagText={0}", tagText);

                if (tagText.equals("Daemon Status")) {
                    // find current time and up running
                    currentTime = tag.parent().parent().text();
                    logger.log(Level.INFO, "currentTime text=[{0}]", currentTime);
                    // "currentTime =Daemon Status lockss.statelib.lib.in.us (usdocspln group) 01:25:55 03/01/12, up 7d5h21m"
                    tmstmpMatcher = currentTimeStampPattern.matcher(currentTime);

                    if (tmstmpMatcher.find()) {
                        logger.log(Level.INFO, "group 0={0}", tmstmpMatcher.group(0));
                        tmpCurrentTime = tmstmpMatcher.group(1);
                        logger.log(Level.INFO, "Current Time:group 1={0}", tmpCurrentTime);
                        tmpUpTime = tmstmpMatcher.group(2);
                        logger.log(Level.INFO, "UpTime:group 2={0}", tmpUpTime);
                    }
                }

                // get the remaining key-value sets
                if (fieldNameSet.contains(tagText)) {

                    Element parent = tag.parent();
                    String fieldValue =
                            parent.nextElementSibling().text();
                    logger.log(Level.FINE, "{0}={1}", 
                                new Object[]{tagText, fieldValue});
                    summaryInfoMap.put(tagText, fieldValue);
                }
            }

            // extract the daemon version and platform info that are located
            // at the bottom
            // these data are sandwitched by a <center> tag
            Elements ctags = body.getElementsByTag("center");
            String version = null;
            String platform = null;
            for (Element ctag : ctags) {
                String cText = ctag.text();
                logger.log(Level.FINE, "center tag Text={0}", cText);
                // cText is like this:
                // Daemon 1.53.3 built 28-Jan-12 01:06:36 on build7.lockss.org, Linux RPM 1
                if (StringUtils.isNotBlank(cText)
                        && ctag.child(0).nodeName().equals("font")) {
                    String[] versionPlatform = cText.split(", ");
                    if (versionPlatform.length == 2) {
                        logger.log(Level.INFO, "daemon version={0};platform={1}", versionPlatform);
                        version = DaemonStatusDataUtil.getDaemonVersion(versionPlatform[0]);
                        platform = versionPlatform[1];
                    } else {
                        // the above regex failed
                        logger.log(Level.WARNING, "String-formatting differs; use pattern matching");
                        version = DaemonStatusDataUtil.getDaemonVersion(cText);
                        int platformOffset = cText.lastIndexOf(", ") + 2;
                        platform = cText.substring(platformOffset);
                        logger.log(Level.INFO, "platform={0}", platform);

                    }
                }
            }

            if (summaryInfoMap.containsKey("V3 Identity")) {
                String ipAddress =
                        DaemonStatusDataUtil.getPeerIpAddress(summaryInfoMap.get("V3 Identity"));
                logger.log(Level.INFO, "ipAddress={0}", ipAddress);

                if (StringUtils.isNotBlank(ipAddress)) {
                    boxInfoMap.put("host", ipAddress);
                    if (!ipAddress.equals(summaryInfoMap.get("IP Address"))) {
                        summaryInfoMap.put("IP Address", ipAddress);
                    }
                } else {
                    logger.log(Level.WARNING, "host token is blank or null: use IP Address instead");
                    logger.log(Level.INFO, "IP Address={0}", summaryInfoMap.get("IP Address"));
                    boxInfoMap.put("host", summaryInfoMap.get("IP Address"));
                }
            }

            // for pre-1.53.3 versions
            boxInfoMap.put("time", tmpCurrentTime);
            if (!summaryInfoMap.containsKey("Current Time")) {
                summaryInfoMap.put("Current Time", tmpCurrentTime);
            }

            boxInfoMap.put("up", tmpUpTime);
            if (!summaryInfoMap.containsKey("Uptime")) {
                summaryInfoMap.put("Uptime", tmpUpTime);
            }


            boxInfoMap.put("version", version);
            if (!summaryInfoMap.containsKey("Daemon Version")) {
                summaryInfoMap.put("Daemon Version", version);
            }

            boxInfoMap.put("platform", platform);
            if (!summaryInfoMap.containsKey("Platform")) {
                summaryInfoMap.put("Platform", platform);
            }

        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO error", ex);
        }

        logger.log(Level.INFO, "boxInfoMap={0}", boxInfoMap);
        logger.log(Level.INFO, "summaryInfo={0}", summaryInfoMap);
    }

    /**
     * @return the boxInfoMap
     */
    @Override
    public Map<String, String> getBoxInfoMap() {
        return boxInfoMap;
    }

    /**
     * @param boxInfoMap the boxInfoMap to set
     */
    @Override
    public void setBoxInfoMap(Map<String, String> boxInfoMap) {
        this.boxInfoMap = boxInfoMap;
    }

    /**
     * @return the summaryInfoMap
     */
    @Override
    public Map<String, String> getSummaryInfoMap() {
        return summaryInfoMap;
    }

    /**
     * @param summaryInfoMap the summaryInfoMap to set
     */
    @Override
    public void setSummaryInfoMap(Map<String, String> summaryInfoMap) {
        this.summaryInfoMap = summaryInfoMap;
    }

    /**
     *
     * @return
     */
    @Override
    public LOCKSSDaemonStatusTableTO getLOCKSSDaemonStatusTableTO() {
        LOCKSSDaemonStatusTableTO ldstTO = new LOCKSSDaemonStatusTableTO();
        ldstTO.tableId = tableId;
        ldstTO.tableData = null;
        ldstTO.tabularData = null;
        ldstTO.hasIncompleteRows = false;
        ldstTO.incompleteRows = null;
        ldstTO.summaryInfoList = null;
        ldstTO.columndescriptorList = null;
        ldstTO.typeList = null;
        ldstTO.boxInfoMap = boxInfoMap;
        ldstTO.summaryInfoMap = summaryInfoMap;
        ldstTO.targetPollIdSet = null;
        ldstTO.boxId = 0L;
        ldstTO.ipAddress = null;
        ldstTO.timezoneOffset = null;
        ldstTO.pollIdToDurationMap = null;
        ldstTO.boxHttpStatusOK = true;
        return ldstTO;
    }
}
