/*
 *  Copyright 2013 President and fellows of Harvard University.
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
package edu.harvard.iq.safe.lockss.api;

import edu.harvard.iq.safe.lockss.impl.PeerRepair;
import edu.harvard.iq.safe.lockss.impl.Peers;
import edu.harvard.iq.safe.lockss.impl.SummaryInfo;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Akio Sone
 */
public interface LOCKSSDaemonStatusTable extends Serializable {

    Map<String, Peers> getAuIdToAgreedPeersTable();

    /**
     *
     * @return
     */
    long getBoxId();

    /**
     *
     * @return
     */
    Map<String, String> getBoxInfoMap();

    /**
     * @return the columndescriptorList
     */
    List<String> getColumndescriptorList();

    Map<String, Object> getCurrentPeerVotingDataUS();

    /**
     * Get the value of currentPollId
     *
     * @return the value of currentPollId
     */
    String getCurrentPollId();

    List<String> getCurrentSuccessfulReplicaIpList();

    Map<String, Map<String, String>> getCurrentUnsuccessfulPollParticipantData();

    List<String> getCurrentUnsuccessfulReplicaIpList();

    /**
     * Get the value of daemonVersion
     *
     * @return the value of daemonVersion
     */
    String getDaemonVersion();

    String getHostname();

    /**
     * @return the incompleteRows
     */
    List<Integer> getIncompleteRows();

    /**
     *
     * @return
     */
    String getIpAddress();

    Map<String, String> getPollIdToAuNameMap();

    Map<String, String> getPollIdToAuNameMapUS();

    /**
     *
     * @return
     */
    Map<String, Long> getPollIdToDurationMap();

    Map<String, Long> getPollIdToDurationMapUS();

    Map<String, Long> getPollIdToEndTimeMap();

    Map<String, Long> getPollIdToEndTimeMapUS();

    Map<String, Map<String, Map<String, String>>> getPollIdToPollParticipantData();

    Map<String, Map<String, String>> getPollIdToSummaryInfoMap();

    /**
     * @return the rowCounter
     */
    int getRowCounter();

    Map<String, List<String>> getSuccessfulReplicaIpMap();

    /**
     * @return the summaryInfoList
     */
    List<SummaryInfo> getSummaryInfoList();

    /**
     *
     * @return
     */
    Map<String, String> getSummaryInfoMap();

    /**
     *
     * @return
     */
    List<String> getSummaryInfoTitleList();

    /**
     *
     * @return
     */
    List<List<String>> getSummaryInfoValueList();

    /**
     * @return the tableData
     */
    List<Map<String, String>> getTableData();

    /**
     * @return the tableId
     */
    String getTableId();

    String getTableKey();

    /**
     * Get the value of tableTitle
     *
     * @return the value of tableTitle
     */
    String getTableTitle();

    /**
     * @return the tabularData
     */
    List<List<String>> getTabularData();

    /**
     *
     * @return
     */
    Set<String> getTargetPollIdSet();

    Set<String> getTargetPollIdSetUS();

    /**
     * Get the value of timezoneOffset
     *
     * @return the value of timezoneOffset
     */
    String getTimezoneOffset();

    /**
     * @return the typeList
     */
    List<String> getTypeList();

    Map<String, List<String>> getUnsuccessfulReplicaIpMap();

    /**
     * Get the value of verifiedCopyCount
     *
     * @return the value of verifiedCopyCount
     */
    int getVerifiedCopyCount();

    /**
     *
     * @return
     */
    boolean hasRowTags();

    /**
     *
     * @return
     */
    boolean isBoxHttpStatusOK();

    /**
     * Get the value of httpProtocol
     *
     * @return the value of httpProtocol
     */
    boolean isHttpProtocol();

    boolean isNew100pctPollDataAvailable();

    boolean isNewNon100pctPollDataAvailable();

    /**
     *
     * @return
     */
    boolean isPageReadable();

    /**
     * Get the value of parsingFailed
     *
     * @return the value of parsingFailed
     */
    boolean isParsingFailed();

    boolean isPollAgreement100pcnt();

    void setAuIdToAgreedPeersTable(Map<String, Peers> auIdToAgreedPeersTable);

    /**
     *
     * @param boxHttpStatus
     */
    void setBoxHttpStatusOK(boolean boxHttpStatus);

    /**
     *
     * @param boxId
     */
    void setBoxId(long boxId);

