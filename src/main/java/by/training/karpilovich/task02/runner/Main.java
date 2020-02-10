package by.training.karpilovich.task02.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.exception.ServiceException;
import by.training.karpilovich.task02.service.MatrixService;

public class Main {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		MatrixService service = new MatrixService();
		try {
			service.changeMatrix();
		} catch (ServiceException e) {
			LOGGER.error("ERROR");
		}

	}

}
