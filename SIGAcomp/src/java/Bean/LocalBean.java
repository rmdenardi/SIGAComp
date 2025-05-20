/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.DAO.DAOLocal;
import Model.Pojo.Local;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author renandenardi
 */
@RequestScoped
@ManagedBean
public class LocalBean{
    private String destino;
    private Local local = new Local();
    private List<Local> lista;
    private List<Local> listalocaisatendimentos;
    private DAOLocal dao = new DAOLocal();
    private List<Local> locaisencontrados;

    public List<Local> getListalocaisatendimentos() {
        return dao.consultarLocaisAtendimentos();
    }

    public void setListalocaisatendimentos(List<Local> listalocaisatendimentos) {
        this.listalocaisatendimentos = listalocaisatendimentos;
    }
    
    

    public List<Local> getLocaisencontrados() {
        return locaisencontrados;
    }

    public void setLocaisencontrados(List<Local> locaisencontrados) {
        this.locaisencontrados = locaisencontrados;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public List<Local> getLista() {
        return dao.consultar();
    }

    public void setLista(List<Local> lista) {
        this.lista = lista;
    }
    
    
    public void novo() {
        dao.inserir(local);
        local = new Local();
        
     
    }

    public String excluir() {
        this.destino = "/publico/ConsultarLocal.jsf";
        dao.excluir(local);
        return destino;
    }

    public String editar() {
       this.destino = "/publico/ConsultarLocal.jsf";
       dao.editar(local);
       local = new Local();
       RequestContext requisicao = RequestContext.getCurrentInstance();
       requisicao.addCallbackParam("edicao", true);
       return destino;
    }


}
