import org.apache.commons.math3.linear.RealVector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataAfterLearn {
    private List<DataContainer> data;
    private RealVector[] outputVectors;
    private Map<Integer, Double> errorData;

    public DataAfterLearn(List<DataContainer> data, RealVector[] outputVectors, Map<Integer, Double> errorData) {
        this.data = data;
        this.outputVectors = outputVectors;
        this.errorData = errorData;
    }

    public List<DataContainer> getData() {
        return data;
    }

    public RealVector[] getOutputVectors() {
        return outputVectors;
    }
    
    public Map<Integer, Double> getErrorData() {
        return errorData;
    }

    public void toFile(DataInterpreter interpreter, boolean trainingData) throws IOException {
        if (!(new File("wyniki").exists())) {
            new File("wyniki").mkdir();
        }
        
        if ((interpreter instanceof StringLineWithTargetInterpreter) || (interpreter instanceof SelectiveDataInterpreter)) {
            String filepath;
            if (trainingData) {
                filepath = "wyniki\\classification_train_po_treningu.txt";
            } else {
                filepath = "wyniki\\classification_test_po_testach.txt";
            }
            
            try (
                 FileWriter fileWriter = new FileWriter(filepath);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ) {
                DataContainer[] dataWithTarget = new DataContainer[data.size()];
                data.toArray(dataWithTarget);
                int[][] table = getClassificationCorrectnessTable(dataWithTarget, outputVectors);
                
                writeCorrectnessTableToBuffer(bufferedWriter, table);
                
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            System.out.println("Wyniki zapisano do pliku " + filepath);
        } else if (interpreter instanceof IdenticalOutputInterpreter) {
            double[][] con = new double[data.size()][8];
            double[][] outputAfterApproximation = new double[4][4];
            
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.size(); j++) {
                    con[i][j] = data.get(i).getData().toArray()[j];
                    con[i][4 + j] = outputVectors[i].toArray()[j];
                }
                
                double max = con[i][4];
                outputAfterApproximation[i][0] = 1.0;
                for (int j = 4; j < 8; j++) {
                    if (con[i][j] >= max) {
                        max = con[i][j];
                        for (int k = 0; k < data.size(); k++) {
                            outputAfterApproximation[i][k] = 0.0;
                        }
                        outputAfterApproximation[i][j - 4] = 1.0;
                    }
                }
            }
            
            String filepath = "wyniki\\transformation_po_nauce.txt";
            try (
                    FileWriter fileWriter = new FileWriter(filepath);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ) {
                for (int i = 0; i < data.size(); i++) {
                    bufferedWriter.write(con[i][0] + " " + con[i][1] + " " + con[i][2] + " " + con[i][3] + " | " +
                                         outputAfterApproximation[i][0] + " " + outputAfterApproximation[i][1] + " " + outputAfterApproximation[i][2] + " " + outputAfterApproximation[i][3]);
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Wyniki zapisano do pliku " + filepath);
        }
    }
    
    public static int[][] getClassificationCorrectnessTable(DataContainer[] dataWithTarget, RealVector[] output) {
        int[][] correctnessTable = new int[3][3];
        
        for (int i = 0; i < dataWithTarget.length; i++) {
            double[] currentOutput = output[i].toArray();

            int group = 1;
            double max = currentOutput[0];
            for (int j = 1; j < 3; j++) {
                if (currentOutput[j] > max) {
                    max = currentOutput[j];
                    group = j + 1;
                }
            }
            
            int targetGroup = 1;
            while (dataWithTarget[i].getTarget().getEntry(targetGroup - 1) == 0.0) {
                targetGroup++;
            }
            
            if (group == targetGroup) {
                correctnessTable[group - 1][group - 1]++;
            } else {
                correctnessTable[targetGroup - 1][group - 1]++;
            }
        }
        
        return correctnessTable;
    }
    
    private void writeCorrectnessTableToBuffer(BufferedWriter bufferedWriter, int [][] table) throws IOException {
        bufferedWriter.write("   G1 | G2 | G3");
        bufferedWriter.newLine();
        bufferedWriter.write("G1 " + table[0][0] + ((table[0][0] / 10 == 0) ? "  " : " ") + "| " + table[0][1] + ((table[0][1] / 10 == 0) ? "  " : " ") + "| " + table[0][2]);
        bufferedWriter.newLine();
        bufferedWriter.write("G2 " + table[1][0] + ((table[1][0] / 10 == 0) ? "  " : " ") + "| " + table[1][1] + ((table[1][1] / 10 == 0) ? "  " : " ") + "| " + table[1][2]);
        bufferedWriter.newLine();
        bufferedWriter.write("G3 " + table[2][0] + ((table[2][0] / 10 == 0) ? "  " : " ") + "| " + table[2][1] + ((table[2][1] / 10 == 0) ? "  " : " ") + "| " + table[2][2]);
        bufferedWriter.newLine();
    }
}
