
public class NeuralLayerProperties {
	private int inputCount;
	private int neuronCount;
	private double learningRate;
	private double inertialInfluence;
	private boolean isBiasUsed;
    private ActivationFunction activationFunction;
	
	public NeuralLayerProperties(int numberOfInputs, int numberOfNeurons, double learningRate, 
	                            double inertia, boolean biasUsage, ActivationFunction function) {
		inputCount = numberOfInputs;
		neuronCount = numberOfNeurons;
		this.learningRate = learningRate;
		inertialInfluence = inertia;
		activationFunction = function;
		isBiasUsed = biasUsage;
	}

	public int getInputCount() {
		return inputCount;
	}
	
	public void setInputCount(int count) {
	    inputCount = count;
	}

	public int getNeuronCount() {
		return neuronCount;
	}
	
	public double getLearningRate() {
		return learningRate;
	}
	
	public double getInertia() {
	    return inertialInfluence;
	}
	
	public boolean getIsBiasUsed() {
	    return isBiasUsed;
	}
	
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}
	
}
