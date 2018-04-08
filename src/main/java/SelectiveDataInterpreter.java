
public class SelectiveDataInterpreter implements DataInterpreter {
    private boolean[] chosenInputs;
    private int targetCount;
    private int numberOfInputs;
    public SelectiveDataInterpreter(boolean[] whichInputsToLoad, int outputTargetCount) {
        chosenInputs = whichInputsToLoad;
        targetCount = outputTargetCount;
    }

    public int getNumberOfInputs(){
        return numberOfInputs;
    }


    @Override
    public DataContainer interpretString(String lineWithData) {
        String[] splitData = lineWithData.split("\\s+"); // split by white space
        
        if (splitData.length - 1 != chosenInputs.length) {
            return null;
        }
        
        int trueInputCount = 0;
        for (int i = 0; i < chosenInputs.length; i++) {
            if (chosenInputs[i]) {
                trueInputCount++;
            }
        }
        numberOfInputs = trueInputCount;
        double[] data = new double[trueInputCount];
        double[] target = new double[targetCount];
        
        int indexInData = 0;
        for (int i = 0; i < splitData.length - 1; i++) {
            if (chosenInputs[i]) {
                data[indexInData] = Double.parseDouble(splitData[i]);
                indexInData++;
            }
        }
        
        int targetedOutput = Integer.parseInt(splitData[splitData.length - 1]); // this tell us which output should give 1
        for (int i = 0; i < targetCount; i++) {
            if ((i + 1) == targetedOutput) {
                target[i] = 1.d;
            } else {
                target[i] = 0.d;
            }
        }
        
        return new DataContainer(data, target);
    }

}
