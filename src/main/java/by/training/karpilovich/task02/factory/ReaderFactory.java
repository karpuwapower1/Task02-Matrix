package by.training.karpilovich.task02.factory;

import by.training.karpilovich.task02.dao.DAO;
import by.training.karpilovich.task02.dao.impl.TXTFileDao;

public class ReaderFactory {
	
	private static TXTFileDao reader;
	
	private static class InstanceHolder {
		private static final ReaderFactory INSTANCE = new ReaderFactory();
	}
	
	public static ReaderFactory getInstance() {
		return InstanceHolder.INSTANCE;
	}
	
	public DAO getReader() {
		if (reader == null) {
			reader = new TXTFileDao();
		}
		return reader;
	}

}
