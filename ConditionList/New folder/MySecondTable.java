package com.ankur.pdf2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MySecondTable {

	/** The resulting PDF file. */
	public static final String RESULT = "D:/Passion/Code Samples/LOT-2.2/Rectangle2.pdf";

	/**
	 * Main method. s
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		new MySecondTable().createPdf(RESULT);
	}

	/**
	 * Creates a PDF with information about the movies
	 * 
	 * @param filename
	 *            the name of the PDF file that will be created.
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createPdf(String filename) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		// step 3
		document.open();
		// step 4
		document.add(createFirstTable());
		// step 5
		document.close();
	}

	/**
	 * Creates our first table
	 * 
	 * @return our first table
	 * @throws DocumentException 
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws BadElementException
	 */
	public static PdfPTable createFirstTable() throws DocumentException {
		
		PdfPTable masterTable = new PdfPTable(1);
		masterTable.setSplitLate(false);
		masterTable.setWidthPercentage(100);
		masterTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		masterTable.addCell(PdfHelper.getHeaderRow());
		
		masterTable.addCell(PdfHelper.getEmptyRow());
		
		for(ConditionList cl : PdfHelper.getDataList()) {
			
			PdfPCell dataRowCell = new PdfPCell(PdfHelper.getTestConditionList(cl));
			dataRowCell.setColspan(5);
			dataRowCell.setBorderWidth(0.75f);
			masterTable.addCell(dataRowCell);
			
			masterTable.addCell(PdfHelper.getEmptyRow());
		}
		
		
		PdfPCell coastDownCell = new PdfPCell(PdfHelper.getCoastDownRow());
		coastDownCell.setColspan(5);
		coastDownCell.setBorderWidth(0.75f);
		masterTable.addCell(coastDownCell);
		
		masterTable.addCell(PdfHelper.getEmptyRow());
		
		PdfPCell pollutantLabel = new PdfPCell(PdfHelper.getPollutantTable());
		pollutantLabel.setColspan(5);
		pollutantLabel.setBorderWidth(0.75f);
		masterTable.addCell(pollutantLabel);
		
		
		return masterTable;
	}

	

	
}
