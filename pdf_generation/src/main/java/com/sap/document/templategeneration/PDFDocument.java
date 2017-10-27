package com.sap.document.templategeneration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sap.document.questionnaireres.bean.Answer;
import com.sap.document.questionnaireres.bean.Questionnaire;
import com.sap.document.questionnaireres.bean.QuestionnaireResponse;
import com.sap.document.questionnaireres.bean.QuestionnairesSection;

public class PDFDocument {
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);
	private Document document;

	public PDFDocument(String fileName) throws FileNotFoundException, DocumentException {
		document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
		document.open();
	}

	public Document getDocumentObject() {
		return document;
	}
	public Document setMergin(float marginLeft, float marginRight, float marginTop, float marginBottom) {
		document.setMargins(marginLeft, marginRight, marginTop, marginBottom);
		return document;
	}

	public Document setFormTitleWithLogoAndAddress(String formTitle, String logoFile, String address) throws MalformedURLException, IOException, DocumentException {
		/*Header table */
		PdfPTable headerTable = new PdfPTable(2);
		headerTable.setWidthPercentage(100);

		PdfPCell formTitleCell = getFormTitleCell(formTitle);

		headerTable.addCell(formTitleCell);

		PdfPCell logoWithAddressCell = createLogoWithAddressCell(logoFile, address);

		headerTable.addCell(logoWithAddressCell);

		document.add(headerTable);
		return document;
	}

	private PdfPCell createLogoWithAddressCell(String logoFile, String address)
			throws BadElementException, MalformedURLException, IOException {
		Image img = getLogoImage(logoFile);


		PdfPCell logoWithAddressCell = new PdfPCell();
		logoWithAddressCell.addElement(img);

		Font addressFont =new Font();
		addressFont.setSize(8);
		Paragraph addressParagraph = new Paragraph(address, addressFont);
		addressParagraph.setFont(addressFont);
		addressParagraph.setAlignment(2);

		logoWithAddressCell.addElement(addressParagraph);
		logoWithAddressCell.setBorderColor(BaseColor.WHITE);
		return logoWithAddressCell;
	}

	private Image getLogoImage(String logoFile) throws BadElementException, MalformedURLException, IOException {
		/*logo image*/
		Image img = Image.getInstance(logoFile);
		img.scaleToFit(100,100);
		img.setAlignment(2);
		return img;
	}

	private PdfPCell getFormTitleCell(String formTitle) {
		/* form title cell*/
		PdfPCell formTitleCell = new PdfPCell();
		Paragraph paddingParagraph = new Paragraph(" ");
		formTitleCell.addElement(paddingParagraph);
		formTitleCell.setBorderColor(BaseColor.WHITE);
		Paragraph formTitleParagraph = new Paragraph(formTitle);
		formTitleParagraph.setAlignment(Element.ALIGN_CENTER);
		formTitleCell.addElement(formTitleParagraph);
		return formTitleCell;
	}

	public Document createSectionTable(ArrayList<String> fields, Document document, String sectionName) throws DocumentException {
		Font font1 =new Font();
		font1.setStyle(Font.UNDERLINE);
		font1.setSize(10);
		Paragraph pData = new Paragraph(sectionName, font1);
		document.add(pData);
		PdfPTable table2 = new PdfPTable(2);
		table2.setWidthPercentage(95);
		PdfPCell emptyCell2 = new PdfPCell();


		for (int row = 0; row < fields.size(); row++) {
			for (int column = 1; column <= 2; column++) {
				if(column%2 != 0) {
					PdfPCell label = new PdfPCell();
					Font labelFont = new Font();
					labelFont.setSize(8);
					Paragraph p = new Paragraph(fields.get(row), labelFont);
					label.addElement(p);
					table2.addCell(label);
				} else {
					table2.addCell(emptyCell2);
				}
			}
		}

		Paragraph pData1 = new Paragraph(" ");
		document.add(pData1);
		document.add(table2);
		return document;
	}

	public Document populateQuestionnaires(Document document, QuestionnaireResponse questionnaireResponse) throws DocumentException {
		PdfPTable tableQuestionnaires = new PdfPTable(1);
		tableQuestionnaires.setWidthPercentage(95);
		Font questionFont = new Font();
		questionFont.setSize(8);
		questionFont.setColor(BaseColor.BLUE);
		for(QuestionnairesSection questionnairesSection:questionnaireResponse.getItem()) {
			PdfPCell questionnairesCell = new PdfPCell();
			questionnairesCell.setBorderColor(BaseColor.WHITE);
			Paragraph pData = new Paragraph(questionnairesSection.getText() == null? " ":questionnairesSection.getText() , smallBold);
			questionnairesCell.addElement(pData);
			int i=1;
			for(Questionnaire questionnaire:questionnairesSection.getItem()) {
				Phrase phrase = new Phrase();
				phrase.add(new Chunk(i+". ",  new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
				phrase.add(new Chunk(questionnaire.getText(), new Font(Font.FontFamily.UNDEFINED,8, Font.BOLD)));
				phrase.add(new Chunk("Your response: ", new Font(Font.FontFamily.UNDEFINED,8, Font.NORMAL)));
				phrase.add(new Chunk(getAnswer(questionnaire), new Font(Font.FontFamily.UNDEFINED,8, Font.NORMAL, BaseColor.BLUE)));
				//Paragraph question = new Paragraph(i+". "+questionnaire.getText() , questionFont);
				//Paragraph answer = new Paragraph("Your response: "+getAnswer(questionnaire), questionFont);
				//questionnairesCell.addElement(question);
				questionnairesCell.addElement(phrase);
				i++;
			}
			tableQuestionnaires.addCell(questionnairesCell);
		}

		Paragraph pData2 = new Paragraph("Questionnaire");
		document.add(pData2);
		Paragraph pData1 = new Paragraph(" ");
		//document.add(pData1);

		document.add(tableQuestionnaires);
		return document;
	}

	private String getAnswer(Questionnaire questionnaire) {
		if(questionnaire.getAnswer().size()>0) {
			Answer answer = questionnaire.getAnswer().get(0);
			if(answer.getValueString() == null) {
				if(answer.getValueInt()==null) {
					if(answer.getValueDate() == null) {
						return " ";
					} else {
						return answer.getValueDate().toString();
					}
				} else {
					return answer.getValueInt().toString();
				}
			} else {
				return answer.getValueString().toString();
			}
		}
		return " ";
	}

}
