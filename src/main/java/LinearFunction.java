
public class LinearFunction implements ActivationFunction {

	@Override
	public double functionValue(double x) {
		return x;
	}
	
	@Override
	public double deriativeValue(double x) {
		return 1;
	}
}
