package com.josephblough.laborstatistics.datasets.faq;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class FaqTopicDataset implements DeptOfLaborDataset {

    public int topicId;
    public String topicValue;
    
    public String getMethod() {
	return "FAQ/Topics";
    }

    public String getFields() {
	return "TopicID,TopicValue";
    }

    public String toString(Map<String, String> results) {
	return results.get("TopicID") + ", " + results.get("TopicValue");
    }

}
