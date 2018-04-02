import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Main {

	public static void main(String[] args) {
		double[][] matrixData = { {1d,2d,3d}, {2d,5d,3d}};
		RealMatrix m = MatrixUtils.createRealMatrix(matrixData);

		System.out.println(m.getRowDimension());
		System.out.println(m.getColumnDimension());
	}
}
