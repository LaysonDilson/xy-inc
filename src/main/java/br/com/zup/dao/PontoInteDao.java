package br.com.zup.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import br.com.zup.model.PontoInte;
/**
 * 
 * @author Layson Dilson
 * 
 */
@RepositoryRestResource
@Transactional
public interface PontoInteDao extends JpaRepository<PontoInte, Long>{

	
	
	@Query("SELECT p FROM PontoInte p WHERE p.nome = :nome")
	List<PontoInte> findPontoInteByName(@Param("nome") Long nome);
	
	@Query("SELECT p FROM PontoInte p")
	List<PontoInte> findPontosInteresses();
	
	@Query("SELECT p FROM PontoInte p WHERE round(sqrt( pow((p.x - :pX), 2) + pow((p.y - :pY),2)), 2) <= :distance")
	List<PontoInte> findCoordByDistance(@Param("pX") Long x, @Param("pY") Long y, @Param("distance") Double distance);
	
}
