package by.training.karpilovich.task02.factory;

import by.training.karpilovich.task02.dao.MatrixDAO;
import by.training.karpilovich.task02.dao.impl.TXTFileMatrixDao;

public class ReaderFactory {
	
	private static TXTFileMatrixDao reader;
	
	private static class InstanceHolder {
		private static final ReaderFactory INSTANCE = new ReaderFactory();
	}
	
	public static ReaderFactory getInstance() {
		return InstanceHolder.INSTANCE;
	}
	
	public MatrixDAO getReader() {
		if (reader == null) {
			reader = new TXTFileMatrixDao();
		}
		return reader;
	}

}
