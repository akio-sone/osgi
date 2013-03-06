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

import java.util.logging.Logger;

/**
 *
 * @author Akio Sone
 */
public class PollParticipantData {

    private static final Logger logger = Logger.getLogger(PollParticipantData.class.getName());

    public PollParticipantData() {
    }

    public PollParticipantData(String pollId, String ipAddress, String peerStatus) {
        this.pollId = pollId;
        this.ipAddress = ipAddress;
        this.peerStatus = peerStatus;
    }


    

    public PollParticipantData(String pollId, String ipAddress, String peerStatus, Double agreement, Long numAgree, Long numDisagree, Long numPolleronly, Long numVoteronly, Long bytesH, Long bytesR) {
        this.pollId = pollId;
        this.ipAddress = ipAddress;
        this.peerStatus = peerStatus;
        this.agreement = agreement;
        this.numAgree = numAgree;
        this.numDisagree = numDisagree;
        this.numPolleronly = numPolleronly;
        this.numVoteronly = numVoteronly;
        this.bytesH = bytesH;
        this.bytesR = bytesR;
    }
    






    String pollId;

    /**
     * Get the value of pollId
     *
     * @return the value of pollId
     */
    public String getPollId() {
        return pollId;
    }

    /**
     * Set the value of pollId
     *
     * @param pollId new value of pollId
     */
    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    String ipAddress;

    /**
     * Get the value of ipAddress
     *
     * @return the value of ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Set the value of ipAddress
     *
     * @param ipAddress new value of ipAddress
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

        String peerStatus;

    /**
     * Get the value of peerStatus
     *
     * @return the value of peerStatus
     */
    public String getPeerStatus() {
        return peerStatus;
    }

    /**
     * Set the value of peerStatus
     *
     * @param peerStatus new value of peerStatus
     */
    public void setPeerStatus(String peerStatus) {
        this.peerStatus = peerStatus;
    }


        Double agreement;

    /**
     * Get the value of agreement
     *
     * @return the value of agreement
     */
    public Double getAgreement() {
        return agreement;
    }

    /**
     * Set the value of agreement
     *
     * @param agreement new value of agreement
     */
    public void setAgreement(Double agreement) {
        this.agreement = agreement;
    }


        Long numAgree;

    /**
     * Get the value of numAgree
     *
     * @return the value of numAgree
     */
    public Long getNumAgree() {
        return numAgree;
    }

    /**
     * Set the value of numAgree
     *
     * @param numAgree new value of numAgree
     */
    public void setNumAgree(Long numAgree) {
        this.numAgree = numAgree;
    }


        Long numDisagree;

    /**
     * Get the value of numDisagree
     *
     * @return the value of numDisagree
     */
    public Long getNumDisagree() {
        return numDisagree;
    }

    /**
     * Set the value of numDisagree
     *
     * @param numDisagree new value of numDisagree
     */
    public void setNumDisagree(Long numDisagree) {
        this.numDisagree = numDisagree;
    }



    Long numPolleronly;

    /**
     * Get the value of numPolleronly
     *
     * @return the value of numPolleronly
     */
    public Long getNumPolleronly() {
        return numPolleronly;
    }

    /**
     * Set the value of numPolleronly
     *
     * @param numPolleronly new value of numPolleronly
     */
    public void setNumPolleronly(Long numPolleronly) {
        this.numPolleronly = numPolleronly;
    }


    Long numVoteronly;

    /**
     * Get the value of numVoteronly
     *
     * @return the value of numVoteronly
     */
    public Long getNumVoteronly() {
        return numVoteronly;
    }

    /**
     * Set the value of numVoteronly
     *
     * @param numVoteronly new value of numVoteronly
     */
    public void setNumVoteronly(Long numVoteronly) {
        this.numVoteronly = numVoteronly;
    }


    Long bytesH;

    /**
     * Get the value of bytesH
     *
     * @return the value of bytesH
     */
    public Long getBytesH() {
        return bytesH;
    }

    /**
     * Set the value of bytesH
     *
     * @param bytesH new value of bytesH
     */
    public void setBytesH(Long bytesH) {
        this.bytesH = bytesH;
    }

    Long bytesR;

    /**
     * Get the value of bytesR
     *
     * @return the value of bytesR
     */
    public Long getBytesR() {
        return bytesR;
    }

    /**
     * Set the value of bytesR
     *
     * @param bytesR new value of bytesR
     */
    public void setBytesR(Long bytesR) {
        this.bytesR = bytesR;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.pollId != null ? this.pollId.hashCode() : 0);
        hash = 79 * hash + (this.ipAddress != null ? this.ipAddress.hashCode() : 0);
        hash = 79 * hash + (this.bytesH != null ? this.bytesH.hashCode() : 0);
        hash = 79 * hash + (this.bytesR != null ? this.bytesR.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PollParticipantData other = (PollParticipantData) obj;
        if ((this.pollId == null) ? (other.pollId != null) : !this.pollId.equals(other.pollId)) {
            return false;
        }
        if ((this.ipAddress == null) ? (other.ipAddress != null) : !this.ipAddress.equals(other.ipAddress)) {
            return false;
        }
        if (this.bytesH != other.bytesH && (this.bytesH == null || !this.bytesH.equals(other.bytesH))) {
            return false;
        }
        if (this.bytesR != other.bytesR && (this.bytesR == null || !this.bytesR.equals(other.bytesR))) {
            return false;
        }
        return true;
    }



}
