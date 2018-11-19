package dna.domain.impl;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dna.model.DiagonalDirection;

/**
 * DNA analysis services
 */

public abstract class AbstractDnaAnalyzer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDnaAnalyzer.class);

	protected static final int MUTANT_LINE_MIN_REQUIRED = 2;

	private static final Pattern MUTANT_LINE_PATTERN = Pattern.compile("AAAA|TTTT|CCCC|GGGG");
	
	protected void findColumns(String[] matrix, AtomicInteger mutantLineCount) {
		final String columnLines = obtainColumLines(matrix);
		findMutantSequence(columnLines, mutantLineCount);
		LOGGER.info("search end: Cols");
	}

	protected void findDiagonals(String[] matrix, DiagonalDirection direction, AtomicInteger mutantLineCount) {
		final String columnLines = obtainDiagonalLines(matrix, direction);
		findMutantSequence(columnLines, mutantLineCount);
		LOGGER.info("search end: {}", direction);
	}
	
	/**
	 * determine when dna line has mutant sequences. Stop search when MUTANT_LINE_MIN_REQUIRED is reached.
	 * @param line dna line
	 * @param mutantLineCount result counter
	 */
	protected void findMutantSequence(String line, AtomicInteger mutantLineCount) {
		if(line ==null)		
			return;
		
		final Matcher matcher = MUTANT_LINE_PATTERN.matcher(line);

		while (mutantLineCount.get() < MUTANT_LINE_MIN_REQUIRED && matcher.find() ) {
			final String group = matcher.group();
			LOGGER.debug("find group: {}", group);
			mutantLineCount.incrementAndGet();
		}
	}

	protected void findRows(String[] matrix, AtomicInteger mutantLineCount) {
		for (final String row : matrix) {
			findMutantSequence(row, mutantLineCount);
		}
		LOGGER.info("search end: Rows");
	}
	
	public abstract boolean isMutant(final String[] dnaMatrix);

	protected String obtainColumLines(String[] matrix) {
		final int n = matrix.length;
		final StringBuilder sbColumnLines = new StringBuilder(n*n+n);
		for (int c=0; c<n; c++) {
			for (int r=0; r<n; r++) {
				sbColumnLines.append( matrix[r].charAt(c));
			}
			sbColumnLines.append(',');
		}
		return sbColumnLines.toString();
	}
	
	protected String obtainDiagonalLines(String matrix[], DiagonalDirection direction) {
		final int n = matrix.length;
		final StringBuilder sbColumnLines = new StringBuilder(n * (n + 2) - 1);

		// left part from main diagonal matrix according DiagonalDirection (main diagonal included)
		for (int level = 0; level < n; level++) {
			for (int row = 0, col = level; col >= 0; row++, col--) {
				final int rowDirIndex = DiagonalDirection.TOP2LEFT == direction ? row : n - 1 - row;
				final char value = matrix[rowDirIndex].charAt(col);
				sbColumnLines.append(value);
			}
			sbColumnLines.append(',');
		}

		// right part from main diagonal matrix according DiagonalDirection (main diagonal excluded)
		for (int level = 1; level < n; level++) {
			for (int row = level, col = n - 1; row < n; row++, col--) {
				final int rowDirIndex = DiagonalDirection.TOP2LEFT == direction ? row : n - 1 - row;
				final char value = matrix[rowDirIndex].charAt(col);
				sbColumnLines.append(value);
			}
			sbColumnLines.append(',');
		}

		return sbColumnLines.toString();
	}
}
