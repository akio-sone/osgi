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

package edu.harvard.iq.safe.lockss;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author asone
 */
public class LOCKSSDaemonStatusTableTO implements java.io.Serializable {

    static final Logger logger =
            Logger.getLogger(LOCKSSDaemonStatusTableTO.class.getName());
    // fields
    /**
     *
     */
    protected String tableId;

    /**
     * @return the tableId
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * @param tableId the tableId to set
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    /**
     *
     */
    protected List<String> columndescriptorList;

    /**
     * @return the columndescriptorList
     */
    public List<String> getColumndescriptorList() {
        return columndescriptorList;
    }

    /**
     * @param columndescriptorList the columndescriptorList to set
     */
    public void setColumndescriptorList(List<String> columndescriptorList) {
        this.columndescriptorList = columndescriptorList;
    }
    /**
     *
     */
    protected List<SummaryInfo> summaryInfoList;

    /**
     * @return the summaryInfoList
     */
    public List<SummaryInfo> getSummaryInfoList() {
        return summaryInfoList;
    }

    /**
     * @param summaryInfoList the summaryInfoList to set
     */
    public void setSummaryInfoList(List<SummaryInfo> summaryInfoList) {
        this.summaryInfoList = summaryInfoList;
    }

    /**
     *
     * @return
     */
    public List<String> getSummaryInfoTitleList() {
        List<String> ttl = new ArrayList<String>();
        for (SummaryInfo si : summaryInfoList) {
            ttl.add(si.getTitle());
        }
        return ttl;
    }
    /**
     *
     */
    protected List<String> typeList;

    /**
     * @return the typeList
     */
    public List<String> getTypeList() {
        return typeList;
    }

    /**
     *
     * @param typeList
     */
    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }
    // main container that keeps parsed data as a list
    /**
     *
     */
    protected List<Map<String, String>> tableData =
        new ArrayList<Map<String, String>>();

    /**
     * @return the tableData
     */
    public List<Map<String, String>> getTableData() {
        return tableData;
    }

    /**
     * @param tableData the tableData to set
     */
    public void setTableData(List<Map<String, String>> tableData) {
        this.tableData = tableData;
    }
    // backup container that keeps parsed data as a map  and is used
    // when a table has missing cells (inconsistent columns)
    /**
     *
     */
    protected List<List<String>> tabularData = new ArrayList<List<String>>();

    /**
     * @return the tabularData
     */
    public List<List<String>> getTabularData() {
        return tabularData;
    }

    /**
     * @param tabularData the tabularData to set
     */
    public void setTabularData(List<List<String>> tabularData) {
        this.tabularData = tabularData;
    }

    /**
     *
     * @return
     */
    public List<List<String>> getSummaryInfoValueList() {
        List<List<String>> values = new ArrayList<List<String>>();
        List<String> value = new ArrayList<String>();

        for (SummaryInfo si : summaryInfoList) {
            value.add(si.getValue());
        }
        //logger.info("value="+value);
        values.add(value);
        return values;
    }
    /**
     *
     */
    public boolean hasIncompleteRows = false;
    /**
     *
     */
    protected int rowCounter = 0;

    /**
     * @return the rowCounter
     */
    public int getRowCounter() {
        return rowCounter;
    }

    /**
     *
     * @return
     */
    public boolean hasRowTags() {
        return rowCounter > 0 ? true : false;
    }
    /**
     *
     */
    protected List<Integer> incompleteRows;

    /**
     * @return the incompleteRows
     */
    public List<Integer> getIncompleteRows() {
        return incompleteRows;
    }

    /**
     * @param incompleteRows the incompleteRows to set
     */
    public void setIncompleteRows(List<Integer> incompleteRows) {
        this.incompleteRows = incompleteRows;
    }
    /**
     *
     */
    protected Map<String, String> boxInfoMap;

    /**
     *
     * @return
     */
    public Map<String, String> getBoxInfoMap() {
        return boxInfoMap;
    }

    /**
     *
     * @param boxInfoMap
     */
    public void setBoxInfoMap(Map<String, String> boxInfoMap) {
        this.boxInfoMap = boxInfoMap;
    }
    /**
     *
     */
    protected Map<String, String> summaryInfoMap;

    /**
     *
     * @return
     */
    public Map<String, String> getSummaryInfoMap() {
        return summaryInfoMap;
    }

    /**
     *
     * @param summaryInfoMap
     */
    public void setSummaryInfoMap(Map<String, String> summaryInfoMap) {
        this.summaryInfoMap = summaryInfoMap;
    }
    /**
     *
     */
    protected Set<String> targetPollIdSet =
            new LinkedHashSet<String>();

    /**
     *
     * @return
     */
    public Set<String> getTargetPollIdSet() {
        return targetPollIdSet;
    }

    /**
     *
     * @param targetPollIdSet
     */
    public void setTargetPollIdSet(Set<String> targetPollIdSet) {
        this.targetPollIdSet = targetPollIdSet;
    }




    protected Set<String> targetPollIdSetUS =
            new LinkedHashSet<String>();

    public Set<String> getTargetPollIdSetUS() {
        return targetPollIdSetUS;
    }

    public void setTargetPollIdSetUS(Set<String> targetPollIdSetUS) {
        this.targetPollIdSetUS = targetPollIdSetUS;
    }




