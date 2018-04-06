import java.util.List;

import javax.swing.JDialog;

import org.apache.commons.math3.linear.RealMatrix;

public class Main {

	public static void main(String[] args) {
		StringLineWithTargetInterpreter classificationInterpreter = new StringLineWithTargetInterpreter(3);
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
	            
	            RealMatrix[] learnedParameters = manager.learn(trainingData, config.epochLimit, config.errorLimit);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
}
