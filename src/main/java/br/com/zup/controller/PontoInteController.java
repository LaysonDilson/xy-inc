package br.com.zup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.model.PontoInte;
import br.com.zup.service.PontoInteService;

/**
 * 
 * @author Layson Dilson
 * 
 */
@RestController
@RequestMapping("/rest/pontos-de-interesses")
public class PontoInteController {


	@Autowired private PontoInteService poiService;

	/* 
	 *  Lista os Pontos de Interesse cadastrados
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<PontoInte> listAll() {
		List<PontoInte> ret = poiService.findAll();

		return ret;
	}

	/* 
	 *  Lista os Pontos de Interesse cadastrados pela distancia do ponto de referencia.
	 */
	@RequestMapping(value="/proximidade", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PontoInte> listAllByProximity(@RequestParam(value="pX") Long pX,@RequestParam(value="pY") Long py,@RequestParam(value="distance") Double distance) {
		return (List<PontoInte>) poiService.findCoordByDistance(pX, py, distance);
	}	

	/*
	 * Adiciona uma Lista de Pontos de Interesse
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> addList(@RequestBody List<PontoInte> pois, UriComponentsBuilder ucBuilder) {
		
		for (PontoInte poi : pois) {
			poi = poiService.save(poi);

			if (poi == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/rest/pontos-interesses").build().toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	/*
	 * Edita um Ponto de Interesse
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> edit(@RequestBody PontoInte poi) {
		
		if (poi == null || poi.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else{
			PontoInte poiBd = poiService.findById(poi.getId());
			if (poiBd == null) {
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
		}
		poi = poiService.save(poi);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		PontoInte poi = poiService.findById(id);
		
		if (poi == null) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		poiService.delete(id);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	

}

