package com.sap.document.questionnaireres.bean;

import java.util.List;

public class Questionnaire {
	private String linkId;
	private String text;
	private List<Answer> answer;
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Answer> getAnswer() {
		return answer;
	}
	public void setAnswer(List<Answer> answer) {
		this.answer = answer;
	}

}
