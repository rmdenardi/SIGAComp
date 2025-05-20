/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.Pojo.Setor;
import Model.DAO.DAOSetor;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author root
 */
@ManagedBean

public class SetorBean {

    private String destino;
    private Setor setor = new Setor();
    private List<Setor> lista;
    private List<SelectItem> listaSetor;
    DAOSetor dao = new DAOSetor();
    private List<Setor> setoresencontrados;

    public List<Setor> getSetoresencontrados() {
        return setoresencontrados;
    }

    public void setSetoresencontrados(List<Setor> setoresencontrados) {
        this.setoresencontrados = setoresencontrados;
    }
    
    
    public List<Setor> getListaSetor() {
        return dao.retornaSetor();
    }

    public void setListaSetor(List<SelectItem> listaSetor) {
        this.listaSetor = listaSetor;
    }
    

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public List<Setor> getLista() throws SQLException {
        return dao.consultaSetores();
    }

    public void setLista(List<Setor> lista) {
        this.lista = lista;
    }

    public void novo() {
        dao.inserir(setor);
        setor = new Setor();
 
    }
    
    public String excluir() {
        this.destino = "/publico/ConsultarSetor.jsf";
        dao.excluir(this.setor);
        return destino;
    }

    public String editar() {
        this.destino = "/publico/ConsultarSetor.jsf";
        dao.editar(setor);
        setor = new Setor();
        RequestContext requisicao = RequestContext.getCurrentInstance();
        requisicao.addCallbackParam("edicao", true);
        return destino;

    }
   
}
