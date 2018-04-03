
public class NeuralLayerProperties {
	private int inputCount;
	private int neuronCount;
	private double learningRate;
	private double inertialInfluence;
	private ActivationFunction activationFunction;
	
	public NeuralLayerProperties(int numberOfInputs, int numberOfNeurons, double learningRate, 
	                            double inertia, ActivationFunction function) {
		inputCount = numberOfInputs;
		neuronCount = numberOfNeurons;
		this.learningRate = learningRate;
		inertialInfluence = inertia;
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
	
	public double getInertia() {
	    return inertialInfluence;
	}
	
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}
}
