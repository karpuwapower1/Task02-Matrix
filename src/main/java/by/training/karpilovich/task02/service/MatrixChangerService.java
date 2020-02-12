package by.training.karpilovich.task02.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.exception.ServiceException;

public class MatrixChangerService implements Callable<Integer> {

	private Matrix matrix = Matrix.getInstance();
	List<Element> locked = new ArrayList<>();
	private CyclicBarrier barrier;
	private int name;
	private int diagonalIndex = 0;

	private static final Logger LOGGER = LogManager.getLogger(MatrixChangerService.class);

	public MatrixChangerService(CyclicBarrier barrier, int name) {
		this.barrier = barrier;
		this.name = name;
	}

	@Override
	public Integer call() throws ServiceException {
		Thread.currentThread().setName(String.valueOf(name));
		changeDiagonalElement();
		changeNonDiagonalElement();
		try {
			barrier.await();
			LOGGER.debug("await barrier");
		} catch (BrokenBarrierException | InterruptedException e) {
			LOGGER.error(e.getClass().getSimpleName());
			throw new ServiceException(e);
		}
		LOGGER.debug("barrier is cancelled");
		releaseElementLock();
		LOGGER.debug("elements is released");
		int sum = countSum();
		return Integer.valueOf(sum);
	}

	private void changeDiagonalElement() {
		while (!(isChanged(diagonalIndex, diagonalIndex))) {
			diagonalIndex = (diagonalIndex == matrix.length() - 1) ? 0 : diagonalIndex + 1;
			LOGGER.debug("index " + diagonalIndex);
		}
		LOGGER.debug("set diagonal index " + diagonalIndex);
	}

	private void changeNonDiagonalElement() {
		Random random = new Random();
		boolean change = false;
		int index;
		boolean isRow;
		while (!change) {
			if ((index = random.nextInt(matrix.length())) == diagonalIndex) {
				continue;
			}
			change = (isRow = random.nextBoolean()) 
					? isChanged(diagonalIndex, index) 
							: isChanged(index, diagonalIndex);

			LOGGER.debug("change = " + change + " isRow= " + isRow
					+ (isRow ? (diagonalIndex + ", " + index) : (index + ", " + diagonalIndex)));
		}
	}

	private boolean isChanged(int i, int j) {
		Element element = matrix.getElement(i, j);
		if (element.changeElement()) {
			locked.add(element);
			return true;
		}
		return false;
	}

	private void releaseElementLock() {
		for (Element element : locked) {
			element.unlock();
		}
	}

	private int countSum() {
		int sum = countRowSum();
		sum += countColumnSum();
		return sum - matrix.getElement(diagonalIndex, diagonalIndex).getValue();
	}

	private int countRowSum() {
		int sum = 0;
		for (int i = 0; i < matrix.length(); i++) {
			sum += matrix.getElement(diagonalIndex, i).getValue();
		}
		return sum;
	}

	private int countColumnSum() {
		int sum = 0;
		for (int i = 0; i < matrix.length(); i++) {
			sum += matrix.getElement(i, diagonalIndex).getValue();
		}
		return sum;
	}

}
