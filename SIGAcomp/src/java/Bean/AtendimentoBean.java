/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.DAO.DAOAluno;
import Model.DAO.DAOAtendimento;
import Model.DAO.SessionContext;
import Model.Pojo.Situacao;
import Model.Pojo.Aluno;
import Model.Pojo.Atendimento;
import Model.Pojo.Local;
import Model.Pojo.Setor;
import Model.Pojo.Turma;
import Model.Pojo.Usuario;
import java.sql.SQLException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.apache.commons.mail.EmailException;
import org.primefaces.context.RequestContext;

/**
 *
 * @author renandenardi
 */
@ManagedBean
public class AtendimentoBean {

    private String destino;
    private Boolean local = false;
    private Boolean enviaremail = false;
    private Situacao situacao = new Situacao();
    private Atendimento atendimento = new Atendimento();
    private DAOAtendimento dao = new DAOAtendimento();
    private DAOAluno alunos = new DAOAluno();

    private Aluno aluno;
    private Usuario servidor;
    private Setor setor;
    private Turma turma;
    private Local localencaminhado;

    public Boolean getEnviaremail() {
        return enviaremail;
    }

    public void setEnviaremail(Boolean enviaremail) {
        this.enviaremail = enviaremail;
    }
    
    

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    
    private List<Aluno> alunosencontrados;
    private List<Atendimento> meusatendimentos;
    private List<Atendimento> atendimentoservidores;
    private List<Atendimento> atendimentosencontrados;
    private List<Aluno> alunosfiltrados;

    public List<Aluno> getAlunosfiltrados() {
        return alunosfiltrados;
    }

    public void setAlunosfiltrados(List<Aluno> alunosfiltrados) {
        this.alunosfiltrados = alunosfiltrados;
    }
    
    

    public List<Atendimento> getAtendimentosencontrados() {
        return atendimentosencontrados;
    }

    public void setAtendimentosencontrados(List<Atendimento> atendimentosencontrados) {
        this.atendimentosencontrados = atendimentosencontrados;
    }
    
    
    

    public DAOAtendimento getDao() {
        return dao;
    }

    public void setDao(DAOAtendimento dao) {
        this.dao = dao;
    }

    
    public Local getLocalencaminhado() {
        return localencaminhado;
    }

    public void setLocalencaminhado(Local localencaminhado) {
        this.localencaminhado = localencaminhado;
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public DAOAluno getAlunos() {
        return alunos;
    }

    public void setAlunos(DAOAluno alunos) {
        this.alunos = alunos;
    }

    public Usuario getServidor() {
        return servidor;
    }

    public void setServidor(Usuario servidor) {
        this.servidor = servidor;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public List<Aluno> getAlunosencontrados() throws SQLException {
        return alunos.retornaAluno();
    }

    public void setAlunosencontrados(List<Aluno> alunosencontrados) {
        this.alunosencontrados = alunosencontrados;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

    public List<Atendimento> getMeusatendimentos() {
        return dao.consultarMeusAtendimentos();
    }

    public void setMeusatendimentos(List<Atendimento> meusatendimentos) {
        this.meusatendimentos = meusatendimentos;
    }

    public List<Atendimento> getAtendimentoservidores() {
        return dao.consultarAtendimentosServidores();
    }

    public void setAtendimentoservidores(List<Atendimento> atendimentoservidores) {
        this.atendimentoservidores = atendimentoservidores;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public void habilitar() {
        if (atendimento.getTipo().contains("encaminhamentointerno")
                || atendimento.getTipo().contains("encaminhamentoexterno")) {
            local = false;
        } else {
            local = true;
        }

    }

    public void novo() throws SQLException, EmailException {
        
        if (getAluno() == null) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aluno deve ser selecionado!", null);
            contexto.addMessage(null, mensagem);
        } else {

            this.destino = "/publico/Atendimento.jsf";
            atendimento.setAluno_id(aluno.getId());
            atendimento.setAluno(aluno);
            atendimento.setSituacao(situacao);
            atendimento.setServidor_id((int) SessionContext.getInstancia().getAttribute("usuario_id"));
            atendimento.setSetor_id((int) SessionContext.getInstancia().getAttribute("setor_id"));

            if (atendimento.getLocal_id() == 0) {
                atendimento.setLocal_id(8);
            }

            dao.inserir(atendimento, aluno, enviaremail);
            atendimento = new Atendimento();
        }
        
        
    }

    public String excluir() {
        dao.excluir(this.atendimento);
        this.destino = "/publico/ConsultarMeusAtendimentos.jsf";
        return destino;
    }

    public String editar() throws EmailException {

        dao.editar(atendimento, enviaremail);
        this.destino = "/publico/ConsultarMeusAtendimentos.jsf";
        RequestContext requisicao = RequestContext.getCurrentInstance();
        requisicao.addCallbackParam("edicao", true);
        atendimento = new Atendimento();
        return this.destino;
    }

}
