/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.DAO.DAOUsuario;
import Model.Pojo.Usuario;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author renandenardi
 */
@ManagedBean
@SessionScoped
public class AutenticacaoBean {
    private Usuario usuario = new Usuario();
    private DAOUsuario dao = new DAOUsuario();

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
     public String verificarUsuario() throws SQLException {
       
         return dao.verificaUsuario(this.usuario);
       
        
    }
    
     public String voltar(){
        return "/publico/Menu.jsf?faces-redirect=true";
     }
    
    public String consultarInformacoes(){
        return dao.consultarInformacoes();
    }
    
    public void encerrarSessao(){
        dao.encerraSessao();
    }
    
}
