
public class ActivationFunctionType {
    public enum FunctionType {
        Sigmoid, Linear
    }
    
    public static String toString(FunctionType type) {
        switch (type) {
        case Sigmoid:
            return "sigmoidalna";
        case Linear:
            return "liniowa";
        default:
            return "";
        }
    }
    
    public static ActivationFunction getFunction(FunctionType type) {
        switch (type) {
        case Sigmoid:
            return new SigmoidFunction();
        case Linear:
            return new LinearFunction();
        default:
            return null;
        }
    }
}
