import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class Main {

	public static void main(String[] args) {
		StringLineWithTargetInterpreter classificationInterpreter = new StringLineWithTargetInterpreter(3);
		try {
			DataLoader loader = new DataLoader("C:\\Users\\barte\\Desktop\\classTrain.txt", classificationInterpreter);
			List<DataContainer> data = loader.getLoadedData();
			
			NeuralLayerProperties[] networkProperties = new NeuralLayerProperties[2];
			networkProperties[0] = new NeuralLayerProperties(4, 4, 0.05, 0.2, new SigmoidFunction());
			networkProperties[1] = new NeuralLayerProperties(4, 3, 0.05, 0.2, new SigmoidFunction());
			
			NeuralManager manager = new NeuralManager(networkProperties);
			
			RealMatrix[] LearnedParameters = manager.learn(data, 500_000, 0.1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
