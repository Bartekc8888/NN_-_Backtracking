
public class IdenticalOutputInterpreter implements DataInterpreter {

    @Override
    public DataContainer interpretString(String lineWithData) {
        String[] splitData = lineWithData.split("\\s+"); // split by white space
        
        double[] data = new double[splitData.length - 1];
        double[] target;
        
        for (int i = 0; i < splitData.length - 1; i++) {
            data[i] = Double.parseDouble(splitData[i]);
        }
        
        target = data.clone();
        
        return new DataContainer(data, target);
    }

}
