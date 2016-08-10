package br.com.zup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.dao.PontoInteDao;
import br.com.zup.model.PontoInte;
/**
 * 
 * @author Layson Dilson
 * 
 */
@Service
public class PontoInteService {

	@Autowired
	private PontoInteDao poiDao;

	/*
	 * Busca todos os Pontos de Interesses cadastrados.
	 */
	public List<PontoInte> findAll(){
		return (List<PontoInte>) poiDao.findPontosInteresses();
	}
	/*
	 * Se o Ponto de Interesse for valido, ele permite o save no banco.
	 */
	public PontoInte save(PontoInte poi){
		return isValid(poi)? poiDao.saveAndFlush(poi):null;
	}

	public PontoInte update(PontoInte poi){

		return isValid(poi)? poiDao.saveAndFlush(poi):null;
	}
	public List<PontoInte> findCoordByDistance(Long x, Long y, Double distance) {
		return poiDao.findCoordByDistance(x, y, distance);
	}
	
	
	public PontoInte findById(Long id) {
		return poiDao.findOne(id);
	}

	public void delete(Long id) {
		poiDao.delete(id);
	}
	
	public void rollback(){
	}

	/*
	 * Verifica se o Ponto de Interesse tem as coordenadas positivas.
	 */
	private static boolean isValid(PontoInte poi) {
		if ((poi.getX() != null && poi.getX() < 0) || (poi.getY() != null && poi.getY() < 0)) {
			return false;
		}
		return true;
	}

}
