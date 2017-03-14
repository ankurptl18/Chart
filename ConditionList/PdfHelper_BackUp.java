package com.ankur.pdf2;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfHelper_BackUp {

	static Font bold_10B = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK);

	static Font bold_10W = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.WHITE);

	static Font normal_10B = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

	static Font normal_10W = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);

	public static PdfPTable getHeaderRow() {

		PdfPTable header = new PdfPTable(5);

		PdfPCell firstCol = new PdfPCell(new Phrase("FICHE CONDITION D'ESSAI V2.5", bold_10W));
		firstCol.setBorder(PdfPCell.NO_BORDER);
		firstCol.setColspan(4);
		firstCol.setBackgroundColor(new BaseColor(247, 150, 70));
		firstCol.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.TOP);
		firstCol.setBorderWidth(0.75f);
		header.addCell(firstCol);

		PdfPCell secondCol = new PdfPCell(new Phrase("EUROPE ", bold_10W));
		secondCol.setBorder(PdfPCell.NO_BORDER);
		secondCol.setBackgroundColor(new BaseColor(247, 150, 70));
		secondCol.setBorder(PdfPCell.RIGHT | PdfPCell.BOTTOM | PdfPCell.TOP);
		secondCol.setBorderWidth(0.75f);
		header.addCell(secondCol);

		PdfPTable dataRow = new PdfPTable(5);

		PdfPCell row = new PdfPCell(new Phrase("TVV", bold_10B));
		setRowAlignment(row);
		dataRow.addCell(row);

		row = new PdfPCell(new Phrase("MOTEUR", bold_10B));
		setRowAlignment(row);
		dataRow.addCell(row);

		row = new PdfPCell(new Phrase("PUISSANCE CV", bold_10B));
		setRowAlignment(row);
		dataRow.addCell(row);

		row = new PdfPCell(new Phrase("PUISSANCE KW", bold_10B));
		setRowAlignment(row);
		dataRow.addCell(row);

		row = new PdfPCell(new Phrase("COUPLE NM", bold_10B));
		setRowAlignment(row);
		dataRow.addCell(row);

		//
		PdfPCell row2 = new PdfPCell(new Phrase("CUHMZ0", normal_10B));
		setRowAlignment(row2);
		dataRow.addCell(row2);

		row2 = new PdfPCell(new Phrase("EB2/E5", normal_10B));
		setRowAlignment(row2);
		dataRow.addCell(row2);

		row2 = new PdfPCell(new Phrase("80", normal_10B));
		setRowAlignment(row2);
		dataRow.addCell(row2);

		row2 = new PdfPCell(new Phrase("60", normal_10B));
		setRowAlignment(row2);
		dataRow.addCell(row2);

		row2 = new PdfPCell(new Phrase("118", normal_10B));
		setRowAlignment(row2);
		dataRow.addCell(row2);

		dataRow.addCell(getEmptyRow());

		PdfPCell dataRowCell = new PdfPCell(dataRow);
		dataRowCell.setColspan(5);
		dataRowCell.setBorderWidth(0.75f);

		PdfPCell row3 = new PdfPCell(new Phrase("FAMILLE", bold_10B));
		setRowAlignment(row3);
		dataRow.addCell(row3);

		row3 = new PdfPCell(new Phrase("SILHOUETE", bold_10B));
		setRowAlignment(row3);
		dataRow.addCell(row3);

		row3 = new PdfPCell(new Phrase("TYPE D'INJECTION", bold_10B));
		setRowAlignment(row3);
		dataRow.addCell(row3);

		row3 = new PdfPCell(new Phrase("BOITE DE VITESSE", bold_10B));
		setRowAlignment(row3);
		dataRow.addCell(row3);

		dataRow.addCell(getEmptyCell());

		//
		PdfPCell row4 = new PdfPCell(new Phrase("2008", normal_10B));
		setRowAlignment(row4);
		dataRow.addCell(row4);

		row4 = new PdfPCell(new Phrase("CROSS-OVER", normal_10B));
		setRowAlignment(row4);
		dataRow.addCell(row4);

		row4 = new PdfPCell(new Phrase("injection IIE", normal_10B));
		setRowAlignment(row4);
		dataRow.addCell(row4);

		row4 = new PdfPCell(new Phrase("MA", normal_10B));
		setRowAlignment(row4);
		dataRow.addCell(row4);

		dataRow.addCell(getEmptyCell());

		dataRow.addCell(getEmptyRow());

		header.addCell(dataRowCell);
		//

		return header;
	}

	public static PdfPCell getEmptyCell() {
		PdfPCell row3 = new PdfPCell(new Phrase("EMTPY CELL", bold_10W));
		setRowAlignment(row3);
		return row3;
	}

	public static PdfPCell getEmptyRow() {
		PdfPCell emptyCell = new PdfPCell(new Phrase("EMPTY ROW", normal_10W));
		setRowAlignment(emptyCell);
		emptyCell.setBorder(PdfPCell.NO_BORDER);
		emptyCell.setColspan(5);
		return emptyCell;
	}

	private static void setRowAlignment(PdfPCell row) {
		row.setBorder(PdfPCell.NO_BORDER);
		row.setVerticalAlignment(Element.ALIGN_MIDDLE);
		row.setHorizontalAlignment(Element.ALIGN_CENTER);
	}

	public static PdfPTable getTestConditionList(ConditionList cl) {

		PdfPTable conditionListTable = new PdfPTable(5);
		conditionListTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		PdfPCell headerRow = new PdfPCell(new Phrase(cl.getListLabel(), bold_10W));
		headerRow.setColspan(5);
		headerRow.setBackgroundColor(new BaseColor(141, 180, 227));
		headerRow.setBorderWidth(0.75f);

		conditionListTable.addCell(headerRow);

		conditionListTable.addCell(getEmptyRow());

		int conditionCount = cl.getConditions().size();
		int extraToAdd = 0;
		
		if(conditionCount > 5) {
			extraToAdd = conditionCount % 5;
		}
		else {
			extraToAdd = 5 - conditionCount;
		}
		
		//int count = 0;
		for(int count = 0 ; count == 5 && count < cl.getConditions().size() ; count ++) {
		//for (Condition con : cl.getConditions()) {
			PdfPCell dataRow = new PdfPCell(new Phrase(cl.getConditions().get(count).getLabel(), bold_10B));
			dataRow.setBorder(PdfPCell.NO_BORDER);
			setRowAlignment(dataRow);
			conditionListTable.addCell(dataRow);

			if (count == 5) {
				count = 0;
				break;

			}

			count++;
		}
		
		if(extraToAdd != 0) {
			for(int i = 0 ; i < extraToAdd; i++) {
			conditionListTable.addCell(getEmptyCell());
			}
		}

		int count2 = 0;
		for (Condition con : cl.getConditions()) {
			PdfPCell dataRow = new PdfPCell(new Phrase(con.getValue(), normal_10B));
			dataRow.setBorder(PdfPCell.NO_BORDER);
			setRowAlignment(dataRow);
			conditionListTable.addCell(dataRow);

			if (count2 == 5) {
				conditionListTable.addCell(getEmptyRow());
				break;

			}

			count2++;
		}
		
		if(extraToAdd != 0) {
			for(int i = 0 ; i < extraToAdd; i++) {
			conditionListTable.addCell(getEmptyCell());
			}
		}

		conditionListTable.addCell(getEmptyRow());
		
		return conditionListTable;
	}

	public static List<ConditionList> getDataList() {

		List<ConditionList> conditionList = new ArrayList<ConditionList>();
		conditionList.add(new ConditionList("TEST CONDITION LIST 1", getConditions()));
		conditionList.add(new ConditionList("TEST CONDITION LIST 2", getConditions()));
		conditionList.add(new ConditionList("TEST CONDITION LIST 3", getConditions()));
		conditionList.add(new ConditionList("TEST CONDITION LIST 4", getConditions()));
		conditionList.add(new ConditionList("TEST CONDITION LIST 5", getConditions()));

		return conditionList;
	}

	public static List<Condition> getConditions() {
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(new Condition("A", "1"));
		conditions.add(new Condition("B", "2"));
		conditions.add(new Condition("C", "3"));
		conditions.add(new Condition("D", "4"));
		conditions.add(new Condition("E", "5"));
		conditions.add(new Condition("F", "6"));
		conditions.add(new Condition("G", "7"));
//		conditions.add(new Condition("H", "8"));
//		conditions.add(new Condition("I", "9"));
//		conditions.add(new Condition("J", "10"));
		return conditions;
	}

}
