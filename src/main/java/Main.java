import java.util.List;

import javax.swing.JDialog;

public class Main {

    public static void main(String[] args) {
        try {
            PropertiesGUI dialog = new PropertiesGUI();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupAndStartNetwork(NetworkConfiguration config) {
        try {
            DataLoader trainingLoader = new DataLoader(config.trainingPath, config.interpreter);
            List<DataContainer> trainingData = trainingLoader.getLoadedData();
            DataLoader testingLoader = new DataLoader(config.testingPath, config.interpreter);
            List<DataContainer> testingData = testingLoader.getLoadedData();

            NeuralManager manager = new NeuralManager(config.networkProperties);

            DataAfterLearn learNN = manager.learn(trainingData, config.epochLimit, config.errorLimit);
            if (!(config.interpreter instanceof ApproximationInterpreter)) {
                learNN.toFile(config.interpreter, true);
                learNN.plot();
            } else {
                PlotFrame plot = new PlotFrame("Po nauce",learNN);
                plot.plotFrame();
                learNN.plot();
            }
            DataAfterLearn testNN = manager.test(testingData);
            if ((!(config.interpreter instanceof IdenticalOutputInterpreter)) && (!(config.interpreter instanceof ApproximationInterpreter))) {
                testNN.toFile(config.interpreter, false);
            } else if (config.interpreter instanceof ApproximationInterpreter) {
                PlotFrame plot = new PlotFrame("Po testach",testNN);
                plot.plotFrame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
