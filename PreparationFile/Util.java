package com.ankur.pdf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class Util {

	static Font bold_10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

	static Font bold_9 = new Font(Font.FontFamily.TIMES_ROMAN, 8.5f, Font.BOLD);

	static Font normal_10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

	/** The Constant DATE_PATTERN. */
	public static final String DATE_PATTERN = "dd/MM/yyyy";

	public static PdfPCell getFourthRow1() {

		PdfPCell fourthRow = new PdfPCell(new Phrase("Type de véhicule ", bold_10));
		setAlignment(fourthRow);
		fourthRow.setColspan(2);
		return fourthRow;
	}

	public static PdfPTable getThirdRow() {

		PdfPTable thirdRow = new PdfPTable(5);
		thirdRow.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		thirdRow.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		try {
			thirdRow.setWidths(new float[] { 2.5f, 3f, 1.5f, 2f, 3f });
		} catch (DocumentException e) {
		}

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

		thirdRow.addCell(new Phrase("PREPARATEUR ", bold_10));

		thirdRow.addCell(new Phrase("AnkurP Ankur Patel", normal_10));
		thirdRow.addCell(new Phrase("Signature ", bold_10));
		thirdRow.getDefaultCell().setBorderWidthLeft(0);
		thirdRow.addCell(new Phrase(" ", normal_10));

		PdfPTable dateTable = new PdfPTable(2);
		dateTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		dateTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		dateTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		dateTable.addCell(new Phrase("Prépa.", bold_10));
		dateTable.addCell(new Phrase(formatter.format(new Date()), normal_10));
		dateTable.addCell(new Phrase("Impr.", bold_10));
		dateTable.addCell(new Phrase(formatter.format(new Date()), normal_10));

		thirdRow.addCell(dateTable);

		return thirdRow;
	}

	public static PdfPTable getSecondRow() {

		PdfPTable secondRow = new PdfPTable(6);
		// secondRow.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		Font normal_White = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);
		PdfPCell firstCol = new PdfPCell(new Phrase("Partie 0", normal_White));
		setAlignment(firstCol);
		BaseColor myColor = new BaseColor(0, 0, 0);
		firstCol.setBackgroundColor(myColor);
		secondRow.addCell(firstCol);

		normal_White = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
		PdfPCell secondCol = new PdfPCell(new Phrase("IDENTIFICATION DU VEHICULE", normal_White));
		setAlignment(secondCol);
		secondCol.setColspan(3);
		myColor = new BaseColor(204, 255, 204);
		secondCol.setBackgroundColor(myColor);
		secondRow.addCell(secondCol);

		PdfPCell thirdCol = new PdfPCell(new Phrase("YZT12345", bold_10));
		setAlignment(thirdCol);
		secondRow.addCell(thirdCol);

		PdfPCell fourthCol = new PdfPCell(new Phrase("YZT12345"));
		setAlignment(fourthCol);
		secondRow.addCell(fourthCol);

		return secondRow;
	}

	public static PdfPCell getFirstRow() {

		PdfPCell firstRow = new PdfPCell(new Phrase("PREPARATION DES VEHICULES AU TEST D'EMISSIONS", bold_10));
		firstRow.setHorizontalAlignment(Element.ALIGN_CENTER);
		firstRow.setVerticalAlignment(Element.ALIGN_MIDDLE);

		BaseColor myColor = new BaseColor(255, 255, 151);
		firstRow.setBackgroundColor(myColor);

		firstRow.setColspan(6);
		return firstRow;
	}

	public static List<PreparationList> getList() {
		List<PreparationList> prepList = new ArrayList<PreparationList>();

		prepList.add(new PreparationList("A", 101, getResultList()));
		prepList.add(new PreparationList("B", 102, getResultList()));
		 prepList.add(new PreparationList("C", 103, getResultList()));
		 prepList.add(new PreparationList("D", 104, getResultList()));
		 prepList.add(new PreparationList("E", 105, getResultList()));
		 prepList.add(new PreparationList("F", 106, getResultList()));
		 prepList.add(new PreparationList("G", 107, getResultList()));

		return prepList;
	}

	public static List<PreparationResult> getResultList() {

		List<PreparationResult> result = new ArrayList<PreparationResult>();

		result.add(new PreparationResult("Label-1", "Value-1", "KM", 1));
		result.add(new PreparationResult("Label-2", "Value-2", "KG", 2));
		 result.add(new PreparationResult("Label-3","Value-3","MM",3));
		 result.add(new PreparationResult("Label-4","Value-4","CM",4));
		 result.add(new PreparationResult("Label-5","Value-5","PM",5));
		 result.add(new PreparationResult("Label-6","Value-6","LL",6));
		 result.add(new PreparationResult("Label-7","Value-7","OI",7));

		return result;

	}

	public static PdfPCell setAlignment(PdfPCell cell) {
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);

		return cell;
	}

	public static PdfPCell createEmptySpace() {
		PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
		emptyCell.setBorder(0);
		return emptyCell;
	}

	public static PdfPTable getObservationRow() {

		PdfPTable observation = new PdfPTable(2);
		observation.getDefaultCell().setBorderWidthRight(PdfPCell.NO_BORDER);
		
		try {
			observation.setWidths(new float[] { 0.8f, 4f });
		} catch (DocumentException e) {
		}

		observation.addCell(new Phrase("Observations : ", bold_10));
		
		observation.getDefaultCell().setBorderWidthLeft(PdfPCell.NO_BORDER);
		observation.addCell(new Phrase(" Observation 1, Observation 2", normal_10));

		return observation;
	}
	
	public static void getSeventhRow(PdfPTable table) {
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(new Phrase("TYPE MOTEUR ", bold_10));
		table.addCell(new Phrase("Moteur ", normal_10));
		table.addCell(new Phrase("INDICE ", bold_10));
		table.addCell(new Phrase("Valeur 'DKC' ", normal_10));
		table.addCell(new Phrase("Type de BV ", bold_10));
		table.addCell(new Phrase("Boîte de vitesses ", normal_10));
	}

}

/*
 * class HockeyPlayer { public final int goalsScored; // ... };
 * 
 * List<HockeyPlayer> players = // ...
 * 
 * Collections.sort(players, new Comparator<HockeyPlayer>() {
 * 
 * @Override public int compare(HockeyPlayer p1, HockeyPlayer p2) { return
 * p1.goalsScored - p2.goalsScored; // Ascending }
 * 
 * });
 */