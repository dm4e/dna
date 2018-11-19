package dna.repository.dao;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import dna.repository.model.DnaMutant;


@EnableScan
public interface DnaMutantDao extends CrudRepository<DnaMutant, String> {

	<S extends DnaMutant> Iterable<S> save(Iterable<S> data);
	
	long count();
}