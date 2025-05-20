/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.JOptionPane;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author root
 */
public class DAOUsuario {

    Conexao conecta = new Conexao();
    private PreparedStatement pstmt;
    private ResultSet rs;
    private HttpServletRequest request;
    private FacesContext contexto;

    /**
     *
     * /**
     * Método que encerra sessão do usuário
     */
    public void encerraSessao() {
        SessionContext.getInstancia().encerraSessao();
    }

    /**
     * Método de recuperação de senha do usuário
     *
     * @param usuario
     * @throws EmailException
     */
    public void recuperarSenha(Usuario usuario) throws EmailException {
        FacesContext contextoaplicacao = FacesContext.getCurrentInstance();

        Usuario usuarioretornado = retornaUsuarioValido(usuario);

        if (usuarioretornado.getId() == 0) {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não existe um servidor cadastrado com o login informado!", null);
            contextoaplicacao.addMessage(null, mensagem);

        } else {
            SimpleEmail emailrecuperasenha = new SimpleEmail();
            emailrecuperasenha.setSubject("Recuperação de Senha");
            emailrecuperasenha.setMsg(" Olá " + usuarioretornado.getNome() + ",\n"
                    + " A seguir suas credenciais de acesso ao sistema: \n"
                    + " Login: " + usuarioretornado.getLogin() + "\n"
                    + " Senha: " + usuarioretornado.getSenha() + "\n\n\n "
                    + "- Este email é gerado automaticamente pelo sistema. Por favor, não responda.");
            Email email = new Email(emailrecuperasenha);
            email.gerarEmail(usuarioretornado.getEmail());

            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Recuperação com sucesso! Um e-mail foi enviado à você para recuperar sua senha.", null);
            contextoaplicacao.addMessage(null, mensagem);

        }

    }

