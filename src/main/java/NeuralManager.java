import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.summary.Sum;

public class NeuralManager {
	private NeuralNetwork network;
	
	public NeuralManager(NeuralLayerProperties[] networkProperties) {
		network = new NeuralNetwork(networkProperties);
	}
	
	public void initWeights(RealMatrix[] parameters) { // use this to setup parameters learned earlier
		
	}
	
	public RealMatrix[] learn(List<DataContainer> data, int epochLimit, double errorLimit) {
		List<DataContainer> dataCopy = new ArrayList<DataContainer>(data);
		double errorAfterEpoch = 0;
		
		int currentEpoch = 0;
		while (currentEpoch < epochLimit) {
			Collections.shuffle(dataCopy);
			RealMatrix[] correctionsAccumulator = new RealMatrix[network.getLayerCount()];
			//RealVector errorAccumulator = new ArrayRealVector(data.get(0).getTarget().getDimension(), 0); // its size equals to count of output nodes
			double errorAccumulator = 0;
			RealVector outputVector;
			
			for (DataContainer dataBit : dataCopy) {
				outputVector = network.calculateOutput(dataBit.getData());
				
				
				if (dataBit.getTarget() != null) {
					//RealVector errorVector = outputVector.subtract(dataBit.getTarget()); // TODO 1/2 * error^2 ? 
				    RealVector errorVector = outputVector.subtract(dataBit.getTarget());
					RealMatrix[] corrections = network.calculateCorrections(dataBit.getData(), errorVector);
					
					errorVector = errorVector.ebeMultiply(errorVector);
                    errorVector = errorVector.mapDivide(2);
                    errorAccumulator += errorVector.getL1Norm();
					
					if (corrections.length == correctionsAccumulator.length) {
						for (int i = 0; i < correctionsAccumulator.length; i++) {
							if (correctionsAccumulator[i] != null) {
								correctionsAccumulator[i] = correctionsAccumulator[i].add(corrections[i]);
							} else {
								correctionsAccumulator[i] = corrections[i];
							}
						}
					} else {
						throw new UnsupportedOperationException("Corrections dimensions don't match");
					}
				}
			}

            errorAfterEpoch = errorAccumulator / dataCopy.size(); // divide to get average error on all samples
            System.out.println(errorAfterEpoch);
            if (errorAfterEpoch < errorLimit) {
                return network.getNetworkParameters();
            }
            
            for (int i = 0; i < correctionsAccumulator.length; i++) {
                correctionsAccumulator[i] = correctionsAccumulator[i].scalarMultiply(1.d / dataCopy.size());
            }
            
            network.applyCorrections(correctionsAccumulator);

			currentEpoch++;
		}
		
		return network.getNetworkParameters();
	}
	
	public RealVector[] processData(List<DataContainer> data) {
		RealVector[] output = new RealVector[data.size()];
		
		return output;
	}
	
	public void calculateErrorOverDataRange(List<DataContainer> data) {
		
	}
}
