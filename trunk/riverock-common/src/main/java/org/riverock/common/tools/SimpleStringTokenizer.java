/*
 * org.riverock.common - Supporting classes and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.riverock.common.tools;

/**
 * 
 * $Revision$
 * $Date$
 * $RCSfile$
 *
 */
public class SimpleStringTokenizer {
    public String token[];
    public int countToken;
    public String str;
    public int currentPosition;
    public int maxPosition;
    public boolean retTokens;

    protected void finalize() throws Throwable {
        token = null;
        str = null;

        super.finalize();
    }

    public SimpleStringTokenizer(){}

    public SimpleStringTokenizer(String s) {
        this(s, null);
    }

    public SimpleStringTokenizer(String str_, String token_[]) {
        this.str = str_;
        this.currentPosition = 0;
        this.maxPosition = str.length();

        if (token_ == null) {
            this.token = new String[]{" "};
        }
        else {
            this.token = new String[token_.length];
            System.arraycopy(token_, 0, this.token, 0, token_.length);
        }
        this.countToken = token.length;
    }

    public String nextToken() {
        if ((currentPosition > maxPosition) || (currentPosition == -1))
            return "";

        int i = 0, pos = -1;

        int start = currentPosition;
        while (countToken > i) {
            pos = str.indexOf(token[i], currentPosition);
            if (pos != -1)
                break;
            i++;
        }
        currentPosition = pos;
        if (pos == -1) {
            return str.substring(start);
        }
        currentPosition += token[i].length();
        return str.substring(start, pos);
    }

    public boolean hasMoreTokens() {
        return !((currentPosition > maxPosition) || (currentPosition == -1));
    }

}
