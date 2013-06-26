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
package com.sap.research.primelife.dc.timebasedtrigger;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import com.sap.research.primelife.dc.entity.OEEStatus;
import com.sap.research.primelife.dc.event.EventHandler;
import com.sap.research.primelife.dc.event.IEventHandler;

import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;

public class TimeBasedTriggerAtTime extends TimeBasedTrigger {

	private final static Logger LOGGER = Logger.getLogger(TimeBasedTriggerAtTime.class);
	protected Duration maxDelay;
	protected DateAndTime start;
	protected long startMillisec = 0; // Epoch time representation of the Start Date
	
	protected TimeBasedTriggerAtTime(OEEStatus oees){
		this(oees, new EventHandler());
	}
	
	protected TimeBasedTriggerAtTime(OEEStatus oees, IEventHandler eventHandler){
		super(oees, eventHandler);
		maxDelay = ((TriggerAtTime)oees.getTrigger()).getMaxDelay();
		start = ((TriggerAtTime)oees.getTrigger()).getStart();
	}
	
	@Override
	public void start() {
		final TimeBasedTriggerAtTime trigger = this;
		timer = new Timer();
		timerTask = new TimerTask() {
            public void run()
            {
            	TimeBasedTriggerAtTime.tick(trigger, this.scheduledExecutionTime());
            }
        };
        
        if(!this.oeeStatus.isTriggeredOnce()){
	        Date now = new Date();
	        if(start.getStartNow() != null){
	        	start.setDateAndTimeItem(new Date());
	        	start.setStartNowObject(null);
	        	start.setStartNow(null);
	        }
			
	        
	        if(start.getDateAndTime() instanceof XMLGregorianCalendar){
	        	XMLGregorianCalendar xmlGregorianDate = start.getDateAndTime();
	        	
	        	if(checkXmlGregorianDate(xmlGregorianDate)){
	        	
		        	Calendar calendar = Calendar.getInstance();
		        	calendar.set(	xmlGregorianDate.getYear(), 
		        					xmlGregorianDate.getMonth()-1, // XMLGregorianCalendar month: 1-12, Calendar month: 0-11
		        					xmlGregorianDate.getDay(), 
		        					xmlGregorianDate.getHour(), 
		        					xmlGregorianDate.getMinute(), 
		        					xmlGregorianDate.getSecond());
		        	
		        	Date startDate = calendar.getTime();
		        	startMillisec = startDate.getTime();
	        	}
	        }
	
	        if((startMillisec - now.getTime()) + maxDelay.getInSecond() * 1000 >= 0){
	        	timer.schedule(timerTask, (startMillisec - now.getTime()) + maxDelay.getInSecond() * 1000);
	        	LOGGER.info("Timer started");        	
	        }else{
	        	if(!this.oeeStatus.isTriggeredOnce()){
	        		timer.schedule(timerTask, 1);        		
	        	}
	        }
        }
	}
	
	private synchronized static void tick(TimeBasedTriggerAtTime trigger, long time){
		LOGGER.info("#" + trigger.pii.getHjid() + " TriggerAtTime triggered at " + time);
		trigger.oeeStatus.setTriggeredOnce(true);
		trigger.eventHandler.fireAtTime(trigger.pii);
		trigger.cancel();
	}
	
	private boolean checkXmlGregorianDate(XMLGregorianCalendar xmlGregorianDate){
		return (xmlGregorianDate.getYear() != DatatypeConstants.FIELD_UNDEFINED && 
				xmlGregorianDate.getMonth() != DatatypeConstants.FIELD_UNDEFINED && 
				xmlGregorianDate.getDay() != DatatypeConstants.FIELD_UNDEFINED &&  
				xmlGregorianDate.getHour() != DatatypeConstants.FIELD_UNDEFINED && 
				xmlGregorianDate.getMinute() != DatatypeConstants.FIELD_UNDEFINED && 
				xmlGregorianDate.getSecond() != DatatypeConstants.FIELD_UNDEFINED);
	}
}
