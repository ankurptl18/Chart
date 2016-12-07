package com.ankur.chart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;

import org.jfree.chart.JFreeChart;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class writeChartToPdf {

	public static void main(String[] args) {

		writeChartToPDF(LineChartDemo.generateLineChart(), 550, 150, "D:/Passion/Office Work/pdfChart/linechart.pdf");
	}

	public static void writeChartToPDF(JFreeChart chart, int width, int height, String fileName) {

		PdfWriter writer = null;

		Document document = new Document();

		try {

			writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));

			document.open();

			PdfContentByte contentByte = writer.getDirectContent();

			PdfTemplate template = contentByte.createTemplate(width, height);

			Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());

			Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);

			chart.draw(graphics2d, rectangle2d);

			graphics2d.dispose();

			contentByte.addTemplate(template, 30, 600);

		} catch (Exception e) {
			e.printStackTrace();
		}
		document.close();
	}
}