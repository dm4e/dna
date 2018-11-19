package dna.domain.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import dna.model.DiagonalDirection;

/**
 * Concurrent implementation of DNA analyzer
 */
@Service
public class ParallelDnaAnalyzer extends AbstractDnaAnalyzer {

	private ExecutorService taskExecutor =  Executors.newCachedThreadPool();
	 
	@Override
	public boolean isMutant(final String[] dnaMatrix) {
		
		final AtomicInteger mutantLineCount = new AtomicInteger(0);
		final List<Future<?>> tasks = new ArrayList<>(4);
		tasks.add(this.taskExecutor.submit(() -> {findRows(dnaMatrix, mutantLineCount);}));
		tasks.add(this.taskExecutor.submit(() -> {findColumns(dnaMatrix, mutantLineCount);}));
		tasks.add(this.taskExecutor.submit(() -> {findDiagonals(dnaMatrix, DiagonalDirection.BOTTOM2LEFT, mutantLineCount);}));
		tasks.add(this.taskExecutor.submit(() -> {findDiagonals(dnaMatrix, DiagonalDirection.TOP2LEFT, mutantLineCount);}));
		
		while(mutantLineCount.get() < MUTANT_LINE_MIN_REQUIRED && tasks.stream().filter(t-> !t.isDone()).findFirst().isPresent()) {
			Thread.yield();
		}
		tasks.stream().filter(t-> !t.isDone()).forEach(c -> c.cancel(true));

		return mutantLineCount.get() >= MUTANT_LINE_MIN_REQUIRED;
	}
	
}
