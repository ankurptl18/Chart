package com.ankur.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

import com.itextpdf.text.Font.FontFamily;

public class LineChartDemo {

	public static JFreeChart generateLineChart() {

		Font font = new Font(FontFamily.TIMES_ROMAN.toString(), Font.PLAIN, 8);

		XYSeries series = new XYSeries("Valeur");
		series.add(0, 50);
		series.add(5, 70);
		series.add(10, 60);
		series.add(15, 90);
		series.add(20, 110);
		series.add(25, 100);

		XYSeries series2 = new XYSeries("Limite");
		series2.add(0, 60);
		series2.add(5, 90);
		series2.add(10, 80);
		series2.add(15, 100);
		series2.add(20, 120);
		series2.add(25, 130);
		
		XYSeries series3 = new XYSeries("0.8*Limite");
		series3.add(0, 140);
		series3.add(5, 44);
		series3.add(10, 120);
		series3.add(15, 100);
		series3.add(20, 130);
		series3.add(25, 170);
		
		XYSeries series4 = new XYSeries("Moyenne+2s");
		
		XYSeries series5 = new XYSeries("Moyenne-2s");
		series5.add(10,200);
		
		SymbolAxis sa = new SymbolAxis("",
			    new String[]{"A","B","C","D","E","F","G",
			    			 "H","I","J","J","K","L","M",
			    			 "N","O","P","Q","R","S","T",
			    			 "U","V","W","X","Y","Z","1",
			    			 "2","3","4","5","6","7","8",
			    			 "9","10","11","12","13","14","15",
			    			 "16","17","18","19","20","21","22",
			    			 
		});
		sa.setGridBandsVisible(false);
		sa.setTickLabelFont(font);
		
		XYDataset dataset = new XYSeriesCollection();
		((XYSeriesCollection) dataset).addSeries(series);
		((XYSeriesCollection) dataset).addSeries(series2);
		((XYSeriesCollection) dataset).addSeries(series3);
		((XYSeriesCollection) dataset).addSeries(series4);
		((XYSeriesCollection) dataset).addSeries(series5);

		JFreeChart chart = ChartFactory.createXYLineChart("Courbe polluant pour: HC", "", "", dataset);
		chart.getTitle().setFont(font);
		chart.getLegend().setPosition(RectangleEdge.TOP);
		chart.getLegend().setItemFont(font);
		chart.getLegend().setBorder(0.75, 0.75, 0.75, 0.75);
		chart.getLegend().setVerticalAlignment(VerticalAlignment.TOP);
		chart.getLegend().setItemLabelPadding(new RectangleInsets(2.0, 3.0, 2.0, 10.0));
		//chart.removeLegend();
		
		final XYPlot plot = chart.getXYPlot();

		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.orange);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.CYAN);
		plot.getDomainAxis().setTickLabelFont(font);
		plot.setDomainAxis(sa);
		plot.getDomainAxis().setVerticalTickLabels(true);
		plot.getRangeAxis().setTickLabelFont(font);
		//plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
		
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
		renderer.setBaseShapesVisible(true);

		//chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
		// ChartUtilities.applyCurrentTheme(chart);

		return chart;

	}
}