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


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
//import edu.harvard.iq.safe.saasystem.etl.TargetDaemonStatusTable;
//import edu.harvard.iq.safe.saasystem.util.PLNmemberIpDAO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Akio Sone at UNC-Odum
 */
public class LOCKSSDaemonStatusTableXmlStreamParser {

    final static Logger logger =
            Logger.getLogger(LOCKSSDaemonStatusTableXmlStreamParser.class.getName());
    // static fields
    /**
     *
     */
    protected static String auRegex = "^(\\d+)\\s+Archival Unit";
    /**
     *
     */
    protected static Pattern pau = null; //Pattern.compile(auRegex);
    /**
     *
     */
    //protected static Matcher m = null;
    /**
     *
     */
    protected final static Set<String> hasReferenceTag = new HashSet<String>();
    /**
     *
     */
    protected final static Set<String> mayHaveReferenceTag = new HashSet<String>();
    /**
     *
     */
    protected final static Set<String> belongsInclusionTableList = new HashSet<String>();
    // tables to be parsed
//    private static String[] TARGET_TABLES = {"AuIds",
//        "ArchivalUnitStatusTable", "crawl_status_table", "Identities",
//        "PlatformStatus", "V3PollerTable", "RepositorySpace",
//        "V3VoterTable", "PeerRepair", "V3PollerDetailTable"};
    final static Set<String> hasRefTitileTagsSI = new HashSet<String>();
    final static List<String> TARGET_TABLE_LIST = new ArrayList<String>();

    final static String IpAdRegex =
            "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
            + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    static Pattern p = null;



    static {
        p = Pattern.compile(IpAdRegex);

        for (TargetDaemonStatusTable targetTableName :
                TargetDaemonStatusTable.values()) {
            TARGET_TABLE_LIST.add(targetTableName.getTableName());
        }

        hasReferenceTag.add("AuName");
        hasReferenceTag.add("Peers");
        hasReferenceTag.add("AuPolls");
        hasReferenceTag.add("auId");
        hasReferenceTag.add("pollId");
        hasReferenceTag.add("au");     // crawl_status_table
        hasReferenceTag.add("crawl_status");
        hasReferenceTag.add("num_urls_with_errors");
        hasReferenceTag.add("num_of_mime_types");
        hasReferenceTag.add("identity");
        hasReferenceTag.add("repo"); // 2013-02-07 after daemon 1.59.2

        hasReferenceTag.add("num_urls_fetched");
        hasReferenceTag.add("num_urls_parsed");
        hasReferenceTag.add("num_urls_pending");
        hasReferenceTag.add("num_urls_excluded");
        hasReferenceTag.add("num_urls_not_modified");

        mayHaveReferenceTag.add("num_urls_fetched");
        mayHaveReferenceTag.add("num_urls_parsed");
        mayHaveReferenceTag.add("num_urls_pending");
        mayHaveReferenceTag.add("num_urls_excluded");
        mayHaveReferenceTag.add("num_urls_not_modified");
        // after version 1.52.3 (2011-Dec)
        mayHaveReferenceTag.add("identity");

        belongsInclusionTableList.addAll(TARGET_TABLE_LIST);
        hasRefTitileTagsSI.add("Volume");
        hasRefTitileTagsSI.add("Agreeing URLs");
        hasRefTitileTagsSI.add("Disagreeing URLs");
        hasRefTitileTagsSI.add("No Quorum URLs");
        hasRefTitileTagsSI.add("Too Close URLs");
        hasRefTitileTagsSI.add("Completed Repairs");
        hasRefTitileTagsSI.add("Queued Repairs");
        hasRefTitileTagsSI.add("Incomplete Repairs");
        

        

    }
//    /**
//     * @return the TARGET_TABLES
//     */
//    public static String[] getTARGET_TABLES() {
//        return TARGET_TABLES;
//    }
//
//    /**
//     * @param tableList
//     */
//    public static void setTARGET_TABLES(String[] tableList) {
//        TARGET_TABLES = tableList;
//    }
    // instance fields
    private String tableId = null;
    String tableKey = null;
    // stores column names
    private List<String> columndescriptorList = new ArrayList<String>();
    // class that store non-table blocks of a page
    private List<SummaryInfo> summaryInfoList = new ArrayList<SummaryInfo>();
    // stores column types
    private List<String> typeList = new ArrayList<String>();
    // main container that keeps parsed data as a list
    private List<Map<String, String>> tableData = new ArrayList<Map<String, String>>();
    // backup container that keeps parsed data as a map  and is used
    // when a table has missing cells (inconsistent columns)
    private List<List<String>> tabularData = new ArrayList<List<String>>();
    // counters that check whether a table has missing cells in a row
    int rowCounter = 0;
    int rowIgnored = 0;
    int cellCounter = 0;
    /**
     * flag for a page filled with errors such as JVM stack-trace messages
     */
    public boolean isTargetPageValid = true;
    
    
    
    XStream xstream = new XStream(new JsonHierarchicalStreamDriver());

    // default constructor
    /**
     *
     */
    public LOCKSSDaemonStatusTableXmlStreamParser() {
    }

    @PostConstruct
    void init() {
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("LOCKSSDaemonStatusTableXmlStreamParser",
                LOCKSSDaemonStatusTableXmlStreamParser.class);
        pau = Pattern.compile(auRegex);
    }

    /**
     *
     * @param name
     * @param encoding
     * @throws FileNotFoundException
     */
    public void read(String name, String encoding) throws FileNotFoundException {
        read(new FileInputStream(name), encoding);
    }

