import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class NeuralLayer {
	private RealMatrix weights;
	private RealMatrix biases;
	
	private NeuralLayerProperties layerProperties;
	private RealMatrix previousWeightChange;
	
	public NeuralLayer(NeuralLayerProperties properties) {
		weights = new Array2DRowRealMatrix(properties.getNeuronCount(), properties.getInputCount());
		initRandomWeights();
		
		layerProperties = properties;
		
		if (layerProperties.getIsBiasUsed()) {
		    biases = new Array2DRowRealMatrix(properties.getNeuronCount(), 1);
		    initBiases();
		}
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
		
		RealMatrix correctionsForWeights = new Array2DRowRealMatrix(layerProperties.getNeuronCount(),
                                                                    layerProperties.getInputCount());
		
		for (int i = 0; i < layerProperties.getNeuronCount(); i++) {
		    RealVector rowOfCorrections = input.mapMultiply(deriativeValues.getEntry(i)).mapMultiply(errors.getEntry(i));

		    correctionsForWeights.setRowVector(i, rowOfCorrections);
		}
		corrections.setSubMatrix(correctionsForWeights.getData(), 0, 0);
		
		RealVector correctionsForBiases = deriativeValues.ebeMultiply(errors);
		corrections.setColumnVector(layerProperties.getInputCount(), correctionsForBiases); // set bias correction in last column

		return corrections;
	}
	
	public RealVector calculateErrorsForPreviousLayer(RealVector errors) {
	    RealMatrix weightsTransposed = weights.transpose();
	    RealMatrix errorsInMatrix = new Array2DRowRealMatrix(errors.toArray());
	    RealMatrix errorsForPreviousLayer = weightsTransposed.multiply(errorsInMatrix);
	    
	    RealVector vectorWithErrorsForPreviousLayer = errorsForPreviousLayer.getColumnVector(0); // just converting to vector
	    
	    return vectorWithErrorsForPreviousLayer;
	}
	
	public RealVector calculateSum(RealVector inputValues) {
		RealMatrix weightedValues = weights.multiply(new Array2DRowRealMatrix(inputValues.toArray()));
		if (layerProperties.getIsBiasUsed()) {
		    weightedValues = weightedValues.add(biases);
		}
		
		return weightedValues.getColumnVector(0);
	}
	
	public void applyCorrection(RealMatrix corrections) {
	    corrections = corrections.scalarMultiply((layerProperties.getLearningRate()) * (1 - layerProperties.getInertia()));
	    if (previousWeightChange != null) {
	        corrections = corrections.add(previousWeightChange.scalarMultiply(layerProperties.getInertia()));
	    }
	    
		weights = weights.add(corrections.getSubMatrix(0, layerProperties.getNeuronCount() - 1,
		                                                   0, layerProperties.getInputCount() - 1));
		if (layerProperties.getIsBiasUsed()) {
		    biases = biases.add(corrections.getSubMatrix(0, layerProperties.getNeuronCount() - 1,
		                                                 layerProperties.getInputCount(), layerProperties.getInputCount()));
		}
		
		previousWeightChange = corrections;
	}
	
	public void setParameters(RealMatrix parameters) {
		weights = new Array2DRowRealMatrix(parameters.getSubMatrix(0, layerProperties.getNeuronCount() - 1, 
		                                                           0, layerProperties.getInputCount() - 1).getData());
		
		if (layerProperties.getIsBiasUsed()) {
		    biases = new Array2DRowRealMatrix(parameters.getColumnVector(layerProperties.getInputCount()).toArray());
		}
	}
	
	public NeuralLayerProperties getProperties() {
        return layerProperties;
    }
	
	public RealMatrix getParameters() {
		RealMatrix parameters = new Array2DRowRealMatrix(layerProperties.getNeuronCount(), layerProperties.getInputCount() + 1);
		
		parameters.setSubMatrix(weights.getData(), 0, 0);
		
		if (layerProperties.getIsBiasUsed()) {
		    parameters.setSubMatrix(biases.getData(), 0, layerProperties.getInputCount());
		}
		
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
	    Random rand = new Random();
	    
		for (int i = 0; i < weights.getRowDimension(); i++) {
			biases.setEntry(i, 0, rand.nextDouble());
		}
	}
}
