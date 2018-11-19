package dna.server.config;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableWebMvc
@ComponentScan({ "dna.domain", "dna.repository", "dna.server.controller" })
public class WebConfiguration extends WebMvcConfigurerAdapter {

	public static ObjectMapper createJsonMapperSnakeCase() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setConfig(mapper.getDeserializationConfig()
				.without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.with(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY));
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return mapper;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		final ObjectMapper mapper = createJsonMapperSnakeCase();
		final MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(mapper);
		converters.add(jsonConverter);
		converters.add(new StringHttpMessageConverter());
	}
}
