package com.ankur.pdf;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class pdfPage extends PdfPageEventHelper {

	/** The resulting PDF file. */
	public static final String RESULT = "D:/Passion/Office Work/pdfChart/HelloWorld.pdf";
	String test;
	PdfTemplate total;

	/**
	 * Main method.
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void generatePdf() {

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));

			Rectangle rect = new Rectangle(36, 54, 559, 788);
			writer.setBoxSize("art", rect);
			pdfPage event = new pdfPage();
			writer.setPageEvent(event);

			document.open();
			document.add(new Paragraph("A Hello World PDF document."));

			document.newPage();
			document.add(new Paragraph("Second Page"));

			document.close();
			writer.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(30, 16);
		total.setRGBColorFill(255, 0, 0);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		PdfPTable table = new PdfPTable(2);
		try {
			table.setWidths(new int[] { 24, 2 });
			table.setTotalWidth(527);
			table.setLockedWidth(true);
			table.getDefaultCell().setFixedHeight(20);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(String.format("Page %d / ", writer.getPageNumber()));

			PdfPCell cell = new PdfPCell(Image.getInstance(total));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			table.writeSelectedRows(0, -1, 34, 50, writer.getDirectContent());

		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	@Override
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(total, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
	}
}