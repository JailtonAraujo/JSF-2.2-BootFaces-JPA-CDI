package br.com.projetojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Endereco;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;
import net.bootsfaces.component.selectOneMenu.SelectOneMenu;

@javax.faces.view.ViewScoped
@Named(value = "pessoaBean")
public class PessoaBean implements Serializable{

	private static final long serialVersionUID = 5272232793894598835L;
	
	private Pessoa pessoa = new Pessoa();
	private List<Pessoa> listaDePessoas = new ArrayList<Pessoa>();
	private Endereco endereco = new Endereco();
	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	
	private String nomeBusca;
	private List<Pessoa> usuariosModal = new ArrayList<Pessoa>();
	
	private Part arquivoFot;

	@Inject
	private DaoGeneric<Pessoa> daoGeneric;
	
	@Inject
	private IDaoPessoa iDaoPessoa;

	public String salvar() throws IOException {
		
		if(arquivoFot.getInputStream() != null) {
		
		byte[] imagemByte = getByte(arquivoFot.getInputStream());
		
		pessoa.setFotoIconBaseOriginal(imagemByte);/*Salva imagem original*/
		
		/*Transformar em BufferedImage*/
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
		
		/*Pergar o tipo da imagem*/
		int type = bufferedImage.getType() == 0? bufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		int largura = 200;
		int altura = 200;
		
		/*Criando miniatura*/
		BufferedImage resizeImage = new BufferedImage(largura, altura, type);
		Graphics2D graphics2d = resizeImage.createGraphics();
		graphics2d.drawImage(bufferedImage, 0, 0, largura, altura, null);
		graphics2d.dispose();
		
		/*Escrever novamente a imagem em tamanho menor*/
		
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		String extencao = arquivoFot.getContentType().split("\\/")[1];
		ImageIO.write(resizeImage, extencao, arrayOutputStream);
		
		String imgMiniatura = "data:" + arquivoFot.getContentType() + ";base64," + 
							  DatatypeConverter.printBase64Binary(arrayOutputStream.toByteArray());
		
		/*Processar imagem*/
		pessoa.setFotoIconBase64(imgMiniatura);
		pessoa.setExtensao(extencao);
		}
		
		if(pessoa.getPerfilUser() == null || pessoa.getPerfilUser().trim().isEmpty()) {
			pessoa.setPerfilUser("VISITANTE");
		}
		endereco.setPessoa(pessoa);
		pessoa.setEndereco(endereco);
		pessoa = daoGeneric.merge(pessoa);
		carregarListaDePessoas();
		mostrarMsg("Salvo com Sucesso!");
		return "";
	}

	public String novo() {
		pessoa = new Pessoa();
		endereco = new Endereco();
		return "";
	}

	public String limpar() {
		pessoa = new Pessoa();
		endereco = new Endereco();
		return "";
	}

	public String deletar() {
		try {
			daoGeneric.deletePorID(pessoa);
			carregarListaDePessoas();
			mostrarMsg("Usuario Excluido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("O usuario não pode ser excluido, pois o mesmo está associados a lançamentos!");
		}
		return "";
	}

	@PostConstruct
	public void carregarListaDePessoas() {
		listaDePessoas = daoGeneric.getListEntityLimit10(Pessoa.class);

	}

	public String logar() {

		Pessoa usuarioLogado = iDaoPessoa.consultaLoginEspecifico(pessoa.getLogin(), pessoa.getSenha());

		if (usuarioLogado != null && usuarioLogado.getId() > 0) {// Achou usuario

			// Adicionar o usuario na sessão usuarioLogado

			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", usuarioLogado);

			return "primeira.jsf";
		}else {
			FacesContext.getCurrentInstance().addMessage("msg", new FacesMessage("Login ou senha incorretos!"));
		}

		return "index.jsf";
	}

	public String logout() {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("usuarioLogado");

		HttpServletRequest request = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();
		request.getSession().invalidate();

		return "index.jsf";
	}

	public boolean retricaoAcesso(String perfil) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa usuarioLogado = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return usuarioLogado.getPerfilUser().equalsIgnoreCase(perfil);
	}

	public void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			
			URL url = new URL("https://viacep.com.br/ws/" + endereco.getCep() + "/json/");
			URLConnection connection = url.openConnection();

			InputStream stream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = reader.readLine()) != null) {
				jsonCep.append(cep);
			}

			Endereco gson = new Gson().fromJson(jsonCep.toString(), Endereco.class);

			endereco.setLocalidade(gson.getLocalidade());
			endereco.setComplemento(gson.getComplemento());
			endereco.setUf(gson.getUf());
			endereco.setLogradouro(gson.getLogradouro());

		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao buscar Cep!");
		}
	}

	public void carregarCidades(AjaxBehaviorEvent event) {

		Estados estado = (Estados) ((SelectOneMenu) event.getSource()).getValue();

		if (estado != null) {

			cidades = iDaoPessoa.listarCidades(estado.getId().toString());

		}
	}
	
	public void editar() {
		if(pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstados(estado);
			
			cidades = iDaoPessoa.listarCidades(estado.getId().toString());
			
		}
	}
	
	public void download() throws IOException{
		Map<String, String> parans = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		String fileDownloadId = parans.get("fileDownloadId");
		
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownloadId);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download."+pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBaseOriginal().length);
		response.getOutputStream().write(pessoa.getFotoIconBaseOriginal());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
		
		
	}
	
	public void BuscarUsuarioModal() {
		usuariosModal = iDaoPessoa.consultarUsuarioModal(nomeBusca);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getListaDePessoas() {
		return listaDePessoas;
	}

	public void setListaDePessoas(List<Pessoa> listaDePessoas) {
		this.listaDePessoas = listaDePessoas;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listarEstados();
		return estados;
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
	
	public Part getArquivoFot() {
		return arquivoFot;
	}
	
	public void setArquivoFot(Part arquivoFot) {
		this.arquivoFot = arquivoFot;
	}
	
	/*Converte inputStream para array de bytes*/
	private byte [] getByte(InputStream stream) throws IOException {
		
		int leng;/*Variavel de controle*/
		
		int size=1024; /*Tamanho padrão*/
		
		byte[] buffer = null; /*Fluxo*/
		
		if(stream instanceof ByteArrayInputStream){
			size = stream.available();
			buffer = new byte[size];
			leng = stream.read(buffer, 0, size);
		}else {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			buffer = new byte[size];
			
			while((leng = stream.read(buffer, 0, size)) != -1 ) {
				outputStream.write(buffer, 0, leng);
			}
			
			buffer = outputStream.toByteArray();
		}
		return buffer;
		
	}

	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
	}
	
	public List<Pessoa> getUsuariosModal() {
		return usuariosModal;
	}

	public void setUsuariosModal(List<Pessoa> usuariosModal) {
		this.usuariosModal = usuariosModal;
	}
	
}
