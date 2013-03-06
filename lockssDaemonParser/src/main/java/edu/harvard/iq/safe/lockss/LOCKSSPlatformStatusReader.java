/*
 * Copyright 2010 President and fellows of Harvard University
 *                (Author: Akio Sone)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.harvard.iq.safe.lockss;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author asone
 */
public class LOCKSSPlatformStatusReader {

    private static final Logger logger =
            Logger.getLogger(LOCKSSPlatformStatusReader.class.getName());
    /**
     *
     */
    protected String tableId = "PlatformStatus";
    /**
     * 
     */
    protected Map<String, String> boxInfoMap = new LinkedHashMap<String, String>();
    /**
     *
     */
    protected Map<String, String> summaryInfoMap = new LinkedHashMap<String, String>();

    /**
     * 
     * @param rdr
     */
    public void getPlatformStatusData(Reader rdr) {
        // String fileName = "haar_DaemonStatus_ps.txt";
        LineNumberReader reader = null;
        String line;
        try {
            reader = new LineNumberReader(rdr);
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                List<String> lst = new ArrayList<String>();
                for (int i = 0; i < tokens.length; i++) {
                    if (tokens[i].endsWith("\\")) {
                        String jn = tokens[i].substring(0, tokens[i].lastIndexOf("\\"));
                        logger.log(Level.FINE, "jn={0}", jn);
                        jn = jn + "," + tokens[i + 1];
                        logger.log(Level.FINE, "update jn={0}", jn);
                        i++;
                        lst.add(jn);
                        jn = null;
                    } else {
                        lst.add(tokens[i]);
                    }
                }
                for (String token : lst) {
                    if (reader.getLineNumber() == 1) {
                        String[] kv = token.split("=");
                        if (kv.length == 2) {
                            boxInfoMap.put(kv[0], kv[1]);
                        }
                    } else if (reader.getLineNumber() == 3) {
                        String[] kv = token.split("=");
                        if (kv.length == 2) {
                            summaryInfoMap.put(kv[0], kv[1]);
                        }
                    }
                }

            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
            }
        }

        logger.log(Level.INFO, "boxInfo={0}", boxInfoMap);
        logger.log(Level.INFO, "summaryInfo={0}", summaryInfoMap);
    }

    /**
     * @return the boxInfoMap
     */
    public Map<String, String> getBoxInfoMap() {
        return boxInfoMap;
    }

    /**
     * @param boxInfoMap the boxInfoMap to set
     */
    public void setBoxInfoMap(Map<String, String> boxInfoMap) {
        this.boxInfoMap = boxInfoMap;
    }

    /**
     * @return the summaryInfoMap
     */
    public Map<String, String> getSummaryInfoMap() {
        return summaryInfoMap;
    }

    /**
     * @param summaryInfoMap the summaryInfoMap to set
     */
    public void setSummaryInfoMap(Map<String, String> summaryInfoMap) {
        this.summaryInfoMap = summaryInfoMap;
    }

    /**
     *
     * @return
     */
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
        ldstTO.targetPollIdSet=null;
        ldstTO.boxId = 0L;
        ldstTO.ipAddress=null;
        ldstTO.pollIdToDurationMap=null;
        ldstTO.boxHttpStatusOK = true;
        return ldstTO;
    }
}
