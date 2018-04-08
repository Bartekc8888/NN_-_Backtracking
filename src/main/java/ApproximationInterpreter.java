public class ApproximationInterpreter implements DataInterpreter {
    @Override
    public DataContainer interpretString(String lineWithData) {
        String[] splitData = lineWithData.split("\\s+"); // split by white space

        double[] data = new double[1];
        double[] target = new double[1];
        data[0] = Double.parseDouble(splitData[0]);
        target[0] = Double.parseDouble(splitData[1]);
        System.out.println(data[0] + " " + target[0]);
        return new DataContainer(data, target);
    }
}
