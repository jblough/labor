package com.josephblough.laborstatistics.datasets.faq;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class FaqSubtopicDataset implements DeptOfLaborDataset {

    public int subtopicId;
    public String subtopicValue;
    public int topicId;
    
    public String getMethod() {
	return "FAQ/SubTopics";
    }

    public String getFields() {
	return "SubTopicID,SubTopicValue,TopicID";
    }

    public String toString(Map<String, String> results) {
	return results.get("SubTopicID") + ", " + results.get("SubTopicValue")+ ", " + results.get("TopicID");
    }

}
