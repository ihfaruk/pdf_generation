package com.sap.document.questionnaireres.bean;

import java.util.List;

public class QuestionnairesSection {
	private String linkId;
	private String text;
	private List<Questionnaire>item;
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
	public List<Questionnaire> getItem() {
		return item;
	}
	public void setItem(List<Questionnaire> item) {
		this.item = item;
	}
}
