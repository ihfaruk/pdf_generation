package com.sap.document.templategeneration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonParseException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;

import com.itextpdf.text.Document;
import com.sap.document.questionnaireres.bean.QuestionnaireResponse;
import com.sap.document.questionnaireres.service.QuestionnaireService;
import com.sap.document.templategeneration.bean.Field;
import com.sap.document.templategeneration.bean.PDFTemplate;
import com.sap.document.templategeneration.bean.Section;

public class GenerateTemplate {
	private static String FILE = "C:\\Users\\i306486\\Documents\\SAPWorkspace\\FirstPdf.pdf";
	private static String configFile = "C:\\Users\\i306486\\Desktop\\Patient Management\\pdfConfiguration.txt";

	public static void main(String[] args) {
		try {
			PDFTemplate pdfTemplate = getTemplateConfiguration();
			PDFDocument pdfDocument = new PDFDocument(FILE);
			Document document = pdfDocument.setMergin(50, 50, 10, 10);
			document = pdfDocument.setFormTitleWithLogoAndAddress(pdfTemplate.getForm().getFormTitle(), pdfTemplate.getForm().getLogo(), pdfTemplate.getForm().getComapnyAddress());

			for(Section section : pdfTemplate.getForm().getSections()) {
				ArrayList<String> fields = new ArrayList();
				for(Field field: section.getFields()) {
					fields.add(field.getLabel());
				}
				pdfDocument.createSectionTable(fields, document, section.getName());
			}

			/*Questionnaires service*/

			QuestionnaireService questionnaireService = new QuestionnaireService();
			QuestionnaireResponse questionnaireResponse = questionnaireService.getQuestionnairesByPatientId("");
			pdfDocument.populateQuestionnaires(document, questionnaireResponse);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static PDFTemplate getTemplateConfiguration() throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] fileContent = Files.readAllBytes(Paths.get(configFile));

		PDFTemplate pdfTemplate = objectMapper.readValue(fileContent, PDFTemplate.class);
		return pdfTemplate;
	}
}