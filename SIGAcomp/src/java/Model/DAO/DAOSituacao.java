/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Situacao;
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
 * @author renandenardi
 */
public class DAOSituacao {

    private PreparedStatement pstmt;
    private ResultSet rs;
    Conexao conecta = new Conexao();

    /**
     * Método que insere situação no banco de dados
     *
     * @param situacao situação informada pelo usuário
     */
    public void inserir(Situacao situacao) {
        try {
            conecta.conectar();
            String sql = "INSERT INTO situacao VALUES (DEFAULT,?)";
            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setString(1, situacao.getDescricao());

                if (pstmt.executeUpdate() > 0) {
                    FacesContext contexto = FacesContext.getCurrentInstance();
                    contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrada com sucesso!", "Cadastrado."));

                }
            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível cadastrar a situação. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao descarregar pstmt de situação: " + ex.getMessage());
        }
    }

    /**
     * Método que exclui uma situação do banco de dados
     *
     * @param situacao situação selecionada pelo usuário
     */
    public void excluir(Situacao situacao) {
        conecta.conectar();
        String sql = "DELETE FROM situacao WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, situacao.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluída com sucesso!", null);
                contexto.addMessage(null, mensagem);
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir a situação. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao descarregar pstmt: " + ex.getMessage());
        }
        conecta.desconectar();

    }

    /**
     * Método que atualiza os dados de uma situação no banco de dados
     *
     * @param situacao situação selecionada pelo usuário
     */
    public void editar(Situacao situacao) {
        try {
            conecta.conectar();
            String sql = "UPDATE situacao"
                    + "   SET descricao=? "
                    + "   WHERE id=?";
            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setString(1, situacao.getDescricao());
                pstmt.setInt(2, situacao.getId());

                if (pstmt.executeUpdate() > 0) {
                    FacesContext contexto = FacesContext.getCurrentInstance();
                    FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Editada com sucesso!", null);
                    contexto.addMessage(null, mensagem);
                }
            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível atualizar as informações da situação. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }
            pstmt.close();
            conecta.desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar: " + ex.getMessage());
        }
    }

    /**
     * Método que retorna as situações encontradas no banco de dados
     *
     * @return lista de situações
     */
    public List<Situacao> consultar() {

        List<Situacao> lista = new ArrayList<>();

        try {
            conecta.conectar();
            String sql = "SELECT id, descricao"
                    + " FROM situacao "
                    + " ORDER BY 2";

            try {
                pstmt = conecta.getConexao().prepareCall(sql);
                rs = pstmt.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        Situacao situacao = new Situacao();
                        situacao.setId(rs.getInt("id"));
                        situacao.setDescricao(rs.getString("descricao"));
                        lista.add(situacao);
                    }
                }
            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar as situações. Continue usando o sistema normalmente! ", "Cadastrado");
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
     * Método que retorna o objeto situação para preencher objeto situação em
     * atendimento
     *
     * @return situação selecionada pelo usuáro
     */
    public String consultarSituacao(int id) {
        String descricao = null;
        try {
            conecta.conectar();
            String sql = "SELECT descricao"
                    + "     FROM situacao"
                    + "     WHERE id=?";
            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();

                if (rs != null) {
                    while (rs.next()) {

                        descricao = (rs.getString("descricao"));
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao consultar situação específica: " + ex.getMessage());
            }

            pstmt.close();
            rs.close();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "Erro de pstmt ou rs: " + ex.getMessage());
        }
        conecta.desconectar();

        return descricao;
    }
}
