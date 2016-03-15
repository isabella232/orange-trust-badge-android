/*
 * Copyright 2016 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Orange Trust Badge library
 *
 * Module name: com.orange.essentials:otb
 * Version:     1.0
 * Created:     2016-03-15 by Aurore Penault, Vincent Boesch, and Giovanni Battista Accetta
 */
package com.orange.essentials.otb.model;

import com.orange.essentials.otb.model.type.TermType;

import java.io.Serializable;

/**
 * <p/>
 * File name:   Term
 * Version:     0.1.0 (see AndroidManifest.xml)
 * Created:     27/01/2016
 * Created by:  VAPU8214 (Aurore Penault)
 * <p/>
 * A term is a block displayed in "Terms" section
 */
public class Term implements Serializable {

    private static final long serialVersionUID = -7647777571996202166L;

    /**
     * Type of a term
     */
    private TermType mTermType;
    /**
     * Localized resource id for term's title
     */
    private int mTitleId;
    /**
     * Localized resource id for term's content
     */
    private int mContentId;

    public Term(TermType termType, int titleId, int contentId) {
        mTermType = termType;
        mTitleId = titleId;
        mContentId = contentId;
    }

    public TermType getTermType() {
        return mTermType;
    }

    public void setTermType(TermType termType) {
        mTermType = termType;
    }

    public int getTitleId() {
        return mTitleId;
    }

    public void setTitleId(int titleId) {
        mTitleId = titleId;
    }

    public int getContentId() {
        return mContentId;
    }

    public void setContentId(int contentId) {
        mContentId = contentId;
    }
}
