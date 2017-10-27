package com.sap.document.templategeneration.bean;

import java.util.List;

public class FormTemplate {
	private String formTitle;
	private String logo;
	private String comapnyAddress;
	private List<Section> sections;
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getComapnyAddress() {
		return comapnyAddress;
	}
	public void setComapnyAddress(String comapnyAddress) {
		this.comapnyAddress = comapnyAddress;
	}
	public List<Section> getSections() {
		return sections;
	}
	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
}
