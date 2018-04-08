
public class DataInterpreterType {
    public enum InterpreterType {
        StringLineWithTarget, IdenticalOutputInterpreter, ApproximationInterpreter
    }

    public static String toString(InterpreterType type) {
        switch (type) {
            case StringLineWithTarget:
                return "Wejscia i cel";
            case IdenticalOutputInterpreter:
                return "Takie samo wyjście";
            case ApproximationInterpreter:
                return "Aproksymacja: x i y";
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
            case ApproximationInterpreter:
                return  new ApproximationInterpreter();
            default:
                return null;
        }
    }
}