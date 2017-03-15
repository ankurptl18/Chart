package com.ankur.pdf2;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfHelper {

	static Font bold_10B = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK);

	static Font bold_10W = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.WHITE);

	static Font normal_10B = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

	static Font normal_10W = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.WHITE);

	public static PdfPTable getHeaderRow() {

		PdfPTable header = new PdfPTable(5);

		PdfPCell firstCol = new PdfPCell(new Phrase("FICHE CONDITION D'ESSAI V2.5", bold_10W));
		firstCol.setColspan(4);
		firstCol.setBackgroundColor(new BaseColor(247, 150, 70));
		firstCol.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM | PdfPCell.TOP);
		firstCol.setBorderWidth(0.75f);
		header.addCell(firstCol);

		PdfPCell secondCol = new PdfPCell(new Phrase("EUROPE ", bold_10W));
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
		PdfPCell row3 = new PdfPCell(new Phrase("EMTPY CELL", bold_10B));
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
		setAlignmentWithBorder(row);
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

		if (conditionCount > 5 && conditionCount % 5 != 0) {
			extraToAdd = 5 - (conditionCount % 5);
		} else {
			extraToAdd = 5 - conditionCount;
		}

		int count = 0;
		for (Condition con : cl.getConditions()) {
			PdfPTable table = new PdfPTable(1);
			setRowAlignment(table.getDefaultCell());
			table.setWidthPercentage(100);
			table.addCell(new Phrase(con.getLabel(), normal_10B));
			table.addCell(new Phrase(con.getValue(), normal_10B));
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

			conditionListTable.addCell(table);

			if (++count % 5 == 0) {
				conditionListTable.addCell(getEmptyRow());
			}

		}

		if (extraToAdd != 0) {
			for (int i = 0; i < extraToAdd; i++) {
				conditionListTable.addCell(getEmptyCell());
			}
		}

		if(conditionCount % 5 != 0)
		conditionListTable.addCell(getEmptyRow());

		return conditionListTable;
	}
	
	
	public static PdfPTable getCoastDownRow() throws DocumentException {

		PdfPTable coastDownTable = new PdfPTable(2);
		
		PdfPCell header = new PdfPCell(new Phrase("COAST DOWN",bold_10W));
		header.setColspan(2);
		header.setBorderWidth(0.75f);
		header.setBackgroundColor(new BaseColor(146,208,80));
		coastDownTable.addCell(header);
		
		PdfPCell emptyCell = new PdfPCell(new Phrase(" ",normal_10W));
		emptyCell.setColspan(2);
		emptyCell.setBorder(PdfPCell.NO_BORDER);
		coastDownTable.addCell(emptyCell);
		
		coastDownTable.setWidths(new int[] {1,3});
		setRowAlignment(coastDownTable.getDefaultCell());

		PdfPTable table = new PdfPTable(1);
		setRowAlignment(table.getDefaultCell());
		table.addCell(new Phrase("LOI DE ROUTE",bold_10B));
		table.addCell(new Phrase("PSA 258",normal_10B));
		
		coastDownTable.addCell(table);
		
		//
		PdfPTable valueTable = new PdfPTable(3);
		
		PdfPCell valueCellHeader = new PdfPCell(new Phrase("CIBLE",normal_10B));
		setAlignmentWithBorder(valueCellHeader);
		valueCellHeader.setBorderWidth(0.75f);
		valueCellHeader.setColspan(3);
		
		valueTable.addCell(valueCellHeader);
		
		PdfPCell valueCell = new PdfPCell(new Phrase("F0 CIBLE",bold_10B));
		setAlignmentWithBorder(valueCell);
		valueCell.setBorder(PdfPCell.LEFT);
		valueCell.setBorderWidth(0.75f);
		valueTable.addCell(valueCell);
		
		valueCell = new PdfPCell(new Phrase("F1 CIBLE",bold_10B));
		setAlignmentWithBorder(valueCell);
		valueCell.setBorder(PdfPCell.NO_BORDER);
		valueTable.addCell(valueCell);
		
		valueCell = new PdfPCell(new Phrase("F2 CIBLE",bold_10B));
		setAlignmentWithBorder(valueCell);
		valueCell.setBorder(PdfPCell.RIGHT);
		valueCell.setBorderWidth(0.75f);
		valueTable.addCell(valueCell);
		
		valueCell = new PdfPCell(new Phrase("71.6",normal_10B));
		valueCell.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM);
		setAlignmentWithBorder(valueCell);
		valueCell.setBorderWidth(0.75f);
		valueTable.addCell(valueCell);
		
		valueCell = new PdfPCell(new Phrase("0.3217",normal_10B));
		valueCell.setBorder(PdfPCell.BOTTOM);
		setAlignmentWithBorder(valueCell);
		valueCell.setBorderWidth(0.75f);
		valueTable.addCell(valueCell);
		
		valueCell = new PdfPCell(new Phrase("0.03255",normal_10B));
		valueCell.setBorder(PdfPCell.RIGHT | PdfPCell.BOTTOM);
		setAlignmentWithBorder(valueCell);
		valueCell.setBorderWidth(0.75f);
		valueTable.addCell(valueCell);
		
		coastDownTable.addCell(valueTable);
		
		emptyCell = new PdfPCell(new Phrase(" ",normal_10W));
		emptyCell.setColspan(2);
		emptyCell.setBorder(PdfPCell.NO_BORDER);
		coastDownTable.addCell(emptyCell);
		
		//
		
		// STARTS
		PdfPTable table1 = new PdfPTable(1);
		setRowAlignment(table1.getDefaultCell());
		table1.addCell(new Phrase("INERTIA",bold_10B));
		table1.addCell(new Phrase("1130",normal_10B));
		
		coastDownTable.addCell(table1);
		
		//
		PdfPTable valueTable1 = new PdfPTable(3);
		
		PdfPCell valueCellHeader1 = new PdfPCell(new Phrase("CONSIGNES MACHINE : calculé ou utilisateur",normal_10B));
		setAlignmentWithBorder(valueCellHeader1);
		valueCellHeader1.setBorderWidth(0.75f);
		valueCellHeader1.setColspan(3);
		
		valueTable1.addCell(valueCellHeader1);
		
		PdfPCell valueCell1 = new PdfPCell(new Phrase("F0 MACHINE",bold_10B));
		setAlignmentWithBorder(valueCell1);
		valueCell1.setBorder(PdfPCell.LEFT);
		valueCell1.setBorderWidth(0.75f);
		valueTable1.addCell(valueCell1);
		
		valueCell1 = new PdfPCell(new Phrase("F1 MACHINE",bold_10B));
		setAlignmentWithBorder(valueCell1);
		valueCell1.setBorder(PdfPCell.NO_BORDER);
		valueTable1.addCell(valueCell1);
		
		valueCell1 = new PdfPCell(new Phrase("F2 MACHINE",bold_10B));
		setAlignmentWithBorder(valueCell1);
		valueCell1.setBorder(PdfPCell.RIGHT);
		valueCell1.setBorderWidth(0.75f);
		valueTable1.addCell(valueCell1);
		
		valueCell1 = new PdfPCell(new Phrase("10.39",normal_10B));
		valueCell1.setBorder(PdfPCell.LEFT | PdfPCell.BOTTOM);
		setAlignmentWithBorder(valueCell1);
		valueCell1.setBorderWidth(0.75f);
		valueTable1.addCell(valueCell1);
		
		valueCell1 = new PdfPCell(new Phrase("0.025",normal_10B));
		valueCell1.setBorder(PdfPCell.BOTTOM);
		setAlignmentWithBorder(valueCell1);
		valueCell1.setBorderWidth(0.75f);
		valueTable1.addCell(valueCell1);
		
		valueCell1 = new PdfPCell(new Phrase("0.0327",normal_10B));
		valueCell1.setBorder(PdfPCell.RIGHT | PdfPCell.BOTTOM);
		setAlignmentWithBorder(valueCell1);
		valueCell1.setBorderWidth(0.75f);
		valueTable1.addCell(valueCell1);
		
		coastDownTable.addCell(valueTable1);
		
		emptyCell = new PdfPCell(new Phrase(" ",normal_10W));
		emptyCell.setColspan(2);
		emptyCell.setBorder(PdfPCell.NO_BORDER);
		coastDownTable.addCell(emptyCell);
		// ENDS
		
		return coastDownTable;
	}

	private static void setAlignmentWithBorder(PdfPCell valueCellHeader) {
		valueCellHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
		valueCellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
	}

	

	public static List<ConditionList> getDataList() {

		List<ConditionList> conditionList = new ArrayList<ConditionList>();
		conditionList.add(new ConditionList("TEST CONDITION LIST 1", getConditions()));

		return conditionList;
	}

	public static List<Condition> getConditions() {
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(new Condition("A", "1"));
		
		return conditions;
	}

	public static PdfPCell getPollutantTable() {

		PdfPCell pollutant  = new PdfPCell(new Phrase("Test"));
		
		return pollutant;
	}

}
