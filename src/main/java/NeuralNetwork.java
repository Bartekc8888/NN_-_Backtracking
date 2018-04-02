import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class NeuralNetwork {
	private NeuralLayer[] layers;
	
	public NeuralNetwork(NeuralLayerProperties[] networkProperties) {
		layers = new NeuralLayer[networkProperties.length];
		
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new NeuralLayer(networkProperties[i]);
		}
	}
	
	public RealVector calculateOutput(RealVector input) {
		return getOutputOfLayer(input, layers.length);
	}
	
	public RealMatrix[] calculateCorrections(RealVector input, RealVector errors) {
		RealMatrix[] corrections = new RealMatrix[layers.length];
		RealVector inputForCurrentLayer = getOutputOfLayer(input, layers.length - 1);
		
		corrections[layers.length - 1] = layers[layers.length - 1].calculateCorrections(inputForCurrentLayer, errors);
		RealVector errorsForPreviousLayer = concentrateWeightCorrection(corrections[layers.length - 1],
		                                                                layers[layers.length - 1].getProperties());
		
		for (int i = layers.length - 2; i > 0; i--) {
			inputForCurrentLayer = getOutputOfLayer(input, i);
			corrections[i] = layers[i].calculateCorrections(inputForCurrentLayer, errorsForPreviousLayer);
			
			//RealVector weightCorrections = concentrateWeightCorrection(corrections[i], layers[i].getProperties());
			//errorsForPreviousLayer = layers[i].calculateErrorsForPreviousLayer(weightCorrections);
			errorsForPreviousLayer = concentrateWeightCorrection(corrections[i], layers[i].getProperties());
		}
		corrections[0] = layers[0].calculateCorrections(input, errorsForPreviousLayer);
		
		return corrections;
	}
	
	public void applyCorrections(RealMatrix[] corrections) {
		for (int i = 0; i < layers.length; i++) {
			layers[i].applyCorrection(corrections[i]);
		}
	}
	
	public int getLayerCount() {
		return layers.length;
	}
	
	public RealMatrix[] getNetworkParameters() {
		RealMatrix[] parameters = new RealMatrix[layers.length];
		
		for (int i = 0; i < layers.length; i++) {
			parameters[i] = layers[i].getParameters();
		}
		
		return parameters;
	}
	
	public void setNetworkParameters(RealMatrix[] parameters) {		
		for (int i = 0; i < layers.length; i++) {
			layers[i].setParameters(parameters[i]);
		}
	}
	
	private RealVector getOutputOfLayer(RealVector input, int layerLevel) { // layerLevel counting from 1
		RealVector output = layers[0].calculateOutputValue(input);
		
		for (int i = 1; i < layerLevel; i++) {
			output = layers[i].calculateOutputValue(output);
		}
		
		return output;
	}
	
	private RealVector concentrateWeightCorrection(RealMatrix corrections, NeuralLayerProperties properties) {
	    RealMatrix weightCorrections = corrections.getSubMatrix(0,  properties.getNeuronCount() - 1,
                                                                0, properties.getInputCount() - 1);
	    
	    RealVector concentratedCorrections = weightCorrections.getRowVector(0);
	    for (int i = 1; i < weightCorrections.getRowDimension(); i++) {
	        concentratedCorrections = concentratedCorrections.add(weightCorrections.getRowVector(i));
	    }
	    
	    return concentratedCorrections;
	}
}
