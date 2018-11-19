package dna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dna.domain.impl.AbstractDnaAnalyzer;
import dna.domain.impl.ParallelDnaAnalyzer;
import dna.domain.impl.SequentialDnaAnalyzer;
import dna.model.InvalidDataException;

/**
 * DNA analysis services
 */

@Service
public  class DnaService {
	
	@Autowired
	private ParallelDnaAnalyzer parallelDnaAnalyzer;
	
	@Autowired
	private SequentialDnaAnalyzer sequentialDnaAnalyzer;
	
	@Autowired
	private DnaStats dnaStats;
	
	@Value("${parallel_stategy.min_size:10}")
	private int minMatrixParallelStategy;
	 
	public boolean isMutant(final String[] dnaMatrix) {
		checkValidDna(dnaMatrix);
		final AbstractDnaAnalyzer dnaService = dnaMatrix.length >= minMatrixParallelStategy ? parallelDnaAnalyzer : sequentialDnaAnalyzer;
		final boolean isMutant = dnaService.isMutant(dnaMatrix);
		dnaStats.addDna(dnaMatrix, isMutant);
		return isMutant;
	}
	
	
	/**
	 * Check valid DNA matrix, that is not null and if matrix array length is n, each string length should be n, that is a matrix of n x n char values.
	 * WHen invalid raise InvalidDataException
	 * @param dnaMatrix
	 */
	private void checkValidDna(String[] dnaMatrix) {
		if(dnaMatrix==null) {
			throw new InvalidDataException("dna data must be not null");
		} 
		final int n =  dnaMatrix.length;
		for(final String s: dnaMatrix) {
			if(s==null || s.length()!=n) {
				throw new InvalidDataException("dna lines should have length of "+ n);
			}
		}
	}

}
