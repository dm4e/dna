package dna.server.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import dna.domain.DnaService;
import dna.domain.DnaStats;
import dna.model.DnaRequestDTO;
import dna.model.DnaStatsDTO;
import dna.server.config.WebConfiguration;

@Test
public class MutantControllerTest {

	private static final ObjectMapper JSON_MAPPER = WebConfiguration.createJsonMapperSnakeCase();

	private MockMvc mockMvc;

    @InjectMocks
    @Spy
    private MutantController controller;

    @Mock
    private DnaStats dnaStats;
    
    @Mock
    private DnaService dnaService;

    @BeforeTest
    public void initTest() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(JSON_MAPPER))
                .build();
    }

    @BeforeMethod
    public void setUp() {
        Mockito.reset(this.controller, this.dnaStats, this.dnaService);
    }

    public void isMutant_dnaMutant_200() throws Exception {
        final DnaRequestDTO request = new DnaRequestDTO();
        request.setDna(new String[] {"12", "34"});

        Mockito.when(dnaService.isMutant(Mockito.anyObject())).thenReturn(true);

        this.mockMvc.perform(post("/mutant/").content(JSON_MAPPER.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200));

        Mockito.verify(this.controller).determineMutant(Mockito.any(DnaRequestDTO.class));
        Mockito.verify(this.dnaService).isMutant(Mockito.eq(request.getDna()));
        Mockito.verifyNoMoreInteractions(this.controller, this.dnaService, this.dnaStats);
    }
    
    public void isMutant_dnaHuman_403() throws Exception {
    	final DnaRequestDTO request = new DnaRequestDTO();
        request.setDna(new String[] {"12", "34"});
        Mockito.when(dnaService.isMutant(Mockito.anyObject())).thenReturn(false);

        this.mockMvc.perform(post("/mutant/").content(JSON_MAPPER.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(403));

        Mockito.verify(this.controller).determineMutant(Mockito.any(DnaRequestDTO.class));
        Mockito.verify(this.dnaService).isMutant(Mockito.eq(request.getDna()));
        Mockito.verifyNoMoreInteractions(this.controller, this.dnaService, this.dnaStats);
    }
    
    public void getStats() throws Exception {
        final DnaStatsDTO result = new DnaStatsDTO(10, 20, 0.5d);
		Mockito.when(dnaStats.getStats()).thenReturn(result);

        this.mockMvc.perform(get("/stats/"))
        .andExpect(status().is(200)).andExpect(content().json("{'count_mutant_dna':10, 'count_human_dna':20, 'ratio':0.5}"));

        Mockito.verify(this.controller).getStats();
        Mockito.verify(this.dnaStats).getStats();
        Mockito.verifyNoMoreInteractions(this.controller, this.dnaService, this.dnaStats);
    }
}
