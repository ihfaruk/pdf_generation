package com.sap.document.questionnaireres.bean;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer {
	private String valueString;
	private String valueDate;
	private String valueInt;

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public String getValueInt() {
		return valueInt;
	}

	public void setValueInt(String valueInt) {
		this.valueInt = valueInt;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

}
