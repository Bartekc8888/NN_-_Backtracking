import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class NeuralLayer {
	private RealMatrix weights;
	private RealMatrix biases;
	
	private NeuralLayerProperties layerProperties;
	
	public NeuralLayer(NeuralLayerProperties properties) {
		weights = new Array2DRowRealMatrix(properties.getNeuronCount(), properties.getInputCount());
		initRandomWeights();
		
		biases = new Array2DRowRealMatrix(properties.getNeuronCount(), 1);
		initBiases();
		
		layerProperties = properties;
	}
	
	public RealVector calculateOutputValue(RealVector inputValues) {
		RealVector weightedValues = calculateSum(inputValues);
		RealVector activationValues = layerProperties.getActivationFunction().functionValue(weightedValues);
		
		return activationValues;
	}
	
	public RealMatrix calculateCorrections(RealVector input, RealVector errors) {
		RealVector weightedValues = calculateSum(input);
		RealVector deriativeValues = layerProperties.getActivationFunction().deriativeValue(weightedValues);

		RealMatrix corrections = new Array2DRowRealMatrix(layerProperties.getNeuronCount(),
		                                                  layerProperties.getInputCount() + 1);
		for (int i = 0; i < layerProperties.getNeuronCount(); i++) {
		    RealVector rowOfCorrections = new ArrayRealVector(layerProperties.getInputCount() + 1);
		    for (int j = 0; j < layerProperties.getInputCount(); j++) {
		        rowOfCorrections.setEntry(j, input.getEntry(j) * deriativeValues.getEntry(i) * errors.getEntry(i));
		    }
		    corrections.setRowVector(i, rowOfCorrections);
		}
		
		RealVector correctionsForBiases = deriativeValues.ebeMultiply(errors);
		corrections.setColumnVector(layerProperties.getInputCount(), correctionsForBiases);
		corrections.scalarMultiply(layerProperties.getLearningRate());

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
		weights = weights.add(corrections.getSubMatrix(0, layerProperties.getNeuronCount() - 1,
		                                               0, layerProperties.getInputCount() - 1));
		biases = biases.add(corrections.getSubMatrix(0, layerProperties.getNeuronCount() - 1,
		                                             layerProperties.getInputCount(), layerProperties.getInputCount()));
	}
	
	public void setParameters(RealMatrix parameters) {
		weights = new Array2DRowRealMatrix(parameters.getColumnVector(0).toArray());
		biases = new Array2DRowRealMatrix(parameters.getColumnVector(1).toArray());
	}
	
	public NeuralLayerProperties getProperties() {
        return layerProperties;
    }
	
	public RealMatrix getParameters() {
		RealMatrix parameters = new Array2DRowRealMatrix(layerProperties.getInputCount(), 2);
		
		parameters.setColumnVector(0, weights.getColumnVector(0));
		parameters.setColumnVector(1, biases.getColumnVector(0));
		
		return parameters;
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
