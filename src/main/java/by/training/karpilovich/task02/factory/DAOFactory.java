package by.training.karpilovich.task02.factory;

import by.training.karpilovich.task02.dao.MatrixDAO;
import by.training.karpilovich.task02.dao.impl.FileMatrixDao;

public class DAOFactory {
	
	private static FileMatrixDao reader;
	
	private static class InstanceHolder {
		private static final DAOFactory INSTANCE = new DAOFactory();
	}
	
	public static DAOFactory getInstance() {
		return InstanceHolder.INSTANCE;
	}
	
	public MatrixDAO getMatrixDAO() {
		if (reader == null) {
			reader = new FileMatrixDao();
		}
		return reader;
	}

}
