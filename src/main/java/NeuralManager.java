import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

public class NeuralManager extends SwingWorker<DataAfterLearn, Pair<Double, Double>> {
    private NeuralNetwork network;
    private boolean calculatePercent = false;

    private NetworkConfiguration currentConfig;
    private List<DataContainer> dataCopy;
    private List<DataContainer> currentTestingData;
    private int epochLimit; 
    private double errorLimit;
    private PlotFrame plotForTrainingErrors;
    private PlotFrame plotForTestErrors;
    private PlotFrame afterLearning;
    
    private boolean drawErrorOnTrain;
    private boolean drawErrorOnTest;
    
    public NeuralManager(NetworkConfiguration config, boolean drawErrorOnTrain, boolean drawErrorOnTest) {
        network = new NeuralNetwork(config.networkProperties);
        currentConfig = config;
        if (config.networkProperties[config.networkProperties.length - 1].getNeuronCount() == 3) {
            calculatePercent = true;
        }
        
        this.drawErrorOnTrain = drawErrorOnTrain;
        this.drawErrorOnTest = drawErrorOnTest;
    }

    public void initWeights(RealMatrix[] parameters) { // use this to setup parameters learned earlier
        //TODO
    }

    public void learn(List<DataContainer> data, List<DataContainer> testingData, int epochLimit, double errorLimit) throws Exception {
        dataCopy = new ArrayList<DataContainer>(data);
        currentTestingData = new ArrayList<DataContainer>(testingData);
        this.epochLimit = epochLimit;
        this.errorLimit = errorLimit;
        plotForTrainingErrors = new PlotFrame("Błąd sieci dla danych treningowych", this);
        plotForTestErrors = new PlotFrame("Błąd sieci dla danych testowych", this);
        
        execute(); // execute doInBackground function on another thread
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
        
        if (calculatePercent) {
            calculatePercentOfCorrectAnswers(data, allOutputVectors, -1);
        }
        
        return new DataAfterLearn(dataCopy, allOutputVectors, null);
    }

    public void calculatePercentOfCorrectAnswers(List<DataContainer> data, RealVector[] outputVectors, int epoch) {
        double errorAccumulator = 0;
        for (int i = 0; i < data.size(); i++) {
            if (epoch == -1) {
                RealVector errorVector = data.get(i).getTarget().subtract(outputVectors[i]);
                errorVector = errorVector.ebeMultiply(errorVector);
                errorVector = errorVector.mapDivide(2);
                errorAccumulator += errorVector.getL1Norm();
            }
        }
        
        DataContainer[] dataWithTarget = new DataContainer[data.size()];
        data.toArray(dataWithTarget);
        int[][] table = DataAfterLearn.getClassificationCorrectnessTable(dataWithTarget, outputVectors);
        
        double[] percent = new double[3];
        for (int i = 0; i < 3; i++) {
            double sumOfAnswers = ((double) (table[i][0]) + (double) (table[i][1]) + (double) (table[i][2]));
            double correctlyGuessedAnswers = table[i][i];
            percent[i] = (correctlyGuessedAnswers / sumOfAnswers) * 100.0;
        }
        
        if (epoch != -1) {
            System.out.print("\033[H\033[2J"); // ASCII escape code for screen clear and cursor movement
            System.out.print("Epoka: " + (epoch + 1));
        } else {
            System.out.print("Błąd: " + errorAccumulator/data.size());
        }
        System.out.println(" GR1: " + new DecimalFormat("#000.00").format(percent[0]) +
                           "% GR2: " + new DecimalFormat("#000.00").format(percent[1]) +
                           "% GR3: " + new DecimalFormat("#000.00").format(percent[2]) + "%");
    }

    public double calculateErrorOverDataRange(List<DataContainer> data) {
        List<DataContainer> dataCopy = new ArrayList<DataContainer>(data);
        RealVector outputVector;
        double errorAccumulator = 0.0;
        
        for (DataContainer dataBit : dataCopy) {
            outputVector = network.calculateOutput(dataBit.getData());
            
            RealVector errorVector = dataBit.getTarget().subtract(outputVector);
            errorVector = errorVector.ebeMultiply(errorVector);
            errorVector = errorVector.mapDivide(2);
            errorAccumulator += errorVector.getL1Norm();
        }
        
        double errorOnDataRange = errorAccumulator / dataCopy.size();
        return errorOnDataRange;
    }

    @Override
    protected DataAfterLearn doInBackground() throws Exception {
        Map<Integer, Double> errorData = new HashMap<Integer, Double>();
        RealVector outputVector;
        RealVector[] allOutputVectors = new RealVector[dataCopy.size()];
        
        double errorAfterEpoch = 0;
        int currentEpoch = 0;
        while (currentEpoch < epochLimit && !isCancelled()) {
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
            if (calculatePercent) {
                calculatePercentOfCorrectAnswers(dataCopy, allOutputVectors, currentEpoch);
            }
            
            if (currentEpoch > 3) {
                errorAfterEpoch = errorAccumulator / dataCopy.size(); // divide to get average error on all samples
                
                Double errorOnTest = null;
                if (drawErrorOnTest) {
                    errorOnTest = calculateErrorOverDataRange(currentTestingData);
                }
                Pair<Double, Double> errorOnTrainAndTest = new Pair<Double, Double>(errorAfterEpoch, errorOnTest);
                publish(errorOnTrainAndTest);
                
                errorData.put(currentEpoch, errorAfterEpoch);
                if (errorAfterEpoch < errorLimit) {
                    return new DataAfterLearn(dataCopy, allOutputVectors, errorData);
                }
            }

            for (int i = 0; i < correctionsAccumulator.length; i++) {
                correctionsAccumulator[i] = correctionsAccumulator[i].scalarMultiply(1.d / dataCopy.size());
            }

            network.applyCorrections(correctionsAccumulator);
            currentEpoch++;
        }
        
        return new DataAfterLearn(dataCopy, allOutputVectors, errorData);
    }
    
    @Override
    protected void process(List<Pair<Double, Double>> chunks) {
        for (Pair<Double, Double> error : chunks) {
            if (drawErrorOnTrain) {
                plotForTrainingErrors.addDataErrorToPlot(error.getFirst());
            }
            
            if (drawErrorOnTest) {
                plotForTestErrors.addDataErrorToPlot(error.getSecond());
            }
        }
    }
    
    @Override
    protected void done() {
        try {
            if (isCancelled()) {
                cancelLearning();
                return;
            }
            
            DataAfterLearn learNN = get();
            if (!(currentConfig.interpreter instanceof ApproximationInterpreter)) {
                learNN.toFile(currentConfig.interpreter, true);
            } else {
                afterLearning = new PlotFrame("Po nauce", this);
                afterLearning.plotFrame(learNN);
            }
            
            if ((!(currentConfig.interpreter instanceof IdenticalOutputInterpreter)) && 
                    (!(currentConfig.interpreter instanceof ApproximationInterpreter))) {
                DataAfterLearn testNN = test(currentTestingData);
                testNN.toFile(currentConfig.interpreter, false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getClass(), "Błąd", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public void cancelLearning() {
        cancel(true);
        plotForTrainingErrors.dispose();
        plotForTestErrors.dispose();
        
        if (afterLearning != null) {
            afterLearning.dispose();
        }
    }
}