    /**
     *
     * @param name
     * @throws FileNotFoundException
     */
    public void read(String name) throws FileNotFoundException {
        read(new FileInputStream(name), "utf8");
    }

    /**
     *
     * @param file
     * @param encoding
     * @throws FileNotFoundException
     */
    public void read(File file, String encoding) throws FileNotFoundException {
        read(new FileInputStream(file), encoding);
    }

    /**
     *
     * @param file
     * @throws FileNotFoundException
     */
    public void read(File file) throws FileNotFoundException {
        read(new FileInputStream(file), "utf8");
    }

    /**
     *
     * @param stream
     */
    public void read(InputStream stream) {
        read(stream, "utf8");
    }

    /**
     *
     * @param stream
     * @param encoding
     */
    public void read(InputStream stream, String encoding) {
        // logger.setLevel(Level.FINE);
        // 1. create Input factory
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        xmlif.setProperty("javax.xml.stream.isCoalescing",
                java.lang.Boolean.TRUE);
        xmlif.setProperty("javax.xml.stream.isNamespaceAware",
                java.lang.Boolean.TRUE);

        long startTime = System.currentTimeMillis();

        int noAUs = 0;
        String aus = null;
        String currentTableId = null;
        String currentTableTitle = null;
        String currentTableKey = null;
        boolean hasErrorsColumn = false;
        String siAuId = null;
        XMLStreamReader xmlr = null;

        try {

            // create reader
            xmlr = xmlif.createXMLStreamReader(new BufferedInputStream(stream), encoding);

            String curElement = "";

            boolean isLastTagnameTable = false;
            String targetTagName = "row";
            String cellTagName = "columnname";
            boolean withinSummaryinfo = false;
            boolean withinColumndescriptor = false;
            boolean withinRow = false;
            boolean withinCell = false;
            boolean withinReference = false;
            boolean isCrawlStatusActive = false;
            boolean isCrawlStatusColumn = false;
            int valueTagCounter = 0;
            String currentColumnName = null;
            String currentCellValue = null;
            String currentCellKey = null;
            SummaryInfo si = null;


            List<String> rowData = null;
            Map<String, String> rowDataH = null;

            w1:
            while (xmlr.hasNext()) {
                int eventType = xmlr.next();
                switch (eventType) {
                    case XMLStreamConstants.START_ELEMENT:
                        curElement = xmlr.getLocalName(); // note: getName() ->
                        // QName
                        logger.log(Level.FINE, "--------- start tag = <{0}> ---------", curElement);
                        // check the table name first
                        if (curElement.equals("table")) {
                            isLastTagnameTable = true;
                        } else if (curElement.equals("error")) {
                            isTargetPageValid = false;
                            break w1;
                        }

                        if (isLastTagnameTable) {
                            if (curElement.equals("name")) {
                                currentTableId = xmlr.getElementText();
                                logger.log(Level.FINE, "########### table Id = [{0}] ###########", currentTableId);
                                tableId = currentTableId;
                                if (belongsInclusionTableList.contains(currentTableId)) {
                                    logger.log(Level.FINE, "!!!!! Table ({0}) belongs to the target list !!!!!", currentTableId);

                                } else {
                                    logger.log(Level.FINE, "XXXXXXXXXXX Table ({0}) does not belong to the target list XXXXXXXXXXX", currentTableId);
                                    break w1;
                                }
                            } else if (curElement.equals("key")) {
                                currentTableKey = xmlr.getElementText();
                                logger.log(Level.FINE, "---------- table key = ({0}) ----------", currentTableKey);
                                tableKey = currentTableKey;
                            } else if (curElement.equals("title")) {
                                currentTableTitle = xmlr.getElementText();
                                logger.log(Level.FINE, "+++++++++ table Title = ({0}) +++++++++", currentTableTitle);
                                if (currentTableId.equals("PeerRepair")){
                                    if (currentTableTitle.startsWith("Repair candidates for AU: ")){
                                        currentTableTitle = currentTableTitle.replaceFirst("Repair candidates for AU: ", "");
                                        logger.log(Level.FINE, "save this modified table-Title as auName={0}",
                                                currentTableTitle);
                                        this.tableTitle=currentTableTitle;
                                    } else {
                                        logger.log(Level.WARNING,
                                                "The table-Title does not start with the expected token={0}",
                                                currentTableTitle);
                                    }
                                }
                                isLastTagnameTable = false;
                            }
                        }

                        if (curElement.equals("columndescriptor")) {
                            withinColumndescriptor = true;
                        } else if (curElement.equals("row")) {
                            withinRow = true;
                            rowCounter++;
                            logger.log(Level.FINE, "================== {0}-th row starts here ==================", rowCounter);
                            // set-up the table storage
                            //if (rowCounter == 1) {
                            // 1st row
                            rowData = new ArrayList<String>();
                            rowDataH = new LinkedHashMap<String, String>();
                            //}
                        } else if (curElement.equals("cell")) {
                            logger.log(Level.FINE, "entering a cell");
                            withinCell = true;
                        } else if (curElement.equals("reference")) {
                            withinReference = true;
                            logger.log(Level.FINE, "within reference on");
                        } else if (curElement.equals("summaryinfo")) {
                            withinSummaryinfo = true;
                            si = new SummaryInfo();
                        } else if (curElement.equals("value")) {
                            logger.log(Level.FINE, "entering a value");
                            valueTagCounter++;
                        }
//---- columndescriptor tag ---------------------------------------------------
                        if (withinColumndescriptor) {
                            if (curElement.equals("name")) {

                                String nameText = xmlr.getElementText();
                                logger.log(Level.FINE, "\tcolumndescriptor: name = {0}",
                                        nameText);
                                columndescriptorList.add(nameText);
                            } else if (curElement.equals("title")) {
                                String titleText = xmlr.getElementText();
                                logger.log(Level.FINE, "\tcolumndescriptor: title = {0}",
                                        titleText);
                            } else if (curElement.equals("type")) {
                                String typeText = xmlr.getElementText();
                                logger.log(Level.FINE, "\tcolumndescriptor: type = {0}",
                                        typeText);
                                getTypeList().add(typeText);
                            }
                        }
//---- cell tag ----------------------------------------------------------------
                        if (withinCell) {
                            logger.log(Level.FINE, "parsing withinCell");
                            if (curElement.equals("columnname")) {

                                String columnname = xmlr.getElementText();
                                logger.log(Level.FINE, "\t\tcolumnname = {0}", columnname);
                                currentColumnName = columnname;
                                if (columnname.equals("crawl_status")) {
                                    isCrawlStatusColumn = true;
                                } else {
                                    isCrawlStatusColumn = false;
                                }

                                if (columnname.equals("Errors")) {
                                    hasErrorsColumn = true;
                                }

                            } else {
                                // value tag block: either value-tag WO a child element
                                // or with a child element
                                /*
                                 * <value><reference>...<value>xxxx</value>
                                 * <value>xxxx</value>
                                 */
                                if ((curElement.equals("value")) && (!withinReference)) {
                                    logger.log(Level.FINE, 
                                            "entering el:value/WO-REF block");
                                    if (!hasReferenceTag.contains(currentColumnName)) {
                                        logger.log(Level.FINE, 
                                                "No child reference tag is expected for this value tag");
                                        logger.log(Level.FINEST, 
                                                "xmlr.getEventType():pre-parsing={0}",
                                                xmlr.getEventType());
                                        String cellValue = xmlr.getElementText();
                                        // note: the above parsing action moves the
                                        // cursor to the end-tag, i.e., </value>
                                        // therefore, the end-element-switch-block below
                                        // cannot catch this </value> tag
                                            
                                       logger.log(Level.FINE, 
                                            "\t\t\t[No ref: value] {0} = {1}",
                                            new Object[]{currentColumnName, cellValue});
                                        
                                        currentCellValue = cellValue;
                                        logger.log(Level.FINEST, 
                                                "xmlr.getEventType():post-parsing={0}",
                                                xmlr.getEventType());
                                        // store this value
// rowData
                                        logger.log(Level.FINE, "current column name={0}",
                                                currentColumnName);
                                        logger.log(Level.FINE, "valueTagCounter={0}",
                                                valueTagCounter);
                                        if (currentColumnName.endsWith("Damaged")) {
                                            if (valueTagCounter <= 1) {
                                                // 2nd value tag is footnot for this column
                                                // ignore this value
                                                rowData.add(cellValue);
                                                rowDataH.put(currentColumnName, currentCellValue);
                                            }
                                        } else {
                                            rowData.add(cellValue);
                                            rowDataH.put(currentColumnName, currentCellValue);
                                        }
                                    } else {
                                        // previously this block was unthinkable, but
                                        // it was found that there are columns that
                                        // temporarily have a <reference> tag in
                                        // crawl_status_table; these columns are
                                        // included in hasReferenceTag by default;
                                        // thus, for such unstable columns,
                                        // when they hava a <reference tag,
                                        // data are caputred in another within-
                                        // reference block; however, when these
                                        // columns no longer have <reference> tag,
                                        // text data would be left uncaptured unless
                                        // some follow-up processing takes place here
                                        logger.log(Level.FINE, "May have to capture data: column={0}",
                                                currentColumnName);
                                        if (mayHaveReferenceTag.contains(currentColumnName) && !isCrawlStatusActive) {
                                            // because the crawling is not active,
                                            // it is safely assume that the maybe columns have no reference tag

                                            // 2011-10-24 the above assumption was found wrong
                                            // a crawling cell does not say active but
                                            // subsequent columns have a reference
                                            logger.log(Level.FINE, "a text or a reference tag : try to parse it as a text");
                                            String cellValue = null;
                                            try {
                                                cellValue = xmlr.getElementText();
                                            } catch (javax.xml.stream.XMLStreamException ex) {
                                                continue;
                                            } finally {
                                            }
                                            logger.log(Level.FINE, "\t\t\t[value WO-ref(crawling_NOT_active case)={0}]",
                                                    currentColumnName
                                                    + " = " + cellValue);
                                            currentCellValue = cellValue;
                                            // store this value
// rowData
                                            logger.log(Level.FINE, "\t\t\tcurrent columnName={0}", currentColumnName);
                                            rowData.add(cellValue);
                                            rowDataH.put(currentColumnName, currentCellValue);

                                        } else {
                                            logger.log(Level.FINE, "WO-Ref: no processing items now:{0}", curElement);
                                        }
                                    }
                                } else if (withinReference) {
                                    // reference tag exists
                                    logger.log(Level.FINE, 
                                            "WR:curElement={0}", curElement);

                                    if (curElement.equals("key")) {
                                        String cellKey = xmlr.getElementText();
                                        logger.log(Level.FINE,
                                                "\t\tcurrentCellKey is set to={0}",
                                                cellKey);
                                        currentCellKey = cellKey;
                                    } else if (curElement.equals("value")) {
                                        String cellValue = xmlr.getElementText();
                                        
                                            
                                        logger.log(Level.FINE,
                                                "\t\twr: {0} = {1}", 
                                                new Object[]{currentColumnName, cellValue});
                                        
                                        // exception cases follow:
                                        if (currentColumnName.equals("AuName")) {
                                            logger.log(Level.FINE,
                                                    "\t\tAuName is replaced with the key[=AuId]= {0}",
                                                    currentCellKey);
// rowData                                  // This block is for ArchivalUnitStatusTable
                                            // add the key as a new datum (auId)
                                            // ahead of its value
                                            rowData.add(currentCellKey);
                                            rowDataH.put("AuId", currentCellKey);
                                            currentCellValue = cellValue;
                                        } else if (currentColumnName.equals("auId")) {
                                            // This block is for V3PollerTable
                                            logger.log(Level.FINE,
                                                    "\t\tnew value for auId(V3PollerTable)={0}",
                                                    currentCellKey);
                                            // deprecated after 2012-02-02: use key as data
                                            // currentCellValue = currentCellKey;
                                            // add auName as a new column ahead of auId

                                            rowData.add(cellValue);
                                            rowDataH.put("auName", cellValue);
                                            logger.log(Level.FINE,
                                                    "\t\tauName(V3PollerTable)={0}",
                                                    cellValue);

                                            currentCellValue = currentCellKey;
                                        } else if (currentColumnName.equals("pollId")) {
                                            // this block is for V3PollerTable
                                            logger.log(Level.FINE,
                                                    "\t\tFull string (key) is used={0}",
                                                    currentCellKey);
                                            // The key has the complete string whereas
                                            // the value is its truncated copy
                                            currentCellValue = currentCellKey;

                                        } else if (currentColumnName.equals("au")) {
                                            logger.log(Level.FINE,
                                                    "\t\tauId is used instead for au(crawl_status_table)={0}",
                                                    currentCellKey);

                                            // 2012-02-02: add auName ahead of au
                                            rowData.add(cellValue);
                                            rowDataH.put("auName", cellValue);
                                            logger.log(Level.FINE,
                                                    "\t\tauName={0}",
                                                    cellValue);

// rowData                                  // This block is for crawl_status_table
                                            // save the key(auId) instead of value
                                            currentCellValue = currentCellKey;

                                         } else if (currentColumnName.equals("Peers")) {

                                            logger.log(Level.FINE,
                                                    "\t\tURL (key) is used={0}",
                                                    currentCellKey);
                                            currentCellValue = DaemonStatusDataUtil.escapeHtml(currentCellKey);
                                            logger.log(Level.FINE,
                                                    "\t\tAfter encoding ={0}",
                                                    currentCellValue);


                                        } else {
                                            if (isCrawlStatusColumn) {
                                                // if the craw status column is
                                                // "active", some later columns
                                                // may have a reference tag
                                                // so turn on the switch
                                                if (cellValue.equals("Active") || (cellValue.equals("Pending"))) {
                                                    isCrawlStatusActive = true;
                                                } else {
                                                    isCrawlStatusActive = false;
                                                }
                                            }
                                            // the default processing
                                            currentCellValue = cellValue;
                                        }
                                        // store currentCellValue
                                        logger.log(Level.FINE, "currentCellValue={0}",
                                                currentCellValue);
// rowData
                                        rowData.add(currentCellValue);
                                        rowDataH.put(currentColumnName, currentCellValue);
                                    } // Within ref tag: key and valu processing
                                } // value with text or value with ref tag
                            } // columnname or value
                        } // within cell
// ---- summaryinfo tag --------------------------------------------------------
                        if (withinSummaryinfo) {
                            logger.log(Level.FINE, "============================ Within SummaryInfo ============================ ");
                            if (curElement.equals("title")) {
                                String text = xmlr.getElementText();
                                si.setTitle(text);

                                logger.log(Level.FINE, "\tsi:titile={0}",
                                        si.getTitle());
                            } else if (curElement.equals("type")) {
                                String text = xmlr.getElementText();
                                si.setType(Integer.parseInt(text));
                                logger.log(Level.FINE, "\tsi:type={0}",
                                        si.getType());
                            } else if (curElement.equals("key")) {
                                if (withinReference && si.getTitle().equals("Volume")) {
                                    String text = xmlr.getElementText();
                                    logger.log(Level.FINE, "\tsi:key contents(Volume case)={0}",
                                            text);
                                    siAuId = text;
//                                    si.setValue(text);
                                    logger.log(Level.FINE, "\tsi:value(Volume case)={0}",
                                            siAuId);
                                }
                            } else if (curElement.equals("value")) {
                                if (withinReference) {
                                    if (hasRefTitileTagsSI.contains(si.getTitle())) {
                                        if (si.getTitle().equals("Volume")) {
                                            // 2012-02-02 use the au name
                                            String text = xmlr.getElementText();
                                            si.setValue(text);
                                            logger.log(Level.FINE, "\tsi:value(Volume case)={0}",
                                                    si.getValue());
                                        } else {
                                            String text = xmlr.getElementText();
                                            si.setValue(text);
                                            logger.log(Level.FINE, "\tsi:value={0}",
                                                    si.getValue());
                                        }
                                    }
                                } else {
                                    // note: 2012-02-07
                                    // daemon 1.59.2 uses the new layout for AU page
                                    // this layout includes a summaryinfo tag
                                    // that now contains a reference tag
                                    String text = null;

                                    try {
                                        text = xmlr.getElementText();
                                        if (!hasRefTitileTagsSI.contains(si.getTitle())) {
                                            si.setValue(text);
                                            logger.log(Level.FINE, "\tsi:value={0}",
                                                si.getValue());
                                        }
                                    } catch (javax.xml.stream.XMLStreamException ex) {
                                        logger.log(Level.WARNING, "encounter a reference tag rather than text");
                                        continue;
                                    } finally {
                                    }
                                }
                            }

                            /*
                             * aus = xmlr.getElementText();
                             * out.println("found token=[" + aus + "]"); if
                             * (currentTableId.equals("ArchivalUnitStatusTable")) {
                             * m = pau.matcher(aus); if (m.find()) {
                             * out.println("How many AUs=" + m.group(1)); noAUs =
                             * Integer.parseInt(m.group(1)); } else {
                             * out.println("not found within[" + aus + "]"); } }
                             */
                        }

                        break;
                    case XMLStreamConstants.CHARACTERS:
                        break;

                    case XMLStreamConstants.ATTRIBUTE:
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (xmlr.getLocalName().equals("columndescriptor")) {
                            withinColumndescriptor = false;
                            logger.log(Level.FINE, "leaving columndescriptor");
                        } else if (xmlr.getLocalName().equals("row")) {
                            if (withinRow) {
                                logger.log(Level.FINE, "========= end of the target row element");
                                withinRow = false;
                            }
                            if (!isCrawlStatusActive) {
                                tabularData.add(rowData);
                                tableData.add(rowDataH);

                            } else {
                                rowIgnored++;
                                rowCounter--;
                            }
                            rowData = null;
                            rowDataH = null;
                            isCrawlStatusActive = false;
                        } else if (xmlr.getLocalName().equals("cell")) {
                            // rowDataH.add(cellDatum);
                            cellCounter++;
                            withinCell = false;
                            currentColumnName = null;
                            currentCellValue = null;
                            currentCellKey = null;
                            isCrawlStatusColumn = false;
                            valueTagCounter = 0;
                            logger.log(Level.FINE, "leaving cell");
                        } else if (xmlr.getLocalName().equals("columnname")) {
                            logger.log(Level.FINE, "leaving columnname");
                        } else if (xmlr.getLocalName().equals("reference")) {
                            withinReference = false;
                        } else if (xmlr.getLocalName().equals("summaryinfo")) {
                            logger.log(Level.FINE, "si={0}", si.toString());
                            summaryInfoList.add(si);
                            si = null;
                            withinSummaryinfo = false;
                        } else if (xmlr.getLocalName().equals("value")) {
                            logger.log(Level.FINE, "leaving value");
                        } else {
                            logger.log(Level.FINE, "--------- end tag = <{0}> ---------",
                                    curElement);
                        }

                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        logger.log(Level.FINE, "Total of {0} row occurrences", rowCounter);
                } // end: switch
            } // end:while
        } catch (XMLStreamException ex) {
            logger.log(Level.WARNING, "XMLStreamException occurs", ex);
            this.isTargetPageValid = false;

        } catch (RuntimeException re){
            logger.log(Level.WARNING, "some RuntimeException occurs", re);
            this.isTargetPageValid = false;
        } catch (Exception e){
            logger.log(Level.WARNING, "some Exception occurs", e);
            this.isTargetPageValid = false;
        } finally {
            // 5. close reader/IO
            if (xmlr != null) {
                try {
                    xmlr.close();
                } catch (XMLStreamException ex) {
                    logger.log(Level.WARNING, "XMLStreamException occurs during close()", ex);
                }
            }
            if (!this.isTargetPageValid){
                logger.log(Level.WARNING, "This parsing session may not be complete due to some exception reported earlier");
            }
        } // end of try


        if (currentTableId.equals("V3PollerDetailTable")) {
            summaryInfoList.add(new SummaryInfo("auId", 4, siAuId));
            summaryInfoMap =
                new LinkedHashMap<String, String>();
            for (SummaryInfo si : summaryInfoList) {
                summaryInfoMap.put(si.getTitle(), si.getValue());
            }
        }

        // parsing summary
        logger.log(Level.FINE, "###################### parsing summary ######################");
        logger.log(Level.FINE, "currentTableId={0}", currentTableId);
        logger.log(Level.FINE, "currentTableTitle={0}", currentTableTitle);
        logger.log(Level.FINE, "currentTableKey={0}", currentTableKey);

        logger.log(Level.FINE, "columndescriptorList={0}", columndescriptorList);
        logger.log(Level.FINE, "# of columndescriptors={0}", columndescriptorList.size());
        logger.log(Level.FINE, "typeList={0}", typeList);
        logger.log(Level.FINE, "# of rows counted={0}", rowCounter);
        logger.log(Level.FINE, "# of rows excluded[active ones are excluded]={0}", rowIgnored);
        logger.log(Level.FINE, "summaryInfoList:size={0}", summaryInfoList.size());
        logger.log(Level.FINE, "summaryInfoList={0}", summaryInfoList);
        logger.log(Level.FINE, "table: cell counts = {0}", cellCounter);
        logger.log(Level.FINE, "tableData[map]=\n{0}", tableData);
        logger.log(Level.FINE, "tabularData[list]=\n{0}", tabularData);


        /*
         * if (currentTableId.equals("ArchivalUnitStatusTable")) { if
         * (rowCounter == noAUs) { out.println("au counting is OK=" +
         * rowCounter); } else { err.println("au counting disagreement"); throw
         * new RuntimeException("parsing error is suspected"); } }
         */
        logger.log(Level.FINE, " completed in {0} ms\n\n", (System.currentTimeMillis() - startTime));

        if (!columndescriptorList.isEmpty()) {
            int noCols = columndescriptorList.size();
            if (currentTableId.equals("V3PollerTable") && !hasErrorsColumn) {
                noCols--;
            }
            int noCellsExpd = rowCounter * noCols;
            if (noCols > 0) {
                // this table has a table
                logger.log(Level.FINE, "checking parsing results: table dimmensions");
                if (noCellsExpd == cellCounter) {
                    logger.log(Level.FINE, "table dimensions and cell-count are consistent");
                } else {
                    int diff = noCellsExpd - cellCounter;
                    logger.log(Level.FINE, "The table has {0} incomplete cells", diff);
                    hasIncompleteRows = true;
                    setIncompleteRowList();
                    logger.log(Level.FINE, "incomplete rows: {0}", incompleteRows);
                }
            }
        }
    }
//------------------------------------------------------------------------------
    List<Integer> incompleteRows = new ArrayList<Integer>();

