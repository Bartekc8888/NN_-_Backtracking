import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class DataContainer {
	private RealVector data;
	private RealVector target;
	private boolean isTrainingData;
	
	public DataContainer(double[] data, double[] target) {
		this.data = new ArrayRealVector(data);
		
		if (target == null) {
			isTrainingData = true;
		} else {
			this.target = new ArrayRealVector(target);
			isTrainingData = false;
		}
	}
	
	public DataContainer(DataContainer otherContainer) {
		data = otherContainer.data.copy();
		target = otherContainer.target.copy();
		isTrainingData = otherContainer.isTrainingData;
	}
	
	public RealVector getData() {
		return data;
	}
	
	public RealVector getTarget() {
		return target;
	}
	
	public boolean isTrainingData() {
		return isTrainingData;
	}
}
