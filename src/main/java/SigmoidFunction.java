
public class SigmoidFunction implements ActivationFunction {

	@Override
	public double functionValue(double x) {
		return 1.0 / (1.0 - Math.exp(-x));
	}

	@Override
	public double deriativeValue(double x) {
		return functionValue(x) * (1.0 - functionValue(x));
	}
}