    /**
     *
     * @return
     */
    public List<Integer> getIncompleteRowList() {
        return incompleteRows;
    }

    void setIncompleteRowList() {
        int noCols = columndescriptorList.size();
        for (int i = 0; i < rowCounter; i++) {
            if (tabularData.get(i).size() != noCols) {
                incompleteRows.add(i);
            }
        }
    }

    /**
     * @return the tableId
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * @return the columndescriptorList
     */
    public List<String> getColumndescriptorList() {
        return columndescriptorList;
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
     * @return the summaryInfoList
     */
    public List<SummaryInfo> getSummaryInfoList() {
        return summaryInfoList;
    }

    /**
     * @return the typeList
     */
    public List<String> getTypeList() {
        return typeList;
    }

    /**
     * @return the tableData
     */
    public List<Map<String, String>> getTableData() {
        return tableData;
    }

    /**
     * @return the tabularData
     */
    public List<List<String>> getTabularData() {
        return tabularData;
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
        //logger.log(Level.FINE, "value="+value);
        values.add(value);
        return values;
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
    public boolean hasIncompleteRows = false;

    /**
     *
     * @return
     */
    public boolean hasRowTags() {
        return rowCounter > 0 ? true : false;
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


    boolean pollAgreement100pcnt=true;

    public boolean isPollAgreement100pcnt() {
        return pollAgreement100pcnt;
    }

    public void setPollAgreement100pcnt(boolean pollAgreement100pcnt) {
        this.pollAgreement100pcnt = pollAgreement100pcnt;
    }


    Map<String, String> summaryInfoMap;

    public Map<String, String> getSummaryInfoMap() {
        return summaryInfoMap;
    }

    public void setSummaryInfoMap(Map<String, String> summaryInfoMap) {
        this.summaryInfoMap = summaryInfoMap;
    }




    private Set<String> getTargetPollIdSet() {
        logger.log(Level.FINE, "within getTargetPollIdSet()");
        Set<String> targetPollIdSet = new LinkedHashSet<String>();
        if (hasIncompleteRows) {
            logger.log(Level.FINE, "V3PollerTable has incomplete rows");
            for (int i = 0; i < tabularData.size(); i++) {
                String tmpPollId = null;
                String tmpResult = null;
                // 6th agreement: String
                if ((tableData.get(i).containsKey("agreement"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("agreement"))) {

                    tmpResult = tableData.get(i).get("agreement");
                }
                // 8th pollId: String
                if ((tableData.get(i).containsKey("pollId"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("pollId"))) {
                    tmpPollId = tableData.get(i).get("pollId");
                }

                if (tmpResult != null && tmpResult.equals("100.00%")) {
                    targetPollIdSet.add(tmpPollId);
                }
            }
        } else {
            logger.log(Level.FINE, "V3PollerTable has complete rows");
            for (int i = 0; i < tabularData.size(); i++) {
                if (tabularData.get(i).get(6) != null &&
                        tabularData.get(i).get(6).equals("100.00%")) {
                    targetPollIdSet.add(tabularData.get(i).get(9));
                }
            }
        }
        return targetPollIdSet;
    }


    private Set<String> getLessthan100pcntPollIdSet() {
        logger.log(Level.FINE, "within getLessthan100pcntPollIdSet()");
        Set<String> targetPollIdSet = new LinkedHashSet<String>();
//        if (hasIncompleteRows) {
//            logger.log(Level.FINE, "V3PollerTable has incomplete rows");
            for (int i = 0; i < tabularData.size(); i++) {

                if (
                    (tableData.get(i).containsKey("status"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("status"))
                        && !tableData.get(i).get("status").equalsIgnoreCase("Complete")
                    ) {
                    logger.log(Level.FINE,
                        "skip this non-100%-poll={0} whose state is not complete={1}",
                        new Object[]{tableData.get(i).containsKey("pollId"),
                    tableData.get(i).get("status")});
                    continue;
                }

                // 6th agreement: String
                if ((tableData.get(i).containsKey("agreement"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("agreement"))
                        && !tableData.get(i).get("agreement").equals("--")
                        && !tableData.get(i).get("agreement").equals("100.00%")
                        ) {
                    logger.log(Level.FINE, "non-100%-case:agreement value={0}", 
                            tableData.get(i).get("agreement"));
                } else {
                    continue;
                }
                // 8th pollId: String
                if ((tableData.get(i).containsKey("pollId"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("pollId"))) {
                    targetPollIdSet.add(tableData.get(i).get("pollId"));
                } else {
                    continue;
                }
                
            }
//        } else {
//            logger.log(Level.FINE, "V3PollerTable has complete rows");
//            for (int i = 0; i < tabularData.size(); i++) {
//                if (tabularData.get(i).get(6) != null &&
//                        !tabularData.get(i).get(6).equals("100.00%") &&
//                        !tabularData.get(i).get(6).equals("--")) {
//                    targetPollIdSet.add(tabularData.get(i).get(9));
//                }
//            }
//        }
        return targetPollIdSet;
    }








    List<String> getSuccessfulPollReplicaIIpData() {
        logger.log(Level.FINE,
                "within getSuccessfulPollReplicaIIpData() for tableKey={0}",
                tableKey);
        List<String> tmp = new ArrayList<String>();
        if (hasIncompleteRows) {
            // map: check-key is peerStatus
            logger.log(Level.FINE, "This V3PollerDetailTable has incomplete rows: use map");

            for (int i = 0; i < tabularData.size(); i++) {

                // 2nd peerStatus: String
                if ((tableData.get(i).containsKey("peerStatus"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("peerStatus"))
                        &&
                        (
                        tableData.get(i).get("peerStatus").equals("Complete")
                        ||
                        tableData.get(i).get("peerStatus").equals("Voted") // 2012-02-19 this special case was found
                        )

                        ) {
                    String ipAddress = getPeerIpAddress(
                            tableData.get(i).get("identity"));
                    if (ipAddress == null) {
                        continue;
                    }
                    logger.log(Level.FINE, "parsed Ip Address={0}", ipAddress);
                    tmp.add(ipAddress);
                }
            }
        } else {
            // list: 4 elemets such as
            // TCP:[141.211.146.29]:9729, Complete, FinalizePoller, 14:36:43 08/17/11
            logger.log(Level.FINE, 
                    "This V3PollerDetailTable has complete rows: use list");
            for (int i = 0; i < tabularData.size(); i++) {
                if (tabularData.get(i).get(1) != null
                        &&
                        (
                        tabularData.get(i).get(1).equals("Complete")
                        ||
                        tabularData.get(i).get(1).equals("Voted")
                        )

                        ) {
                    String ipAddress = 
                            getPeerIpAddress(tabularData.get(i).get(0));
                    if (ipAddress == null) {
                        continue;
                    }
                    logger.log(Level.FINE, "parsed Ip Address={0}", ipAddress);
                    tmp.add(ipAddress);
                }
            }
        }
        if (tmp == null || tmp.isEmpty()) {
            logger.log(Level.WARNING, 
                    "ip list is null or empty for this poll id:{0}",
                    tableKey);
            logger.log(Level.FINE, "table data dump for check:{0}",
                    xstream.toXML(tableData));
            tmp = null;

        } else {
                logger.log(Level.FINE, 
                        "the size of the ip list for this poll id({0})={1}",
                        new Object[]{tableKey, tmp.size()});
        }
        return tmp;
    }


    Map<String, Map<String, String>> getUnsuccessfulPollParticipantData(){
        Map<String, Map<String, String>> participantData =
            new LinkedHashMap<String, Map<String, String>>();
        logger.log(Level.FINE,
                "within getUnsuccessfulPollParticipantData() for tableKey={0}",
                tableKey);
            for (int i = 0; i < tableData.size(); i++) {


                    String ipAddress = getPeerIpAddress(
                            tableData.get(i).get("identity"));
                    if (ipAddress == null) {
                        continue;
                    }
                    logger.log(Level.FINE, "parsed Ip Address={0}", ipAddress);
                    participantData.put(ipAddress, tableData.get(i));
            }
        return participantData;
    }

    Map<String, String> addUnsuccessfulPollPeerVotingData(Map<String, String> summaryInfoMap){

        logger.log(Level.FINE,
                "within getUnsuccessfulPollPeerData() for tableKey={0}",
                tableKey);
        int invitedPeers = 0;
        int votedPeers = 0;
        int pct100Peers = 0;
            for (int i = 0; i < tableData.size(); i++) {


                String ipAddress = getPeerIpAddress(
                    tableData.get(i).get("identity"));
                if (ipAddress == null) {
                    continue;
                } else {
                    invitedPeers++;
                    logger.log(Level.FINE, "parsed Ip Address={0}", ipAddress);

                    if ((tableData.get(i).containsKey("peerStatus"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("peerStatus"))
                        && (tableData.get(i).get("peerStatus").equals("Complete")
                        || tableData.get(i).get("peerStatus").equals("Voted") // 2012-02-19 this special case was found
                        )) {
                        votedPeers++;

                        if (tableData.get(i).containsKey("agreement")
                            && StringUtils.isNotEmpty(tableData.get(i).get("agreement"))
                            && tableData.get(i).get("agreement").equals("100.00%")) {
                            pct100Peers++;
                        }

                    }
                }
            }
            summaryInfoMap.put("invitedPeers", Integer.toString(invitedPeers));
            summaryInfoMap.put("votedPeers", Integer.toString(votedPeers));
            summaryInfoMap.put("pct100Peers", Integer.toString(pct100Peers));
        return summaryInfoMap;
    }

    List<String> getUnsuccessfulPollReplicaIIpData() {
        logger.log(Level.FINE,
                "within getUnsuccessfulPollReplicaIIpData() for tableKey={0}",
                tableKey);
        List<String> tmp = new ArrayList<String>();
        if (hasIncompleteRows) {
            // map: check-key is peerStatus
            logger.log(Level.FINE, "This V3PollerDetailTable has incomplete rows: use map");

            for (int i = 0; i < tabularData.size(); i++) {

                // 2nd peerStatus: String
                if ((tableData.get(i).containsKey("peerStatus"))
                        && StringUtils.isNotEmpty(tableData.get(i).get("peerStatus"))
                        &&
                        (
                        tableData.get(i).get("peerStatus").equals("Complete")
                        ||
                        tableData.get(i).get("peerStatus").equals("Voted") // 2012-02-19 this special case was found
                        )

                        ) {
                    String ipAddress = getPeerIpAddress(
                            tableData.get(i).get("identity"));
                    if (ipAddress == null) {
                        continue;
                    }
                    logger.log(Level.FINE, "parsed Ip Address={0}", ipAddress);
                    tmp.add(ipAddress);
                }
            }
        } else {
            // list: 4 elemets such as
            // TCP:[141.211.146.29]:9729, Complete, FinalizePoller, 14:36:43 08/17/11
            logger.log(Level.FINE,
                    "This V3PollerDetailTable has complete rows: use list");
            for (int i = 0; i < tabularData.size(); i++) {
                if (tabularData.get(i).get(1) != null
                        &&
                        (
                        tabularData.get(i).get(1).equals("Complete")
                        ||
                        tabularData.get(i).get(1).equals("Voted")
                        )

                        ) {
                    String ipAddress =
                            getPeerIpAddress(tabularData.get(i).get(0));
                    if (ipAddress == null) {
                        continue;
                    }
                    logger.log(Level.FINE, "parsed Ip Address={0}", ipAddress);
                    tmp.add(ipAddress);
                }
            }
        }
        if (tmp == null || tmp.isEmpty()) {
            logger.log(Level.WARNING,
                    "ip list is null or empty for this poll id:{0}",
                    tableKey);
            logger.log(Level.FINE, "table data dump for check:{0}",
                    xstream.toXML(tableData));
            tmp = null;

        } else {
                logger.log(Level.FINE,
                        "the size of the ip list for this poll id({0})={1}",
                        new Object[]{tableKey, tmp.size()});
        }
        return tmp;
    }




    /**
     *
     * @return
     */
    public LOCKSSDaemonStatusTableTO getLOCKSSDaemonStatusTableTO() {
        LOCKSSDaemonStatusTableTO ldstTO = new LOCKSSDaemonStatusTableTO();
        ldstTO.tableId = tableId;
        ldstTO.tableTitle = tableTitle;
        ldstTO.tableKey = tableKey;
        ldstTO.tableData = tableData;
        ldstTO.tabularData = tabularData;
        ldstTO.hasIncompleteRows = hasIncompleteRows;
        ldstTO.incompleteRows = incompleteRows;
        ldstTO.summaryInfoList = summaryInfoList;
        ldstTO.columndescriptorList = columndescriptorList;
        ldstTO.typeList = typeList;
        ldstTO.boxInfoMap = null;
        ldstTO.summaryInfoMap = this.summaryInfoMap;







        if (isTargetPageValid) {
            if (tableId.equals("V3PollerTable")) {
                ldstTO.targetPollIdSet = getTargetPollIdSet();

                ldstTO.targetPollIdSetUS = getLessthan100pcntPollIdSet();


            } else {
                ldstTO.targetPollIdSet = null;
                ldstTO.targetPollIdSetUS = null;
            }

            if (tableId.equals("V3PollerDetailTable")) {
                ldstTO.setPollAgreement100pcnt(this.isPollAgreement100pcnt());
                if (ldstTO.isPollAgreement100pcnt()) {
                    logger.log(Level.FINE, "100% case");
                    if (ldstTO.currentSuccessfulReplicaIpList == null) {
                        ldstTO.currentSuccessfulReplicaIpList =
                                getSuccessfulPollReplicaIIpData();
                    } else {
                        logger.log(Level.FINE,
                                "currentSuccessfulReplicaIpList is not null: size={0}",
                                ldstTO.currentSuccessfulReplicaIpList.size());
                    }

                    ldstTO.currentUnsuccessfulReplicaIpList=null;

                } else {
                    logger.log(Level.FINE, "less than 100% case");
                    if (ldstTO.currentUnsuccessfulReplicaIpList == null) {
                        ldstTO.currentUnsuccessfulReplicaIpList = 
                                getUnsuccessfulPollReplicaIIpData();
                    } else {
                        logger.log(Level.FINE,
                                "currentUnsuccessfulReplicaIpList is not null: size={0}",
                                ldstTO.currentUnsuccessfulReplicaIpList.size());
                    }
                    ldstTO.currentSuccessfulReplicaIpList = null;


                    //
                    ldstTO.currentUnsuccessfulPollParticipantData =
                        getUnsuccessfulPollParticipantData();
                    // add extra voting data
                    ldstTO.summaryInfoMap = addUnsuccessfulPollPeerVotingData(this.summaryInfoMap);

                    
                }

                if (ldstTO.currentPollId == null
                        && tableKey != null) {
                    logger.log(Level.FINE, 
                            "currentPollId is not set: use the tableKey:{0}",
                            tableKey);
                    ldstTO.currentPollId = tableKey;
                } else {

                    logger.log(Level.FINE, "currentPollId={0}:tableKey={1}",
                            new Object[]{ldstTO.currentPollId, tableKey});
                    
                }

            } else {
                ldstTO.currentSuccessfulReplicaIpList = null;
                ldstTO.currentUnsuccessfulReplicaIpList = null;
            }


        } else {
            ldstTO.targetPollIdSet = null;
            ldstTO.targetPollIdSetUS= null;
        }
        ldstTO.boxId = 0L;
        ldstTO.ipAddress = null;
        ldstTO.pollIdToDurationMap = null;
        ldstTO.pollIdToAuNameMap = null;
        ldstTO.pollIdToEndTimeMap = null;
        ldstTO.boxHttpStatusOK = true;

        ldstTO.pageReadable = isTargetPageValid;

        logger.log(Level.FINE, "table={0}:LOCKSSDaemonStatusTableXmlStreamParser.getLOCKSSDaemonStatusTableTO(): ldstTO:\n{1}",
            new Object[]{tableId, xstream.toXML(ldstTO)});

        return ldstTO;
    }


    /**
     *
     * @param rawList
     * @return
     */
    String getPeerIpAddress(String rawIp) {
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


}
