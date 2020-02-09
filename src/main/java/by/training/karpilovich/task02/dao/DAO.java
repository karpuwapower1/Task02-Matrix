package by.training.karpilovich.task02.dao;

import java.util.List;

import by.training.karpilovich.task02.exception.DAOException;

public interface DAO {
	
	public List<String> read() throws DAOException;
	
	public void writeMatrix() throws DAOException;
	
	public void writeSum();

}
