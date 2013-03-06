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

import java.io.Serializable;

/**
 * Supersede Class PeerRepair (as of 2012-11-28)
 * 
 * @author Akio Sone
 */
public class PeerRepairBox implements Serializable{

    String box;

    Long lastAgreeTime;

    Double lastPercentAgreement;

    public PeerRepairBox() {
    }


    public PeerRepairBox(String box, Long lastAgreeTime) {
        this.box = box;
        this.lastAgreeTime = lastAgreeTime;
    }

    public PeerRepairBox(String box, Long lastAgreeTime, Double lastPercentAgreement) {
        this.box = box;
        this.lastAgreeTime = lastAgreeTime;
        this.lastPercentAgreement = lastPercentAgreement;
    }





    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public Long getLastAgreeTime() {
        return lastAgreeTime;
    }

    public void setLastAgreeTime(Long lastAgreeTime) {
        this.lastAgreeTime = lastAgreeTime;
    }

    public Double getLastPercentAgreement() {
        return lastPercentAgreement;
    }

    public void setLastPercentAgreement(Double lastPercentAgreement) {
        this.lastPercentAgreement = lastPercentAgreement;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.box != null ? this.box.hashCode() : 0);
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
        final PeerRepairBox other = (PeerRepairBox) obj;
        if ((this.box == null) ? (other.box != null) : !this.box.equals(other.box)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PeerRepairBox{" + "box=" + box + ", lastAgreeTime=" + lastAgreeTime + ", lastPercentAgreement=" + lastPercentAgreement + '}';
    }

}
