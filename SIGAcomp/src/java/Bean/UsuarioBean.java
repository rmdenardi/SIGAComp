/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.Pojo.Usuario;
import Model.DAO.DAOUsuario;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.commons.mail.EmailException;
import org.primefaces.context.RequestContext;


/**
 *
 * @author root
 */
@RequestScoped
@ManagedBean
public class UsuarioBean {

    public UsuarioBean() {
    }

    private String destino;
    private Usuario usuario = new Usuario();
    private List<Usuario> lista;
    private List<Usuario> servidoresencontrados;
    DAOUsuario dao = new DAOUsuario();
    
    public List<Usuario> getServidoresencontrados() {
        return servidoresencontrados;
    }

    public void setServidoresencontrados(List<Usuario> servidoresencontrados) {
        this.servidoresencontrados = servidoresencontrados;
    }
    
    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getLista() throws SQLException {
        return dao.retornaUsuario();
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }

    /**
     * Método de inserção de novo usuário
     *
     * 
     * @throws org.apache.commons.mail.EmailException
     */
    public void novo() throws EmailException {
         dao.inserir(usuario);
         usuario = new Usuario();
         
    }

    /**
     * Método que verifica a existência do usuário no banco de dados
     *
     * @return 
     * @throws SQLException
     */
    public String verificarUsuario() throws SQLException {
       
        return dao.verificaUsuario(this.usuario);
        
    }
    
    /**
     * Método que redireciona o usuário à página de recuperação de senha
     * 
     * @return retorna a página a ser redirecionada
     */
    public String navegarRecuperacaoSenha(){
        this.destino = "RecuperarSenha.jsf?faces-redirect=true";
        return destino;
    }
    
    /**
     * Metodo que redireciona o usuário à pagina de login
     * @return 
     */
    
    public String navegarLogin(){
        this.destino = "/publico/Login.jsf?faces-redirect=true";
        return destino;
    }
    
    /**
     * Método que recupera a senha do usuário 
     * @throws EmailException 
     */
    public void recuperarSenha() throws EmailException{
        dao.recuperarSenha(usuario);
        usuario = new Usuario();
      
        
    }
    
    /**
     * Método que consulta as informações do usuário logado
     * @return 
     */
    public String consultarInformacoes(){
        return dao.consultarInformacoes();
    }
    
    /**
     * Método que encerra a sessão do usuário
     * @return redireciona ao login
     */
    public String encerrarSessao(){
        dao.encerraSessao(); 
        this.destino = "/publico/Login.jsf?faces-redirect=true";
        return destino;
    }

    /**
     * Método que exclui o usuário do banco de dados
     *
     * @return
     */
    public String excluir() {
        this.destino = "/publico/ConsultarServidor.jsf";
        dao.excluir(this.usuario);
        return this.destino;
    }
    
    /**
     * Método que atualiza as informações do usuário no banco de dados
     * @return 
     */

    public String editar() {
        this.destino = "/publico/ConsultarServidor.jsf";
        dao.editar(usuario);
        usuario = new Usuario();
        RequestContext requisicao = RequestContext.getCurrentInstance();
        requisicao.addCallbackParam("edicao", true);
        return this.destino;
    }


}
