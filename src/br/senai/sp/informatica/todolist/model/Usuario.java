package br.senai.sp.informatica.todolist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
//@JsonIgnoreProperties("senha")
public class Usuario {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(length=40, nullable=false, unique=true)
	private String login;
	@Column(nullable=false)
	private String senha;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		String md5 = encoder.encodePassword(senha, null);
		this.senha = md5;
	}
}
