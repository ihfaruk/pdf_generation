package com.sap.document;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adobe.fdf.exceptions.FDFBadParameterException;
import com.adobe.fdf.exceptions.FDFCantInsertFieldException;
import com.adobe.fdf.exceptions.FDFEmbeddedFDFsException;
import com.adobe.fdf.exceptions.FDFFileSysErrException;
import com.adobe.fdf.exceptions.FDFIncompatibleFDFException;

@RestController
public class ConsumerResource {

	@RequestMapping("/getPDFForm/{tenantId}/{patientId}")
	public ResponseEntity<InputStreamResource> getAttr(HttpServletRequest request, @PathVariable String tenantId, @PathVariable String patientId) throws Exception {
		DocumentService documentService = new DocumentServiceImpl();
		try {
			return documentService.formGeneration(tenantId, patientId);
		} catch (FDFBadParameterException | FDFIncompatibleFDFException | FDFEmbeddedFDFsException
				| FDFCantInsertFieldException | FDFFileSysErrException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/getPDFTemplate/{tenantId}/{templateId}")
	public ResponseEntity<InputStreamResource> getPDF(@PathVariable String tenantId, @PathVariable String patientId) {
		DocumentServiceUnused documentService = new DocumentServiceUnused();
		try {
			return documentService.getPDF(patientId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/uploadFormTemplate/{tenantId}/{formCode}", method=RequestMethod.POST)
	public ResponseEntity<InputStreamResource> postFormTemplateAndMapping(@PathVariable String tenantId, @PathVariable String formCode, @RequestParam("file") MultipartFile file) {

		//storageService.store(file);
		DocumentServiceUnused documentService = new DocumentServiceUnused();
		try {
			return documentService.getPDF(formCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}