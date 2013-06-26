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
package com.sap.research.primelife.exceptions;

/**
 * This exception is thrown in case a syntax error is in a structural XML element.
 * 
 * @Version 0.1
 * @Date May 20, 2010
 * 
 */
public class SyntaxException extends Exception {

	private static final long serialVersionUID = -1098706356175119588L;

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public SyntaxException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param message
	 *            The detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
	 *            method.
	 */
	public SyntaxException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * 
	 * @param message
	 *            The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause
	 *            The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
	 *            permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public SyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of (cause==null ? null :
	 * cause.toString()) (which typically contains the class and detail message of cause).
	 * 
	 * @param cause
	 *            The cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is
	 *            permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public SyntaxException(Throwable cause) {
		super(cause);
	}

}
