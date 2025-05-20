/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Pojo;

import Model.DAO.DAOUsuario;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author root
 */
public class Usuario {

    public Usuario() {
    }

    private int id;
    private String cargo;
    private String nome;
    private String telefone;
    private String email;
    private int nivel;
    private String login;
    private String senha;
    private String confirmarsenha;
    private int setor_id;

    DAOUsuario listaUser = new DAOUsuario();

    public String getConfirmarsenha() {
        return confirmarsenha;
    }

    public void setConfirmarsenha(String confirmarsenha) {
        this.confirmarsenha = confirmarsenha;
    }
   
    
    
    public List<Usuario> getLista() throws SQLException {

        return listaUser.retornaUsuario();
    }

    public int getSetor_id() {
        return setor_id;
    }

    public void setSetor_id(int setor_id) {
        this.setor_id = setor_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
