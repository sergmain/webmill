/*

 * org.riverock.cache -- Generic cache implementation

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public

 * License as published by the Free Software Foundation; either

 * version 2 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * General Public License for more details.

 *

 * You should have received a copy of the GNU General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */



/**

 * $Id$

 */

package org.riverock.cache.impl;



/**

 * Generates increasing identifiers (in a single VM only).

 * Not valid across multiple VMs. Identifiers are not necessarily

 * strictly increasing, but usually are.

 */

public final class Timestamper {

	private static short counter = 0;

	private static long time;

	private static final int BIN_DIGITS = 12;

	public static final short ONE_MS = 1<<BIN_DIGITS;

	

	public static long next() {

		synchronized(Timestamper.class) {

			long newTime = System.currentTimeMillis() << BIN_DIGITS;

			if (time<newTime) {

				time = newTime;

				counter = 0;

			}

			else if (counter < ONE_MS - 1 ) {

				counter++;

			}

			

			return time + counter;

		}

	}

	

	private Timestamper() {}

}













