/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.Pojo.Turma;
import Model.DAO.DAOTurma;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;

/**
 *
 * @author root
 */
@ManagedBean
public class TurmaBean {

    private String destino;
    DAOTurma dao = new DAOTurma();
    private Turma turma = new Turma();
    private List<Turma> lista;
    private List<SelectItem> listarturmas;
    private List<Turma> turmasencontradas;

    /**
     * Retorna a lista de turmas
     *
     * @return
     */
    public List<Turma> getListarturmas() {
        return dao.consultarTurma();
    }

    public void setListarturmas(List<SelectItem> listarturmas) {
        this.listarturmas = listarturmas;
    }

    public List<Turma> getTurmasencontradas() {
        return turmasencontradas;
    }

    public void setTurmasencontradas(List<Turma> turmasencontradas) {
        this.turmasencontradas = turmasencontradas;
    }
    
    

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public List<Turma> getLista() throws SQLException {
        return dao.retornarTurmas();
    }

    public void setLista(List<Turma> lista) {
        this.lista = lista;
    }

    public void novo() {
        dao.inserir(turma);
        turma = new Turma();
    }

    public String excluir() {
        this.destino = "/publico/ConsultarTurma.jsf";
        dao.excluir(this.turma);
        return destino;
    }

    public String editar() {
        this.destino = "/publico/ConsultarTurma.jsf";
        dao.editar(this.turma);
        turma = new Turma();
        RequestContext requisicao = RequestContext.getCurrentInstance();
        requisicao.addCallbackParam("edicao", true);
        return destino;   
    }

}
