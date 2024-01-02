package com.vastpro.onlineexam.events;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import com.vastpro.onlineexam.constants.ConstantValue;


public class CreateorUpdateExamTopicMappingEvent {

	public static final String module = CreateorUpdateExamTopicMappingEvent.class.getName();
	
	
	/*
	 * 
	 * This event is used to either create or update a topics  on the topic  master entity and
	 * it should create or update a record of the exam master mapping entity.
	 * 
	 * */
	
	
	public static String createOrUpdateExamTopicEvent(HttpServletRequest request,HttpServletResponse response) {
		
		 Delegator delegator = (Delegator) request.getAttribute(ConstantValue.DELEGATOR);
	        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute(ConstantValue.DISPATCHER);
			String examId = (String) request.getAttribute(ConstantValue.EXAM_ID);
			String topicId = (String) request.getAttribute(ConstantValue.TOPIC_ID);
			String topicName=(String)request.getAttribute(ConstantValue.TOPIC_NAME);
			String percentage = (String) request.getAttribute(ConstantValue.PERCENTAGE);
			String topicPassPercentage = (String) request.getAttribute(ConstantValue.TOPIC_PASS_PERCENTAGE);
//			String questionsPerExam = (String) request.getAttribute(ConstantValue.QUESTION_PER_EXAM);
	       
			try {
				
				GenericValue oneGenericValue=EntityQuery.use(delegator).from("ExamMaster").where("examId",examId).cache().queryOne();
				String noOfquestions=(String)oneGenericValue.get("noOfQuestions");
				long noOfQuestionPerExam=Long.parseLong(noOfquestions);
				Long topicPercentage=Long.parseLong(percentage);
        		long questionsPerTopic=topicPercentage/100*noOfQuestionPerExam;
        		System.out.println("noofQuestions" + questionsPerTopic);
        		
        		long totalPercentage=100;
        		long initialPercentage=0;
        		initialPercentage=initialPercentage+topicPercentage;
        		if(initialPercentage == totalPercentage) {
        			request.setAttribute("No more topics to be added", topicPercentage);
        		}

        			
    
        		
        		GenericValue genericsvalue = EntityQuery.use(delegator).from("TopicMaster").where("topicId", topicId).cache()
						.queryOne();
        		if(genericsvalue!=null) {
	        		 Debug.logInfo("=====update service called...... =========", module);
	        		Map<String, Object> registerMap = new HashMap<String, Object>();
        			registerMap.put("topicId", topicId);
	        		registerMap.put("topicName", topicName);
	        		request.setAttribute("topicMasterValue", registerMap);
	 	            dispatcher.runSync("updateTopicMaster", UtilMisc.toMap("topicId", topicId,"topicName",topicName));
        		}
        		else {
	        		 Debug.logInfo("=====create service called of topic master.....! =========", module);
	 	            dispatcher.runSync("createTopicMaster", UtilMisc.toMap("topicId", topicId,"topicName",topicName));

        		}
        		
        		
        		
        		
        		
        	}
        	catch(GenericServiceException | GenericEntityException  e) {
        		
        		 String errMsg = "Unable to create or update records in examTopicMappingmasterentity: " + e.toString();
 	            request.setAttribute("_ERROR_MESSAGE_", errMsg);
 	            return "error";
        	}
        	
			try {
	        	List<GenericValue> genericvalue = EntityQuery.use(delegator).from("ExamTopicMappingMaster").where("examId", examId,"topicId",topicId).cache()
						.queryList();
	        	
	        	if(genericvalue!=null) {
	        		String myexamId=delegator.getNextSeqId(examId);
	        		Map<String, Object> registerMap = new HashMap<String, Object>();
	        		registerMap.put("examId", examId);
	        		registerMap.put("topicId", topicId);
	        		registerMap.put("percentage", percentage);
	        		registerMap.put("topicPassPercentage", topicPassPercentage);
	        		//registerMap.put("questionsPerExam", questionsPerExam);

	        		Map<String, Object> examTopicResultMap = null;
	        		 Debug.logInfo("=====create service called...... =========", module);
	 	            dispatcher.runSync("createExamTopicMappingMaster", UtilMisc.toMap("examId",examId,
	 	            		"topicId",topicId,"percentage", percentage,
	 	            		"topicPassPercentage", topicPassPercentage));
	        	}
	        	else {
	        		 Debug.logInfo("=====create service called.....of exam topic mapping master! =========", module);
	        		 dispatcher.runSync("createExamTopicMappingMaster", UtilMisc.toMap("examId",examId,
		 	            		"topicId",topicId,"percentage", percentage,
		 	            		"topicPassPercentage", topicPassPercentage));
	        	}
	        	
	        	
	        } catch (GenericServiceException | GenericEntityException e) {
	            String errMsg = "Unable to create or update records in examTopicMappingmasterentity: " + e.toString();
	            request.setAttribute("_ERROR_MESSAGE_", errMsg);
	            return "error";
	        }
	        request.setAttribute("_EVENT_MESSAGE_", "ExamTopicMappingMaster created or updated succesfully.");
	        return "success";
		}
	
}
