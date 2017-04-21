package com.thf.ge.am.wbui.viewpart.prototypes.jfree;

import java.awt.geom.Rectangle2D;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

//
//import java.awt.geom.Rectangle2D;
//
//import javax.annotation.PostConstruct;
//
//import org.eclipse.e4.core.services.events.IEventBroker;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleEdge;
//
public class ChartDemo_02 implements ChartMouseListener {

    private ChartPanel chartPanel;
    private Crosshair xCrosshair;
    private Crosshair yCrosshair;

	public ChartDemo_02() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void createControls(Composite parent, IEventBroker eventBroker) {
//		final PieDataset dataset = createDataset();
//		final JFreeChart chart = createChart(dataset, "Operating Systems");
//
//		new ChartComposite(parent, SWT.NONE, chart, true);

        JFreeChart chart = createChart(createDataset());
        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.addChartMouseListener(this);
//        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
//        this.xCrosshair = new Crosshair(Double.NaN, Color.),
//                new BasicStroke(0f));
//        this.xCrosshair.setLabelVisible(true);
//        this.yCrosshair = new Crosshair(Double.NaN, Color.GRAY, 
//                new BasicStroke(0f));
//        this.yCrosshair.setLabelVisible(true);
//        crosshairOverlay.addDomainCrosshair(xCrosshair);
//        crosshairOverlay.addRangeCrosshair(yCrosshair);
//        this.chartPanel.addOverlay(crosshairOverlay);

//        add(this.chartPanel);
        // Experimental
//        org.eclipse.swt.graphics.Color backgroundColor = parent.getBackground();
//        this.chartPanel.setBackground(new Color(backgroundColor.getRed()
//        								, backgroundColor.getGreen()
//        								,backgroundColor.getBlue()));
        // Experimental
		new ChartComposite(parent, SWT.NONE, chart, true);
	}

    private XYDataset createDataset() {
        XYSeries series = new XYSeries("S1");
        for (int x = 0; x < 100; x++) {
            series.add(x, 2.75 + Math.random() * 0.5);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Time Series Demo", "X", "Y", dataset);
        return chart;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
                RectangleEdge.BOTTOM);
        // make the crosshairs disappear if the mouse is out of range
        if (!xAxis.getRange().contains(x)) { 
            x = Double.NaN;                  
        }
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        this.yCrosshair.setValue(y);
    }  

    public void setFocus() {
	}
}
