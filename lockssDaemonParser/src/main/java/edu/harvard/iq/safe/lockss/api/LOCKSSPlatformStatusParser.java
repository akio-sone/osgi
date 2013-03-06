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
import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author Akio Sone
 */
public interface LOCKSSPlatformStatusParser {

    /**
     * @return the boxInfoMap
     */
    Map<String, String> getBoxInfoMap();

    /**
     *
     * @return
     */
    LOCKSSDaemonStatusTableTO getLOCKSSDaemonStatusTableTO();

    /**
     *
     * @param is
     */
    void getPlatformStatusData(InputStream is);

    /**
     * @return the summaryInfoMap
     */
    Map<String, String> getSummaryInfoMap();

    /**
     * @param boxInfoMap the boxInfoMap to set
     */
    void setBoxInfoMap(Map<String, String> boxInfoMap);

    /**
     * @param summaryInfoMap the summaryInfoMap to set
     */
    void setSummaryInfoMap(Map<String, String> summaryInfoMap);
    
}