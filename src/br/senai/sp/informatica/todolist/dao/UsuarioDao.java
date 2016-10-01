package br.senai.sp.informatica.todolist.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import br.senai.sp.informatica.todolist.model.Usuario;

@Repository
public class UsuarioDao {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	public void inserir (Usuario usuario){
		manager.persist(usuario);
	}
	
	public List<Usuario> listar(){
		TypedQuery<Usuario> query = manager.createQuery("SELECT u FROM Usuario u", Usuario.class);
		return query.getResultList();
	}
	
	public Usuario logar(Usuario usuario){
		TypedQuery<Usuario> query = manager.createQuery("SELECT u FROM Usuario u WHERE u.login = :login and u.senha = :senha", Usuario.class);
		query.setParameter("login", usuario.getLogin());
		query.setParameter("senha", usuario.getSenha());
		/*
		List<Usuario> lista = query.getResultList();
		
		if (lista.isEmpty()) {
			return null;
		}else{
			return lista.get(0);
		}*/
		
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}
