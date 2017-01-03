package com.ankur.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
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
		//plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
		
		final Paint[] paintArray;
		paintArray = ChartColor.createDefaultPaintArray();
		paintArray[0] = Color.BLACK;
		paintArray[1] = Color.RED; 
		paintArray[2] = new Color(255,0,255);
		paintArray[3] = new Color(0, 128, 0);
		paintArray[4] = Color.green;
		
		plot.setDrawingSupplier(new DefaultDrawingSupplier(
		        paintArray,
		        DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
		        DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
		        DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
		        DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
		        DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		
		/*XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		renderer.setSeriesStroke(1, new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
		renderer.setSeriesStroke(2, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		renderer.setSeriesStroke(3, new BasicStroke(1, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_ROUND));
		renderer.setSeriesStroke(4, new BasicStroke(1, BasicStroke.JOIN_MITER, BasicStroke.JOIN_MITER));
		renderer.setBaseShapesVisible(true);*/
		
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setBaseShapesVisible(true);
		
		/*final Paint[] paintArray;
		paintArray = ChartColor.createDefaultPaintArray();
		paintArray[0] = Color.BLACK;
		paintArray[1] = Color.RED; 
		paintArray[2] = new Color(255,0,255);
		paintArray[3] = new Color(0, 128, 0);
		paintArray[4] = Color.green;*/

		//chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
		//ChartUtilities.applyCurrentTheme(chart);

		return chart;
		


	}
	
	private static void setupSeriesRenderer(XYPlot xyplot) {
		XYLineAndShapeRenderer lineAndShapeRenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();

		for (int i = 0; i < xyplot.getSeriesCount(); i++) {
			lineAndShapeRenderer.setSeriesPaint(i, Color.red);
			lineAndShapeRenderer.setSeriesStroke(i, new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
			lineAndShapeRenderer.setSeriesFillPaint(i, Color.white);
			lineAndShapeRenderer.setSeriesOutlinePaint(i, Color.black);
			lineAndShapeRenderer.setUseOutlinePaint(true);
			lineAndShapeRenderer.setUseFillPaint(true);
			
			xyplot.getRenderer().setSeriesVisible(i, new Boolean(true));
		}
		
		xyplot.getRenderer().setSeriesVisible(0, new Boolean(true));
	}
}





