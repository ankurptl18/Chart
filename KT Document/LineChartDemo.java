package com.ankur.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
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

	public static JFreeChart generateLineChart(List<Map<Integer, Long>> dataSet, List<String> seriesName, String chartTitle, Boolean showLegend) {

		Font font = new Font(FontFamily.TIMES_ROMAN.toString(), Font.PLAIN, 8);

		XYSeries series;
		int count = 0;
		
		XYDataset dataset = new XYSeriesCollection();

		if (dataSet.size() == seriesName.size()) {
			for (Map<Integer, Long> temp : dataSet) {

				series = new XYSeries(seriesName.get(count++));

				for (Entry<Integer, Long> temp2 : temp.entrySet()) {
					series.add(temp2.getKey(), temp2.getValue());
				}
				
				((XYSeriesCollection) dataset).addSeries(series);
			}
		}

		SymbolAxis sa = new SymbolAxis("", new String[] { "A", "B", "C", "D", "EA", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22" });
		sa.setGridBandsVisible(false);
		sa.setTickLabelFont(font);

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "", "", dataset, PlotOrientation.VERTICAL, true, true, true);
		chart.getTitle().setFont(font);
		chart.getLegend().setPosition(RectangleEdge.TOP);
		chart.getLegend().setItemFont(font);
		chart.getLegend().setBorder(0.75, 0.75, 0.75, 0.75);
		chart.getLegend().setVerticalAlignment(VerticalAlignment.TOP);
		chart.getLegend().setItemLabelPadding(new RectangleInsets(2.0, 3.0, 2.0, 10.0));
		if(!showLegend) {
		chart.removeLegend();
		}

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
		// plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(0.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
		renderer.setBaseShapesVisible(true);

		// chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
		// ChartUtilities.applyCurrentTheme(chart);

		return chart;

	}
}



/*
 * final Paint[] paintArray;
		paintArray = ChartColor.createDefaultPaintArray();
		paintArray[2] = Color.RED; 
		paintArray[3] = Color.BLACK;
		
		plot.setDrawingSupplier(new DefaultDrawingSupplier(
                paintArray,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
 * 
 * */
