/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.DAO.DAOSituacao;
import Model.Pojo.Situacao;
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
public class SituacaoBean {
    private List<Situacao> situacoes;
    private DAOSituacao dao = new DAOSituacao();
    private String destino;
    private Situacao situacao = new Situacao();
    private List<Situacao> situacoesencontradas;

    public List<Situacao> getSituacoesencontradas() {
        return situacoesencontradas;
    }

    public void setSituacoesencontradas(List<Situacao> situacoesencontradas) {
        this.situacoesencontradas = situacoesencontradas;
    }
    
    

    public List<Situacao> getSituacoes() {
        return dao.consultar();
    }

    public void setSituacoes(List<Situacao> situacoes) {
        this.situacoes = situacoes;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
    
    public void novo(){
        dao.inserir(situacao);
        situacao = new Situacao();
       
    }
    
    public String excluir(){
       this.destino = "/publico/ConsultarSituacao.jsf";
       dao.excluir(situacao);
       return destino;
 
    }
    
    public String editar(){
        this.destino = "/publico/ConsultarSituacao.jsf";
        dao.editar(situacao); 
        situacao = new Situacao();
        RequestContext requisicao = RequestContext.getCurrentInstance();
        requisicao.addCallbackParam("edicao", true);
        return destino;
    }
    
}
