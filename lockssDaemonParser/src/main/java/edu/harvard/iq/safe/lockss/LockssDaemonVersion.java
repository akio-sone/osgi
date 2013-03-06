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
package edu.harvard.iq.safe.lockss;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Akio Sone
 */
public class LockssDaemonVersion implements Comparator<LockssDaemonVersion> {
static final Logger logger =
            Logger.getLogger(LockssDaemonVersion.class.getName());

    public LockssDaemonVersion() {
        }

        public LockssDaemonVersion(String daemonVersion) {
            this.versionString = daemonVersion;
        }
        protected String versionString;

        /**
         * Get the value of versionString
         *
         * @return the value of versionString
         */
        public String getVersionString() {
            return versionString;
        }

        /**
         * Set the value of versionString
         *
         * @param versionString new value of versionString
         */
        public void setVersionString(String versionString) {
            this.versionString = versionString;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LockssDaemonVersion other = (LockssDaemonVersion) obj;
            if ((this.versionString == null) ? (other.versionString != null) : !this.versionString.equals(other.versionString)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (this.versionString != null ? this.versionString.hashCode() : 0);
            return hash;
        }

        public int compareTo(LockssDaemonVersion ldv) {
            return (this.versionString).compareTo(ldv.versionString);
        }

        @Override
        public int compare(LockssDaemonVersion ldv1, LockssDaemonVersion ldv2) {
            // version string such as "1.52.3"
            
            int result = 0;
            if (StringUtils.isBlank(ldv1.getVersionString()) ||
                    StringUtils.isBlank(ldv2.getVersionString())){
                return result;
            }
            String[] ldv1s = ldv1.getVersionString().split("\\.");
            String[] ldv2s = ldv2.getVersionString().split("\\.");

            logger.log(Level.INFO, "how many elements={0}", ldv1s.length);
            logger.log(Level.INFO, "how many elements={1}", ldv2s.length);
            Integer l1 = Integer.parseInt(ldv1s[0]);
            Integer l2 = Integer.parseInt(ldv2s[0]);
            if (l1 > l2) {
                result = 1;
            } else if (l1 < l2) {
                result = -1;
            } else if (l1 == l2) {
                Integer m1 = Integer.parseInt(ldv1s[1]);
                Integer m2 = Integer.parseInt(ldv2s[1]);
                if (m1 > m2) {
                    result = 1;
                } else if (m1 < m2) {
                    result = -1;
                } else if (m1 == m2) {
                    Integer r1 = Integer.parseInt(ldv1s[2]);
                    Integer r2 = Integer.parseInt(ldv2s[2]);
                    if (r1 > r2) {
                        result = 1;
                    } else if (r1 < r2) {
                        result = -1;
                    } else if (r1 == r2) {
                        result = 0;
                    }
                }
            }
            return result;
        }
}
