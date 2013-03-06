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
 * Supersede Class PeerRepair (as of 2012-11-28)
 *
 * @author Akio Sone
 */
public class Peers implements Serializable{

    public Peers() {
    }

    String poller;
    String auName;
    String auId;

    Set<PeerRepairBox> fullyAgreedPeerSet;

    Set<PeerRepairBox> nonfullyAgreedPeerSet;

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

    public Set<PeerRepairBox> getFullyAgreedPeerSet() {
        return fullyAgreedPeerSet;
    }

    public void setFullyAgreedPeerSet(Set<PeerRepairBox> fullyAgreedPeerSet) {
        this.fullyAgreedPeerSet = fullyAgreedPeerSet;
    }

    public Set<PeerRepairBox> getNonfullyAgreedPeerSet() {
        return nonfullyAgreedPeerSet;
    }

    public void setNonfullyAgreedPeerSet(Set<PeerRepairBox> nonfullyAgreedPeerSet) {
        this.nonfullyAgreedPeerSet = nonfullyAgreedPeerSet;
    }


    public Long getLastAgree() {
        return lastAgree;
    }

    public void setLastAgree(Long lastAgree) {
        this.lastAgree = lastAgree;
    }
}