//    protected Set<String> lessthan100pcntPollIdSet =
//            new LinkedHashSet<String>();
//
//    public Set<String> getLessthan100pcntPollIdSet() {
//        return lessthan100pcntPollIdSet;
//    }
//
//    public void setLessthan100pcntPollIdSet(Set<String> lessthan100pcntPollIdSet) {
//        this.lessthan100pcntPollIdSet = lessthan100pcntPollIdSet;
//    }


    /**
     *
     */
    protected long boxId;

    /**
     *
     * @return
     */
    public long getBoxId() {
        return boxId;
    }

    /**
     *
     * @param boxId
     */
    public void setBoxId(long boxId) {
        this.boxId = boxId;
    }
    /**
     *
     */
    protected String ipAddress;

    /**
     *
     * @return
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     *
     * @param ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }




    /**
     *
     */
    protected Map<String, Long> pollIdToDurationMap;

    /**
     *
     * @return
     */
    public Map<String, Long> getPollIdToDurationMap() {
        return pollIdToDurationMap;
    }

    /**
     *
     * @param pollIdToDurationMap
     */
    public void setPollIdToDurationMap(Map<String, Long> pollIdToDurationMap) {
        this.pollIdToDurationMap = pollIdToDurationMap;
    }


    protected Map<String, String> pollIdToAuNameMap;

    public Map<String, String> getPollIdToAuNameMap() {
        return pollIdToAuNameMap;
    }

    public void setPollIdToAuNameMap(Map<String, String> pollIdToAuNameMap) {
        this.pollIdToAuNameMap = pollIdToAuNameMap;
    }


    protected Map<String, Long> pollIdToEndTimeMap;

    public Map<String, Long> getPollIdToEndTimeMap() {
        return pollIdToEndTimeMap;
    }

    public void setPollIdToEndTimeMap(Map<String, Long> pollIdToEndTimeMap) {
        this.pollIdToEndTimeMap = pollIdToEndTimeMap;
    }




    /**
     *
     */
    protected Map<String, Long> pollIdToDurationMapUS;

    public Map<String, Long> getPollIdToDurationMapUS() {
        return pollIdToDurationMapUS;
    }

    public void setPollIdToDurationMapUS(Map<String, Long> pollIdToDurationMapUS) {
        this.pollIdToDurationMapUS = pollIdToDurationMapUS;
    }



    protected Map<String, String> pollIdToAuNameMapUS;

    public Map<String, String> getPollIdToAuNameMapUS() {
        return pollIdToAuNameMapUS;
    }

    public void setPollIdToAuNameMapUS(Map<String, String> pollIdToAuNameMapUS) {
        this.pollIdToAuNameMapUS = pollIdToAuNameMapUS;
    }



    protected Map<String, Long> pollIdToEndTimeMapUS;

    public Map<String, Long> getPollIdToEndTimeMapUS() {
        return pollIdToEndTimeMapUS;
    }

    public void setPollIdToEndTimeMapUS(Map<String, Long> pollIdToEndTimeMapUS) {
        this.pollIdToEndTimeMapUS = pollIdToEndTimeMapUS;
    }



    protected Map<String, Map<String, Map<String, String>>> pollIdToPollParticipantData;

    public Map<String, Map<String, Map<String, String>>> getPollIdToPollParticipantData() {
        return pollIdToPollParticipantData;
    }

    public void setPollIdToPollParticipantData(Map<String, Map<String, Map<String, String>>> pollIdToPollParticipantData) {
        this.pollIdToPollParticipantData = pollIdToPollParticipantData;
    }

    protected Map<String, Map<String, String>> pollIdToSummaryInfoMap;

    public Map<String, Map<String, String>> getPollIdToSummaryInfoMap() {
        return pollIdToSummaryInfoMap;
    }

    public void setPollIdToSummaryInfoMap(Map<String, Map<String, String>> pollIdToSummaryInfoMap) {
        this.pollIdToSummaryInfoMap = pollIdToSummaryInfoMap;
    }



    /**
     *
     */
    protected boolean boxHttpStatusOK;

    /**
     *
     * @return
     */
    public boolean isBoxHttpStatusOK() {
        return boxHttpStatusOK;
    }

    /**
     *
     * @param boxHttpStatus
     */
    public void setBoxHttpStatusOK(boolean boxHttpStatus) {
        this.boxHttpStatusOK = boxHttpStatus;
    }

    /**
     *
     */
    protected boolean pageReadable;

    /**
     *
     * @return
     */
    public boolean isPageReadable() {
        return pageReadable=true;
    }

    /**
     *
     * @param pageReadable
     */
    public void setPageReadable(boolean pageReadable) {
        this.pageReadable = pageReadable;
    }

    protected String currentPollId=null;

    /**
     * Get the value of currentPollId
     *
     * @return the value of currentPollId
     */
    public String getCurrentPollId() {
        return currentPollId;
    }

    /**
     * Set the value of currentPollId
     *
     * @param currentPollId new value of currentPollId
     */
    public void setCurrentPollId(String currentPollId) {
        this.currentPollId = currentPollId;
    }

