package com.sap.document;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ITextPDFService {
	private String src = null;
	private String dest = null;

	public ITextPDFService(String src, String dest) throws Exception {
		if(src != null && src.trim().length() > 0 && dest!= null && dest.trim().length() > 0) {
			File file = new File(getActualPath(dest));
			file.getParentFile().mkdirs();
			this.src = getActualPath(src);
			this.dest = getActualPath(dest);
		} else {
			throw new Exception("ITextPDFService instance can not be created.");
		}
	}
	public void manipulatePdf(Map<String, String> pdfFieldValueMap) throws Exception {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		AcroFields form = stamper.getAcroFields();
		for(String field: pdfFieldValueMap.keySet()) {
			form.setField(field, pdfFieldValueMap.get(field), pdfFieldValueMap.get(field));
		}
		stamper.close();
	}

	private String getActualPath(String fileName) {
		ClassLoader classLoader = this.getClass().getClassLoader();
		return classLoader.getResource("").getPath()+fileName;
	}
}
