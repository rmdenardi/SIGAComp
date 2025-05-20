/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Bean.AutenticacaoBean;
import Model.Pojo.Usuario;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author renandenardi
 */
public class Autenticacao implements PhaseListener {
    private Usuario usuario;

    /**
     * código que será executado após o processamento da fase;
     *
     * @param event
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext contextoapp = event.getFacesContext(); //capturando o contexto da aplicaçao jsf

        String paginaatual = contextoapp.getViewRoot().getViewId(); //captura de página atual
        boolean ehpaginaautenticacao = paginaatual.contains("Login.xhtml");
        boolean ehpaginarecuperarsenha = paginaatual.contains("RecuperarSenha.xhtml");
        
            ExternalContext contextoexterno = contextoapp.getExternalContext();            
            Map<String, Object> mapa = contextoexterno.getSessionMap();
            AutenticacaoBean autenticacaoBean =  (AutenticacaoBean) mapa.get("autenticacaoBean");

        // teste que verifica se a página interceptada é a página de login
        usuario = autenticacaoBean.getUsuario();
        if (!ehpaginaautenticacao && !ehpaginarecuperarsenha) {
          
            
            if (usuario.getNivel() == 0) {

                Application aplicacao = contextoapp.getApplication();
                NavigationHandler navegar = aplicacao.getNavigationHandler();
                navegar.handleNavigation(contextoapp, null, "/publico/Login.jsf?faces-redirect=true");

            } else {

                if (usuario.getNivel() == 2 && paginaatual.contains("/adm/")) {

                     FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Você não tem permissão para acessar a página solicitada.", null);
                     contextoapp.addMessage(null, mensagem);
                     contextoapp.getExternalContext().getFlash().setKeepMessages(true); //adicionando objetos de mensagens em escopo de flash
     
                     Application aplicacao = contextoapp.getApplication();
                     NavigationHandler navegar = aplicacao.getNavigationHandler();
                     navegar.handleNavigation(contextoapp, null, "/publico/Menu.jsf?faces-redirect=true");

                }
            }

        }else{
            
            if((ehpaginaautenticacao || ehpaginarecuperarsenha) && (usuario.getNivel() != 0)){
                  
                   FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN," Você já entrou como " + SessionContext.getInstancia().getAttribute("nome") , null);
                   FacesMessage mensagemdica = new FacesMessage(FacesMessage.SEVERITY_WARN, "Faça login novamente para entrar como outro usuário", null);
                    
                   contextoapp.addMessage(null, mensagem);
                   contextoapp.addMessage(null, mensagemdica);
                   contextoapp.getExternalContext().getFlash().setKeepMessages(true); //adicionando objetos de mensagens em escopo de flash
     
                   Application aplicacao = FacesContext.getCurrentInstance().getApplication();
                   NavigationHandler navegar = aplicacao.getNavigationHandler();
                   navegar.handleNavigation(contextoapp, null, "/publico/Menu.jsf?faces-redirect=true");
                    
                   
                 
                                        
                   
            }
        }
    }

    /**
     * código que será executado antes do processamento da fase;
     *
     * @param pe
     */
    @Override
    public void beforePhase(PhaseEvent pe) {
    }

    /**
     * retorna um Enum com o nome da fase atual.
     *
     * @return
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
