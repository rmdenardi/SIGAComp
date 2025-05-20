/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
/**
 *
 * @author renandenardi
 */
public class SessionContext {

    private static SessionContext instancia;

    /**
     * Método que cria e retorna sessão para manter informações de navegação do
     * usuário
     *
     * @return
     */
    public static SessionContext getInstancia() {

        if(instancia == null){
            instancia = new SessionContext();

          
        }
        
        
          return instancia;
       
    }
    

   
    /*
    Método que retorna um contexto externo para trabalhar com recursos usdos pelo jsf
     */
    private ExternalContext currentExternalContext() {
        if (FacesContext.getCurrentInstance() == null) {
            throw new RuntimeException("O FacesContext não pode ser chamado fora de uma requisição HTTP");
        } else {
            return FacesContext.getCurrentInstance().getExternalContext();
        }
    }

    /**
     * Método que recupera informações importantes do usuário na sessão atual
     *
     * @param nome
     * @return
     */
    public Object getAttribute(String nome) {
        return currentExternalContext().getSessionMap().get(nome);
    }

    /**
     * Método que adiciona informações importantes na sessão
     *
     * @param nome
     * @param valor
     */
    public void setAttribute(String nome, Object valor) {
        currentExternalContext().getSessionMap().put(nome, valor);
    }

    /**
     * Método que encerra a sessão do usuário
     */
    public void encerraSessao() {
        currentExternalContext().invalidateSession();
    }


}
