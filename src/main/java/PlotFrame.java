import org.apache.commons.math3.linear.RealVector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class PlotFrame extends ApplicationFrame {
    private static final long serialVersionUID = -2752385353747427641L;
    
    private List<DataContainer> data;
    private RealVector[] outputVector;
    private String title;

    public PlotFrame(String title, DataAfterLearn dataAfterLearn) {
        super(title);
        this.title = title;
        data = dataAfterLearn.getData();
        outputVector = dataAfterLearn.getOutputVectors();
    }

    public PlotFrame(String title){
        super(title);
        this.title = title;
    }

    public void plotFrame() {
        XYSeries pointsBefore = new XYSeries("Punkty oryginalne");
        XYSeries pointsAfter = new XYSeries("Punkty aproksymowane");
        double x;
        double y1;
        double y2;
        for (int i = 0; i < data.size(); i++) {
            x = data.get(i).getData().toArray()[0];
            y1 = data.get(i).getTarget().toArray()[0];
            y2 = outputVector[i].toArray()[0];
            pointsBefore.add(x,y1);
            pointsAfter.add(x,y2);
        }

        XYSeriesCollection seriesCollection = new XYSeriesCollection( );
        seriesCollection.addSeries(pointsBefore);
        seriesCollection.addSeries(pointsAfter);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                title,
                "X",
                "Y",
                seriesCollection,
                PlotOrientation.VERTICAL,
                true, false, false);

        xylineChart.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        
        XYPlot plot = xylineChart.getXYPlot();
        XYShapeRenderer renderer = new XYShapeRenderer();
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        renderer.setSeriesStroke(1, new BasicStroke(1.0f));
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        setContentPane(chartPanel);
        setSize(800, 600);
        setVisible(true);
    }

    public void plotError(Map<Integer,Double> errorData) {
        XYSeries error = new XYSeries("Błąd");
        double x;
        double y;
        for (int i = 0; i < errorData.size(); i++) {
            x = i;
            y = errorData.get(i);
            error.add(x,y);
        }

        XYSeriesCollection seriesCollection = new XYSeriesCollection( );
        seriesCollection.addSeries(error);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                title,
                "Epoka",
                "Błąd średniokwadradtowy",
                seriesCollection,
                PlotOrientation.VERTICAL,
                true, false, false);

        xylineChart.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        
        XYPlot plot = xylineChart.getXYPlot();
        XYShapeRenderer renderer = new XYShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        setContentPane(chartPanel);
        setSize(800, 600);
        setVisible(true);
    }

}
