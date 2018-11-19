package dna.server.controller;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dna.domain.DnaService;
import dna.domain.DnaStats;
import dna.model.DnaRequestDTO;
import dna.model.DnaStatsDTO;

@RestController
public class MutantController {
	
    @Autowired
    private DnaService snaService;
    
    @Autowired
    private DnaStats dnaStats;

    @RequestMapping(value="mutant", method = POST)
    public ResponseEntity<Void> determineMutant(@RequestBody DnaRequestDTO request) {
    	
    	final String[] dna = request.getDna();
    	
		final HttpStatus httpStatus = this.snaService.isMutant(dna) ? OK: FORBIDDEN;
		return ResponseEntity.status(httpStatus).body(null);
    }
    
	@RequestMapping(value="stats", method = GET)
    public DnaStatsDTO getStats() {
    	return dnaStats.getStats();
    }
}
