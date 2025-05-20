/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.Pojo.Curso;
import Model.DAO.DAOCurso;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author root
 */
@ManagedBean
public class CursoBean {

    private Curso curso = new Curso();
    private String destino;
    private List<Curso> lista;
    private List<SelectItem> listarcursos;
    private DAOCurso dao = new DAOCurso();
    private List<Curso> cursosencontrados;

    public DAOCurso getDao() {
        return dao;
    }

    public void setDao(DAOCurso dao) {
        this.dao = dao;
    }

    public List<Curso> getCursosencontrados() {
        return cursosencontrados;
    }

    public void setCursosencontrados(List<Curso> cursosencontrados) {
        this.cursosencontrados = cursosencontrados;
    }
    
    

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Curso> getLista() throws SQLException {
        return dao.retornarCursos();
    }

    public void setLista(List<Curso> lista) {
        this.lista = lista;
    }

    public List<Curso> getListarcursos() {
        return dao.consultarCurso();
    }

    public void setListarcursos(List<SelectItem> listarcursos) {
        this.listarcursos = listarcursos;
    }


    public void novo() {
        dao.inserir(this.curso);
        curso = new Curso();

    }

    public String excluir() {
        this.destino = "/publico/ConsultarCurso.jsf";
        dao.excluir(this.curso);
        return this.destino;
    }

    public String editar() {
        this.destino = "/publico/ConsultarCurso.jsf";
        dao.editar(this.curso);
        curso = new Curso();
        RequestContext contexto = RequestContext.getCurrentInstance();
        contexto.addCallbackParam("edicao", true);
        return this.destino;
    }

}
