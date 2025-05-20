/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Curso;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DAOCurso {

    Conexao conecta = new Conexao();
    private PreparedStatement pstmt;
    private ResultSet rs;

    public void inserir(Curso curso) {

        conecta.conectar();
        String sql = "INSERT INTO curso VALUES (DEFAULT,?,?,?)";

        try {

            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, curso.getNome());
            pstmt.setString(2, curso.getCoordenador_curso());
            pstmt.setString(3, curso.getCoordenador_eixo());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso", null);
                contexto.addMessage(null, mensagem);

            }

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível cadastrar o curso. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar: " + ex.getMessage());
        }

    }

    /* Método que exclui um curso do banco de dados
   * @param setor 
     */
    public void excluir(Curso curso) {

        conecta.conectar();
        String sql = "DELETE FROM curso WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, curso.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluído com sucesso!", "Excluído"));
            }

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir o curso. Continue usando o sistema normalmente! ", "Cadastrado");
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
     * Método que edita os dados do curso no banco de dados.
     *
     * @param curso
     *
     */
    public void editar(Curso curso) {
        try {
            conecta.conectar();

            String sql = "UPDATE curso SET nome=?, coordenador_curso=?, coordenador_eixo=? "
                    + "WHERE id=?";

            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setString(1, curso.getNome());
                pstmt.setString(2, curso.getCoordenador_curso());
                pstmt.setString(3, curso.getCoordenador_eixo());
                pstmt.setInt(4, curso.getId());

                if (pstmt.executeUpdate() > 0) {
                    FacesContext contexto = FacesContext.getCurrentInstance();
                    contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado com sucesso!", "Editado"));

                }

            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível atualizar as informações do curso. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "erro de pstmt ou desconectar: " + ex.getMessage());
        }
    }

    /**
     * Método que retorna uma lista de cursos com todas as suas informações
     *
     * @return
     * @throws SQLException
     */
    public List<Curso> retornarCursos() throws SQLException {
        conecta.conectar();
        ArrayList<Curso> lista = new ArrayList<>();
        String sql = "SELECT * FROM curso ORDER BY nome";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(DAOAluno.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        if (rs != null) {
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                curso.setCoordenador_curso(rs.getString("coordenador_curso"));
                curso.setCoordenador_eixo(rs.getString("coordenador_eixo"));

                lista.add(curso);

            }

        }
        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os cursos. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        conecta.desconectar();

        return lista;

    }

    /**
     * Retorna uma lista selecionável de cursos presentes no banco de dados
     *
     * @return
     */
    public List<Curso> consultarCurso() {
        ArrayList lista = new ArrayList();
        conecta.conectar();
        String sql = "SELECT id, nome "
                + "FROM curso "
                + "ORDER BY nome";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs != null) {

                while (rs.next()) {
                    Curso curso = new Curso();
                    curso.setId(rs.getInt("id"));
                    curso.setNome(rs.getString("nome"));
                    lista.add(curso);

                }
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os cursos. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        conecta.desconectar();
        return lista;
    }

}
