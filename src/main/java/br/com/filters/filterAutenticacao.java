package br.com.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;


@WebFilter(urlPatterns = {"/*"})
public class filterAutenticacao implements Filter {
    
	@Inject
	private JPAUtil jpaUtil;
	
    public filterAutenticacao() {
       
    }

	public void destroy() {
		
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		Pessoa usuarioLogado = (Pessoa) session.getAttribute("usuarioLogado");
		
		String url = req.getServletPath();
		
		if(url.equalsIgnoreCase("/cadvisitante.jsf")) {
			chain.doFilter(request, response);
		}
		else if(!url.equalsIgnoreCase("/index.jsf") && usuarioLogado == null ){
			request.getRequestDispatcher("index.jsf").forward(req, response);
			return;
		}
		
		else {
		chain.doFilter(request, response);
		}
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
		jpaUtil.getEntityManager();
	}

}
