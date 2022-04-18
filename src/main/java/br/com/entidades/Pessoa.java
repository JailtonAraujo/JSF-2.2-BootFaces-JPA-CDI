package br.com.entidades;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("deprecation")
@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Size(min = 1, max = 100, message = "O nome deve ter mais que 10 caracteres!")
	private String nome;

	@NotEmpty(message = "O sobrenome é obrigatorio!")
	@NotNull(message = "O sobrenome não pode ser nulo")
	private String sobrenome;
	
	private String cpf;

	@DecimalMin(value = "10", message = "a Idade deve ser superior a 10 anoss")
	private int idade;

	private String sexo;

	private String[] frameworks;

	private Boolean ativo;

	private String login;

	private String senha;

	private String perfilUser;

	private String nivelProgramacao;

	private Integer[] linguagensDeProgramacao;

	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
	private Endereco endereco;

	@Transient /* Não fica persistente / não grava no banco */
	private Estados estados;

	
	@ManyToOne
	@JoinColumn(name = "cidades_id")
	@ForeignKey(name = "fk_pessoa_cidade")
	private Cidades cidades;

	@Column(columnDefinition = "text") /* Tipo text grava arquivos em base 64 */
	private String fotoIconBase64;

	private String extensao;

	@Lob /* gravar arquivos no banco de dados */
	@Basic(fetch = FetchType.LAZY)
	private byte[] fotoIconBaseOriginal;

	public Pessoa() {}
	
	

	public Pessoa(long id, String nome, String cpf, int idade, String perfilUser, String logradouro) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.idade = idade;
		this.perfilUser = perfilUser;
		this.endereco = new Endereco();
		this.endereco.setLogradouro(logradouro);
	}

	public Pessoa(long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	

	public Pessoa(long id, String nome, String sobrenome, String cpf, int idade, String sexo, String[] frameworks,
			Boolean ativo, String login, String senha, String perfilUser, String nivelProgramacao,
			Integer[] linguagensDeProgramacao, Date dataNascimento,Cidades cidades,
			String fotoIconBase64, String extensao, String cep, String uf, String localidade, String logradouro, String complemento) {
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.cpf = cpf;
		this.idade = idade;
		this.sexo = sexo;
		this.frameworks = frameworks;
		this.ativo = ativo;
		this.login = login;
		this.senha = senha;
		this.perfilUser = perfilUser;
		this.nivelProgramacao = nivelProgramacao;
		this.linguagensDeProgramacao = linguagensDeProgramacao;
		this.dataNascimento = dataNascimento;
		this.cidades = cidades;
		this.fotoIconBase64 = fotoIconBase64;
		this.extensao = extensao;
		this.endereco = new Endereco(cep, uf, localidade, logradouro, complemento);
		
	}



	public String getFotoIconBase64() {
		return fotoIconBase64;
	}

	public void setFotoIconBase64(String fotoIconBase64) {
		this.fotoIconBase64 = fotoIconBase64;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public byte[] getFotoIconBaseOriginal() {
		return fotoIconBaseOriginal;
	}

	public void setFotoIconBaseOriginal(byte[] fotoIconBaseOriginal) {
		this.fotoIconBaseOriginal = fotoIconBaseOriginal;
	}

	public Cidades getCidades() {
		return cidades;
	}

	public void setCidades(Cidades cidades) {
		this.cidades = cidades;
	}

	public Estados getEstados() {
		return estados;
	}

	public void setEstados(Estados estados) {
		this.estados = estados;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getNivelProgramacao() {
		return nivelProgramacao;
	}

	public void setNivelProgramacao(String nivelProgramacao) {
		this.nivelProgramacao = nivelProgramacao;
	}

	public String getPerfilUser() {
		return perfilUser;
	}

	public void setPerfilUser(String perfilUser) {
		this.perfilUser = perfilUser;
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
		this.senha = senha;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String[] getFrameworks() {
		return frameworks;
	}

	public void setFrameworks(String[] frameworks) {
		this.frameworks = frameworks;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Integer[] getLinguagensDeProgramacao() {
		return linguagensDeProgramacao;
	}

	public void setLinguagensDeProgramacao(Integer[] linguagensDeProgramacao) {
		this.linguagensDeProgramacao = linguagensDeProgramacao;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getCpf() {
		return cpf;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", idade=" + idade + ", sexo="
				+ sexo + ", frameworks=" + Arrays.toString(frameworks) + ", ativo=" + ativo + ", login=" + login
				+ ", senha=" + senha + ", perfilUser=" + perfilUser + ", nivelProgramacao=" + nivelProgramacao
				+ ", linguagensDeProgramacao=" + Arrays.toString(linguagensDeProgramacao) + ", dataNascimento="
				+ dataNascimento + ", endereco=" + endereco + ", estados=" + estados + ", cidades=" + cidades
				+ ", fotoIconBase64=" + fotoIconBase64 + ", extensao=" + extensao + ", fotoIconBaseOriginal="
				+ Arrays.toString(fotoIconBaseOriginal) + "]";
	}
	
	

}
