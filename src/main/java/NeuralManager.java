import java.text.DecimalFormat;
import java.util.*;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.summary.Sum;

public class NeuralManager {
    private NeuralNetwork network;
    private boolean calculatePercent = false;

    public NeuralManager(NeuralLayerProperties[] networkProperties) {
        network = new NeuralNetwork(networkProperties);
        if (networkProperties[networkProperties.length - 1].getNeuronCount() == 3) {
            calculatePercent = true;
        }
    }

    public void initWeights(RealMatrix[] parameters) { // use this to setup parameters learned earlier
        //TODO
    }

    public DataAfterLearn learn(List<DataContainer> data, int epochLimit, double errorLimit) {
        List<DataContainer> dataCopy = new ArrayList<DataContainer>(data);
        double errorAfterEpoch = 0;
        Map<Integer, Double> errorData = new HashMap<Integer, Double>();
        int currentEpoch = 0;
        RealVector outputVector;
        RealVector[] allOutputVectors = new RealVector[dataCopy.size()];
        while (currentEpoch < epochLimit) {
            Collections.shuffle(dataCopy);
            RealMatrix[] correctionsAccumulator = new RealMatrix[network.getLayerCount()];
            double errorAccumulator = 0;
            int j = 0;
            for (DataContainer dataBit : dataCopy) {
                outputVector = network.calculateOutput(dataBit.getData());
                allOutputVectors[j] = outputVector;
                j++;
                if (dataBit.getTarget() != null) {
                    RealVector errorVector = dataBit.getTarget().subtract(outputVector);
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
            if (calculatePercent)
                calculatePercentOfCorrectAnswers(dataCopy, allOutputVectors, currentEpoch);
            errorAfterEpoch = errorAccumulator / dataCopy.size(); // divide to get average error on all samples
            errorData.put(currentEpoch, errorAfterEpoch);
            if (errorAfterEpoch < errorLimit) {
                return new DataAfterLearn(dataCopy, allOutputVectors, errorData);
            }

            for (int i = 0; i < correctionsAccumulator.length; i++) {
                correctionsAccumulator[i] = correctionsAccumulator[i].scalarMultiply(1.d / dataCopy.size());
            }

            network.applyCorrections(correctionsAccumulator);

            currentEpoch++;

        }
        return new DataAfterLearn(dataCopy, allOutputVectors, errorData);
    }

    public DataAfterLearn test(List<DataContainer> data) {
        List<DataContainer> dataCopy = new ArrayList<DataContainer>(data);
        RealVector outputVector;
        RealVector[] allOutputVectors = new RealVector[dataCopy.size()];
        int j = 0;
        for (DataContainer dataBit : dataCopy) {
            outputVector = network.calculateOutput(dataBit.getData());
            allOutputVectors[j] = outputVector;
            j++;
        }
        calculatePercentOfCorrectAnswers(data, allOutputVectors, -1);
        return new DataAfterLearn(dataCopy, allOutputVectors, null);
    }

    public void calculatePercentOfCorrectAnswers(List<DataContainer> data, RealVector[] outputVectors, int epoch) {
        double[] con = new double[3];
        int[][] table = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        double errorAccumulator = 0;
        for (int i = 0; i < data.size(); i++) {
            if (epoch == -1) {
                RealVector errorVector = data.get(i).getTarget().subtract(outputVectors[i]);
                errorVector = errorVector.ebeMultiply(errorVector);
                errorVector = errorVector.mapDivide(2);
                errorAccumulator += errorVector.getL1Norm();
            }
            con[0] = outputVectors[i].toArray()[0];
            con[1] = outputVectors[i].toArray()[1];
            con[2] = outputVectors[i].toArray()[2];
            int group = 1;
            double max = con[0];
            for (int j = 1; j <= 2; j++) {
                if (con[j] > max) {
                    max = con[j];
                    group = j + 1;
                }
            }
            int targetGroup = 1;
            while (data.get(i).getTarget().toArray()[targetGroup - 1] == 0.0) {
                targetGroup++;
            }
            if (group == targetGroup) {
                table[group - 1][group - 1]++;
            } else {
                table[targetGroup - 1][group - 1]++;
            }
        }
        double[] percent = new double[3];
        for (int i = 0; i < 3; i++) {
            percent[i] = (((double) (table[i][i])) / ((double) (table[i][0]) + (double) (table[i][1]) + (double) (table[i][2]))) * 100.0;
        }
        if (epoch != -1) {
            System.out.print("\033[H\033[2J");
            System.out.println("Epoka: " + (epoch + 1) + " GR1: " + new DecimalFormat("#000.00").format(percent[0]) + "% GR2: " + new DecimalFormat("#000.00").format(percent[1]) + "% GR3: " + new DecimalFormat("#000.00").format(percent[2]) + "%");
        } else {
            System.out.println("Błąd: " + errorAccumulator/data.size() + " GR1: " + new DecimalFormat("#000.00").format(percent[0]) + "% GR2: " + new DecimalFormat("#000.00").format(percent[1]) + "% GR3: " + new DecimalFormat("#000.00").format(percent[2]) + "%");
        }
    }

    public RealVector[] processData(List<DataContainer> data) {
        RealVector[] output = new RealVector[data.size()];
        //TODO
        return output;
    }

    public void calculateErrorOverDataRange(List<DataContainer> data) {
        // calculating error on test data
        //TODO
    }
}
