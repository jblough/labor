package com.josephblough.laborstatistics.datasets.faq;

import java.util.Date;
import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class TopicQuestionsDataset implements DeptOfLaborDataset {

    public int faqId;
    public int topicId;
    public int subtopicId;
    public String question;
    public String answer;
    public String sourceURL;
    public int hits;
    public Date dateModified;
    public boolean faqSource;
    public String keywords;
    public Date lastReviewDate;
    
    public String getMethod() {
	return "FAQ/TopicQuestions";
    }

    public String getFields() {
	return "FAQID,TopicID,SubTopicID,Question,Answer,SourceURL,Hits,DateMod,FAQSource,Keywords,LastReviewDate";
    }

    public String toString(Map<String, String> results) {
	return results.get("FAQID") + ", " + results.get("TopicID") + ", " + results.get("SubTopicID")
		 + ", " + results.get("Question") + ", " + results.get("Answer") + ", " + results.get("SourceURL") + 
		 ", " + results.get("Hits") + ", " + results.get("DateMod") + ", " + results.get("FAQSource") + 
		 ", " + results.get("Keywords") + ", " + results.get("LastReviewDate");
    }

}
