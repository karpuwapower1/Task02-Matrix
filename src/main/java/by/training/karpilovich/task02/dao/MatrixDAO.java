package by.training.karpilovich.task02.dao;

import java.io.File;
import java.util.List;

import by.training.karpilovich.task02.exception.DAOException;

public interface MatrixDAO {
	
	public List<String> read() throws DAOException;
	
	public void write(String str) throws DAOException;
	
	public void setResource(File file);

}
