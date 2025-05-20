/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import Model.Pojo.Aluno;
import Model.Pojo.Atendimento;
import Model.Pojo.Local;
import Model.Pojo.Setor;
import Model.Pojo.Situacao;
import Model.Pojo.Turma;
import Model.Pojo.Usuario;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.swing.JOptionPane;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author renandenardi
 */
public class DAOAtendimento {

    Conexao conecta = new Conexao();

    private PreparedStatement pstmt;
    private ResultSet rs;

    public String assuntoEmail(Atendimento atendimento) {

        DAOLocal local = new DAOLocal();
        String assunto = null;
        String descricao = null;
        descricao = local.consultarDescricaoLocal(atendimento.getLocal_id());

        if (atendimento.getTipo().contains("encaminhamentoexterno")) {
            assunto = "Encaminhamento Externo para " + descricao;
        } else {
            if (atendimento.getTipo().contains("encaminhamentointerno")) {
                assunto = "Encaminhamento Interno para " + descricao;
            } else {

                assunto = "Novo atendimento"; //título do email

            }
        }

        return assunto;

    }

    /**
     * Método para inserção do atendimento ao aluno
     *
     * @param atendimento
     * @param aluno
     * @param enviaremail
     * @throws org.apache.commons.mail.EmailException
     *
     */
    public void inserir(Atendimento atendimento, Aluno aluno, boolean enviaremail) throws EmailException {

        conecta.conectar();

        String sql = "INSERT INTO atendimento(id, tipo, status, data, aluno_id,"
                + " recurso, servidor_id, setor_id, local_id, situacao_id, observacao)"
                + " VALUES (DEFAULT,?,?,?,?,?,?,?,?,?,?)";

        //Instancia para o formato de data desejado
        SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
        //formatando data recebida do formulário
        Date dataatendimento = Date.valueOf(data.format(atendimento.getData()));

        SimpleDateFormat dataemail = new SimpleDateFormat("dd/MM/yyyy");
        String datamail = dataemail.format(atendimento.getData());

        if (atendimento.getLocal_id() == 0) {
            atendimento.setLocal_id((int) SessionContext.getInstancia().getAttribute("setor_id"));
        }

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setString(1, atendimento.getTipo());
            pstmt.setString(2, atendimento.getStatus());
            pstmt.setDate(3, dataatendimento);
            pstmt.setInt(4, atendimento.getAluno_id());
            pstmt.setString(5, atendimento.getRecurso());
            pstmt.setInt(6, atendimento.getServidor_id());
            pstmt.setInt(7, atendimento.getSetor_id());
            pstmt.setInt(8, atendimento.getLocal_id());
            pstmt.setInt(9, atendimento.getSituacao_id());
            pstmt.setString(10, atendimento.getObservacao());

            if (pstmt.executeUpdate() > 0) {

                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Inserido com sucesso!", null);
                contexto.addMessage(null, mensagem);
                
                if(enviaremail == true){

                //Envio de email para notificar o usuário da aplicação
                SimpleEmail email = new SimpleEmail();

                String assunto = assuntoEmail(atendimento);
                DAOSituacao descsituacao = new DAOSituacao();
                email.setSubject(assunto);
                email.setMsg("Data: " + datamail
                        + "\nAluno: " + aluno.getNome()
                        + "\nSituação: " + descsituacao.consultarSituacao(atendimento.getSituacao_id())
                        + "\nObservação: " + atendimento.getObservacao()
                        + "\nServidor: " + SessionContext.getInstancia().getAttribute("nome")
                        + "\nSetor: " + SessionContext.getInstancia().getAttribute("nomesetor")
                        + "\nStatus: " + atendimento.getStatus() + "\n\n"
                        + "  -Este email é gerado automaticamente pelo sistema. Por favor, não responda."); //mensagem do email

                Email atendimentonovo = new Email(email);
                atendimentonovo.gerarEmail();
                }

            }

        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível inserir o atendimento. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        conecta.desconectar();
        try {
            pstmt.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível inserir o atendimento. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);

        }

    }

    /**
     * Método que formata a data recebida do formulário
     *
     * @param formato
     * @param data
     * @return data formatada
     */
    public String formatarData(String formato, Date data) {
        SimpleDateFormat formatodata = new SimpleDateFormat(formato);
        String dataformatada = formatodata.format(data);
        return dataformatada;
    }

    /**
     * Método que recupera os atendimentos registrados pelo servidor no sistema
     *
     * @return retorna a lista de atendimentos realizado pelo servidor no
     * sistema
     */
    public List<Atendimento> consultarMeusAtendimentos() {
        conecta.conectar();

        List<Atendimento> lista = new ArrayList<>();
        String sql = "SELECT atendimento.*, aluno.nome as nome, aluno.data_nasc as datanasc,\n"
                + "                aluno.email as email, aluno.endereco as endereco, aluno.naturalidade as nat,\n"
                + "                 aluno.telefone as fone,\n"
                + "                aluno.turma_id as turma,\n"
                + "                setor.nome as setornome,\n"
                + "                servidor.id as id_servidor,\n"
                + "                cargo, servidor.nome as servidor, servidor.telefone as telef,\n"
                + "                servidor.email as servmail, nivel, login, senha,\n"
                + "                servidor.setor_id as setor,\n"
                + "                 situacao.id as id_situacao,\n"
                + "                situacao.descricao as descsituacao,"
                + "                 local.descricao as local, "
                + "                 local.id as id_local, "
                + "                 observacao"
                + "                FROM atendimento \n"
                + "                INNER JOIN aluno\n"
                + "                	ON atendimento.aluno_id = aluno.id\n"
                + "                INNER JOIN setor\n"
                + "                	ON atendimento.setor_id = setor.id\n"
                + "                INNER JOIN servidor\n"
                + "                 	ON atendimento.servidor_id = servidor.id\n"
                + "                 INNER JOIN situacao"
                + "                     ON atendimento.situacao_id = situacao.id"
                + "                 INNER JOIN local"
                + "                     ON atendimento.local_id = local.id"
                + "                WHERE\n"
                + "                	atendimento.servidor_id = ? \n"
                + "                ORDER BY atendimento.id DESC";

        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, (int) SessionContext.getInstancia().getAttribute("usuario_id"));

            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {

                    Aluno aluno = new Aluno();
                    aluno.setNome(rs.getString("nome"));
                    aluno.setId(rs.getInt("aluno_id"));
                    aluno.setData(rs.getDate("datanasc"));
                    aluno.setEmail(rs.getString("email"));
                    aluno.setEndereco(rs.getString("endereco"));
                    aluno.setNaturalidade(rs.getString("nat"));
                    aluno.setTelefone(rs.getString("fone"));
                    aluno.setTurma_id(rs.getInt("turma"));
                   

                    Atendimento atendimento = new Atendimento();
                    atendimento.setAluno(aluno);

                    Setor setor = new Setor();
                    setor.setId(rs.getInt("setor_id"));
                    setor.setNome(rs.getString("setornome"));
                    atendimento.setSetor(setor);

                    Usuario servidor = new Usuario();
                    servidor.setId(rs.getInt("id_servidor"));
                    servidor.setNome(rs.getString("servidor"));
                    servidor.setCargo(rs.getString("cargo"));
                    servidor.setTelefone(rs.getString("telef"));
                    servidor.setEmail(rs.getString("servmail"));
                    servidor.setNivel(rs.getInt("nivel"));
                    servidor.setLogin(rs.getString("login"));
                    servidor.setSenha(rs.getString("senha"));
                    servidor.setSetor_id(rs.getInt("setor"));
                    atendimento.setServidor(servidor);

                    atendimento.setId(rs.getInt("id"));
                    atendimento.setData(rs.getDate("data"));
                    atendimento.setRecurso(rs.getString("recurso"));
                    atendimento.setServidor_id(rs.getInt("servidor_id"));
                    atendimento.setSetor_id(rs.getInt("setor_id"));
                    atendimento.setAluno_id(rs.getInt("aluno_id"));
                    atendimento.setSituacao_id(rs.getInt("id_situacao"));
                    atendimento.setStatus(rs.getString("status"));
                    atendimento.setTipo(rs.getString("tipo"));
                    atendimento.setObservacao(rs.getString("observacao"));

                    Situacao situacao = new Situacao();
                    situacao.setId(rs.getInt("id_situacao"));
                    situacao.setDescricao(rs.getString("descsituacao"));
                    atendimento.setSituacao(situacao);

                    Local local = new Local();
                    local.setId(rs.getInt("id_local"));
                    local.setDescricao(rs.getString("local"));
                    atendimento.setLocal_id(rs.getInt("id_local"));
                    atendimento.setLocal(local);

                    lista.add(atendimento);

                }
            }
        } catch (SQLException ex) {

            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar seus atendimentos. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }
        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar seus atendimentos. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }
        conecta.desconectar();

        return lista;
    }

    /**
     * Método que retorna os atendimentos registrados pelos servidores no
     * sistema
     *
     * @return
     */
    public List<Atendimento> consultarAtendimentosServidores() {
        conecta.conectar();
        List<Atendimento> lista = new ArrayList<>();
        String sql = "SELECT at.id, at.data,\n"
                + "                        al.nome as aluno,\n"
                + "                        situacao.descricao as descricao,\n"
                + "                        serv.nome as servidor,\n"
                + "                        set.nome as setor,\n"
                + "                        status, "
                + "                        tipo , "
                + "                        recurso ,"
                + "                        local.descricao as local, observacao, "
                + "                        turma.nome as turmaaluno \n"
                + "                     FROM servidor serv \n"
                + "                     INNER JOIN atendimento at  \n"
                + "                       ON serv.id = at.servidor_id \n"
                + "                     INNER JOIN aluno al \n"
                + "                       ON al.id = at.aluno_id \n"
                + "                     INNER JOIN setor set \n"
                + "                       ON set.id = at.setor_id \n"
                + "                     INNER JOIN situacao \n"
                + "                       ON at.situacao_id = situacao.id \n"
                + "                     INNER JOIN local \n"
                + "                       ON at.local_id = local.id \n"
                +"                      INNER JOIN turma \n"
                + "                       ON  al.turma_id = turma.id \n"
                + "                     ORDER BY at.id DESC;";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Atendimento atendimento = new Atendimento();
                    Usuario servidor = new Usuario();
                    Setor setor = new Setor();
                    Aluno aluno = new Aluno();
                    Situacao situacao = new Situacao();
                    Local local = new Local();
                    Turma turma = new Turma();
                   

                    atendimento.setId(rs.getInt("id"));
                    atendimento.setData(rs.getDate("data"));
                    atendimento.setStatus(rs.getString("status"));
                    atendimento.setTipo(rs.getString("tipo"));
                    atendimento.setRecurso(rs.getString("recurso"));
                    atendimento.setObservacao(rs.getString("observacao"));
                    servidor.setNome(rs.getString("servidor"));
                    setor.setNome(rs.getString("setor"));
                    aluno.setNome(rs.getString("aluno"));
                    turma.setNome(rs.getString("turmaaluno"));
                    aluno.setTurma(turma);
                    situacao.setDescricao(rs.getString("descricao"));
                    local.setDescricao(rs.getString("local"));
                    atendimento.setServidor(servidor);
                    atendimento.setSetor(setor);
                    atendimento.setAluno(aluno);
                    atendimento.setSituacao(situacao);
                    atendimento.setLocal(local);
                    lista.add(atendimento);
                }
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar os atendimentos do campus. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
            rs.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível consultar seus atendimentos. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }
        conecta.desconectar();

        return lista;

    }

    /**
     * Método que faz alteração dos dados de atendimento
     *
     *
     * @param atendimento
     * @param enviaremail
     * @throws org.apache.commons.mail.EmailException
     */
    public void editar(Atendimento atendimento, boolean enviaremail) throws EmailException {
        conecta.conectar();

        String sql = "UPDATE atendimento"
                + "    SET data=?, aluno_id=?, situacao_id=?, "
                + "     recurso=?, tipo=?, status=?, local_id=?, setor_id=?, observacao=?"
                + "      WHERE id=?";

        //Instancia para o formato de data desejado
        SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");

        //formatando data recebida do formulário
        Date dataatendimento = Date.valueOf(data.format(atendimento.getData()));

        SimpleDateFormat dataemail = new SimpleDateFormat("dd/MM/yyyy");
        String datamail = dataemail.format(atendimento.getData());
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setDate(1, (Date) dataatendimento);
            pstmt.setInt(2, atendimento.getAluno_id());
            pstmt.setInt(3, atendimento.getSituacao_id());
            pstmt.setString(4, atendimento.getRecurso());
            pstmt.setString(5, atendimento.getTipo());
            pstmt.setString(6, atendimento.getStatus());
            pstmt.setInt(7, atendimento.getLocal_id());
            pstmt.setInt(8, atendimento.getSetor_id());
            pstmt.setString(9, atendimento.getObservacao());
            pstmt.setInt(10, atendimento.getId());


            if (pstmt.executeUpdate() > 0) {

               
                
                if(enviaremail == true){
                    DAOSituacao descsituacao = new DAOSituacao();
                    DAOAluno nomealuno = new DAOAluno();
                    SimpleEmail emailalteracao = new SimpleEmail();
                    emailalteracao.setSubject("Alteração em atendimento nº " + atendimento.getId());
                    emailalteracao.setMsg("Data: " + datamail
                        + "\nAluno: " + nomealuno.retornaNomeAluno(atendimento.getAluno_id())
                        + "\nSituação: " + descsituacao.consultarSituacao(atendimento.getSituacao_id())
                        + "\nServidor: " + SessionContext.getInstancia().getAttribute("nome")
                        + "\nSetor: " + SessionContext.getInstancia().getAttribute("nomesetor")
                        + "\nObservação: " + atendimento.getObservacao()
                        + "\nStatus: " + atendimento.getStatus() + "\n\n"
                        + "-Este email é gerado automaticamente pelo sistema. Por favor, não responda.");

                Email email = new Email(emailalteracao);
                email.gerarEmail();
                }
                
                 FacesContext contexto = FacesContext.getCurrentInstance();
                 FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Editado com sucesso!", null);
                 contexto.addMessage(null, mensagem);
                

            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível editar seu atendimento. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        conecta.desconectar();
        try {
            pstmt.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível editar seu atendimento. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

    }

    public void excluir(Atendimento atendimento) {
        conecta.conectar();
        String sql = "DELETE FROM atendimento "
                + "WHERE id=?";
        try {
            pstmt = conecta.getConexao().prepareStatement(sql);
            pstmt.setInt(1, atendimento.getId());
            if (pstmt.executeUpdate() > 0) {
                FacesContext contexto = FacesContext.getCurrentInstance();
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluído com sucesso!", null);
                contexto.addMessage(null, mensagem);
            }
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir seu atendimento. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        try {
            pstmt.close();
        } catch (SQLException ex) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível excluir seu atendimento. Continue usando o sistema normalmente! ", "Cadastrado");
            contexto.addMessage(null, mensagem);
        }

        conecta.desconectar();

    }

}
