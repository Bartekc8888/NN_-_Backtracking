
public class DataInterpreterType {
    public enum InterpreterType {
        StringLineWithTarget, IdenticalOutputInterpreter
    }
    
    public static String toString(InterpreterType type) {
        switch (type) {
        case StringLineWithTarget:
            return "Wejscia i cel";
        case IdenticalOutputInterpreter:
            return "Takie samo wyjście";
        default:
            return "";
        }
    }
    
    public static DataInterpreter getInterpreter(InterpreterType type) {
        switch (type) {
        case StringLineWithTarget:
            return new StringLineWithTargetInterpreter(3);
        case IdenticalOutputInterpreter:
            return new IdenticalOutputInterpreter();
        default:
            return null;
        }
    }
}