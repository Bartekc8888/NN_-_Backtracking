
public class StringLineWithTargetInterpreter implements DataInterpreter {
	int targetCount;
	
	public StringLineWithTargetInterpreter(int ouputTargetCount) {
		targetCount = ouputTargetCount;
	}
	
	@Override
	public DataContainer interpretString(String lineWithData) {
		String[] splitData = lineWithData.split("\\s+"); // split by white space
		
		double[] data = new double[splitData.length - 1];
		double[] target = new double[targetCount];
		
		for (int i = 0; i < splitData.length - 1; i++) {
			data[i] = Double.parseDouble(splitData[i]);
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
