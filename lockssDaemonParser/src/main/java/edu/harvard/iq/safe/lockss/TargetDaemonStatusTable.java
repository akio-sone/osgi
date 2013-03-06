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

/**
 *
 * @author asone
 */
public enum TargetDaemonStatusTable {
    /**
     *
     */
    PLATFORM_STATUS_TABLE("PlatformStatus"),
    /**
     *
     */
    ARCHIVAL_UNIT_STATUS_TABLE("ArchivalUnitStatusTable"),
    /**
     *
     */
    REPOSITORY_SPACE_TABLE("RepositorySpace"),
    /**
     *
     */
    V3POLLER_TABLE("V3PollerTable"),
    /**
     *
     */
    CRAWL_STATUS_TABLE("crawl_status_table"),
    /**
     *
     */
    V3POLLER_DETAIL_TABLE("V3PollerDetailTable"),

    
    
    PEER_REPAIR_TABLE("PeerRepair");
    
    private String tableName;

    private TargetDaemonStatusTable(String tableName){
        this.tableName=tableName;
    }

    /**
     *
     * @return
     */
    public String getTableName() {
        return tableName;
    }

}
