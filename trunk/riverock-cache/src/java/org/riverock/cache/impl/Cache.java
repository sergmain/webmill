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

 * Implementors define a caching algorithm. All implementors

 * <b>must</b> be threadsafe.

 */

public interface Cache {

	/**

	 * Get an item from the cache

	 * @param key

	 * @return the cached object or <tt>null</tt>

	 * @throws CacheException

	 */

	public Object get(Object key) throws CacheException;

	/**

	 * Add an item to the cache

	 * @param key

	 * @param value

	 * @throws CacheException

	 */

	public void put(Object key, Object value) throws CacheException;

	/**

	 * Remove an item from the cache

	 */

	public void remove(Object key) throws CacheException;

	/**

	 * Clear the cache

	 */

	public void clear() throws CacheException;

	/**

	 * Clean up

	 */

	public void destroy() throws CacheException;

	/**

	 * If this is a clustered cache, lock the item

	 */

	public void lock(Object key) throws CacheException;

	/**

	 * If this is a clustered cache, unlock the item

	 */

	public void unlock(Object key) throws CacheException;

	/**

	 * Generate a timestamp

	 */

	public long nextTimestamp();

	/**

	 * Get a reasonable "lock timeout"

	 */

	public int getTimeout();



}













