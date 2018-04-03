import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
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
		corrections.setColumnVector(layerProperties.getInputCount(), correctionsForBiases); // set bias correction in last column

		return corrections;
	}
	
	public RealVector calculateErrorsForPreviousLayer(RealVector input, RealVector errors) {
	    /*
		RealVector weightedValues = calculateSum(input);
        RealVector deriativeValues = layerProperties.getActivationFunction().deriativeValue(weightedValues);
        
        RealMatrix errorsInMatrix = new Array2DRowRealMatrix(errors.ebeMultiply(deriativeValues).toArray());
        RealMatrix errorsForPreviousLayer = new Array2DRowRealMatrix(weights.getRowDimension(), 
                                                                     weights.getColumnDimension());
        
        for (int i = 0; i < errorsInMatrix.getRowDimension(); i++) {
            errorsForPreviousLayer.setRowVector(i, weights.getRowVector(i).mapMultiplyToSelf(errorsInMatrix.getEntry(i, 0)));
        }
		*/
	    
	    /*
	    RealMatrix errorsForPreviousLayer = new Array2DRowRealMatrix(weights.getRowDimension(), 
                                                                     weights.getColumnDimension());
	    RealVector weightSumInRows = new ArrayRealVector(weights.getRowDimension());
        for (int i = 0; i < weightSumInRows.getDimension(); i++) {
            weightSumInRows.setEntry(i, weights.getRowVector(i).getL1Norm());
        }
	    
        for (int i = 0; i < weightSumInRows.getDimension(); i++) {
            errorsForPreviousLayer.setRowVector(i, weights.getRowVector(i).mapMultiplyToSelf(errors.getEntry(i) / weightSumInRows.getEntry(i)));
        }
        */
	    
		//return concentrateErrors(errorsForPreviousLayer);
	    
	    return weights.transpose().multiply(new Array2DRowRealMatrix(errors.toArray())).getColumnVector(0);
	}
	
	public RealVector calculateSum(RealVector inputValues) {
		RealMatrix weightedValues = weights.multiply(new Array2DRowRealMatrix(inputValues.toArray()));
		weightedValues = weightedValues.add(biases);
		
		return weightedValues.getColumnVector(0);
	}
	
	public void applyCorrection(RealMatrix corrections) {
	    corrections = corrections.scalarMultiply(layerProperties.getLearningRate());
	    if (previousWeightChange != null) {
	        corrections = corrections.add(previousWeightChange.scalarMultiply(layerProperties.getInertia()));
	    }
	    
		weights = weights.subtract(corrections.getSubMatrix(0, layerProperties.getNeuronCount() - 1,
		                                                   0, layerProperties.getInputCount() - 1));
		biases = biases.subtract(corrections.getSubMatrix(0, layerProperties.getNeuronCount() - 1,
		                                                 layerProperties.getInputCount(), layerProperties.getInputCount()));
		
		previousWeightChange = corrections;
	}
	
	public void setParameters(RealMatrix parameters) {
		weights = new Array2DRowRealMatrix(parameters.getSubMatrix(0, layerProperties.getNeuronCount() - 1, 
		                                                           0, layerProperties.getInputCount() - 1).getData());
		biases = new Array2DRowRealMatrix(parameters.getColumnVector(layerProperties.getInputCount()).toArray());
	}
	
	public NeuralLayerProperties getProperties() {
        return layerProperties;
    }
	
	public RealMatrix getParameters() {
		RealMatrix parameters = new Array2DRowRealMatrix(layerProperties.getNeuronCount(), layerProperties.getInputCount() + 1);
		
		parameters.setSubMatrix(weights.getData(), 0, 0);
		parameters.setSubMatrix(biases.getData(), 0, layerProperties.getInputCount());
		
		return parameters;
	}
	
	private void initRandomWeights() {
		Random rand = new Random();
		
		for (int i = 0; i < weights.getRowDimension(); i++) {
			for (int j = 0; j < weights.getColumnDimension(); j++) {
				double randomDouble = rand.nextDouble() * 6 - 3;
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
	
    private RealVector concentrateErrors(RealMatrix errors) {
        RealMatrix weightErrors = errors.getSubMatrix(0, layerProperties.getNeuronCount() - 1,
                                                      0, layerProperties.getInputCount() - 1);
        
        RealVector concentratedErrors = weightErrors.getRowVector(0);
        for (int i = 1; i < weightErrors.getRowDimension(); i++) {
            concentratedErrors = concentratedErrors.add(weightErrors.getRowVector(i));
        }
        
        return concentratedErrors;
    }
}
