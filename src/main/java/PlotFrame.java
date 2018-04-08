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
    
    private XYShapeRenderer renderer;
    private JFreeChart xylineChart;
    private XYSeriesCollection seriesCollection;
    
    private String title;
    private XYSeries persistentErrorSeries;

    public PlotFrame(String title) {
        super(title);
        this.title = title;
    }

    public void plotFrame(DataAfterLearn networkData) {
        initPlotFrame(title, "X", "Y");
        
        XYSeries pointsBefore = new XYSeries("Punkty oryginalne");
        XYSeries pointsAfter = new XYSeries("Punkty aproksymowane");
        double x;
        double y1, y2;
        
        List<DataContainer> dataAndTarget = networkData.getData(); 
        for (int i = 0; i < dataAndTarget.size(); i++) {
            x = dataAndTarget.get(i).getData().toArray()[0];
            y1 = dataAndTarget.get(i).getTarget().toArray()[0];
            y2 = networkData.getOutputVectors()[i].toArray()[0];
            
            pointsBefore.add(x, y1);
            pointsAfter.add(x, y2);
        }

        seriesCollection.addSeries(pointsBefore);
        seriesCollection.addSeries(pointsAfter);

        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesStroke(1, new BasicStroke(0.5f));
    }

    public void plotError(Map<Integer, Double> errorData) {
        initPlotFrame(title, "Epoka", "Błąd średniokwadradtowy");
        
        XYSeries error = new XYSeries("Błąd");
        double x;
        double y;
        for (int i = 0; i < errorData.size(); i++) {
            x = i;
            y = errorData.get(i);
            error.add(x, y);
        }

        seriesCollection.addSeries(error);
        repaint();
    }

    public void addDataErrorToPlot(double error) {
        if (persistentErrorSeries == null) {
            initPlotFrame(title, "Epoka", "Błąd średniokwadradtowy");
            
            persistentErrorSeries = new XYSeries("Błąd");
            seriesCollection.addSeries(persistentErrorSeries);
        }
        
        persistentErrorSeries.add(persistentErrorSeries.getItemCount() + 1, error);
    }
    
    private void initPlotFrame(String mainTitle, String xTitle, String yTitle) {
        seriesCollection = new XYSeriesCollection();
        
        xylineChart = ChartFactory.createXYLineChart(
                mainTitle,
                xTitle,
                yTitle,
                seriesCollection,
                PlotOrientation.VERTICAL,
                true, false, false);

        XYPlot plot = xylineChart.getXYPlot();
        xylineChart.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                                                         RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        
        renderer = new XYShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(0.1f));
        plot.setRenderer(renderer);
        
        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        
        setContentPane(chartPanel);
        setSize(800, 600);
        setVisible(true);
    }
}
