/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Local;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.swing.JOptionPane;

/**
 *
 * @author renandenardi
 */
public class DAOLocal {

    Conexao conecta = new Conexao();
    private PreparedStatement pstmt;
    private ResultSet rs;

    /**
     * Método que insere um local no banco de dados
     *
     * @param local
     */
    public void inserir(Local local) {
        try {
            conecta.conectar();
            String sql = "INSERT INTO local VALUES (DEFAULT,?)";
            try {
                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setString(1, local.getDescricao());

                if (pstmt.executeUpdate() > 0) {
                    FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso!", null);
                    FacesContext contexto = FacesContext.getCurrentInstance();
                    contexto.addMessage(null, mensagem);

                }
            } catch (SQLException ex) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível cadastrar o local. Continue usando o sistema normalmente! ", "Cadastrado");
                contexto.addMessage(null, mensagem);
            }

            pstmt.close();
            conecta.desconectar();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar: " + ex.getMessage());
        }

    }

    /**
     * Método que exclui um local no banco de dados
     *
     * @param local
     */
    public void excluir(Local local) {
        conecta.conectar();
        String sql = "DELETE FROM local "
                + "WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, local.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluído com sucesso!", null);
                contexto.addMessage(null, mensagem);
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir o local. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou rs: " + ex.getMessage());
        }
        conecta.desconectar();

    }

    /**
     * Método que altera informações de um local no banco de dados
     *
     * @param local
     */
    public void editar(Local local) {
        conecta.conectar();
        String sql = "UPDATE local "
                + "SET descricao=? "
                + "WHERE id =?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, local.getDescricao());
            pstmt.setInt(2, local.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado com sucesso!", "Editado"));
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível atualizar as informações do local. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
        }

        conecta.desconectar();
    }

    /**
     * Método que retorna uma lista dos locais inseridos no banco de dados, com exceção do local com a descrição  'iffarsvs'.
     *
     * @return
     */
    public List<Local> consultar() {
        List<Local> lista = new ArrayList<>();
        conecta.conectar();
        String sql = "SELECT id, descricao\n"
                + "FROM local\n"
                + "WHERE id NOT IN (\n"
                + "SELECT id\n"
                + "FROM local\n"
                + "WHERE\n"
                + " descricao = 'IFFARSVS') \n"
                + "ORDER BY 2;";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Local local = new Local();
                    local.setId(rs.getInt("id"));
                    local.setDescricao(rs.getString("descricao"));
                    lista.add(local);
                }
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os locais. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
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
     * Método  que consulta os locais de atendimento/encaminhamento
     * @return 
     */
    public List<Local> consultarLocaisAtendimentos() {
        List<Local> lista = new ArrayList<>();
        conecta.conectar();
        String sql = "SELECT id, descricao\n"
                + "FROM local \n"
                + "ORDER BY 2;";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Local local = new Local();
                    local.setId(rs.getInt("id"));
                    local.setDescricao(rs.getString("descricao"));
                    lista.add(local);
                }
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os locais. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
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
     * Método que retorna a descrição do local
     *
     * @return
     */
    public String consultarDescricaoLocal(int id) {
        conecta.conectar();
        String descricao = null;
        String sql = "SELECT descricao"
                + "   FROM local"
                + "   WHERE id=?";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    descricao = rs.getString("descricao");
                }
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar a descrição do local. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou rs: " + ex.getMessage());
        }
        conecta.desconectar();

        return descricao;
    }

}
