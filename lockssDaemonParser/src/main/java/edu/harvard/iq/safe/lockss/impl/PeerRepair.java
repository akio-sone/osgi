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
package edu.harvard.iq.safe.lockss.impl;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Akio Sone
 */
public class PeerRepair implements Serializable{

    public PeerRepair() {
    }
    
    
    String poller;
    String auName;
    String auId;
    Set<String> fullyAgreedPeerSet;
    Set<String> nonfullyAgreedPeerSet;
//    Double pctAgreement;
    Long lastAgree;

    public String getPoller() {
        return poller;
    }

    public void setPoller(String poller) {
        this.poller = poller;
    }

    public String getAuName() {
        return auName;
    }

    public void setAuName(String auName) {
        this.auName = auName;
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    public Set<String> getFullyAgreedPeerSet() {
        return fullyAgreedPeerSet;
    }

    public void setFullyAgreedPeerSet(Set<String> fullyAgreedPeerSet) {
        this.fullyAgreedPeerSet = fullyAgreedPeerSet;
    }

    public Set<String> getNonfullyAgreedPeerSet() {
        return nonfullyAgreedPeerSet;
    }

    public void setNonfullyAgreedPeerSet(Set<String> nonfullyAgreedPeerSet) {
        this.nonfullyAgreedPeerSet = nonfullyAgreedPeerSet;
    }

//    public Double getPctAgreement() {
//        return pctAgreement;
//    }
//
//    public void setPctAgreement(Double pctAgreement) {
//        this.pctAgreement = pctAgreement;
//    }

    public Long getLastAgree() {
        return lastAgree;
    }

    public void setLastAgree(Long lastAgree) {
        this.lastAgree = lastAgree;
    }


}
