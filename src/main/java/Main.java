import java.io.IOException;
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
            
			DataLoader loader = new DataLoader("C:\\Users\\barte\\Desktop\\classTrain.txt", classificationInterpreter);
			List<DataContainer> data = loader.getLoadedData();
			
			NeuralLayerProperties[] networkProperties = new NeuralLayerProperties[2];
			networkProperties[0] = new NeuralLayerProperties(4, 4, 0.2, 0.2, new SigmoidFunction());
			networkProperties[1] = new NeuralLayerProperties(4, 3, 0.2, 0.2, new SigmoidFunction());
			
			NeuralManager manager = new NeuralManager(networkProperties);
			
			RealMatrix[] learnedParameters = manager.learn(data, 500_000, 0.01);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
