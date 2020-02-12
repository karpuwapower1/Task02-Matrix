package by.training.karpilovich.task02.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.exception.ServiceException;
import by.training.karpilovich.task02.service.Initializator;
import by.training.karpilovich.task02.service.MatrixService;

public class Main {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		MatrixService service = new MatrixService();
		Initializator initializator = new Initializator();
		try {
			initializator.initMatrix();
			int[][] threads = initializator.initThreadNames();
			service.changeMatrix(threads);
		} catch (ServiceException e) {
			LOGGER.error("ERROR\n" + e.getStackTrace());
		}
	}
}
