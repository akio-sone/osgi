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

/**
 *
 * @author Akio Sone
 */
public class PeerVoteResult {

    public PeerVoteResult(String box, Integer lastAgreementPercent, 
            String lastConsensusLocalTime, Long lastConsensusUTCTime) {
        this.box = box;
        this.lastAgreementPercent = lastAgreementPercent;
        this.lastConsensusLocalTime = lastConsensusLocalTime;
        this.lastConsensusUTCTime = lastConsensusUTCTime;
    }

    public PeerVoteResult(String box, Integer lastAgreementPercent, 
            String lastConsensusLocalTime) {
        this.box = box;
        this.lastAgreementPercent = lastAgreementPercent;
        this.lastConsensusLocalTime = lastConsensusLocalTime;
    }
    
    
    protected String box;

    /**
     * Get the value of box
     *
     * @return the value of box
     */
    public String getBox() {
        return box;
    }

    /**
     * Set the value of box
     *
     * @param box new value of box
     */
    public void setBox(String box) {
        this.box = box;
    }

    protected Integer lastAgreementPercent;

    /**
     * Get the value of lastAgreementPercent
     *
     * @return the value of lastAgreementPercent
     */
    public Integer getLastAgreementPercent() {
        return lastAgreementPercent;
    }

    /**
     * Set the value of lastAgreementPercent
     *
     * @param lastAgreementPercent new value of lastAgreementPercent
     */
    public void setLastAgreementPercent(Integer lastAgreementPercent) {
        this.lastAgreementPercent = lastAgreementPercent;
    }

    protected String lastConsensusLocalTime;

    /**
     * Get the value of lastConsensusLocalTime
     *
     * @return the value of lastConsensusLocalTime
     */
    public String getLastConsensusLocalTime() {
        return lastConsensusLocalTime;
    }

    /**
     * Set the value of lastConsensusLocalTime
     *
     * @param lastConsensusLocalTime new value of lastConsensusLocalTime
     */
    public void setLastConsensusLocalTime(String lastConsensusLocalTime) {
        this.lastConsensusLocalTime = lastConsensusLocalTime;
    }



    protected Long lastConsensusUTCTime;

    /**
     * Get the value of lastConsensusUTCTime
     *
     * @return the value of lastConsensusUTCTime
     */
    public Long getLastConsensusUTCTime() {
        return lastConsensusUTCTime;
    }

    /**
     * Set the value of lastConsensusUTCTime
     *
     * @param lastConsensusUTCTime new value of lastConsensusUTCTime
     */
    public void setLastConsensusUTCTime(Long lastConsensusUTCTime) {
        this.lastConsensusUTCTime = lastConsensusUTCTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PeerVoteResult other = (PeerVoteResult) obj;
        if ((this.box == null) ? (other.box != null) : !this.box.equals(other.box)) {
            return false;
        }
        if (this.lastAgreementPercent != other.lastAgreementPercent && (this.lastAgreementPercent == null || !this.lastAgreementPercent.equals(other.lastAgreementPercent))) {
            return false;
        }
        if ((this.lastConsensusLocalTime == null) ? (other.lastConsensusLocalTime != null) : !this.lastConsensusLocalTime.equals(other.lastConsensusLocalTime)) {
            return false;
        }
        if (this.lastConsensusUTCTime != other.lastConsensusUTCTime && (this.lastConsensusUTCTime == null || !this.lastConsensusUTCTime.equals(other.lastConsensusUTCTime))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.box != null ? this.box.hashCode() : 0);
        hash = 37 * hash + (this.lastAgreementPercent != null ? this.lastAgreementPercent.hashCode() : 0);
        hash = 37 * hash + (this.lastConsensusLocalTime != null ? this.lastConsensusLocalTime.hashCode() : 0);
        hash = 37 * hash + (this.lastConsensusUTCTime != null ? this.lastConsensusUTCTime.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PeerVoter={");

        String body = this.box+", "+
                       this.lastAgreementPercent.toString()+", "+
                this.lastConsensusLocalTime+", "+
                this.lastConsensusUTCTime.toString() +"}\n";

        sb.append(body);
        return sb.toString();
    }
    
    

}
