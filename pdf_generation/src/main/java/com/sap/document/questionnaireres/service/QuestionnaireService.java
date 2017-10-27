package com.sap.document.questionnaireres.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;

import com.sap.document.questionnaireres.bean.QuestionnaireResponse;

public class QuestionnaireService {
	private String configFile = "C:\\Users\\i306486\\Desktop\\Patient Management\\questionnaires.txt";

	public QuestionnaireResponse getQuestionnairesByPatientId(String id) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] fileContent = Files.readAllBytes(Paths.get(configFile));

		QuestionnaireResponse questionnaireResponse = objectMapper.readValue(fileContent, QuestionnaireResponse.class);
		return questionnaireResponse;
	}

}
