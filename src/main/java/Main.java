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
			networkProperties[0] = new NeuralLayerProperties(4, 10, 0.001, new SigmoidFunction());
			networkProperties[1] = new NeuralLayerProperties(10, 3, 0.001, new SigmoidFunction());
			
			NeuralManager manager = new NeuralManager(networkProperties);
			
			RealMatrix[] LearnedParameters = manager.learn(data, 1000, 0.01);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
