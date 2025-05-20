/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Relatorio;
import Model.Pojo.Setor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author renandenardi
 */
public class DAORelatorio {

    Conexao conecta = new Conexao();
    private PreparedStatement pstmt;
    private ResultSet rs;

    
    /**
     * Método responsável por recuperar dados do relatório
     * @return lista de objetos
     */
    public List<Relatorio> relatAtendSetor() {
        conecta.conectar();
        List<Relatorio> lista = new ArrayList<>();
        String sql = "SELECT  set.nome as setor, COUNT(at) as Quantidade\n"
                + "FROM atendimento at\n"
                + "INNER JOIN setor set\n"
                + "	ON at.setor_id = set.id\n"
                + "WHERE\n"
                + "	data BETWEEN '2017-04-01' AND '2017-04-30'\n"
                + "GROUP BY set.id\n"
                + "ORDER BY quantidade DESC";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Setor setor = new Setor();
                    setor.setNome(rs.getString("setor"));

                    Relatorio relatorio = new Relatorio();
                    relatorio.setSetor(setor);
                    relatorio.setQuantidade(rs.getInt("quantidade"));
                    lista.add(relatorio);

                }
            }
            
         conecta.desconectar();
         pstmt.close();
         rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao retornar consulta para relatório: " + ex.getMessage());
        }
    
        return lista;
    }
}
