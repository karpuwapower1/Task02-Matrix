package by.training.karpilovich.task02.service;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.dao.MatrixDAO;
import by.training.karpilovich.task02.dao.util.Formatter;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.exception.DAOException;
import by.training.karpilovich.task02.exception.FormatException;
import by.training.karpilovich.task02.exception.ServiceException;
import by.training.karpilovich.task02.factory.DAOFactory;

public class MatrixService {
	private static final Logger LOGGER = LogManager.getLogger(MatrixService.class);
	private Initializator init = new Initializator();
	private Map<Integer, Future<Integer>> map;
	private MatrixDAO writer = DAOFactory.getInstance().getMatrixDAO();
	private static final String MATRIX_FILE = "change.txt";
	private Formatter formatter = new Formatter();

	public void changeMatrix() throws ServiceException {
		init.initMatrix();
		int[][] threadNames = init.initThreadNames();
		for (int[] name : threadNames) {
			changeMatrix(name);
			writeMatrix();
			writeSum();
		}
	}

	private Map<Integer, Future<Integer>> changeMatrix(int[] name) {
		int threadQuantity = Matrix.getInstance().length();
		map = new HashMap<>();
		CyclicBarrier barrier = Matrix.getInstance().getBarrier();
		Future<Integer> future;
		ExecutorService executor = Executors.newFixedThreadPool(threadQuantity);
		for (int i = 0; i < Matrix.getInstance().length(); i++) {
			future = executor.submit(new MatrixChangerService(barrier, name[i]));
			map.put(name[i], future);
		}
		executor.shutdown();

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException " + e);
		}
		return map;
	}

	private void writeMatrix() throws ServiceException {
		initWriter(MATRIX_FILE);
		try {
			writer.write(formatter.formatMatrix());
		} catch (DAOException e) {
			LOGGER.error(e.getClass().getSimpleName());
			throw new ServiceException(e);
		}
	}

	private void writeSum() throws ServiceException {
		initWriter(MATRIX_FILE);
		try {
			writer.write(formatter.formatStringSum(map));
		} catch (FormatException | DAOException e) {
			throw new ServiceException(e);
		}
	}

	private void initWriter(String fileName) throws ServiceException {
		File file = initFile(fileName);
		writer.setResource(file);
		
		LOGGER.debug(file.getName());
	}

	private File initFile(String fileName) throws ServiceException {
		URL url = getClass().getClassLoader().getResource(fileName);
		if (url == null) {
			LOGGER.error("File " + fileName + " not found");
			throw new ServiceException();
		}
		return new File(url.getFile());
	}

}
