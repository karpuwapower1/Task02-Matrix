package by.training.karpilovich.task02.service;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.dao.MatrixDAO;
import by.training.karpilovich.task02.dao.util.Formatter;
import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.exception.DAOException;
import by.training.karpilovich.task02.exception.FormatException;
import by.training.karpilovich.task02.exception.ServiceException;
import by.training.karpilovich.task02.factory.DAOFactory;
import by.training.karpilovich.task02.validator.Validator;

public class Initializator {

	private final static Logger LOGGER = LogManager.getLogger(Initializator.class);

	private Formatter format = new Formatter();
	private static final String MATRIX_FILE = "matrix.txt";
	private static final String THREAD_NAME_FILE = "threads.txt";
	MatrixDAO reader = DAOFactory.getInstance().getMatrixDAO();
	private Validator validator = new Validator();

	public void initMatrix() throws ServiceException {
		int[][] values = readIntsFromFile(MATRIX_FILE);
		if (!validator.isMatrixSquare(values)) {
			LOGGER.error("Matrix isn't squatre matrix ");
			throw new ServiceException("Matrix isn't squatre matrix ");
		}
		Element[][] elements = new Element[values.length][values.length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values.length; j++) {
				elements[i][j] = new Element(values[i][j]);
			}
		}
		Matrix.getInstance().setMatrix(elements);
	}

	public int[][] initThreadNames() throws ServiceException {
		int[][] names = readIntsFromFile(THREAD_NAME_FILE);
		int capacity = 0;
		for (int [] row : names) {
			capacity += row.length;
			LOGGER.debug("capacity= " + capacity);
		}
		if (!validator.isThreadQuantityLegal(capacity, Matrix.getInstance().length()) 
				|| !validator.isThreadNamesUnique(names, capacity)) {
			LOGGER.error("Thread names are illegal ");
			throw new ServiceException("Thread names are illegal ");
		}
		return names;

	}

	private int[][] readIntsFromFile(String fileName) throws ServiceException {
		initReader(fileName);
		int[] row;
		String str = "";
		try {
			List<String> list = reader.read();
			int[][] elements = new int[list.size()][];
			for (int i = 0; i < list.size(); i++) {
				str = list.get(i);
				row = format.parse(str);
				elements[i] = row;
			}
			return elements;
		} catch (FormatException e) {
			LOGGER.error("Illegel String: " + str + " file =" + fileName);
			throw new ServiceException(e);
		} catch (DAOException e) {
			LOGGER.warn(e);
			throw new ServiceException(e);
		}
	}

	private void initReader(String fileName) throws ServiceException {
		File file = initFile(fileName);
		reader.setResource(file);
	}

	private File initFile(String fileName) throws ServiceException {
		URL url = getClass().getClassLoader().getResource(fileName);
		if (url == null) {
			LOGGER.error("File matrix.txt not found");
			throw new ServiceException();
		}
		return new File(url.getFile());
	}
}
