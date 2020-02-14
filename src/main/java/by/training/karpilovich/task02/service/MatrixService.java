package by.training.karpilovich.task02.service;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.dao.MatrixDAO;
import by.training.karpilovich.task02.dao.util.MatrixAndThreadFormat;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.entity.thread.MatrixChangerThread;
import by.training.karpilovich.task02.exception.DAOException;
import by.training.karpilovich.task02.exception.FormatException;
import by.training.karpilovich.task02.exception.ServiceException;
import by.training.karpilovich.task02.factory.DAOFactory;

public class MatrixService {
	private static final Logger LOGGER = LogManager.getLogger(MatrixService.class);
	private Map<Integer, Future<Integer>> map = new HashMap<>();
	private MatrixDAO writer = DAOFactory.getInstance().getMatrixDAO();
	private static final String MATRIX_FILE = "change.txt";
	private MatrixAndThreadFormat formatter = new MatrixAndThreadFormat();

	public void changeMatrix(int[][] threadNames) throws ServiceException {
		initWriter(MATRIX_FILE);
		for (int[] name : threadNames) {
			changeMatrix(name);
			writeMatrix();
			writeSum();
			map.clear();
		}
	}

	private Map<Integer, Future<Integer>> changeMatrix(int[] name) {
		int threadQuantity = Matrix.getInstance().getLength();
		CyclicBarrier barrier = new CyclicBarrier(Matrix.getInstance().getLength());
		Future<Integer> future;
		ExecutorService executor = Executors.newFixedThreadPool(threadQuantity);
		for (int i = 0; i < Matrix.getInstance().getLength(); i++) {
			future = executor.submit(new MatrixChangerThread(barrier, name[i]));
			map.put(name[i], future);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		return map;

	}

	private void writeMatrix() throws ServiceException {
		try {
			writer.write(formatter.formatMatrix());
		} catch (DAOException e) {
			LOGGER.error(e.getClass().getSimpleName());
			throw new ServiceException(e);
		}
	}

	private void writeSum() throws ServiceException {
		try {
			writer.write(formatter.formatStringSum(map));
		} catch (FormatException | DAOException e) {
			throw new ServiceException(e);
		}
	}

	private void initWriter(String fileName) throws ServiceException {
		File file = initFile(fileName);
		writer.setResource(file);
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
