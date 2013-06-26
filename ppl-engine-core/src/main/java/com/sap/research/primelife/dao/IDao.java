/*******************************************************************************
 * Copyright (c) 2013, SAP AG
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *  
 *     - Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *     - Neither the name of the SAP AG nor the names of its contributors may
 *      be used to endorse or promote products derived from this software 
 *      without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.sap.research.primelife.dao;

import java.util.List;

/**
 * Generic interface that makes the CRUD(Create, Read, Update, Delete) operations of any object that is defined in the persistence context.
 * 
 * @Version 0.1
 * @Date Apr 20, 2010
 * 
 */
public interface IDao <T> {

	/**
	 * Persist an object in the Data base.
	 * @param object
	 * 			The object to persist
	 * @return
	 * 			The object persisted
	 */
	public T persistObject(final T object);

	/**
	 * Find an object in the Data base.
	 * @param objectId
	 * 		The id of the object
	 * @return
	 * 		The object if found
	 */
	public T findObject(Class<T> clazz, final Long objectId);

	/**
	 * Delete an object from the Data base.
	 * @param object
	 * 			The object that we would like to delete
	 */
	public void deleteObject(final T object);

	/**
	 * Update an object in the Data base.
	 * @param object
	 * 		The updated object
	 * @return
	 * 		The updated object
	 */
	public T updateObject(final T object);

	/**
	 *  load all the instances of a class from the Data base.
	 * @param clazz
	 * 			The class name of the objects to  load
	 * @return
	 * 			List of objects
	 */
	public List<T> findObjects(Class<T> clazz);

}
