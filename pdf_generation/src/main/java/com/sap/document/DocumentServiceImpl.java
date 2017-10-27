package com.sap.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class DocumentServiceImpl implements DocumentService {
	private String srcTemplateName="Yourdetails.pdf";
	private String generatedPdfName="YourdetailsGenerated.pdf";
	@Override
	public ResponseEntity<InputStreamResource> formGeneration(String tenantId, String patientId) throws Exception {
		Map<String, String> pdfFieldValueMap = getPDFFieldValueMap(tenantId, patientId);
		ITextPDFService docService = new ITextPDFService(srcTemplateName, generatedPdfName);
		docService.manipulatePdf(pdfFieldValueMap);
		return returnDocument(patientId);
	}
	private ResponseEntity<InputStreamResource> returnDocument(String patientId)
			throws FileNotFoundException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		File fileToDownload = new File(classLoader.getResource(generatedPdfName).getFile());
		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		respHeaders.setContentLength(fileToDownload.length());
		respHeaders.setContentDispositionFormData("attachment", patientId+".pdf");
		InputStreamResource isr = new InputStreamResource(new FileInputStream(fileToDownload));
		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}

	public Map<String, String> getMappingTemplate() {
		/*PDF Field Id, Odata Response Field*/
		Map<String, String> pdfFieldToDBFieldMap = new HashMap<String, String>();
		pdfFieldToDBFieldMap.put("Name", "name");
		pdfFieldToDBFieldMap.put("DateOfBirth", "birthDate");
		pdfFieldToDBFieldMap.put("Relationship", "maritalStatus");
		pdfFieldToDBFieldMap.put("Gender", "gender");
		return pdfFieldToDBFieldMap;
	}


	private Map<String, String> getPDFFieldValueMap(String tenantId, String patientId) {
		PatientService patientService = new PatientService();
		Map<String, Object> fields = patientService.getPatientDataById(tenantId, patientId);
		Map<String, String> mappingTemplateMap=getMappingTemplate();
		Map<String, String> pdfFieldValueMap = new HashMap<String, String>();
		for(String pdfField:mappingTemplateMap.keySet()) {
			pdfFieldValueMap.put(pdfField, fields.get(mappingTemplateMap.get(pdfField)).toString());
		}

		/*Current Date */
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yy");
		Date date = new Date();
		String dateString=sdf.format(date);
		pdfFieldValueMap.put("CurrentDate", dateString);
		return pdfFieldValueMap;

	}
}