//    public Map<String, List<String>> successfulReplicaIpList = null;
//
//    public Map<String, List<String>> getSuccessfulReplicaIpList() {
//        return successfulReplicaIpList;
//    }
//
//    public void setSuccessfulReplicaIpList(Map<String, List<String>> successfulReplicaIpList) {
//        this.successfulReplicaIpList = successfulReplicaIpList;
//    }

    protected List<String> currentSuccessfulReplicaIpList = null;

    public List<String> getCurrentSuccessfulReplicaIpList() {
        return currentSuccessfulReplicaIpList;
    }

    public void setCurrentSuccessfulReplicaIpList(List<String> currentSuccessfulReplicaIpList) {
        this.currentSuccessfulReplicaIpList = currentSuccessfulReplicaIpList;
    }


    protected List<String> currentUnsuccessfulReplicaIpList = null;

    public List<String> getCurrentUnsuccessfulReplicaIpList() {
        return currentUnsuccessfulReplicaIpList;
    }

    public void setCurrentUnsuccessfulReplicaIpList(List<String> currentUnsuccessfulReplicaIpList) {
        this.currentUnsuccessfulReplicaIpList = currentUnsuccessfulReplicaIpList;
    }



    protected Map<String, List<String>> successfulReplicaIpMap = null;

    public Map<String, List<String>> getSuccessfulReplicaIpMap() {
        return successfulReplicaIpMap;
    }

    public void setSuccessfulReplicaIpMap(Map<String, List<String>> successfulReplicaIpMap) {
        this.successfulReplicaIpMap = successfulReplicaIpMap;
    }

    protected Map<String, List<String>> unsuccessfulReplicaIpMap = null;

    public Map<String, List<String>> getUnsuccessfulReplicaIpMap() {
        return unsuccessfulReplicaIpMap;
    }

    public void setUnsuccessfulReplicaIpMap(Map<String, List<String>> unsuccessfulReplicaIpMap) {
        this.unsuccessfulReplicaIpMap = unsuccessfulReplicaIpMap;
    }


    Map<String, Map<String, String>> currentUnsuccessfulPollParticipantData;

    public Map<String, Map<String, String>> getCurrentUnsuccessfulPollParticipantData() {
        return currentUnsuccessfulPollParticipantData;
    }

    public void setCurrentUnsuccessfulPollParticipantData(Map<String, Map<String, String>> currentUnsuccessfulPollParticipantData) {
        this.currentUnsuccessfulPollParticipantData = currentUnsuccessfulPollParticipantData;
    }

    Map<String, Object> currentPeerVotingDataUS =
            new LinkedHashMap<String, Object>();

    public Map<String, Object> getCurrentPeerVotingDataUS() {
        return currentPeerVotingDataUS;
    }

    public void setCurrentPeerVotingDataUS(Map<String, Object> currentPeerVotingDataUS) {
        this.currentPeerVotingDataUS = currentPeerVotingDataUS;
    }




    protected String daemonVersion;

    /**
     * Get the value of daemonVersion
     *
     * @return the value of daemonVersion
     */
    public String getDaemonVersion() {
        return daemonVersion;
    }

    /**
     * Set the value of daemonVersion
     *
     * @param daemonVersion new value of daemonVersion
     */
    public void setDaemonVersion(String daemonVersion) {
        this.daemonVersion = daemonVersion;
    }

    protected String tableTitle;

    /**
     * Get the value of tableTitle
     *
     * @return the value of tableTitle
     */
    public String getTableTitle() {
        return tableTitle;
    }

    /**
     * Set the value of tableTitle
     *
     * @param tableTitle new value of tableTitle
     */
    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }


    protected String tableKey;

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }


    protected String timezoneOffset;

    /**
     * Get the value of timezoneOffset
     *
     * @return the value of timezoneOffset
     */
    public String getTimezoneOffset() {
        return timezoneOffset;
    }

    /**
     * Set the value of timezoneOffset
     *
     * @param timezoneOffset new value of timezoneOffset
     */
    public void setTimezoneOffset(String timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    protected boolean httpProtocol=true;

    /**
     * Get the value of httpProtocol
     *
     * @return the value of httpProtocol
     */
    public boolean isHttpProtocol() {
        return httpProtocol;
    }

    /**
     * Set the value of httpProtocol
     *
     * @param httpProtocol new value of httpProtocol
     */
    public void setHttpProtocol(boolean httpProtocol) {
        this.httpProtocol = httpProtocol;
    }

    protected String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }



    private int verifiedCopyCount;

    /**
     * Get the value of verifiedCopyCount
     *
     * @return the value of verifiedCopyCount
     */
    public int getVerifiedCopyCount() {
        return verifiedCopyCount;
    }

    /**
     * Set the value of verifiedCopyCount
     *
     * @param verifiedCopyCount new value of verifiedCopyCount
     */
