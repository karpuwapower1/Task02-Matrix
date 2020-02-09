package by.training.karpilovich.task02.runner;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.training.karpilovich.task02.entity.Element;
import by.training.karpilovich.task02.entity.Matrix;
import by.training.karpilovich.task02.service.Service;

public class Main {
	private static final Logger LOGGER = LogManager.getLogger();
	private static int count = 1;

	public static void main(String[] args) throws InterruptedException {

		Element[][] elements = new Element[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				elements[i][j] = new Element(0);
			}
		}

		Matrix matrix = Matrix.getInstance();
		matrix.setMatrix(elements);
		change();
		change();
		change();
		change();
		change();
		change();
		change();
		change();
		change();
	}

	public static void change() {
		Map<Integer, Future<Integer>> map = new HashMap<>();
		CyclicBarrier barrier = Matrix.getInstance().getBarrier();
		Future<Integer> future;
		ExecutorService executor = Executors.newFixedThreadPool(Matrix.getInstance().length());
		for (int i = 0; i < Matrix.getInstance().length(); i++) {
			future = executor.submit(new Service(barrier, count));
			map.put(count++, future);
		}
		executor.shutdown();
		try {
		executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException " + e);
		}

		for (Element row[] : Matrix.getInstance().getMatrix()) {
			String str = "";
			for (Element el : row) {
				str = str + el.getValue() + "  ";
			}
			LOGGER.debug(str);
		}

		try {
			for (Entry<Integer, Future<Integer>> entry : map.entrySet()) {
				LOGGER.debug(entry.getKey().toString() + " " + entry.getValue().get().toString());
			}
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException " + e);
		} catch (ExecutionException e) {
			LOGGER.error("ExecutionException " + e);
		}
	}

}
