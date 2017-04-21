package com.thf.ge.am.wbui.viewpart.prototypes.jfree;

import java.awt.image.BufferedImage;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import com.thf.ge.am.wbui.utils.ImageUtils;

public class ChartDemo_01 {

	public ChartDemo_01() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void createControls(Composite parent, IEventBroker eventBroker) {
		Label chartLabel = new Label(parent, SWT.NONE);

		final PieDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset, "Operating Systems");
		BufferedImage img = chart.createBufferedImage(600, 600);
		chartLabel.setImage(new Image(Display.getCurrent(), ImageUtils.getInstance().convertToSWT(img)));

//		new ChartComposite(parent, SWT.NONE, chart, true);
	}

	private PieDataset createDataset() {
		final DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Linux", 29);
		result.setValue("Mac", 20);
		result.setValue("Windows", 51);
		return result;
	}

	private org.jfree.chart.JFreeChart createChart(final PieDataset dataset, final String title) {
		final JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
		final PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}

	public void setFocus() {
	}
}