/*
 * @version     1.0.0
 * @author      Edivando J. Alves
 * @contact     edivando@j7ss.com ( http://www.j7ss.com )
 * 
 * @copyright  	Copyright 2010 - 2016 J7 Smart Solutions, all rights reserved.
 * 
 */
package com.j7ss.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Getter;
import lombok.Setter;

import com.j7ss.entity.Aluno;
import com.j7ss.entity.Campus;
import com.j7ss.entity.Curso;
import com.j7ss.entity.Departamento;
import com.j7ss.entity.Instituicao;
import com.j7ss.entity.TipoUsuario;
import com.j7ss.entity.VagaEstagio;
import com.j7ss.util.MD5;
import com.j7ss.util.Messages;
import com.j7ss.util.WebContext;

/**
 * 
 * @author Edivando Alves
 * @date  10/02/2016
 * 
 */
@ManagedBean
@ViewScoped
public class AlunoCadastroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Setter
	private Aluno aluno;
	
	List<Instituicao> instituicaos = new ArrayList<>();
	
	@Getter @Setter
	private Instituicao instituicao = new Instituicao();
	@Getter @Setter
	private Campus campus = new Campus();
	@Getter @Setter
	private Departamento departamento = new Departamento();
	
	public void save(){
		try {
			aluno.getUsuario().senha(MD5.md5(aluno.getUsuario().getSenha())).ativo(true).tipoUsuario(TipoUsuario.ALUNO).save();
			aluno.save();
			aluno.vagaEstagio(new VagaEstagio("Minha Vaga").save());
			
//			List<Documento> docs = aluno.getCurso().getDocumentos();
//			for (int i = 0; i < docs.size(); i++) {
//				aluno.addDocumento( new DocumentoAluno(aluno)
//						.documento(docs.get(i))
//						.ordem(i)
//						.status((i == 0 || !docs.get(i).getObrigatorio()) ? DocumentoStatus.DISPONIVEL : DocumentoStatus.INDISPONIVEL));
//			}
			
			instituicaos = new ArrayList<>();
			instituicao = new Instituicao();
			campus = new Campus();
			departamento = new Departamento();
			WebContext.redirect("login.html");
			Messages.showGrowlInfo("Test", "Test");
		} catch (Exception e) {
			
			Messages.showGrowlInfo("Test", "Test");
		}
	}
	
	public Aluno getAluno() {
		return aluno == null ? aluno = new Aluno() : aluno;
	}
	
	public List<Instituicao> searchInstituicao(String nome){
		return instituicaos = Instituicao.findByNomeLike(nome);
	}
	
	public List<Campus> searchCampus(String nome){
		return Campus.findByNomeLike(instituicao, nome);
	}
	
	public List<Departamento> searchDepartamentos(String nome){
		return Departamento.findByNomeLike(campus, nome);
	}
	
	public List<Curso> searchCursos(String nome){
		return Curso.findByNomeLike(departamento, nome);
	}

	
	//########
	// Converts
	public Instituicao getInstituicaoByNome(String nome){
		for (Instituicao instituicao : instituicaos) {
			if(instituicao.getNome().equals(nome)) return instituicao;
		}
		return null;
	}
	
	public Campus getCampusByNome(String nome){
		if(instituicao != null){
			for (Campus campus : instituicao.getCampus()) {
				if(campus.getNome().equals(nome)) return campus;
			}
		}
		return null;
	}
	
	public Departamento getDepartamentoByNome(String nome){
		if(campus != null){
			for (Departamento departamento : campus.getDepartamentos()) {
				if(departamento.getNome().equals(nome)) return departamento;
			}
		}
		return null;
	}
	
	public Curso getCursoByNome(String nome){
		if(departamento != null){
			for (Curso curso : departamento.getCursos()) {
				if(curso.getNome().equals(nome)) return curso;
			}
		}
		return null;
	}

}