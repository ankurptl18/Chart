package com.ankur.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MyFirstTable {

	/** The resulting PDF file. */
	public static final String RESULT = "D:/Passion/Code Samples/LOT-2.2/Rectangle.pdf";

	static Font bold_10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

	static Font bold_9 = new Font(Font.FontFamily.TIMES_ROMAN, 8.5f, Font.BOLD);

	static Font normal_10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

	/**
	 * Main method. s
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		new MyFirstTable().createPdf(RESULT);
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
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws BadElementException
	 */
	public static PdfPTable createFirstTable() {

		PdfPTable table = new PdfPTable(6);
		table.setSplitLate(false);
		table.setWidthPercentage(100);

		// FIRST ROW -- STARTS
		table.addCell(Util.getFirstRow());
		// FIRST ROW -- ENDS

		// SECOND ROW -- STARTS
		PdfPCell temp = new PdfPCell(Util.getSecondRow());
		temp.setColspan(6);
		table.addCell(temp);
		// SECOND ROW -- ENDS

		// THIRD ROW -- STARTS
		PdfPCell temp2 = new PdfPCell(Util.getThirdRow());
		temp2.setColspan(6);
		table.addCell(temp2);
		// THIRD ROW -- ENDS

		// FOURTH ROW -- STARTS
		PdfPCell temp3 = new PdfPCell(Util.getFourthRow1());
		table.addCell(temp3);

		PdfPCell temp4 = new PdfPCell(new Phrase("Project Code Label and Vehicle Family Label", normal_10));
		temp4.setColspan(4);
		Util.setAlignment(temp4);
		table.addCell(temp4);
		// FOURTH ROW -- ENDS

		// FIFTH ROW -- STARTS
		Font normal_blue = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLUE);
		PdfPCell temp5 = new PdfPCell(new Phrase("N° VIN ", normal_blue));
		Util.setAlignment(temp5);
		table.addCell(temp5);

		normal_blue = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLUE);
		PdfPCell temp6 = new PdfPCell(new Phrase("VF _ _ _ _ _ _ _ _", normal_blue));
		temp6.setColspan(2);
		table.addCell(temp6);

		Font bold_orange = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.ORANGE);
		PdfPCell temp7 = new PdfPCell(new Phrase("TVV ", bold_orange));
		Util.setAlignment(temp7);
		table.addCell(temp7);

		bold_orange = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.ORANGE);
		PdfPCell temp8 = new PdfPCell(new Phrase("VF _ _ _ _ _ _ _ _", bold_orange));
		temp8.setColspan(2);
		table.addCell(temp8);
		// FIFTH ROW -- ENDS

		// SIXTH ROW -- STARTS
		PdfPCell temp9 = new PdfPCell(new Phrase("RELEVE TVV SUR PLAQUE CHASSIS VEHICULE AVEC VARIANTE", bold_10));
		temp9.setColspan(6);
		table.addCell(temp9);
		// SIXTH ROW -- END

		// SEVENTH ROW -- STARTS
		Util.getSeventhRow(table);
		// SEVENTH ROW -- ENDS

		// EIGHT ROW -- STARTS
		table.addCell(new Phrase("UP D'ORIGINE ", bold_10));
		table.addCell(new Phrase("Corvet ", normal_10));
		table.addCell(new Phrase("AM ", bold_10));
		table.addCell(new Phrase("2006", normal_10));
		table.addCell(new Phrase("Pays dest ", bold_10));
		table.addCell(new Phrase("ES ", normal_10));
		// EIGHT ROW -- ENDS

		// NINTH ROW -- STARTS
		for (PreparationList pl : Util.getList()) {
			PdfPCell prepListCell = new PdfPCell(getPreparationCells(pl));
			prepListCell.setColspan(6);
			table.addCell(prepListCell);
		}
		// NINTH ROW -- ENDS

		// TENTH ROW -- STARTS
		// BLANK ROW -- COMPLETE BLACK ROW
		PdfPCell temp10 = new PdfPCell(new Phrase(" "));
		temp10.setBorderWidth(.75f);
		temp10.setBackgroundColor(BaseColor.BLACK);
		temp10.setColspan(6);
		table.addCell(temp10);
		// TENTH ROW -- ENDS

		// ELEVENTH ROW -- STARTS
		PdfPCell temp11 = new PdfPCell(Util.getObservationRow());
		temp11.setBorderWidth(.75f);
		temp11.setColspan(6);
		table.addCell(temp11);
		// ELEVENTH ROW -- ENDS

		// TWELTH ROW -- STARTS
		PdfPCell temp12 = new PdfPCell(new Phrase(" "));
		temp12.setFixedHeight(10);
		temp12.setColspan(6);
		temp12.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
		table.addCell(temp12);
		// TWELTH ROW -- ENDS

		// Last ROW
		getLastRow(table);
		// LAST ROW

		return table;
	}

	private static void getLastRow(PdfPTable table) {

		PdfPTable lastRowTable = new PdfPTable(7);

		try {
			lastRowTable.setWidths(new float[] { 4f, .5f, .5f, 2f, .5f, .5f, 2f });
		} catch (DocumentException e) {
		}

		PdfPCell temp13 = new PdfPCell(new Phrase("DECISION FIN DE PREPARATION", bold_10));
		temp13.setBackgroundColor(new BaseColor(255, 255, 151));
		Util.setAlignment(temp13);
		lastRowTable.addCell(temp13);

		Image arrow = null;
		Image check = null;
		try {
			arrow = Image.getInstance("src/Arrow.png");
			
			arrow.scaleAbsolute(50f, 50f);
			arrow.setAlignment(Image.MIDDLE);

			check = Image.getInstance("src/Check.png");
			check.scaleAbsolute(50f, 50f);
			check.setAlignment(Image.MIDDLE);

		} catch (BadElementException e) {
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		

		lastRowTable.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM);
		Util.setAlignment(lastRowTable.getDefaultCell());
		//lastRowTable.addCell(arrow);
		lastRowTable.addCell("");

		lastRowTable.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM | PdfPCell.TOP);
		Util.setAlignment(lastRowTable.getDefaultCell());
		
		if(Boolean.TRUE) {
			lastRowTable.addCell(check); }
			else {
				lastRowTable.addCell("");
			}

		Util.setAlignment(lastRowTable.getDefaultCell());
		lastRowTable.getDefaultCell().setBackgroundColor(new BaseColor(204, 255, 204));
		lastRowTable.addCell(new Phrase("OK POUR TEST", bold_10));

		lastRowTable.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM);
		Util.setAlignment(lastRowTable.getDefaultCell());
		lastRowTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		//lastRowTable.addCell(arrow);
		lastRowTable.addCell("");

		lastRowTable.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM | PdfPCell.TOP);
		Util.setAlignment(lastRowTable.getDefaultCell());
		lastRowTable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		
		if(Boolean.FALSE) {
		lastRowTable.addCell(check); }
		else {
			lastRowTable.addCell("");
		}

		Util.setAlignment(lastRowTable.getDefaultCell());
		lastRowTable.getDefaultCell().setBackgroundColor(BaseColor.RED);
		lastRowTable.addCell(new Phrase("EXPERTISE ", bold_10));

		table.getDefaultCell().setColspan(6);
		table.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM);
		table.addCell(lastRowTable);
	}

	private static PdfPTable getPreparationCells(PreparationList pl2) {

		PdfPTable listTable = new PdfPTable(2);
		listTable.setSplitLate(false);
		int count = 1;

		try {
			listTable.setWidths(new float[] { 0.80f, 4f });
		} catch (DocumentException e) {
		}

		Font normal_White = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
		PdfPCell firstCol = new PdfPCell(new Phrase("Partie " + count++, normal_White));
		firstCol.setBorderWidth(.75f);
		Util.setAlignment(firstCol);
		BaseColor myColor = new BaseColor(0, 0, 0);
		firstCol.setBackgroundColor(myColor);

		listTable.addCell(firstCol);

		PdfPCell secondCol = new PdfPCell(new Phrase(pl2.getListName(), bold_10));
		myColor = new BaseColor(204, 255, 204);
		secondCol.setBackgroundColor(myColor);
		secondCol.setBorderWidth(.75f);
		Util.setAlignment(secondCol);
		listTable.addCell(secondCol);

		listTable.getDefaultCell().setColspan(2);
		listTable.addCell(getInnerTable(pl2.getPreparationResultList()));

		return listTable;
	}

	private static PdfPTable getInnerTable(List<PreparationResult> list) {

		PdfPTable innerTable = new PdfPTable(2);
		try {
			innerTable.setWidths(new float[] { 1f, 2f });
		} catch (DocumentException e) {
		}

		innerTable.getDefaultCell().setBorder(0);

		Util.setAlignment(innerTable.getDefaultCell());

		PdfPCell innerCell = null;

		int count = 1;

		for (PreparationResult pr : list) {

			innerTable.addCell(new Phrase("Label ", normal_10));
			innerCell = new PdfPCell(new Phrase(pr.getLabel(), normal_10));
			Util.setAlignment(innerCell);
			innerTable.addCell(innerCell);

			innerTable.addCell("");
			innerTable.addCell("");

			innerTable.addCell(new Phrase("Value ", normal_10));
			innerCell = new PdfPCell(new Phrase(pr.getValue(), normal_10));
			Util.setAlignment(innerCell);
			innerTable.addCell(innerCell);

			innerTable.addCell("");
			innerTable.addCell("");

			innerTable.addCell(new Phrase("Unit ", normal_10));
			innerCell = new PdfPCell(new Phrase(pr.getUnit(), normal_10));
			Util.setAlignment(innerCell);
			innerTable.addCell(innerCell);

			if (count++ != list.size()) {
				innerTable.addCell(" ");
				innerTable.addCell(" ");
			}
		}

		return innerTable;
	}

}
