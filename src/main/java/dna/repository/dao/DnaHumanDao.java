package dna.repository.dao;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import dna.repository.model.DnaHuman;


@EnableScan
public interface DnaHumanDao extends CrudRepository<DnaHuman, String> {

	<S extends DnaHuman> Iterable<S> save(Iterable<S> data);
	
	long count();
}