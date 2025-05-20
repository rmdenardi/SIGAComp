/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Turma;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class DAOTurma {

    Conexao conecta = new Conexao();
    private PreparedStatement pstmt;
    private ResultSet rs;

    /**
     * Método de inserção de uma nova turma no banco de dados
     *
     * @param turma
     */
    public void inserir(Turma turma) {
        conecta.conectar();
        String sql = "INSERT INTO turma VALUES (DEFAULT,?,?,?,?,?)";

        try {

            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, turma.getAno());
            pstmt.setString(2, turma.getNome());
            pstmt.setString(3, turma.getLider());
            pstmt.setString(4, turma.getConselheiro());
            pstmt.setInt(5, turma.getCurso_id());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrada com sucesso", null);
                contexto.addMessage(null, mensagem);
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível cadastrar a turma. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt: " + ex.getMessage());
        }

        conecta.desconectar();

    }

    /**
     * Método que exclui uma turma do banco de dados
     *
     * @param turma
     */
    public void excluir(Turma turma) {

        conecta.conectar();
        String sql = "DELETE FROM turma WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, turma.getId());
            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluída com sucesso!", null);
                contexto.addMessage(null, mensagem);
            }

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir a turma. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar: " + ex.getMessage());
        }
    }

    /**
     * Método que edita os dados da turma no banco de dados.
     *
     * @param turma
     */
    public void editar(Turma turma) {
        try {
            conecta.conectar();

            String sql = "UPDATE turma SET ano=?, nome=?, lider=?,  conselheiro=? "
                    + "WHERE id=?";

            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setInt(1, turma.getAno());
                pstmt.setString(2, turma.getNome());
                pstmt.setString(3, turma.getLider());
                pstmt.setString(4, turma.getConselheiro());
                pstmt.setInt(5, turma.getId());

                if (pstmt.executeUpdate() > 0) {
                    FacesContext contexto = FacesContext.getCurrentInstance();
                    FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Editada com sucesso!", null);
                    contexto.addMessage(null, mensagem);
                }

            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível atualizar as informações da turma. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar" + ex.getMessage());
        }
    }

    /**
     * Método que retorna uma lista de turmas com todas as suas informações
     *
     * @return
     * @throws SQLException
     */
    public List<Turma> retornarTurmas() throws SQLException {
        conecta.conectar();
        ArrayList<Turma> lista = new ArrayList<>();
        String sql = "SELECT * FROM turma "
                + " ORDER BY nome";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar as turmas. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        if (rs != null) {
            while (rs.next()) {
                Turma turma = new Turma();
                turma.setId(rs.getInt("id"));
                turma.setAno(rs.getInt("ano"));
                turma.setNome(rs.getString("nome"));
                turma.setLider(rs.getString("lider"));
                turma.setConselheiro(rs.getString("conselheiro"));
                turma.setCurso_id(rs.getInt("curso_id"));
                lista.add(turma);

            }

        }
        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou rs: " + ex.getMessage());
        }

        conecta.desconectar();

        return lista;

    }

    /**
     * Retorna uma lista selecionável de turmas presentes no banco de dados
     *
     * @return
     */
    public List<Turma> consultarTurma() {
        ArrayList lista = new ArrayList();

        try {
            conecta.conectar();
            String sql = "SELECT id, nome "
                    + " FROM turma "
                    + " ORDER BY nome";
            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                rs = pstmt.executeQuery();
                if (rs != null) {

                    while (rs.next()) {
                        Turma turma = new Turma();
                        turma.setId(rs.getInt("id"));
                        turma.setNome(rs.getString("nome"));
                        lista.add(turma);

                    }
                }
            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar as turmas. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }

            pstmt.close();
            rs.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt, rs ou desconectar: " + ex.getMessage());

        }

        return lista;

    }

}
