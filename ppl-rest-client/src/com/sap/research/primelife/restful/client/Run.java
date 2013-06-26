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
package com.sap.research.primelife.restful.client;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.sap.research.primelife.restful.client.test.CreatePiiTest;
import com.sap.research.primelife.restful.client.test.DeletePiiTest;
import com.sap.research.primelife.restful.client.test.FileTest;
import com.sap.research.primelife.restful.client.test.PolicyTest;
import com.sap.research.primelife.restful.client.test.RequestPiiTest;
import com.sap.research.primelife.restful.client.test.UpdatePiiTest;

/**
 * 
 * 
 *
 */
public class Run {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		CreatePiiTest cTest = new CreatePiiTest();
		UpdatePiiTest uTest = new UpdatePiiTest();
		DeletePiiTest dTest = new DeletePiiTest();
		FileTest fTest = new FileTest();
		RequestPiiTest rTest = new RequestPiiTest();
		PolicyTest policyTest = new PolicyTest();
		
		cTest.run();
		//cTest.run();
		//uTest.run();
		//dTest.run();
		//fTest.run();
		//test();
		//rTest.run();
		//policyTest.run();
		
		//fTest.read("../ppl-engine/systemconfig.txt");
		//test();
	}
	
	public static void test(){
		/*UnThread thread1 = new UnThread();
		UnThread thread2 = new UnThread();
		thread1.start();
		thread2.start();*/
		
		UnTimer timer1 = new UnTimer("Timer1");
		UnTimer timer2 = new UnTimer("Timer2");
		timer1.start();
		timer2.start();
	}
	
	public static synchronized void foo(String txt){
		System.out.println("I am thread #"+Thread.currentThread().getId() + " I'm going to sleep 10 seconds and blocks the others");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("I am thread #"+Thread.currentThread().getId() + " Message: " + txt);
	}

}
