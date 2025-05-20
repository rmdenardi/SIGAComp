/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Pojo;

/**
 *
 * @author root
 */
public class Turma {
    private int id;
    private int ano;
    private String nome;
    private String lider;
    private String conselheiro;
    private int curso_id;

    public int getCurso_id() {
        return curso_id;
    }

    public void setCurso_id(int curso_id) {
        this.curso_id = curso_id;
    }

    public Turma() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public String getConselheiro() {
        return conselheiro;
    }

    public void setConselheiro(String conselheiro) {
        this.conselheiro = conselheiro;
    }
    
    
    
    
}
