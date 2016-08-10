package br.com.zup;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import br.com.zup.dao.PontoInteDao;
import br.com.zup.model.PontoInte;
import br.com.zup.service.PontoInteService;
import junit.framework.Assert;

/**
 * 
 * @author Layson Dilson
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = XYIncApplication.class)
@WebAppConfiguration
public class XYIncApplicationTests {


	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired  private PontoInteService poiService;
	@Autowired  private PontoInteDao poiDao;
	@Autowired  private WebApplicationContext webApplicationContext;
	private String url = "/rest/pontos-de-interesses";


	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void listAll() throws Exception {
		int qtd = poiDao.findAll().size();
		this.mockMvc.perform(get(url))
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$", hasSize(qtd)));		
	}
	
	@Test
	public void listByProximity() throws Exception {
		String x = "20",y="10",distance="10";
		
		int qtd = (poiDao.findCoordByDistance(20L, 10L, 10.0)).size();

		this.mockMvc.perform(get(url+"/proximidade").param("pX", x).param("pY", y).param("distance", distance))
		
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$", hasSize(qtd)));		
	}
	

	@Test
	public void addPontoInteresse() throws Exception {

		List<PontoInte> poiList = new ArrayList<PontoInte>();
		poiList.add(new PontoInte("Lanchonete", 27L,12L));

		String poiJson = toJson(poiList);
		this.mockMvc.perform(post(url)
				.contentType(contentType)
				.content(poiJson))
		.andExpect(status().isCreated());		
	}
	@Test
	public void addPontoInteWithInvalidValues() throws Exception {
		List<PontoInte> poiList = new ArrayList<PontoInte>();
		poiList.add(new PontoInte("Lanchonetes", -25L,-30L));

		String poiJson = toJson(poiList);
		this.mockMvc.perform(post(url)
				.contentType(contentType)
				.content(poiJson))
		.andExpect(status().isBadRequest());	
	}
	@Test
	public void addListPontoInteresse() throws Exception {

		List<PontoInte> poiList = new ArrayList<PontoInte>();
		poiList.add(new PontoInte("Posto", 31L,18L));
		poiList.add(new PontoInte("Joalheria", 15L,12L));
		poiList.add(new PontoInte("Floricultura", 19L,21L));
		poiList.add(new PontoInte("Pub", 12L,8L));
		poiList.add(new PontoInte("Supermercado", 23L,6L));
		poiList.add(new PontoInte("Churrascaria", 28L,2L));


		String poiJson = toJson(poiList);
		this.mockMvc.perform(post(url)
				.contentType(contentType)
				.content(poiJson))
		.andExpect(status().isCreated());		
	}


	protected String toJson(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write( o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
