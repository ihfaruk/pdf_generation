package com.sap.document.questionnaireres.bean;

import java.util.List;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionnaireResponse {
	private String resourceType;
	private String id;
	private List<QuestionnairesSection> item;
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public List<QuestionnairesSection> getItem() {
		return item;
	}
	public void setItem(List<QuestionnairesSection> item) {
		this.item = item;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
