
public class NeuralLayerProperties {
	private int inputCount;
	private ActivationFunction activationFunction;
	
	public NeuralLayerProperties(int numberOfInputs, ActivationFunction function) {
		inputCount = numberOfInputs;
		activationFunction = function;
	}

	public int getInputCount() {
		return inputCount;
	}

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}
}
