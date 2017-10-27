package com.sap.document;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface DocumentService {
	ResponseEntity<InputStreamResource> formGeneration(String tenantId, String patientId) throws Exception;
}
