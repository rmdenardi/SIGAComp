/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Aluno;
import Model.Pojo.Turma;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
@ManagedBean
public class DAOAluno {

    Conexao conecta = new Conexao();
    private PreparedStatement pstmt;
    private ResultSet rs;

    /**
     * Método de inserção do aluno no banco de dados
     *
     * @param aluno
     */
    public void inserir(Aluno aluno) {
        conecta.conectar();
        String sql = "INSERT INTO aluno VALUES (DEFAULT,?,?,?,?,?,?,?)";


        //Instancia para o formato de data desejado
        SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");

        //formatando data recebida do formulário
       String dataformatada= data.format(aluno.getData());
        Date datanasc = Date.valueOf(dataformatada);
        try {

            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, aluno.getNome());
            pstmt.setDate(2, datanasc);
            pstmt.setString(3, aluno.getTelefone());
            pstmt.setString(4, aluno.getEmail());
            pstmt.setString(5, aluno.getNaturalidade());
            pstmt.setString(6, aluno.getEndereco());
            pstmt.setInt(7, aluno.getTurma_id());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso!", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível cadastrar o aluno. Continue usando o sistema normalmente! ", "Cadastrado");
            FacesMessage erro = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro: " + ex.getMessage() ,null);
            contexto.addMessage(null, mensagem);
            contexto.addMessage(null, erro);
        }

        try {
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }

        conecta.desconectar();

    }

    /**
     * Método que edita os dados do aluno no banco de dados
     *
     * @param aluno
     */
    public void editar(Aluno aluno) {
        conecta.conectar();

        String sql = "UPDATE aluno SET nome=?, telefone=?, email=?, "
                + " endereco=?, naturalidade=?, turma_id=?, data_nasc=? "
                + "WHERE id=?";
  
        //Instancia para o formato de data desejado
        SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
        Date datanasc = Date.valueOf(data.format(aluno.getData()));

        //formatando data recebida do formulário

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getTelefone());
            pstmt.setString(3, aluno.getEmail());
            pstmt.setString(4, aluno.getEndereco());
            pstmt.setString(5, aluno.getNaturalidade());
            pstmt.setInt(6, aluno.getTurma_id());
            pstmt.setDate(7, datanasc);
            pstmt.setInt(8, aluno.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado com sucesso!", null));
            }

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível atualizar as informações do aluno. Continue usando o sistema normalmente! " + ex.getMessage(), "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        conecta.desconectar();
    }

    /**
     * Método que exclui o aluno do banco de dados
     *
     * @param aluno
     */
    public void excluir(Aluno aluno) {
        conecta.conectar();
        String sql = "DELETE FROM aluno WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, aluno.getId());
            if (pstmt.executeUpdate() > 0) {

                FacesContext contexto = FacesContext.getCurrentInstance();
                contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluído com sucesso!", null));
            }
        } catch (SQLException ex) {

            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir o aluno. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(DAOUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que retorna uma lista de alunos com suas informações.
     *
     * @return
     * @throws SQLException
     */
    public List<Aluno> retornaAluno() throws SQLException {
        conecta.conectar();
        ArrayList<Aluno> lista = new ArrayList<>();
        String sql = "SELECT aluno.id as id_aluno,"
                + " aluno.nome as nome, turma_id, telefone,"
                + " email, endereco, naturalidade , data_nasc,"
                + " turma.id as id_turma, turma.nome as nome_turma, lider, conselheiro,"
                + " ano, curso_id"
                + " FROM aluno"
                + " INNER JOIN turma"
                + " ON aluno.turma_id = turma.id"
                + " ORDER BY nome";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(DAOAluno.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        if (rs != null) {
            while (rs.next()) {

                Aluno aluno = new Aluno();
                aluno.setId(rs.getInt("id_aluno"));
                aluno.setNome(rs.getString("nome"));
                aluno.setData(rs.getDate("data_nasc"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setEmail(rs.getString("email"));
                aluno.setNaturalidade(rs.getString("naturalidade"));
                aluno.setEndereco(rs.getString("endereco"));
                aluno.setTurma_id(rs.getInt("turma_id"));

                Turma turma = new Turma();
                turma.setId(rs.getInt("id_turma"));
                turma.setNome(rs.getString("nome_turma"));
                turma.setAno(rs.getInt("ano"));
                turma.setLider(rs.getString("lider"));
                turma.setConselheiro(rs.getString("conselheiro"));
                turma.setCurso_id(rs.getInt("curso_id"));
                aluno.setTurma(turma);
                lista.add(aluno);

            }

        }
        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os alunos. Continue usando o sistema normalmente! ", "Cadastrado");
            FacesMessage erro = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro: " + ex.getMessage(), null);
            contexto.addMessage(null, mensagem);
            contexto.addMessage(null, erro);
            
        }

        conecta.desconectar();

        return lista;

    }

    public List<Aluno> retornarAluno(String nome) throws SQLException {
        conecta.conectar();
        ArrayList<Aluno> lista = new ArrayList<>();
        String sql = "SELECT * FROM aluno "
                + "WHERE nome =? "
                + " ORDER BY nome";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, nome);
            rs = pstmt.executeQuery();

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os alunos. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        if (rs != null) {
            while (rs.next()) {
                Aluno aluno = new Aluno();

                aluno.setId(rs.getInt("id"));
                aluno.setNome(rs.getString("nome"));
                aluno.setData(rs.getDate("data_nasc"));
                aluno.setTelefone(rs.getString("telefone"));
                aluno.setEmail((rs.getString("email")));
                aluno.setNaturalidade(rs.getString("naturalidade"));
                aluno.setEndereco((rs.getString("endereco")));
                aluno.setTurma_id(rs.getInt("turma_id"));
                lista.add(aluno);

            }

        }
        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os alunos. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        conecta.desconectar();

        return lista;

    }

    public String retornaNomeAluno(int id) {
        String nome = null;

        String sql = "SELECT nome "
                + " FROM aluno "
                + " WHERE id=?";

        try {
            conecta.conectar();

            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    nome = rs.getString("nome");

                }

            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível recuperar o nome do aluno. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }
        conecta.desconectar();

        return nome;

    }

}
