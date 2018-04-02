
public class NeuralLayerProperties {
	private int inputCount;
	private int neuronCount;
	private double learningRate;
	private ActivationFunction activationFunction;
	
	public NeuralLayerProperties(int numberOfInputs, int numberOfNeurons, double learningRate, ActivationFunction function) {
		inputCount = numberOfInputs;
		neuronCount = numberOfNeurons;
		this.learningRate = learningRate;
		activationFunction = function;
	}

	public int getInputCount() {
		return inputCount;
	}

	public int getNeuronCount() {
		return neuronCount;
	}
	
	public double getLearningRate() {
		return learningRate;
	}
	
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}
}
