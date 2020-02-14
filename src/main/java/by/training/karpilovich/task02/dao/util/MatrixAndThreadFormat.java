package by.training.karpilovich.task02.dao.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.exception.FormatException;

public class MatrixAndThreadFormat {

	private static final Logger LOGGER = LogManager.getLogger(MatrixAndThreadFormat.class);

	private static final String DELIMETER = " ";
	private static final char NEW_LINE = '\n';
	private static final String SUM_PATTERN = "Sum of thread's N %d column and row = %s%n";

	public int[] parseStringToIntArray(String str) throws FormatException {
		String[] parameters = str.split(DELIMETER);
		int[] ints = new int[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			try {
				ints[i] = Integer.parseInt(parameters[i]);
			} catch (NumberFormatException e) {
				LOGGER.error("Illegal string " + str);
				throw new FormatException(e);
			}
		}
		return ints;
	}

	public String formatMatrix() {
		Element[][] elements = Matrix.getInstance().getMatrix();
		StringBuilder builder = new StringBuilder();
		for (Element[] rowElements : elements) {
			for (Element element : rowElements) {
				builder.append(element.getValue() + DELIMETER);
			}
			builder.append(NEW_LINE);
		}
		return builder.toString();
	}

	public String formatStringSum(Map<Integer, Future<Integer>> map) throws FormatException {
		StringBuilder builder = new StringBuilder();
		try {
			for (Entry<Integer, Future<Integer>> entry : map.entrySet()) {
				builder.append(String.format(SUM_PATTERN, entry.getKey(), entry.getValue().get()));
			}
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error(e.getClass().getSimpleName());
			throw new FormatException(e);
		}
		return builder.toString();
	}

}
