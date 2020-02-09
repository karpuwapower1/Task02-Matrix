package by.training.karpilovich.task02.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.dao.DAO;
import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.exception.DAOException;
import by.training.karpilovich.task02.exception.ServiceException;
import by.training.karpilovich.task02.factory.ReaderFactory;

public class MatrixInitializator {

	private final static Logger LOGGER = LogManager.getLogger(MatrixInitializator.class);

	private ElementFormat format = new ElementFormat();

	public void init() throws ServiceException {
		DAO reader = ReaderFactory.getInstance().getReader();
		try {
			List<String> list = reader.read();
			Element[][] elements = new Element[list.size()][list.size()];
			for (int i = 0; i < list.size(); i++) {
				try {
					Element[] rowElements = format.parse(list.get(i));
					if (rowElements.length != list.size()) {
						LOGGER.error("Invalid string " + list.get(i));
						throw new ServiceException();
					}
					elements[i] = rowElements;
				} catch (DAOException e) {
					LOGGER.error("Invalid string " + list.get(i));
					throw new ServiceException(e);
				}
			}
			Matrix.getInstance().setMatrix(elements);
		} catch (DAOException e) {
			LOGGER.warn(e);
			throw new ServiceException(e);
		}
	}

}
