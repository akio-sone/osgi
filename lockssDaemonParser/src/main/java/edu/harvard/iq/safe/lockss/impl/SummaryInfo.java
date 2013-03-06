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

package edu.harvard.iq.safe.lockss.impl;

// TODO: Auto-generated Javadoc
/**
 * The Class SummaryInfo.
 *
 * @author Akio Sone at UNC-Odum
 */
public class SummaryInfo {

    /** The title. */
    private String title;

    /** The type. */
    private int type;

    /** The value. */
    private String value;

    /** The footnote. */
    private String footnote;

    /**
     *
     */
    public SummaryInfo() {
    }

    /**
     * Instantiates a new summary info.
     *
     * @param title the title
     * @param type the type
     * @param value the value
     */
    public SummaryInfo(String title, int type, String value) {
        this.title = title;
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public int getType() {
        return this.type;
    }

    /**
     * 
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the footnote.
     *
     * @return the footnote
     */
    public String getFootnote() {
        return this.footnote;
    }

    /**
     * Sets the footnote.
     *
     * @param footnote the new footnote
     */
    public void setFootnote(String footnote) {
        this.footnote = footnote;
    }

    @Override
    public String toString() {
        String si = null;
        si = "title=[" + title + "], type=[" + type + "], value=[" + value
                + "]";
        return si;
    }
}
