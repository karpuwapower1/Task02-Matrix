package by.training.karpilovich.task02.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.exception.DAOException;

public class ElementFormat {
	
	private static final Logger LOGGER = LogManager.getLogger(ElementFormat.class);
	
	private static final String DELIMETER = " ";
	
	public Element[] parse(String str) throws DAOException {
		String[] parameters = str.split(DELIMETER);
		Element[] elements = new Element[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			try {
			elements[i] = new Element(Integer.parseInt(parameters[i]));
			} catch (NumberFormatException e) {
				LOGGER.error(str + " " + e);
				throw new DAOException(e);
			}
		}
		return elements;
	}
	
	public String format() {
		Element[][] elements = Matrix.getInstance().getMatrix();
		StringBuilder builder = new StringBuilder();
		for (Element[] rowElements : elements) {
			for (Element element : rowElements) {
				builder.append(element.getValue() + DELIMETER);
			}
			builder.append('\n');
		}
		return builder.toString();
	}

}
