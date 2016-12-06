package com.ankur.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * The Class RectangleInCell.
 */
public class RectangleInPdf {

	/** The Constant DEST. */
	public static final String DEST = "C:/Users/ANKUR/Documents/Rectangle.pdf";

	/** The Constant DATE_PATTERN. */
	public static final String DATE_PATTERN = "dd/MM/yyyy";

	Font bold = new Font(Font.FontFamily.UNDEFINED, 8, Font.BOLD);

	Font italic = new Font(Font.FontFamily.UNDEFINED, 8, Font.ITALIC);
	
	final int FONT_SIZE = 8;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DocumentException
	 *             the document exception
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new RectangleInPdf().createPdf(DEST);
	}

	/**
	 * Creates the pdf.
	 *
	 * @param dest
	 *            the dest
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DocumentException
	 *             the document exception
	 */
	public void createPdf(String dest) throws IOException, DocumentException {

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));

		BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		document.open();

		float llx = 30;
		float lly = 770;
		float urx = 170;
		float ury = 810;

		PdfContentByte canvas = writer.getDirectContent();

		Rectangle rect1 = new Rectangle(llx, lly, urx, ury);
		rect1.setBorder(Rectangle.BOX);
		rect1.setBorderWidth(0.75f);
		canvas.rectangle(rect1);
		canvas.setFontAndSize(bf, FONT_SIZE);
		canvas.beginText();
		canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "DIRECTION RECHERCHE ", llx + 10, 800, 0);
		canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "ET DEVELOPPEMENT", llx + 10, 788, 0);
		canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "DRD/DCTC/VCT/MESL/MEMU", llx + 10, 776, 0);
		canvas.endText();

		llx = llx + (140) + 5; // 175
		lly = 770; // 770
		urx = urx + 5 + 80; // 255
		ury = 810; // 810

		Rectangle rect2 = new Rectangle(llx, lly, urx, ury);
		rect2.setBorder(Rectangle.BOX);
		rect2.setBorderColor(BaseColor.BLACK);
		rect2.setBorderWidth(0.75f);
		canvas.setFontAndSize(bf, 10);
		canvas.rectangle(rect2);

		canvas.beginText();
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Courbes", llx + 10 + 30, 800, 0);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "des", llx + 10 + 30, 788, 0);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Polluants", llx + 10 + 30, 776, 0);
		canvas.endText();

		llx = llx + 10 + 65 + 10;
		lly = 770;
		urx = urx + 85 + 5 + 10;
		ury = 810;

		Rectangle rect3 = new Rectangle(llx, lly, urx, ury);
		rect3.setBorder(Rectangle.BOX);
		rect3.setBorderColor(BaseColor.BLACK);
		rect3.setBorderWidth(0.75f);
		canvas.rectangle(rect3);
		canvas.setFontAndSize(bf, FONT_SIZE);
		canvas.beginText();
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Periode de reference", llx + 50, 800, 0);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Debut: " + formatter.format(new Date()), llx + 50, 788, 0);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Fin:  " + formatter.format(new Date()), llx + 50, 776, 0);
		canvas.endText();

		llx = llx + 100;
		lly = 770;
		urx = urx + 155;
		ury = 810;

		Rectangle rect4 = new Rectangle(llx, lly, urx, ury);
		rect4.setBorder(Rectangle.BOX);
		rect4.setBorderColor(BaseColor.BLACK);
		rect4.setBorderWidth(0.75f);
		canvas.rectangle(rect4);
		canvas.beginText();
		canvas.setFontAndSize(bf, FONT_SIZE + 2);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "PSA PEUGEOT CITROEN", llx + 80, 800, 0);
		canvas.setFontAndSize(bf, FONT_SIZE);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "Site de Mulhouse", llx + 80, 788, 0);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER,
				"Date : " + formatter.format(new Date()) + "  Heure:  23:33", llx + 80, 776, 0);
		canvas.endText();

		PdfPTable secondRow = new PdfPTable(3);
		secondRow.setTotalWidth(480);

		LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();
		map1.put("TVV", "CUHMZ0");
		map1.put("Famille", "2008");
		map1.put("Moteur", "EB2/E5");
		map1.put("BV", "MA");
		map1.put("Particularites", "");

		LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
		map2.put("Calculateur", "VALEO");
		map2.put("Inertie", "1130");
		map2.put("Loi de route", "PSA258");

		LinkedHashMap<String, String> map3 = new LinkedHashMap<String, String>();
		map3.put("Famille stat", "EUROPE");
		map3.put("Type Limite", "Ess-EURO5-b");
		map3.put("Genre", "SERIE ou TDS");
		map3.put("UP", "MU");

		secondRow.addCell(getFirstCell(map1));
		secondRow.addCell(getFirstCell(map2));
		secondRow.addCell(getFirstCell(map3));

		secondRow.completeRow();
		
		secondRow.writeSelectedRows(0, -1, 30, 765, canvas);

		document.close();
	}

	public PdfPCell getFirstCell(Map<String, String> objectMap) throws DocumentException {
		
		PdfPTable inner = new PdfPTable(2);
		inner.setWidths(new int[] { 2, 4 });
		inner.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		inner.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		for (Map.Entry<String, String> map : objectMap.entrySet()) {
			PdfPCell temp = new PdfPCell(new Phrase(map.getKey() + ":", italic));
			temp.setBorder(0);
			inner.addCell(temp);

			temp = new PdfPCell(new Phrase(map.getValue(), italic));
			temp.setBorder(0);
			inner.addCell(temp);
		}
		
		PdfPCell cell = new PdfPCell(inner);
		cell.setBorderWidth(0.75f);
		cell.setFixedHeight(65f);
		
		return cell;
	}
}
