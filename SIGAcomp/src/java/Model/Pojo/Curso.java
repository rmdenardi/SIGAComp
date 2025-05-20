/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Pojo;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author root
 */
public class Curso {
    private int id;
    private String nome;
    private String coordenador_curso;
    private String coordenador_eixo;
    

    public Curso() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCoordenador_curso() {
        return coordenador_curso;
    }

    public void setCoordenador_curso(String coordenador_curso) {
        this.coordenador_curso = coordenador_curso;
    }

    public String getCoordenador_eixo() {
        return coordenador_eixo;
    }

    public void setCoordenador_eixo(String coordenador_eixo) {
        this.coordenador_eixo = coordenador_eixo;
    }

  
    
    
    
}
