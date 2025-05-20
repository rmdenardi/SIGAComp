/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Setor;
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
import javax.faces.model.SelectItem;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
@ManagedBean
public class DAOSetor {

    Conexao conecta = new Conexao();
    private PreparedStatement pstmt;
    private ResultSet rs;

    /**
     * Método que insere um setor no banco de dados
     *
     * @param setor
     */
    public void inserir(Setor setor) {

        conecta.conectar();
        String sql = "INSERT INTO setor VALUES (DEFAULT,?)";

        try {

            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, setor.getNome());

            if (pstmt.executeUpdate() > 0) {

                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso!", null);
                contexto.addMessage(null, mensagem);
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível cadastrar o setor. Continue usando o sistema normalmente! ", "Cadastrado");
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
     * Método que exclui um setor do banco de dados
     *
     * @param setor
     */
    public void excluir(Setor setor) {

        conecta.conectar();
        String sql = "DELETE FROM setor WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, setor.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluído com sucesso!", null);
                contexto.addMessage(null, mensagem);
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir o setor. Continue usando o sistema normalmente! ", "Cadastrado");
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
     * Método que edita os dados do setor no banco de dados.
     *
     * @param setor
     */
    public void editar(Setor setor) {
        try {
            conecta.conectar();

            String sql = "UPDATE setor SET nome=? "
                    + "WHERE id=?";

            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setString(1, setor.getNome());
                pstmt.setInt(2, setor.getId());

                if (pstmt.executeUpdate() > 0) {
                    FacesContext contexto = FacesContext.getCurrentInstance();
                    FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado com sucesso!", null);
                    contexto.addMessage(null, mensagem);
                }

            } catch (SQLException ex) {

                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível atualizar as informações do setor. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }

            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar: " + ex.getMessage());
        }
    }

    /**
     * Método que retorna lista de setores cadastrados.
     *
     * @return
     */
    public List<SelectItem> consultar() {
        ArrayList lista = new ArrayList();

        try {
            conecta.conectar();
            String sql = "SELECT id FROM setor";
            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                rs = pstmt.executeQuery();
                if (rs != null) {

                    while (rs.next()) {
                        lista.add(new SelectItem(rs.getString("id")));

                    }
                }
            } catch (SQLException ex) {

                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os setores. Continue usando o sistema normalmente! ", "Cadastrado");
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

    /**
     * Método que retorna uma lista de setores com todas as suas informações
     *
     * @return
     * @throws SQLException
     */
    public List<Setor> consultaSetores() throws SQLException {
        conecta.conectar();
        ArrayList<Setor> lista = new ArrayList<>();
        String sql = "SELECT id, nome"
                + "      FROM setor ORDER BY nome";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os setores. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        if (rs != null) {
            while (rs.next()) {
                Setor setor = new Setor();
                setor.setId(rs.getInt("id"));
                setor.setNome(rs.getString("nome"));

                lista.add(setor);

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
     * Método que retorna os setores cadastrados no banco de dados
     *
     * @return
     */
    public List<Setor> retornaSetor() {
        ArrayList lista = new ArrayList();

        try {
            conecta.conectar();
            String sql = "SELECT id, nome "
                    + "FROM setor "
                    + "ORDER BY nome";
            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                rs = pstmt.executeQuery();
                if (rs != null) {

                    while (rs.next()) {
                        Setor setor = new Setor();
                        setor.setId(rs.getInt("id"));
                        setor.setNome(rs.getString("nome"));
                        lista.add(setor);

                    }
                }
            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os setores. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }
            rs.close();
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt, rs ou desconectar: " + ex.getMessage());
        }
        return lista;

    }

    /**
     * Método que retorna o nome do setor
     *
     * @param id
     * @return nome do setor
     */
    public String retornaSetorServidor(int id) {
        String nome = null;
        try {
            conecta.conectar();

            String sql = "SELECT nome "
                    + "FROM setor "
                    + "WHERE id=?";
            try {

                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();

                if (rs != null) {
                    while (rs.next()) {
                        nome = rs.getString("nome");
                    }

                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao retornar o nome do setor do servidor logado: " + ex.getMessage());
            }
            pstmt.close();
            rs.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt, rs ou desconectar : " + ex.getMessage());

        }
        return nome;

    }

}
