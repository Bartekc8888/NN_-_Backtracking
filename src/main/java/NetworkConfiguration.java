
public class NetworkConfiguration {
    public String trainingPath;
    public String testingPath;
    
    public NeuralLayerProperties[] networkProperties;
    
    public double errorLimit;
    public int epochLimit;
    
    public DataInterpreter interpreter;
    
    public boolean[] selectedInput;
}
