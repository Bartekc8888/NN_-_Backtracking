import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class NeuralLayer {
	private RealMatrix weights;
	private RealMatrix biases;
	
	private NeuralLayerProperties layerProperties;
	
	public NeuralLayer(NeuralLayerProperties properties) {
		weights = new Array2DRowRealMatrix(properties.getInputCount(), properties.getInputCount());
		initRandomWeights();
		
		biases = new Array2DRowRealMatrix(properties.getInputCount(), 1);
		initBiases();
		
		layerProperties = properties;
	}
	
	public RealVector calculateOutputValue(RealVector inputValues) {
		RealVector weightedValues = calculateSum(inputValues);
		RealVector activationValues = layerProperties.getActivationFunction().functionValue(weightedValues);
		
		return activationValues;
	}
	
	public RealMatrix calculateCorrections(RealVector input, RealVector errors) {
		RealMatrix corrections = new Array2DRowRealMatrix(layerProperties.getInputCount(), 2);
		
		RealVector weightedValues = calculateSum(input);
		RealVector deriativeValues = layerProperties.getActivationFunction().deriativeValue(weightedValues);

		RealVector correctionsForWeights = input.ebeMultiply(deriativeValues).ebeMultiply(errors);
		RealVector correctionsForBiases = deriativeValues.ebeMultiply(errors);
		
		corrections.setColumnVector(0, correctionsForWeights);
		corrections.setColumnVector(1, correctionsForBiases);
		
		return corrections;
	}
	
	public RealVector calculateErrorsForPreviousLayer(RealVector errors) {
		RealMatrix errorsInMatrix = new Array2DRowRealMatrix(errors.toArray());
		RealMatrix errorsForPreviousLayer = weights.multiply(errorsInMatrix);
		
		return errorsForPreviousLayer.getColumnVector(0);
	}
	
	public RealVector calculateSum(RealVector inputValues) {
		RealMatrix weightedValues = weights.multiply(new Array2DRowRealMatrix(inputValues.toArray()));
		weightedValues = weightedValues.add(biases);
		
		return weightedValues.getColumnVector(0);
	}
	
	public void applyCorrection(RealMatrix corrections) {
		weights = weights.add(new Array2DRowRealMatrix(corrections.getColumnVector(0).toArray()));
		biases = biases.add(new Array2DRowRealMatrix(corrections.getColumnVector(1).toArray()));
	}
	
	private void initRandomWeights() {
		Random rand = new Random();
		
		for (int i = 0; i < weights.getRowDimension(); i++) {
			for (int j = 0; j < weights.getColumnDimension(); j++) {
				double randomDouble = rand.nextDouble() * 2 - 1;
				weights.setEntry(i, j, randomDouble);
			}
		}
	}
	
	private void initBiases() {	
		for (int i = 0; i < weights.getRowDimension(); i++) {
			biases.setEntry(i, 0, 1);
		}
	}
}
