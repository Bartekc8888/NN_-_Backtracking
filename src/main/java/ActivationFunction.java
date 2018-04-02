import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public interface ActivationFunction {
	public double functionValue(double x);
	public default RealVector functionValue(RealVector x) {
		RealVector vector = new ArrayRealVector(x);
		
		for (int i = 0; i < x.getDimension(); i++) {
			vector.setEntry(i, functionValue(x.getEntry(i)));
		}
		return vector;
	}
	
	public double deriativeValue(double x);
	public default RealVector deriativeValue(RealVector x) {
		RealVector vector = new ArrayRealVector(x);
		
		for (int i = 0; i < x.getDimension(); i++) {
			vector.setEntry(i, deriativeValue(x.getEntry(i)));
		}
		return vector;
	}
}