//    public void setVerifiedCopyCount(int verifiedCopyCount) {
//        this.verifiedCopyCount = verifiedCopyCount;
//    }
    private boolean parsingFailed;

    /**
     * Get the value of parsingFailed
     *
     * @return the value of parsingFailed
     */
    public boolean isParsingFailed() {
        return parsingFailed;
    }

    public void setParsingFailed(boolean parsingFailed) {
        this.parsingFailed = parsingFailed;
    }


    boolean pollAgreement100pcnt;

    public boolean isPollAgreement100pcnt() {
        return pollAgreement100pcnt;
    }

    public void setPollAgreement100pcnt(boolean pollAgreement100pcnt) {
        this.pollAgreement100pcnt = pollAgreement100pcnt;
    }

    boolean new100pctPollDataAvailable = false;

    boolean newNon100pctPollDataAvailable = false;

    public boolean isNew100pctPollDataAvailable() {
        return new100pctPollDataAvailable;
    }

    public void setNew100pctPollDataAvailable(boolean new100pctPollDataAvailable) {
        this.new100pctPollDataAvailable = new100pctPollDataAvailable;
    }

    public boolean isNewNon100pctPollDataAvailable() {
        return newNon100pctPollDataAvailable;
    }

    public void setNewNon100pctPollDataAvailable(boolean newNon100pctPollDataAvailable) {
        this.newNon100pctPollDataAvailable = newNon100pctPollDataAvailable;
    }


    public void summarizePeerRepairResults(long lastPollTime) {

        int nVerifiedReplicas = 0;
        String timeZone = this.getTimezoneOffset();
        //String rawlastPollTime = "03:16:33 04/14/12";
        //String timeZone = DaemonStatusDataUtil.getJVMTimezoneOffset();//null;//"US/Eastern";
        logger.log(Level.FINE, "timeZone={0}", timeZone);
        //long lastPollTime = DaemonStatusDataUtil.stringToEpocTime(rawlastPollTime);
//        long lastPollTime =
//                DaemonStatusDataUtil.getEpocTimeFromString(rawlastPollTime, timeZone);
//
        logger.log(Level.FINE, "lastPollTime={0}", lastPollTime);
         int non100pcntBox = 0;
        if (this.hasIncompleteRows) {
            logger.log(Level.FINE, "use Map");
            // use map
            List<Map<String, String>> tblh = this.getTableData();


            List<PeerVoteResult> voterList = new ArrayList<PeerVoteResult>();
            for (int i = 0; i < tblh.size(); i++) {
                if (!tblh.get(i).get("Last").equals("Yes")) {
                    continue;
                } else {
                    // work on concensus=yes peers only

                    String pcnt =
                            tblh.get(i).get("LastPercentAgreement").replace("%", "");
                    String ipAddressFromTable =
                            DaemonStatusDataUtil.getPeerIpAddress(tblh.get(i).get("Box"));
                    logger.log(Level.FINE, "{0}-th ip={1} pcnt={2}",
                            new Object[]{i, ipAddressFromTable, pcnt});
                    long pollEndTime =
                            DaemonStatusDataUtil.getEpocTimeFromString(
                            tblh.get(i).get("LastAgree"),
                            timeZone);
                    if (pollEndTime == lastPollTime
                            || (pollEndTime >= (lastPollTime - 1000)
                            && pollEndTime <= (lastPollTime + 1000))) {
                        logger.log(Level.FINE,
                                "H: {0}: pollEndTime is close to the one in the au_status_table",
                                ipAddressFromTable);
                        if (pcnt.equals("100")) {
                            voterList.add(new PeerVoteResult(ipAddressFromTable, new Integer(pcnt),
                                    tblh.get(i).get("LastAgree"), pollEndTime));
                        } else {
                            non100pcntBox++;
                            logger.log(Level.FINE,
                                    "H:non-100% box was found:{0}%", pcnt);
                        }
                    } else {
                        logger.log(Level.FINE,
                                "H: {0}: pollEndTime is NOT close to the one in the au_status_table", ipAddressFromTable);
                        continue;
                    }
                }
            }

            logger.log(Level.FINE, "H:voterList: contents={0}", voterList);
            logger.log(Level.FINE, "H:voterList: size={0}", voterList.size());
            logger.log(Level.FINE, "H:number of non-100% boxes={0}", non100pcntBox);


            nVerifiedReplicas = voterList.size();
            logger.log(Level.FINE,
                    "H: lastAgree time is within the permissible range: "
                    + "how many successfully replicated boxes={0}",
                    nVerifiedReplicas);

        } else {
            // use list
            List<List<String>> tbl = this.getTabularData();
            logger.log(Level.FINE, "tbl size={0}", tbl.size());

            List<PeerVoteResult> voterList = new ArrayList<PeerVoteResult>();
            for (int i = 0; i < tbl.size(); i++) {
                if (!tbl.get(i).get(1).equals("Yes")) {
                    continue;
                } else {
                    // work on concensus=yes peers only

                    String pcnt =
                            tbl.get(i).get(3).replace("%", "");
                    String ipAddressFromTable =
                            DaemonStatusDataUtil.getPeerIpAddress(tbl.get(i).get(0));
                    logger.log(Level.FINE, "L:{0}-th ip={1} pcnt={2}",
                            new Object[]{i, ipAddressFromTable, pcnt});
                    long pollEndTime = DaemonStatusDataUtil.getEpocTimeFromString(tbl.get(i).get(6),
                            timeZone);
                    if (pollEndTime == lastPollTime
                            || (pollEndTime >= (lastPollTime - 1000)
                            && pollEndTime <= (lastPollTime + 1000))) {
                        logger.log(Level.FINE,
                                "L: {0}: pollEndTime is equal to the one in the au_status_table", ipAddressFromTable);

                        if (pcnt.equals("100")) {
                        voterList.add(new PeerVoteResult(ipAddressFromTable, new Integer(pcnt),
                                tbl.get(i).get(6), pollEndTime));
                        } else {
                            non100pcntBox++;
                            logger.log(Level.FINE,
                                    "L:non-100% box was found:{0}%", pcnt);
                        }

                    } else {
                        logger.log(Level.FINE,
                                "L: {0}: pollEndTime is NOT close to the one in the au_status_table", ipAddressFromTable);
                        continue;
                    }
                }
            }

            logger.log(Level.FINE, "L:voterList: contents={0}", voterList);
            logger.log(Level.FINE, "L:voterList: size={0}", voterList.size());
            logger.log(Level.FINE, "L:number of non-100% boxes={0}", non100pcntBox);

            nVerifiedReplicas = voterList.size();
            logger.log(Level.FINE,
                    "L: lastAgree time is within the permissible range: "
                    + "how many successfully replicated boxes={0}",
                    nVerifiedReplicas);
        }

        this.verifiedCopyCount = nVerifiedReplicas;
    }
    
    
