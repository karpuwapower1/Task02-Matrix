package by.training.karpilovich.task02.dao.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.dao.MatrixDAO;
import by.training.karpilovich.task02.exception.DAOException;

public class TXTFileMatrixDao implements MatrixDAO {

	private File file;
	
	@Override
	public void setResource(File file) {
		this.file = file;
	}

	private static final Logger LOGGER = LogManager.getLogger(TXTFileMatrixDao.class);

	public List<String> read() throws DAOException {
		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					list.add(line);
				}
			}
		} catch (IOException e) {
			LOGGER.warn(e);
			throw new DAOException();
		}
		return list;
	}

	@Override
	public void write(String str) throws DAOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			LOGGER.debug(str);
			writer.write(str);
			writer.flush();
		} catch (IOException e) {
			LOGGER.error("IOException " + e.getClass().getSimpleName() + " while writing into a file " + file.getName());
		throw new DAOException(e);
		}
	}
}
