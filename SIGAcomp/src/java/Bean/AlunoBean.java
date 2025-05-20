/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.Pojo.Aluno;
import Model.DAO.DAOAluno;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author root
 */
@RequestScoped
@ManagedBean

public class AlunoBean{

    private String destino;
    private boolean fechar;
    private Aluno aluno = new Aluno();
    private List<Aluno> lista;
    private List<Aluno> selecionaraluno;
    private List<Aluno> pesquisaraluno;
    private List<Aluno> alunosencontrados;
    DAOAluno dao = new DAOAluno();

    public List<Aluno> getAlunosencontrados() {
        return alunosencontrados;
    }

    public void setAlunosencontrados(List<Aluno> alunosencontrados) {
        this.alunosencontrados = alunosencontrados;
    }

    public AlunoBean() {
        fechar = false;
    }

    public boolean isFechar() {
        return fechar;
    }

    public void setFechar(boolean fechar) {
        this.fechar = fechar;
    }

    public List<Aluno> getPesquisaraluno() throws SQLException {
        return dao.retornaAluno();
    }

    public void setPesquisaraluno(List<Aluno> pesquisaraluno) {
        this.pesquisaraluno = pesquisaraluno;
    }

    public void setSelecionaraluno(List<Aluno> selecionaraluno) {
        this.selecionaraluno = selecionaraluno;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public List<Aluno> getLista() throws SQLException {
        return dao.retornaAluno();
    }

    public void setLista(List<Aluno> lista) {
        this.lista = lista;
    }

    public DAOAluno getDao() {
        return dao;
    }

    public void setDao(DAOAluno dao) {
        this.dao = dao;
    }

    public void novo() {
        dao.inserir(this.aluno);
        aluno = new Aluno();
    }

    public String excluir() {
        this.destino = "/publico/ConsultarAluno.jsf";
        dao.excluir(this.aluno);
        return this.destino;
    }

    public String editar() {
        this.destino = "/publico/ConsultarAluno.jsf";
        dao.editar(this.aluno);
        RequestContext requisicao = RequestContext.getCurrentInstance();
        requisicao.addCallbackParam("edicao", true);
        aluno = new Aluno();
        return this.destino;

    }


}