/*
    Map<String, PeerRepair> auIdToPeerRepairTable;

    public Map<String, PeerRepair> getAuIdToPeerRepairTable() {
        return auIdToPeerRepairTable;
    }

    public void setAuIdToPeerRepairTable(Map<String, PeerRepair> auIdToPeerRepairTable) {
        this.auIdToPeerRepairTable = auIdToPeerRepairTable;
    }
*/
    Map<String, Peers> auIdToAgreedPeersTable;

    public Map<String, Peers> getAuIdToAgreedPeersTable() {
        return auIdToAgreedPeersTable;
    }

    public void setAuIdToAgreedPeersTable(Map<String, Peers> auIdToAgreedPeersTable) {
        this.auIdToAgreedPeersTable = auIdToAgreedPeersTable;
    }


    @Deprecated
    public PeerRepair tabulatePeerRepairResults(long pollCutoffTime) {

        String timeZone = this.getTimezoneOffset();
        //String rawlastPollTime = "03:16:33 04/14/12";

        logger.log(Level.FINE, "timeZone={0}", timeZone);

//
        logger.log(Level.FINE, "Poll cutoff Time={0}", pollCutoffTime);

        // use map
        List<Map<String, String>> tblh = this.getTableData();

        PeerRepair result = new PeerRepair();

        result.setAuId(this.tableKey);
        result.setAuName(this.tableTitle);

        Set<String> ipSet100pct = new LinkedHashSet<String>();
        Set<String> ipSetNon100pct = new LinkedHashSet<String>();
        long newestPollDate =0;
        for (int i = 0; i < tblh.size(); i++) {
            // 1st filter: deals with concesus-reached peers only
            if (!tblh.get(i).get("Last").equals("Yes")) {
                continue;
            }

            // 2nd filter: exclude old-poll peer
            String pcnt =
                tblh.get(i).get("LastPercentAgreement").replace("%", "");
            String ipAddressFromTable =
                DaemonStatusDataUtil.getPeerIpAddress(tblh.get(i).get("Box"));
            logger.log(Level.FINE, "{0}-th ip={1} pcnt={2}",
                new Object[]{i, ipAddressFromTable, pcnt});

            long pollEndTime =
                DaemonStatusDataUtil.getEpocTimeFromString(
                tblh.get(i).get("LastAgree"),
                timeZone);
            logger.log(Level.FINE,
                "H:pollEndTime is    {0}", pollEndTime);

            if (pollEndTime < pollCutoffTime) {
                logger.log(Level.FINE,
                    "H: {0}: pollEndTime is beyond the cutoff point", ipAddressFromTable);
                continue;
            }


            logger.log(Level.FINE,
                "H: {0}: pollEndTime is within the cutoff range",
                ipAddressFromTable);
            if (pollEndTime > newestPollDate){
                newestPollDate = pollEndTime;
            }
            if (pcnt.equals("100.00")) {
            // add this peer's Ip address
                ipSet100pct.add(ipAddressFromTable);
            } else {
                ipSetNon100pct.add(ipAddressFromTable);
            }

        }
        logger.log(Level.FINE,
                "The latest Poll time of this AU={0}", newestPollDate);
        result.setLastAgree(newestPollDate);
        result.setFullyAgreedPeerSet(ipSet100pct);
        result.setNonfullyAgreedPeerSet(ipSetNon100pct);
        result.setPoller(this.ipAddress);

        logger.log(Level.FINE, "H:number of 100% boxes={0}", ipSet100pct.size());
        logger.log(Level.FINE, "H:number of non-100% boxes={0}", ipSetNon100pct.size());

        return result;
    }



    public Peers tabulateAgreedPeers(long pollCutoffTime) {

        String timeZone = this.getTimezoneOffset();
        //String rawlastPollTime = "03:16:33 04/14/12";

        logger.log(Level.FINE, "timeZone={0}", timeZone);

//
        logger.log(Level.FINE, "Poll cutoff Time={0}", pollCutoffTime);

        // use map
        List<Map<String, String>> tblh = this.getTableData();

        Peers result = new Peers();

        result.setAuId(this.tableKey);
        result.setAuName(this.tableTitle);

        Set<PeerRepairBox> ipSet100pct = new LinkedHashSet<PeerRepairBox>();
        Set<PeerRepairBox> ipSetNon100pct = new LinkedHashSet<PeerRepairBox>();
        long newestPollDate =0;
        for (int i = 0; i < tblh.size(); i++) {
            // 1st filter: deals with concesus-reached peers only
            if (!tblh.get(i).get("Last").equals("Yes")) {
                continue;
            }

            // 2nd filter: exclude old-poll peer
            String pcnt = null;
            if (StringUtils.isNotBlank(tblh.get(i).get("LastPercentAgreement"))){
                pcnt =
                tblh.get(i).get("LastPercentAgreement").replace("%", "");
                logger.log(Level.FINE, "pcnt is not null:{0}", pcnt);
            } else {
                logger.log(Level.FINE, "pcnt is null or empty:{0}", pcnt);
            }

            String ipAddressFromTable =
                DaemonStatusDataUtil.getPeerIpAddress(tblh.get(i).get("Box"));
            logger.log(Level.FINE, "{0}-th ip={1} pcnt={2}",
                new Object[]{i, ipAddressFromTable, pcnt});

            long pollEndTime =
                DaemonStatusDataUtil.getEpocTimeFromString(
                tblh.get(i).get("LastAgree"),
                timeZone);
            logger.log(Level.FINE,
                "H:pollEndTime is    {0}", pollEndTime);

            if (pollEndTime < pollCutoffTime) {
                logger.log(Level.FINE,
                    "H: {0}: pollEndTime is beyond the cutoff point", ipAddressFromTable);
                continue;
            }
            logger.log(Level.FINE,
                "H: {0}: pollEndTime is within the cutoff range",
                ipAddressFromTable);



            if (pollEndTime > newestPollDate){
                newestPollDate = pollEndTime;
            }
            // older daemon 1.53.3 used 100% instead of 100.00
//            if (pcnt.startsWith("100")) {
//                // add this peer's Ip address
//                ipSet100pct.add(new PeerRepairBox(ipAddressFromTable,
//                    pollEndTime));
//            } else {
//                Double parsedPcnt = 0.0d;
//                if (StringUtils.isNotBlank(pcnt)) {
//                    try {
//                        parsedPcnt = Double.parseDouble(pcnt);
//                    } catch (NumberFormatException e) {
//                        logger.log(Level.WARNING, "percent value(={1}) cannot be parsed for {0}-th auId", new Object[]{i, pcnt});
//                    } finally {
//logger.log(Level.INFO, "double value(={1}) for {0}-th auId", new Object[]{i, parsedPcnt});
//                    ipSetNon100pct.add(new PeerRepairBox(ipAddressFromTable,
//                        pollEndTime, parsedPcnt));
//                    }
//                } else {
//
//                    ipSetNon100pct.add(new PeerRepairBox(ipAddressFromTable,
//                        pollEndTime, parsedPcnt));
//                }
//
//            }


            Double parsedPcnt = 0.0d;
            
            if (StringUtils.isNotBlank(pcnt)) {
                try {
                    parsedPcnt = Double.parseDouble(pcnt);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "percent value(={1}) cannot be parsed for {0}-th auId", new Object[]{i, pcnt});
                } finally {
                    logger.log(Level.FINE, "double value(={1}) for {0}-th auId",
                        new Object[]{i, parsedPcnt});

                    if (Double.compare(parsedPcnt, 100d) == 0) {
                        // 100% case
                        ipSet100pct.add(new PeerRepairBox(ipAddressFromTable,
                            pollEndTime));
                    logger.log(Level.FINER, "100%: double value(={1}) for {0}-th auId",
                        new Object[]{i, parsedPcnt});
                    } else {
                        // less than 100% cases
                        ipSetNon100pct.add(new PeerRepairBox(ipAddressFromTable,
                            pollEndTime, parsedPcnt));
                    logger.log(Level.FINER, "not 100%: double value(={1}) for {0}-th auId",
                        new Object[]{i, parsedPcnt});
                    }

                }
            } else {
                logger.log(Level.FINE, "null %: double value(={1}) for {0}-th auId",
                        new Object[]{i, parsedPcnt});
                ipSetNon100pct.add(new PeerRepairBox(ipAddressFromTable,
                    pollEndTime, parsedPcnt));
            }





        }
        logger.log(Level.FINE,
                "The latest Poll time of this AU={0}", newestPollDate);
        result.setLastAgree(newestPollDate);
        result.setFullyAgreedPeerSet(ipSet100pct);
        result.setNonfullyAgreedPeerSet(ipSetNon100pct);
        result.setPoller(this.ipAddress);

        logger.log(Level.FINE, "H:number of 100% boxes={0}", ipSet100pct.size());
        logger.log(Level.FINE, "H:number of non-100% boxes={0}", ipSetNon100pct.size());

        return result;
    }








    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nLOCKSSDaemonStatusTableTO:[\n");
        String body =
                "tableId = " + this.tableId + "\n"
                + "tableTitle = " + this.tableTitle + "\n"
                + "tableKey = " + this.tableKey + "\n"
                + "tableData = " + this.tableData + "\n"
                + "tabularData = " + this.tabularData + "\n"
                + "hasIncompleteRows = " + this.hasIncompleteRows + "\n"
                + "incompleteRows = " + this.incompleteRows + "\n"
                + "summaryInfoList = " + this.summaryInfoList + "\n"
                + "columndescriptorList = " + this.columndescriptorList + "\n"
                + "typeList = " + this.typeList + "\n"
                + "boxInfoMap = " + this.boxInfoMap + "\n"
                + "summaryInfoMap = " + this.summaryInfoMap + "\n"
                + "targetPollIdSet = " + this.targetPollIdSet + "\n"
                + "targetPollIdSetUS = " + this.targetPollIdSetUS + "\n"
//                + "lessthan100pcntPollIdSet = " + this.lessthan100pcntPollIdSet + "\n"
                + "boxId = " + this.boxId + "\n"
                + "ipAddress = " + this.ipAddress + "\n"
                + "timezoneOffset = " + this.timezoneOffset + "\n"
                + "daemonVersion = " + this.daemonVersion + "\n"
                + "pollIdToDurationMap =" + this.pollIdToDurationMap + "\n"
                + "pollIdToDurationMapUS =" + this.pollIdToDurationMapUS + "\n"
                + "boxHttpStatusOK = " + this.boxHttpStatusOK + "\n"
                + "pageReadable = " + this.pageReadable+"\n"
                + "currentPollId = "+ this.currentPollId +"\n"
                + "currentSuccessfulReplicaIpList = "+ this.currentSuccessfulReplicaIpList +"\n"
                + "currentUnsuccessfulReplicaIpList=" + this.currentUnsuccessfulReplicaIpList +"\n"
                + "currentUnsuccessfulPollParticipantData = " + this.currentUnsuccessfulPollParticipantData +"\n"
                + "successfulReplicaIpMap = "+this.successfulReplicaIpMap +"\n"
                + "unsuccessfulReplicaIpMap = "+this.unsuccessfulReplicaIpMap +"\n"
                + "pollAgreement100pcnt=" + this.pollAgreement100pcnt +"\n"
                + "pollIdToAuNameMap = "+this.pollIdToAuNameMap +"\n"
                + "pollIdToEndTimeMap = "+this.pollIdToEndTimeMap+"\n"

                + "pollIdToAuNameMapUS = "+this.pollIdToAuNameMapUS +"\n"
                + "pollIdToEndTimeMapUS = "+this.pollIdToEndTimeMapUS+"\n"
                + "pollIdToSummaryInfoMap = "+pollIdToSummaryInfoMap+"\n"
                + "pollIdToPollParticipantData = " +this.pollIdToPollParticipantData+"\n"
                + "httpProtocol = "+this.httpProtocol+"\n"
                + "hostname = "+this.hostname+"\n"
                + "verifiedCopyCount="+this.verifiedCopyCount+"\n"
                + "isParsingFailed="+this.isParsingFailed()+"\n"
                + "isNew100pctPollDataAvailable="+this.isNew100pctPollDataAvailable()+"\n"
                + "isNewNon100pctPollDataAvailable="+this.isNewNon100pctPollDataAvailable()+"\n"
                + "auIdToAgreedPeersTable="+ this.auIdToAgreedPeersTable +"\n"
                ;
        sb.append(body);
        sb.append("]\n");
        return sb.toString();
    }
}
