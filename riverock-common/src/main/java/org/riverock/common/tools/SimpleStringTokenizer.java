/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.riverock.common.tools;

/**
 * 
 * $Revision: 1040 $
 * $Date: 2006-11-14 13:57:38 +0000 (Tue, 14 Nov 2006) $
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
