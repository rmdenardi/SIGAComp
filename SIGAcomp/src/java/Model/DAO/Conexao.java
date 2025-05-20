/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class Conexao {          

    private final String url = "jdbc:postgresql://localhost:5432/RedeInterna";
    private final String usuario ="postgres";
    private final String senha="w190pkk17";
    private Connection conexao;

    protected Connection getConexao() {
        return conexao;
    }

    protected void setConexao(Connection conexao) {
        this.conexao = conexao;
    }
    
    
    protected void conectar(){
        try {
            Class.forName("org.postgresql.Driver");
            try {
               conexao = DriverManager.getConnection(url, usuario, senha);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao tentar conectar: " + ex.getMessage());
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao tentar conectar: " + ex.getMessage());
        }
        
    }
    
    protected void desconectar(){
        try {
            conexao.close();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null,"Erro ao desconectar: " + ex.getMessage());
        }
        
    }
    
}
