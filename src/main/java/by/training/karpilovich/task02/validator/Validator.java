package by.training.karpilovich.task02.validator;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Validator {
	
	Logger LOGGER = LogManager.getLogger(Validator.class);
	
	public boolean isMatrixSquare(int[][] matrix) {
		for (int[] row : matrix) {
			if (matrix.length != row.length) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isThreadQuantityLegal(int threadQuantity, int matrixLength) {
		return threadQuantity % matrixLength == 0;
	}
	
	public boolean isThreadNamesUnique(int[][] names, int quantity) {
		Set<Integer> set = new HashSet<>();
		for (int[] row : names) {
			for (int name : row) {
				set.add(name);
			}
		}
		return set.size() == quantity;
	}

}
