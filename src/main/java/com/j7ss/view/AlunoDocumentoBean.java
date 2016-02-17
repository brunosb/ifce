/*
 * @version     1.0.0
 * @author      Edivando J. Alves
 * @contact     edivando@j7ss.com ( http://www.j7ss.com )
 * 
 * @copyright  	Copyright 2010 - 2016 J7 Smart Solutions, all rights reserved.
 * 
 */
package com.j7ss.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import lombok.Setter;

import com.github.rjeschke.txtmark.Processor;
import com.j7ss.entity.Aluno;
import com.j7ss.entity.Documento;
import com.j7ss.entity.DocumentoAluno;
import com.j7ss.entity.constraint.DocumentoKey;
import com.j7ss.entity.constraint.DocumentoStatus;
import com.j7ss.entity.constraint.TipoUsuario;
import com.j7ss.util.BasicView;
import com.j7ss.util.DAOException;
import com.j7ss.util.WebContext;

/**
 * 
 * @author Edivando Alves
 * @date  10/02/2016
 * 
 */
@ManagedBean
@ViewScoped
public class AlunoDocumentoBean extends BasicView<Aluno>{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private DocumentoAluno documentoAluno;
	
	@Setter
	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;
	
	private String docPage;
	
	
	public void saveDocumento(DocumentoStatus status){
		try {
			documentoAluno.status(status).save();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDocPage() {
		if(docPage == null){
			id = Integer.parseInt( WebContext.getExternalContext().getRequestParameterMap().get("id") );
			setDocumentoAluno();
			parse();
			docPage = Processor.process(docPage, true);
		}
		return docPage;
	}

	private void parse(){
		docPage = documentoAluno.getDocumento().getHtmlPage();
		if(documentoAluno.getDocumento().getKeys() != null){
			for (String key : documentoAluno.getDocumento().getKeys().split(",")) {
				replace(DocumentoKey.valueOf(key.replaceAll("&", "").replaceAll("`", "")));
			}
		}
	}
	
	private void setDocumentoAluno(){
		documentoAluno = getAlunoFichaMatricula();
		if(documentoAluno == null){
			for (Documento doc : loginBean.getUsuario().getAluno().getCurso().getDocumentos()) {
				if(doc.getIdDocumento().equals(id)){
					documentoAluno = new DocumentoAluno(loginBean.getUsuario().getAluno())
										.documento(doc)
										.ordem(loginBean.getUsuario().getAluno().getDocumentos().size())
										.status(DocumentoStatus.DISPONIVEL);
					try {
						loginBean.getUsuario().getAluno().addDocumento(documentoAluno);
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private DocumentoAluno getAlunoFichaMatricula(){
		if(loginBean.getUsuario().getAluno().getDocumentosAluno().size() != 0){
			for (DocumentoAluno doc : loginBean.getUsuario().getAluno().getDocumentosAluno()) {
				if(doc.getDocumento() != null &&  doc.getDocumento().getIdDocumento().equals(id)){
					return doc;
				}	
			}
		}
		return null;
	}
	
	private void replace(DocumentoKey key){
		switch (key) {
		// Usuario
		case USUARIO_NOME:
			replace(DocumentoKey.USUARIO_NOME, loginBean.getUsuario().getNome());
			break;
		case USUARIO_EMAIL:
			replace(DocumentoKey.USUARIO_EMAIL, loginBean.getUsuario().getEmail());
			break;
			
		// Aluno
		case ALUNO_DESCRICAO:
			replace(DocumentoKey.ALUNO_DESCRICAO, loginBean.getUsuario().getAluno().getDescricao());
			break;
		case ALUNO_MATRICULA:	
			replace(DocumentoKey.ALUNO_MATRICULA, loginBean.getUsuario().getAluno().getMatricula());
			break;
		case ALUNO_TELEFONE:
			replace(DocumentoKey.ALUNO_TELEFONE, loginBean.getUsuario().getAluno().getTelefone());
			break;
		case ALUNO_CELULAR:	
			replace(DocumentoKey.ALUNO_CELULAR, loginBean.getUsuario().getAluno().getCelular());
			break;
		case ALUNO_CV_LATTES:
			replace(DocumentoKey.ALUNO_CV_LATTES, loginBean.getUsuario().getAluno().getCvLattes());
			break;
		case ALUNO_CPF:
			replace(DocumentoKey.ALUNO_CPF, loginBean.getUsuario().getAluno().getCpf());
			break;
		case ALUNO_RG:
			replace(DocumentoKey.ALUNO_RG, loginBean.getUsuario().getAluno().getRg());
			break;
		case ALUNO_DATA_NASCIMENTO:
			replace(DocumentoKey.ALUNO_DATA_NASCIMENTO, loginBean.getUsuario().getAluno().getDataNascimento());
			break;
		case ALUNO_ENDERECO_ENDERECO:
			replace(DocumentoKey.ALUNO_ENDERECO_ENDERECO, loginBean.getUsuario().getAluno().getEndereco());
			break;
		case ALUNO_ENDERECO_NUMERO:
			replace(DocumentoKey.ALUNO_ENDERECO_NUMERO, loginBean.getUsuario().getAluno().getNumero());
			break;
		case ALUNO_ENDERECO_BAIRRO:
			replace(DocumentoKey.ALUNO_ENDERECO_BAIRRO, loginBean.getUsuario().getAluno().getBairro());
			break;
		case ALUNO_ENDERECO_CEP:
			replace(DocumentoKey.ALUNO_ENDERECO_CEP, loginBean.getUsuario().getAluno().getCep());
			break;
		case ALUNO_ENDERECO_CIDADE:
			replace(DocumentoKey.ALUNO_ENDERECO_CIDADE, loginBean.getUsuario().getAluno().getCidade());
			break;
		case ALUNO_ENDERECO_UF:
			replace(DocumentoKey.ALUNO_ENDERECO_UF, loginBean.getUsuario().getAluno().getUf());
			break;
		
		// Empresa
		case EMPRESA_NOME:
			replace(DocumentoKey.EMPRESA_NOME, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getNome());
			break;
		case EMPRESA_EMAIL:
			replace(DocumentoKey.EMPRESA_EMAIL, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getEmail());
			break;
		case EMPRESA_TELEFONE:
			replace(DocumentoKey.EMPRESA_TELEFONE, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getTelefone());
			break;
		case EMPRESA_FAX:
			replace(DocumentoKey.EMPRESA_FAX, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getFax());
			break;
		case EMPRESA_SUPERVISOR:
			// TODO: Empresa Supervisor
//			replace(DocumentoKey.EMPRESA_SUPERVISOR, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa());
			break;
		case EMPRESA_CARGO_SUPERVISOR:
			// TODO: Empresa Supervisor
//			replace(DocumentoKey.EMPRESA_CARGO_SUPERVISOR, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa());
			break;
		case EMPRESA_TELEFONE_SUPERVISOR:
			// TODO: Empresa Supervisor
//			replace(DocumentoKey.EMPRESA_TELEFONE_SUPERVISOR, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa());
			break;
		case EMPRESA_CNPJ:
			replace(DocumentoKey.EMPRESA_CNPJ, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getCnpj());
			break;
		case EMPRESA_SITE:
			replace(DocumentoKey.EMPRESA_SITE, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getSite());
			break;
		case EMPRESA_RAMO_ATIVIDADE:
			replace(DocumentoKey.EMPRESA_RAMO_ATIVIDADE, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getRamoAtividade());
			break;
		case EMPRESA_ENDERECO_ENDERECO:
			replace(DocumentoKey.EMPRESA_ENDERECO_ENDERECO, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getEndereco());
			break;
		case EMPRESA_ENDERECO_NUMERO:
			replace(DocumentoKey.EMPRESA_ENDERECO_NUMERO, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getNumero());
			break;
		case EMPRESA_ENDERECO_BAIRRO:
			replace(DocumentoKey.EMPRESA_ENDERECO_BAIRRO, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getBairro());
			break;
		case EMPRESA_ENDERECO_CEP:
			replace(DocumentoKey.EMPRESA_ENDERECO_CEP, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getCep());
			break;
		case EMPRESA_ENDERECO_UF:
			replace(DocumentoKey.EMPRESA_ENDERECO_UF, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getUf());
			break;
		case EMPRESA_ENDERECO_CIDADE:
			replace(DocumentoKey.EMPRESA_ENDERECO_CIDADE, loginBean.getUsuario().getAluno().getVagaEstagio().getEmpresa().getCidade());
			break;
			
		// Instituicao
		case INSTITUICAO_NOME:
			replace(DocumentoKey.INSTITUICAO_NOME, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getInstituicao().getNome());
			break;
			
		// Campus
		case CAMPUS_NOME:
			replace(DocumentoKey.CAMPUS_NOME, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getNome());
			break;
		case CAMPUS_EMAIL:
			replace(DocumentoKey.CAMPUS_EMAIL, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getEmail());
			break;
		case CAMPUS_TELEFONE:
			replace(DocumentoKey.CAMPUS_TELEFONE, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getTelefone());
			break;
		case CAMPUS_ENDERECO_ENDERECO:
			replace(DocumentoKey.CAMPUS_ENDERECO_ENDERECO, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getEndereco());
			break;
		case CAMPUS_ENDERECO_NUMERO:
			replace(DocumentoKey.CAMPUS_ENDERECO_NUMERO, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getNumero());
			break;
		case CAMPUS_ENDERECO_BAIRRO:
			replace(DocumentoKey.CAMPUS_ENDERECO_BAIRRO, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getBairro());
			break;
		case CAMPUS_ENDERECO_CEP:
			replace(DocumentoKey.CAMPUS_ENDERECO_CEP, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getCep());
			break;
		case CAMPUS_ENDERECO_UF:
			replace(DocumentoKey.CAMPUS_ENDERECO_UF, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getUf());
			break;
		case CAMPUS_ENDERECO_CIDADE:
			replace(DocumentoKey.CAMPUS_ENDERECO_CIDADE, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getCampus().getCidade());
			break;
	
		// Departamento
		case DEPARTAMENTO_NOME:
			replace(DocumentoKey.DEPARTAMENTO_NOME, loginBean.getUsuario().getAluno().getCurso().getDepartamento().getNome());
			break;
			
		// Curso
		case ALUNO_CURSO_SEMESTRE_ATUAL:
			replace(DocumentoKey.ALUNO_CURSO_SEMESTRE_ATUAL, loginBean.getUsuario().getAluno().getSemestreAtual().toString());
			break;
		case ALUNO_CURSO_NOME:
			replace(DocumentoKey.ALUNO_CURSO_NOME, loginBean.getUsuario().getAluno().getCurso().getNome());
			break;
		}
	}
	
	private void replace(DocumentoKey key, Object value){
		if(value != null){
			docPage = docPage.replace(key.getKey(), value.toString());
			
		}
	}

	
	
}
