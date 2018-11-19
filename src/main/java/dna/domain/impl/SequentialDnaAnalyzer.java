package dna.domain.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import dna.model.DiagonalDirection;

/**
 * Sequential implementation of DNA analyzer
 */
@Service
public class SequentialDnaAnalyzer extends AbstractDnaAnalyzer {

	@Override
	public boolean isMutant(final String[] dnaMatrix) {
		final AtomicInteger mutantLineCount = new AtomicInteger(0);
		findRows(dnaMatrix, mutantLineCount);
		findColumns(dnaMatrix, mutantLineCount);
		findDiagonals(dnaMatrix, DiagonalDirection.BOTTOM2LEFT, mutantLineCount);
		findDiagonals(dnaMatrix, DiagonalDirection.TOP2LEFT, mutantLineCount);
		
		return mutantLineCount.get() >= MUTANT_LINE_MIN_REQUIRED;
	}
}
