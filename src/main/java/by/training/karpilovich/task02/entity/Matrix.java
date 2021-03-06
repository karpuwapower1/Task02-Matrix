package by.training.karpilovich.task02.entity;

import java.util.Arrays;

public class Matrix {

	private Element[][] matrix;
	
	private static class InstanceHolder {
		private static final Matrix instance = new Matrix();
	}

	public static Matrix getInstance() {
		return InstanceHolder.instance;
	}

	public void setMatrix(Element[][] matrix) {
		this.matrix = matrix;
	}

	public Element[][] getMatrix() {
		return matrix;
	}
	
	public Element getElement(int i, int j) {
		return matrix[i][j];
	}
	
	public int getLength() {
		return matrix.length;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (matrix == null) {
			return result;
		}
		for (Element[] rowElement : matrix) {
			for (Element element : rowElement) {
				result = prime * result + element.getValue();
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(matrix, other.matrix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Matrix [matrix=" + Arrays.toString(matrix) + "]";
	}

}