    public Usuario retornaUsuarioValido(Usuario usuario) {
        Usuario usuariovalido = new Usuario();
        conecta.conectar();
        String sql = "SELECT * FROM "
                + "servidor WHERE login=?";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, usuario.getLogin());
            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    usuariovalido.setId(rs.getInt("id"));
                    usuariovalido.setNome(rs.getString("nome"));
                    usuariovalido.setLogin(rs.getString("login"));
                    usuariovalido.setSenha(rs.getString("senha"));
                    usuariovalido.setEmail(rs.getString("email"));

                }

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao retornar e-mail do servidor: " + ex.getMessage());
        }

        conecta.desconectar();

        return usuariovalido;

    }

    /**
     * Método para consulta de informações do usuário da sessão atual
     *
     * @return
     */
    public String consultarInformacoes() {
        return (String) SessionContext.getInstancia().getAttribute("nome");

    }

    /**
     * Método de verificação do usuário para autenticação
     *
     * @param usuario
     * @return retorna a página a ser direcionada.
     * @throws SQLException
     */
    public String verificaUsuario(Usuario usuario) throws SQLException {
        String retorno = null;
        Usuario usuario1 = selecionaUsuario(usuario);

        if (usuario1.getId() == 0) {

            retorno = null;
            contexto = FacesContext.getCurrentInstance();
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login ou senha Inválidos!", "inválidos"));
        } else {
            retorno = "/publico/Menu.jsf?faces-redirect=true";
            /**
             * Adicionando informações importantes do usuário na sessão
             */

            SessionContext.getInstancia().setAttribute("usuario_id", usuario1.getId());
            SessionContext.getInstancia().setAttribute("nome", usuario1.getNome());
            SessionContext.getInstancia().setAttribute("nivel", usuario1.getNivel());
            SessionContext.getInstancia().setAttribute("setor_id", usuario1.getSetor_id());
            SessionContext.getInstancia().setAttribute("nomesetor", new DAOSetor().
                    retornaSetorServidor((int) SessionContext.getInstancia().getAttribute("setor_id")));

        }

        return retorno;
    }

    /**
     * Método que retorna o usuário existente na base dados.
     *
     * @param usuario
     * @return retorna o usuário.
     */
    public Usuario selecionaUsuario(Usuario usuario) {
        conecta.conectar();

        String sql = "SELECT id, nivel, nome, setor_id "
                + " FROM servidor "
                + " WHERE login = ? AND senha=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, usuario.getLogin());
            pstmt.setString(2, usuario.getSenha());
            rs = pstmt.executeQuery();
            if (rs != null) {
                if (rs.next()) {

                    usuario.setId(rs.getInt("id"));

                    usuario.setNivel(rs.getInt("nivel"));

                    usuario.setNome(rs.getString("nome"));

                    usuario.setSetor_id(rs.getInt("setor_id"));

                }

            }
        } catch (SQLException ex) {
            contexto = FacesContext.getCurrentInstance();
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O sistema de recuperou de um erro inesperado. Continue usando normalmente!", null));
        }

        try {
            pstmt.close();
            rs.close();
            conecta.desconectar();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou rs, ou desconectar: " + ex.getMessage());
        }

        return usuario;
    }

    /**
     * Método responsável por retornar os emails dos servidores cadastrados.
     *
     * @return
     * @throws java.sql.SQLException
     */
    public List<String> retornaEmails() throws SQLException {
        conecta.conectar();
        List<String> lista = new ArrayList<>();
        String sql = "SELECT email\n"
                + "FROM servidor\n";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    lista.add(rs.getString("email"));
                }

            }

        } catch (SQLException ex) {
            contexto = FacesContext.getCurrentInstance();
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O sistema de recuperou de um erro inesperado. Continue usando normalmente!", null));
        }

        pstmt.close();
        rs.close();
        conecta.desconectar();

        return lista;
    }

    /**
     * Método para inserção de usuário
     *
     * @param usuario
     * @throws org.apache.commons.mail.EmailException
     *
     */
    public void inserir(Usuario usuario) throws EmailException {

        FacesContext contextoapp = FacesContext.getCurrentInstance();

        if (!usuario.getSenha().equals(usuario.getConfirmarsenha())) {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Senha e confirmar senha tem que ser iguais!", null);
            contextoapp.addMessage(null, mensagem);
        } else {

            conecta.conectar();
            String sql = "INSERT INTO servidor VALUES (DEFAULT,?,?,?,?,?,"
                    + "?,?,?)";

            int nivel = Integer.parseInt(String.valueOf(usuario.getNivel()));

            try {

                pstmt = conecta.getConexao().prepareStatement(sql);
                pstmt.setString(1, usuario.getCargo());
                pstmt.setString(2, usuario.getNome());
                pstmt.setString(3, usuario.getTelefone());
                pstmt.setString(4, usuario.getEmail());
                pstmt.setInt(5, nivel);
                pstmt.setString(6, usuario.getLogin());
                pstmt.setString(7, usuario.getSenha());
                pstmt.setInt(8, usuario.getSetor_id());

                if (pstmt.executeUpdate() > 0) {
                    FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso!", null);
                    contextoapp.addMessage(null, mensagem);

                    SimpleEmail novoemail = new SimpleEmail();
                    novoemail.setSubject("SIGAcomp - Bem vindo!");
                    novoemail.setMsg(" Bem vindo ao SIGAcomp - Sistema Web Gerencial para Acompanhamento de Alunos"
                            + "\n A seguir suas credenciais para acessar o sistema: \n"
                            + "   Login: " + usuario.getLogin()
                            + "\n   Senha: " + usuario.getSenha()
                            + "\n\n   Qualquer dúvida, entre em contato com o administrador do sistema no Campus. "
                            + "\n\n\n   - Este email é gerado automaticamente pelo sistema. Por favor, não responda.");
                    Email email = new Email(novoemail);
                    email.gerarEmail(usuario.getEmail());

                }

            } catch (SQLException ex) {
                contextoapp = FacesContext.getCurrentInstance();
                contextoapp.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O sistema de recuperou de um erro inesperado. Continue usando normalmente!", null));
            }

            try {
                pstmt.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar: " + ex.getMessage());

            }
            conecta.desconectar();

        }

    }

    /**
     * Método que retorna os usuários existentes no banco de dados
     *
     * @return
     * @throws SQLException
     */
    public List<Usuario> retornaUsuario() throws SQLException {

        conecta.conectar();
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM servidor ORDER BY nome";

        pstmt = conecta.getConexao().prepareStatement(sql);
        rs = pstmt.executeQuery();

        if (rs != null) {
            while (rs.next()) {
                Usuario user = new Usuario();

                user.setId(rs.getInt("id"));
                user.setCargo(rs.getString("cargo"));
                user.setNome(rs.getString("nome"));
                user.setTelefone(rs.getString("telefone"));
                user.setEmail(rs.getString("email"));
                user.setNivel(rs.getInt("nivel"));
                user.setLogin(rs.getString("login"));
                user.setSenha(rs.getString("senha"));
                user.setSetor_id(rs.getInt("setor_id"));
                lista.add(user);

            }

        }
        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            contexto = FacesContext.getCurrentInstance();
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O sistema de recuperou de um erro inesperado. Continue usando normalmente!", null));
        }

        conecta.desconectar();

        return lista;

    }

    /**
     * Método que exclui o servidor do banco de dados
     *
     * @param user
     * @return
     */
    public String excluir(Usuario user) {
        conecta.conectar();
        String sql = "DELETE FROM servidor WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, user.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluído com sucesso!", "Excluído"));
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir este usuário. Continue usando o sistema normalmente!", null));
        }

        try {
            pstmt.close();
            conecta.desconectar();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Método que edita as informações do servidor
     *
     * @param user
     */
    public void editar(Usuario user) {

        conecta.conectar();

        String sql = "UPDATE servidor SET nome=?, cargo=?, telefone=?, email=?, setor_id=?, nivel=?, senha=? "
                + "WHERE id=?";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, user.getNome());
            pstmt.setString(2, user.getCargo());
            pstmt.setString(3, user.getTelefone());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getSetor_id());
            pstmt.setInt(6, user.getNivel());
            pstmt.setString(7, user.getSenha());
            pstmt.setInt(8, user.getId());

            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                contexto.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado com sucesso!", "Editado"));
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
            JOptionPane.showMessageDialog(null, "Erro de pstmt ou desconectar" + ex.getMessage());
        }

    }

}
