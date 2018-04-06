
public class DataInterpreterType {
    public enum InterpreterType {
        StringLineWithTarget
    }
    
    public static String toString(InterpreterType type) {
        switch (type) {
        case StringLineWithTarget:
            return "Wejscia i cel";
        default:
            return "";
        }
    }
    
    public static DataInterpreter getInterpreter(InterpreterType type) {
        switch (type) {
        case StringLineWithTarget:
            return new StringLineWithTargetInterpreter(3);
        default:
            return null;
        }
    }
}