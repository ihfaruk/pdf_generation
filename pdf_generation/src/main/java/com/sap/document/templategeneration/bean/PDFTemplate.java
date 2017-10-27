package com.sap.document.templategeneration.bean;

public class PDFTemplate {
	private String templateId;
	private String templateName;
	private FormTemplate form;
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public FormTemplate getForm() {
		return form;
	}
	public void setForm(FormTemplate form) {
		this.form = form;
	}

}
