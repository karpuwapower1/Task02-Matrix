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

import by.training.karpilovich.task02.dao.DAO;
import by.training.karpilovich.task02.exception.DAOException;
import by.training.karpilovich.task02.service.ElementFormat;

public class TXTFileDao implements DAO {

	private File file;
	private ElementFormat format = new ElementFormat();
	
	public void setFile(File file) {
		this.file = file;
	}
	
	private static final Logger LOGGER = LogManager.getLogger();

	public List<String> read() throws DAOException {
		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			LOGGER.warn(e);
			throw new DAOException();
		}
		return list;
	}

	@Override
	public void writeMatrix() throws DAOException {
		String writingString = format.format();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(writingString);
		} catch (IOException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
		
	}

	@Override
	public void writeSum() {
		// TODO Auto-generated method stub
		
	}

}
