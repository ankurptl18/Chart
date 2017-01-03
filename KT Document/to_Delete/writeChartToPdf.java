package com.ankur.chart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * The Class RectangleInCell.
 */
public class writeChartToPdf {

	/** The Constant DEST. */
	public static final String DEST = "D:/Passion/Office Work/pdfChart/Rectangle2.pdf";

	/** The Constant DATE_PATTERN. */
	public static final String DATE_PATTERN = "dd/MM/yyyy";

	/** The Constant FONT_SIZE. */
	public static final int FONT_SIZE = 8;

	/** The bold. */
	public static final Font BOLD_FONT = new Font(Font.FontFamily.TIMES_ROMAN, FONT_SIZE, Font.BOLD);

	/** The italic. */
	public static final Font ITALIC_FONT = new Font(Font.FontFamily.TIMES_ROMAN, FONT_SIZE, Font.ITALIC);
	
	/** The normal. */
	public static final Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, FONT_SIZE, Font.NORMAL);
	
	/** The chart width. */
	private int chartWidth = 550;
	
	/** The chart height. */
	private int chartHeight = 150;

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
		new writeChartToPdf().createChartInPdf(DEST);
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
	public void createChartInPdf(String dest) throws IOException, DocumentException {

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));

		BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

		document.open();

		float llx = 65;
		float lly = 770;
		float urx = 205;
		float ury = 810;

		PdfContentByte canvas = writer.getDirectContent();

		Rectangle rect1 = new Rectangle(llx, lly, urx, ury);
		rect1.setBorder(Rectangle.BOX);
		rect1.setBorderWidth(0.75f);
		canvas.rectangle(rect1);
		canvas.setFontAndSize(baseFont, FONT_SIZE);
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
		canvas.setFontAndSize(baseFont, 10);
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
		canvas.setFontAndSize(baseFont, FONT_SIZE);
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
		canvas.setFontAndSize(baseFont, FONT_SIZE + 2);
		canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, "PSA PEUGEOT CITROEN", llx + 80, 800, 0);
		canvas.setFontAndSize(baseFont, FONT_SIZE);
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

		secondRow.addCell(getSecondRow(map1));
		secondRow.addCell(getSecondRow(map2));
		secondRow.addCell(getSecondRow(map3));

		secondRow.completeRow();
		secondRow.writeSelectedRows(0, -1, 65, 765, canvas);
		
		final List<String> seriesName = Arrays.asList("Valeur","Limite","0.8*Limite","Moyenne+2s", "Moyenne-2s");
		
		String chartTitle = "Courbe polluant pour: HC";
		
		List<Map<Integer, Long>> data = prepareChartData();

		float x = 10;
		float y = 550;
		
		float check = writer.getVerticalPosition(Boolean.TRUE);
		
		/*PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		PdfContentByte canvas = stamper.getOverContent(1);
		
		PdfStamper stamper = new PdfStamper(reader, os)
				PdfPTable table = new PdfPTable(1);
				table.setTotalWidth(200);
				table.addCell("Hello");
		        table.writeSelectedRows(0, -1, 0, 1300,template);*/

		
		PdfPTable masterTable = new PdfPTable(1);
		masterTable.setTotalWidth(y);
		masterTable.setWidthPercentage(100);
		masterTable.getDefaultCell().setBorder(0);
		
		PdfPTable chartTable = new PdfPTable(1);
		chartTable.setTotalWidth(y);
		chartTable.setWidthPercentage(100);
		chartTable.getDefaultCell().setBorder(0);
		
		for(int i = 0; i<7 ; i++) {
			chartTable.addCell(" ");
		}
		
		int i = 0;
		while(i++ < 5) {

			/* if(i==1) {
				 chartTable.addCell(Image.getInstance(prepareChart(data, seriesName, chartTitle + i, Boolean.TRUE, canvas)));
			 }*/
			
			chartTable.addCell(Image.getInstance(prepareChart(data, seriesName, chartTitle + i, Boolean.TRUE, canvas)));
		}
		masterTable.addCell(chartTable);
		
		/*prepareChart(data, seriesName, chartTitle, Boolean.TRUE, canvas, x, y);
		prepareChart(data, seriesName, chartTitle, Boolean.FALSE, canvas, x, y-150);
		prepareChart(data, seriesName, chartTitle, Boolean.FALSE, canvas, x, y-150-150);
		prepareChart(data, seriesName, chartTitle, Boolean.FALSE, canvas, x, y-150-150-150);
		prepareChart(data, seriesName, chartTitle, Boolean.FALSE, canvas, x, y-150-150-150-150);
		prepareChart(data, seriesName, chartTitle, Boolean.FALSE, canvas, x, y-150-150-150-150-150);*/
		
		PdfTemplate template1 = canvas.createTemplate(chartWidth, chartHeight);
		masterTable.writeSelectedRows(1, -1,x,y,template1);
		canvas.addTemplate(template1, x, y);
		
		document.add(masterTable);
		
		document.close();
	}
	/**
	 * Prepare data.
	 *
	 * @return the list
	 */
	private List<Map<Integer, Long>> prepareChartData() {
		
		List<Map<Integer,Long>> data = new ArrayList<Map<Integer,Long>>();
		
		Map<Integer,Long> valeur = new HashMap<Integer, Long>();
		valeur.put(0, 50L);
		valeur.put(5, 70L);
		valeur.put(10, 60L);
		valeur.put(15, 90L);
		valeur.put(20, 110L);
		valeur.put(25, 100L);
		data.add(valeur);
		
		Map<Integer,Long> limite = new HashMap<Integer, Long>();
		limite.put(0, 60L);
		limite.put(5, 90L);
		limite.put(10, 80L);
		limite.put(15, 100L);
		limite.put(20, 120L);
		limite.put(25, 130L);
		data.add(limite);
		
		Map<Integer,Long> limite08 = new HashMap<Integer, Long>();
		limite08.put(0, 140L);
		limite08.put(5, 44L);
		limite08.put(10, 120L);
		limite08.put(15, 100L);
		limite08.put(20, 130L);
		limite08.put(25, 170L);
		data.add(limite08);
		
		Map<Integer,Long> moyenne2s = new HashMap<Integer, Long>();
		moyenne2s.put(20, 22L);
		moyenne2s.put(48, 150L);
		data.add(moyenne2s);
		
		Map<Integer,Long> moyenneMinus2s = new HashMap<Integer, Long>();
		moyenneMinus2s.put(10, 200L);
		data.add(moyenneMinus2s);
		
		return data;
	}

	/**
	 * Gets the first cell.
	 *
	 * @param objectMap the object map
	 * @return the first cell
	 * @throws DocumentException the document exception
	 */
	public PdfPCell getSecondRow(Map<String, String> objectMap) throws DocumentException {
		
		PdfPTable secondRow = new PdfPTable(2);
		secondRow.setWidths(new int[] { 2, 4 });
		secondRow.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		secondRow.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPCell temp;
		
		for (Map.Entry<String, String> map : objectMap.entrySet()) {
			temp = new PdfPCell(new Phrase(map.getKey() + ":", ITALIC_FONT));
			temp.setBorder(0);
			secondRow.addCell(temp);

			temp = new PdfPCell(new Phrase(map.getValue(), NORMAL_FONT));
			temp.setBorder(0);
			secondRow.addCell(temp);
		}
		
		PdfPCell cell = new PdfPCell(secondRow);
		cell.setBorderWidth(0.75f);
		cell.setFixedHeight(65f);
		
		return cell;
	}
	
	/**
	 * Prepare chart.
	 *
	 * @param dataSet the data set
	 * @param seriesName the series name
	 * @param chartTitle the chart title
	 * @param showLegend the show legend
	 * @param canvas the canvas
	 * @param xPoint the x point
	 * @param yPoint the y point
	 */
	public PdfTemplate prepareChart(List<Map<Integer, Long>> dataSet, List<String> seriesName, String chartTitle, Boolean showLegend,PdfContentByte canvas) {

		JFreeChart chart = LineChartDemo.generateLineChart(dataSet, seriesName, chartTitle, showLegend);

		PdfTemplate template = canvas.createTemplate(chartWidth, chartHeight);

		Graphics2D graphics2d = template.createGraphics(chartWidth, chartHeight, new DefaultFontMapper());
		
		Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, chartWidth, chartHeight);
		
		chart.draw(graphics2d, rectangle2d);
		
		graphics2d.dispose();
		
		//canvas.addTemplate(template, xPoint, yPoint);

		return template;

	}
}