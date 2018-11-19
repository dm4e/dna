package dna.domain;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import dna.domain.impl.ParallelDnaAnalyzer;
import dna.domain.impl.SequentialDnaAnalyzer;
import dna.model.InvalidDataException;

@Test
public class DnaServiceTest {

	@InjectMocks
	private DnaService target;
	
	@Mock
	private ParallelDnaAnalyzer dnaParallelAnalyzer;
	
	@Mock
	private SequentialDnaAnalyzer dnaSequentialAnalyzer;
	
	@Mock
	private DnaStats dnaStats;
	
	@BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
	}
	
	@BeforeMethod
    public void reset() {
        Mockito.reset(this.dnaStats);
        Mockito.reset(this.dnaParallelAnalyzer);
        Mockito.reset(this.dnaSequentialAnalyzer);
        ReflectionTestUtils.setField(target, "minMatrixParallelStategy", 3);
	}
	
	public void isMutant_seq_ok() {
		Mockito.when(this.dnaSequentialAnalyzer.isMutant(Mockito.any())).thenReturn(true);
		ReflectionTestUtils.setField(target, "minMatrixParallelStategy", 30);
		final String[] matrix = new String[]{"TTTT","TTTT","TTTT","TTTT"};
		final boolean result = target.isMutant(matrix);
		Assert.assertTrue(result);
		Mockito.verify(this.dnaSequentialAnalyzer).isMutant(Mockito.eq(matrix));
		Mockito.verify(this.dnaStats).addDna(Mockito.eq(matrix), Mockito.eq(true));
		Mockito.verifyNoMoreInteractions(this.dnaParallelAnalyzer, dnaSequentialAnalyzer, this.dnaStats);
	}
	
	public void isMutant_seq_false() {
		Mockito.when(this.dnaSequentialAnalyzer.isMutant(Mockito.any())).thenReturn(false);
		ReflectionTestUtils.setField(target, "minMatrixParallelStategy", 30);
		final String[] matrix = new String[]{"TTTT","TTTT","TTTT","TTTT"};
		final boolean result = target.isMutant(matrix);
		Assert.assertFalse(result);
		Mockito.verify(this.dnaSequentialAnalyzer).isMutant(Mockito.eq(matrix));
		Mockito.verify(this.dnaStats).addDna(Mockito.eq(matrix), Mockito.eq(false));
		Mockito.verifyNoMoreInteractions(this.dnaParallelAnalyzer, dnaSequentialAnalyzer, this.dnaStats);
	}
	
	public void isMutant_pal_ok() {
		Mockito.when(this.dnaParallelAnalyzer.isMutant(Mockito.any())).thenReturn(true);
		ReflectionTestUtils.setField(target, "minMatrixParallelStategy", 2);
		final String[] matrix = new String[]{"TTTT","TTTT","TTTT","TTTT"};
		final boolean result = target.isMutant(matrix);
		Assert.assertTrue(result);
		Mockito.verify(this.dnaParallelAnalyzer).isMutant(Mockito.eq(matrix));
		Mockito.verify(this.dnaStats).addDna(Mockito.eq(matrix), Mockito.eq(true));
		Mockito.verifyNoMoreInteractions(this.dnaParallelAnalyzer, dnaSequentialAnalyzer, this.dnaStats);
	}
	

	@Test(expectedExceptions=InvalidDataException.class, expectedExceptionsMessageRegExp="dna lines should have length of 3")
	public void isMutant_badFormat_emptyValue() {
		final String[] matrix = new String[]{"",
											 "456",
											 "789"};
		target.isMutant(matrix);
	}
	
	@Test(expectedExceptions=InvalidDataException.class, expectedExceptionsMessageRegExp="dna lines should have length of 3")
	public void isMutant_badFormat_nullValue() {
		final String[] matrix = new String[]{null,
											 "456",
											 "789"};
		target.isMutant(matrix);
	}
	
	@Test(expectedExceptions=InvalidDataException.class, expectedExceptionsMessageRegExp="dna data must be not null")
	public void isMutant_null() {
		target.isMutant(null);
	}
}
