package dna.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import dna.model.DnaStatsDTO;
import dna.repository.dao.DnaHumanDao;
import dna.repository.dao.DnaMutantDao;
import dna.repository.model.DnaHuman;
import dna.repository.model.DnaMutant;

/**
 * Provides statistics DNA result analysis services
 */
@Service
public class DnaStats {

	private static final Logger LOGGER = LoggerFactory.getLogger(DnaStats.class);

	@Autowired
	private DnaMutantDao dnaMutantDao;
	@Autowired
	private DnaHumanDao dnaHumanDao;
	
    public void addDna(String[] dnaMatrix, boolean isMutant) {
    	final String dnaSequence = Joiner.on(',').join(dnaMatrix);
		if (isMutant) {
    		dnaMutantDao.save(DnaMutant.create(dnaSequence));
    	} else {
    		dnaHumanDao.save(DnaHuman.create(dnaSequence));
    	}
	}

	public DnaStatsDTO getStats() {
		final long mutant = dnaMutantDao.count();
		final long human = dnaHumanDao.count();
		final Double ratio = computeRatio(mutant, human);
		
		LOGGER.info("mutant:{}, human:{}, ratio:{}", mutant, human, ratio);
		return new DnaStatsDTO(mutant, human, ratio);
	}

	/**
	 * Determine mutant ratio as #mutant / #human
	 * @param mutant count of mutant
	 * @param human count of human
	 * @return mutant ratio or null when is undefined.
	 */
	protected Double computeRatio(final long mutant, final long human) {
		return human!=0  ? (double) mutant / human : null;
	}
}
