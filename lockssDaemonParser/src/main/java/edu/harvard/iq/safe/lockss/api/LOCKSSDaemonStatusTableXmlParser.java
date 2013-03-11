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

import edu.harvard.iq.safe.lockss.impl.LOCKSSDaemonStatusTableTO;
import edu.harvard.iq.safe.lockss.impl.SummaryInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Akio Sone
 */
public interface LOCKSSDaemonStatusTableXmlParser {

    /**
     * @return the columndescriptorList
     */
    List<String> getColumndescriptorList();

    /**
     *
     * @return
     */
    List<Integer> getIncompleteRowList();

    /**
     *
     * @return
     */
    LOCKSSDaemonStatusTable getLOCKSSDaemonStatusTable();

    /**
     * @return the summaryInfoList
     */
    List<SummaryInfo> getSummaryInfoList();

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
     * @return the typeList
     */
    List<String> getTypeList();

    /**
     *
     * @return
     */
    boolean hasRowTags();

    boolean isPollAgreement100pcnt();

    /**
     *
     * @param name
     * @param encoding
     * @throws FileNotFoundException
     */
    void read(String name, String encoding) throws FileNotFoundException;

    /**
     *
     * @param name
     * @throws FileNotFoundException
     */
    void read(String name) throws FileNotFoundException;

    /**
     *
     * @param file
     * @param encoding
     * @throws FileNotFoundException
     */
    void read(File file, String encoding) throws FileNotFoundException;

    /**
     *
     * @param file
     * @throws FileNotFoundException
     */
    void read(File file) throws FileNotFoundException;

    /**
     *
     * @param stream
     */
    void read(InputStream stream);

    /**
     *
     * @param stream
     * @param encoding
     */
    void read(InputStream stream, String encoding);

    void setPollAgreement100pcnt(boolean pollAgreement100pcnt);

    void setSummaryInfoMap(Map<String, String> summaryInfoMap);

    /**
     * @param tableId the tableId to set
     */
    void setTableId(String tableId);

    /**
     * Set the value of tableTitle
     *
     * @param tableTitle new value of tableTitle
     */
    void setTableTitle(String tableTitle);

}
