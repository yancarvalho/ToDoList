package br.senai.sp.informatica.todolist.controller;



import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWTSigner;

import br.senai.sp.informatica.todolist.dao.UsuarioDao;
import br.senai.sp.informatica.todolist.model.Usuario;
import sun.security.provider.MD5;

@RestController
public class UsuarioRestController {
	// SECRET pode ser qualquer coisa
	public static final String SECRET = "todolistsenai";
	public static final String ISSUER = "http://www.sp.senai.br";
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@RequestMapping(value="/usuario", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> inserir(@RequestBody Usuario usuario){		
		try {			
			//usuario.setSenha(new Md5PasswordEncoder().encodePassword(usuario.getSenha(), null));			
			usuarioDao.inserir(usuario);
			URI location = new URI("/usuario/" + usuario.getId());
			return ResponseEntity.created(location).body(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/usuario", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Usuario> listar(){
		return usuarioDao.listar();
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> logar(@RequestBody Usuario usuario){
		try {
			usuario = usuarioDao.logar(usuario);
			if (usuario != null) {
				// data de emissão
				long iat = System.currentTimeMillis() / 1000;
				// data de expiração
				long exp = iat + 60;
				// objeto para gerar o token, passando a secret
				JWTSigner signer = new JWTSigner(SECRET);
				HashMap<String, Object> claims = new HashMap<>();
				claims.put("iss", ISSUER);
				claims.put("exp", exp);
				claims.put("iat", iat);
				claims.put("id_usuario", usuario.getId());
				// gera o token	
				String jwt = signer.sign(claims);
				JSONObject token = new JSONObject();
				token.put("token", jwt);
				return ResponseEntity.ok(token.toString());
			}else{
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
