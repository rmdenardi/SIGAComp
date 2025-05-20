/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Pojo;

import java.util.Date;

/**
 *
 * @author root
 */
public class Atendimento {

    public Atendimento() {
    }

    private int id;
    private String tipo;
    private int local_id;
    private String status;
    private Date data;
    private int aluno_id;
    private String recurso;
    private String observacao;
    private int servidor_id;
    private int setor_id;
    private int situacao_id;
    
    private Usuario servidor;
    private Aluno aluno;
    private Setor setor;
    private Situacao situacao;
    private Local local;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local) {
        this.local_id = local;
    }

    public int getSituacao_id() {
        return situacao_id;
    }

    public void setSituacao_id(int situacao_id) {
        this.situacao_id = situacao_id;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
    
    
    
    public Usuario getServidor() {
        return servidor;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public void setServidor(Usuario servidor) {
        this.servidor = servidor;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public int getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(int aluno_id) {
        this.aluno_id = aluno_id;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getServidor_id() {
        return servidor_id;
    }

    public void setServidor_id(int servidor_id) {
        this.servidor_id = servidor_id;
    }

    public int getSetor_id() {
        return setor_id;
    }

    public void setSetor_id(int setor_id) {
        this.setor_id = setor_id;
    }

}