    /**
     *
     * @param boxInfoMap
     */
    void setBoxInfoMap(Map<String, String> boxInfoMap);

    /**
     * @param columndescriptorList the columndescriptorList to set
     */
    void setColumndescriptorList(List<String> columndescriptorList);

    void setCurrentPeerVotingDataUS(Map<String, Object> currentPeerVotingDataUS);

    /**
     * Set the value of currentPollId
     *
     * @param currentPollId new value of currentPollId
     */
    void setCurrentPollId(String currentPollId);

    void setCurrentSuccessfulReplicaIpList(List<String> currentSuccessfulReplicaIpList);

    void setCurrentUnsuccessfulPollParticipantData(Map<String, Map<String, String>> currentUnsuccessfulPollParticipantData);

    void setCurrentUnsuccessfulReplicaIpList(List<String> currentUnsuccessfulReplicaIpList);

    /**
     * Set the value of daemonVersion
     *
     * @param daemonVersion new value of daemonVersion
     */
    void setDaemonVersion(String daemonVersion);

    void setHostname(String hostname);

    /**
     * Set the value of httpProtocol
     *
     * @param httpProtocol new value of httpProtocol
     */
    void setHttpProtocol(boolean httpProtocol);

    /**
     * @param incompleteRows the incompleteRows to set
     */
    void setIncompleteRows(List<Integer> incompleteRows);

    /**
     *
     * @param ipAddress
     */
    void setIpAddress(String ipAddress);

    void setNew100pctPollDataAvailable(boolean new100pctPollDataAvailable);

    void setNewNon100pctPollDataAvailable(boolean newNon100pctPollDataAvailable);

    /**
     *
     * @param pageReadable
     */
    void setPageReadable(boolean pageReadable);

    void setParsingFailed(boolean parsingFailed);

    void setPollAgreement100pcnt(boolean pollAgreement100pcnt);

    void setPollIdToAuNameMap(Map<String, String> pollIdToAuNameMap);

    void setPollIdToAuNameMapUS(Map<String, String> pollIdToAuNameMapUS);

    /**
     *
     * @param pollIdToDurationMap
     */
    void setPollIdToDurationMap(Map<String, Long> pollIdToDurationMap);

    void setPollIdToDurationMapUS(Map<String, Long> pollIdToDurationMapUS);

    void setPollIdToEndTimeMap(Map<String, Long> pollIdToEndTimeMap);

    void setPollIdToEndTimeMapUS(Map<String, Long> pollIdToEndTimeMapUS);

    void setPollIdToPollParticipantData(Map<String, Map<String, Map<String, String>>> pollIdToPollParticipantData);

    void setPollIdToSummaryInfoMap(Map<String, Map<String, String>> pollIdToSummaryInfoMap);

    void setSuccessfulReplicaIpMap(Map<String, List<String>> successfulReplicaIpMap);

    /**
     * @param summaryInfoList the summaryInfoList to set
     */
    void setSummaryInfoList(List<SummaryInfo> summaryInfoList);

    /**
     *
     * @param summaryInfoMap
     */
    void setSummaryInfoMap(Map<String, String> summaryInfoMap);

    /**
     * @param tableData the tableData to set
     */
    void setTableData(List<Map<String, String>> tableData);

    /**
     * @param tableId the tableId to set
     */
    void setTableId(String tableId);

    void setTableKey(String tableKey);

    /**
     * Set the value of tableTitle
     *
     * @param tableTitle new value of tableTitle
     */
    void setTableTitle(String tableTitle);

    /**
     * @param tabularData the tabularData to set
     */
    void setTabularData(List<List<String>> tabularData);

    /**
     *
     * @param targetPollIdSet
     */
    void setTargetPollIdSet(Set<String> targetPollIdSet);

    void setTargetPollIdSetUS(Set<String> targetPollIdSetUS);

    /**
     * Set the value of timezoneOffset
     *
     * @param timezoneOffset new value of timezoneOffset
     */
    void setTimezoneOffset(String timezoneOffset);

    /**
     *
     * @param typeList
     */
    void setTypeList(List<String> typeList);

    void setUnsuccessfulReplicaIpMap(Map<String, List<String>> unsuccessfulReplicaIpMap);

    void summarizePeerRepairResults(long lastPollTime);

    Peers tabulateAgreedPeers(long pollCutoffTime);

    @Deprecated
    PeerRepair tabulatePeerRepairResults(long pollCutoffTime);

    String toString();

}
