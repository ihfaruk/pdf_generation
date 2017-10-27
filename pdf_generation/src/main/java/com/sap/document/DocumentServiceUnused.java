package com.sap.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.adobe.fdf.FDFDoc;
import com.adobe.fdf.exceptions.FDFBadParameterException;
import com.adobe.fdf.exceptions.FDFCantInsertFieldException;
import com.adobe.fdf.exceptions.FDFEmbeddedFDFsException;
import com.adobe.fdf.exceptions.FDFFileSysErrException;
import com.adobe.fdf.exceptions.FDFIncompatibleFDFException;

@Deprecated
public class DocumentServiceUnused {
	private String pdfTemplateName="Yourdetails.pdf";
	private String fdfTemplateName="Yourdetails.fdf";
	public ResponseEntity<InputStreamResource> formGeneration(String tenantId, String patientId, String requestUrl) throws FDFBadParameterException, FDFIncompatibleFDFException, FDFEmbeddedFDFsException, FDFCantInsertFieldException, FDFFileSysErrException, IOException {
		PatientService patientService = new PatientService();
		Map<String, Object> fields = patientService.getPatientDataById(tenantId, patientId);
		FDFDoc outFDF = new FDFDoc();
		ClassLoader classLoader = this.getClass().getClassLoader();
		File pdfFile = new File(classLoader.getResource(pdfTemplateName).getFile());
		URL requestUri = new URL(requestUrl);
		outFDF.SetFile(pdfTemplateName);

		Map<String, String> pdfFieldToDBFieldMap = getMappingTemplate();
		for(String field:pdfFieldToDBFieldMap.keySet()) {
			outFDF.SetValue(field, fields.get(pdfFieldToDBFieldMap.get(field)).toString());
		}

		/*Current Date */
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yy");
		Date date = new Date();
		String dateString=sdf.format(date);
		outFDF.SetValue("CurrentDate", dateString);

		File fdfFile = new File(classLoader.getResource(fdfTemplateName).getFile());
		FileOutputStream out = new FileOutputStream(fdfFile);
		outFDF.Save(out);
		out.close();

		//com.adobe.fdf.PDFDoc p = new com.adobe.fdf.PDFDoc();
		return getPDF(patientId);
	}
	private ResponseEntity<InputStreamResource> returnDocument(String patientId)
			throws FileNotFoundException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		File fileToDownload = new File(classLoader.getResource(fdfTemplateName).getFile());
		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		respHeaders.setContentLength(fileToDownload.length());
		respHeaders.setContentDispositionFormData("attachment", patientId+".fdf");
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

	public ResponseEntity<InputStreamResource> getPDF(String patientId) throws FileNotFoundException {
		List<File> fileList = new ArrayList<File>();
		ClassLoader classLoader = this.getClass().getClassLoader();
		File pdfFileToDownload = new File(classLoader.getResource(pdfTemplateName).getFile());
		fileList.add(pdfFileToDownload);
		File fdfFileToDownload = new File(classLoader.getResource(fdfTemplateName).getFile());
		fileList.add(fdfFileToDownload);
		File zipFile = getMappedData(fileList, patientId);
		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		respHeaders.setContentLength(zipFile.length());
		respHeaders.setContentDispositionFormData("attachment", patientId+".zip");
		InputStreamResource isr = new InputStreamResource(new FileInputStream(zipFile));
		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}

	public File getMappedData(List<File> files, String filename) {
		ClassLoader classLoader = this.getClass().getClassLoader();
		File zipfile = new File(classLoader.getResource("").getPath()+filename+".zip");
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		try {
			// create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
			// compress the files
			for(int i=0; i<files.size(); i++) {
				FileInputStream in = new FileInputStream(files.get(i).getAbsolutePath());
				// add ZIP entry to output stream
				out.putNextEntry(new ZipEntry(files.get(i).getName()));
				// transfer bytes from the file to the ZIP file
				int len;
				while((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// complete the entry
				out.closeEntry();
				in.close();
			}
			// complete the ZIP file
			out.close();
			return zipfile;
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return null;
	}
}

