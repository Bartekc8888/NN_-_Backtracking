import org.apache.commons.math3.linear.RealVector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataAfterLearn {
    private List<DataContainer> data;
    private RealVector[] outputVectors;
    private Map<Integer,Double> errorData;

    public DataAfterLearn(List<DataContainer> data, RealVector[] outputVectors, Map<Integer,Double> errorData) {
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

    public void toFile(DataInterpreter interpreter, boolean trainingData) throws IOException {
        if ((interpreter instanceof StringLineWithTargetInterpreter) || (interpreter instanceof SelectiveDataInterpreter)) {
            String filepath;
            if (trainingData) {
                filepath = "wyniki\\classification_train_po_treningu.txt";
            } else {
                filepath = "wyniki\\classification_test_po_testach.txt";
            }
            BufferedWriter bw = null;
            FileWriter fw = null;
            try {
                double[] con = new double[3];
                int[][] table = {
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                };
                fw = new FileWriter(filepath);
                bw = new BufferedWriter(fw);
                for (int i = 0; i < data.size(); i++) {
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
                bw.write("   G1 | G2 | G3");
                bw.newLine();
                bw.write("G1 " + table[0][0] + ((table[0][0] / 10 == 0) ? "  " : " ") + "| " + table[0][1] + ((table[0][1] / 10 == 0) ? "  " : " ") + "| " + table[0][2]);
                bw.newLine();
                bw.write("G2 " + table[1][0] + ((table[1][0] / 10 == 0) ? "  " : " ") + "| " + table[1][1] + ((table[1][1] / 10 == 0) ? "  " : " ") + "| " + table[1][2]);
                bw.newLine();
                bw.write("G3 " + table[2][0] + ((table[2][0] / 10 == 0) ? "  " : " ") + "| " + table[2][1] + ((table[2][1] / 10 == 0) ? "  " : " ") + "| " + table[2][2]);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bw.close();
            fw.close();
            System.out.println("Wyniki zapisano do pliku " + filepath);
        } else if (interpreter instanceof IdenticalOutputInterpreter) {
            double[][] con = new double[data.size()][8];
            double[][] outputAfterApproximation = {
                    {0.0, 0.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0, 0.0}
            };
            for (int i = 0; i < data.size(); i++) {
                con[i][0] = data.get(i).getData().toArray()[0];
                con[i][1] = data.get(i).getData().toArray()[1];
                con[i][2] = data.get(i).getData().toArray()[2];
                con[i][3] = data.get(i).getData().toArray()[3];
                con[i][4] = outputVectors[i].toArray()[0];
                con[i][5] = outputVectors[i].toArray()[1];
                con[i][6] = outputVectors[i].toArray()[2];
                con[i][7] = outputVectors[i].toArray()[3];
                double max = con[i][4];
                outputAfterApproximation[i][0] = 1.0;
                for (int j = 5; j <= 7; j++) {
                    if (con[i][j] > max) {
                        max = con[i][j];
                        outputAfterApproximation[i][0] = 0.0;
                        outputAfterApproximation[i][1] = 0.0;
                        outputAfterApproximation[i][2] = 0.0;
                        outputAfterApproximation[i][3] = 0.0;
                        outputAfterApproximation[i][j - 4] = 1.0;
                    }
                }
            }
            String filepath = "wyniki\\transformation_po_nauce.txt";
            BufferedWriter bw = null;
            FileWriter fw = null;
            try {
                fw = new FileWriter(filepath);
                bw = new BufferedWriter(fw);
                for (int i = 0; i < data.size(); i++) {
                    bw.write(con[i][0] + " " + con[i][1] + " " + con[i][2] + " " + con[i][3] + " | " + outputAfterApproximation[i][0] + " " + outputAfterApproximation[i][1] + " " + outputAfterApproximation[i][2] + " " + outputAfterApproximation[i][3]);
                    bw.newLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            bw.close();
            fw.close();
            System.out.println("Wyniki zapisano do pliku " + filepath);
        }
    }

    public void plot(){
        PlotFrame errorPlot = new PlotFrame("Wykres błędu");
        errorPlot.plotError(errorData);
    }
}
