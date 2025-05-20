/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Model.DAO.GeradorRelatorio;
import Model.DAO.SessionContext;
import Model.Pojo.Aluno;
import java.util.Date;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author renandenardi
 */
@ManagedBean
public class RelatorioBean {

    private GeradorRelatorio relatorio;
    private HashMap parametrosrelatorio;
    private Aluno aluno;
    private Date data_inicial;
    private Date data_final;
    private RequestContext requisicao;

    public Date getData_inicial() {
        return data_inicial;
    }

    public void setData_inicial(Date data_inicial) {
        this.data_inicial = data_inicial;
    }

    public Date getData_final() {
        return data_final;
    }

    public void setData_final(Date data_final) {
        this.data_final = data_final;
    }

    public GeradorRelatorio getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(GeradorRelatorio relatorio) {
        this.relatorio = relatorio;
    }

    public HashMap getParametrosrelatorio() {
        return parametrosrelatorio;
    }

    public void setParametrosrelatorio(HashMap parametrosrelatorio) {
        this.parametrosrelatorio = parametrosrelatorio;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    /**
     * Método que gera o relatório de atendimentos por setor
     */
    public void gerarRelatorio() {
        if (getData_final().before(getData_inicial())) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "A data de início não pode ser"
                    + " posterior à data final do período!", null);
            contexto.addMessage(null, mensagem);
        } else {
            
            parametrosrelatorio = new HashMap();
            parametrosrelatorio.put("data_inicial", getData_inicial());
            parametrosrelatorio.put("data_final", getData_final());

            relatorio = new GeradorRelatorio("AtendimentosporSetor",
                    "/Relatorio/AtendimentosXSetor.jasper", parametrosrelatorio);
            relatorio.gerarPDF();
            
        }

    }
    /**
     * Método que gera o relatório da quantidade de atendimentos por servidor
     */
     public void gerarRelatorioServidor() {
        if (getData_final().before(getData_inicial())) {
            FacesContext contexto = FacesContext.getCurrentInstance();
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "A data de início não pode ser"
                    + " posterior à data final do período!", null);
            contexto.addMessage(null, mensagem);
        } else {
            
            parametrosrelatorio = new HashMap();
            parametrosrelatorio.put("data_inicial", getData_inicial());
            parametrosrelatorio.put("data_final", getData_final());

            relatorio = new GeradorRelatorio("AtendimentosporServidor",
                    "/Relatorio/RelatorioQuantidadeAtendimentosServidor.jasper", parametrosrelatorio);
            relatorio.gerarPDF();
            
        }

    }

    /**
     * Método que gera gráfico de quant. de atendimentos em setores
     */
    public void gerarGrafico() {
        FacesContext contexto = FacesContext.getCurrentInstance();

        if (getData_inicial().after(getData_final())) {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "A data de início não pode ser posterior "
                    + "à data final do período!", null);
            contexto.addMessage(null, mensagem);
        } else {
            parametrosrelatorio = new HashMap();
            parametrosrelatorio.put("data_inicial", getData_inicial());
            parametrosrelatorio.put("data_final", getData_final());
            relatorio = new GeradorRelatorio("GráficodeAtendimentosporSetor",
                    "/Grafico/GraficoAtendimentoPorSetor.jasper", parametrosrelatorio);
            relatorio.gerarPDF();
        }

    }

    /**
     * Método que gera gráfico de quant. de situações em atendimentos
     */
    public void gerarGraficoSituacao() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        if (getData_inicial().after(getData_final())) {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "A data de início não pode ser posterior "
                    + "à data final do período!", null);
            contexto.addMessage(null, mensagem);
        } else {
            parametrosrelatorio = new HashMap();
            parametrosrelatorio.put("data_inicial", getData_inicial());
            parametrosrelatorio.put("data_final", getData_final());
            relatorio = new GeradorRelatorio("GráficoSituacaodeAtendimentos", "/Grafico/Situacoes.jasper", parametrosrelatorio);
            relatorio.gerarPDF();
        }

    }

    /**
     * Método que gera relatório de histórico de atendimento de aluno
     */
    public void gerarRelatorioHistorico() {
        FacesContext contexto = FacesContext.getCurrentInstance();

        if (!(getAluno() == null) && !(getData_inicial().after(getData_final()))) {
            parametrosrelatorio = new HashMap();
            parametrosrelatorio.put("aluno_id", aluno.getId());
            parametrosrelatorio.put("data_inicial", getData_inicial());
            parametrosrelatorio.put("data_final", getData_final());
            parametrosrelatorio.put("servidor_nome", (String) SessionContext.getInstancia().getAttribute("nome"));

            relatorio = new GeradorRelatorio("HistóricodeAtendimento",
                    "/Relatorio/HistoricoAtendimento.jasper", parametrosrelatorio);
            relatorio.gerarPDF();
        } else {
            
            if (getAluno() == null) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aluno deve ser selecionado!", null);
                contexto.addMessage(null, msg);
            }
            
            if (getData_inicial().after(getData_final())) {  //teste que verifica se a data final é anterior à data inicial
                FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "A data de início não pode ser"
                        + " posterior à data final do período!", null);
                contexto.addMessage(null, mensagem);

            }

        }

    }
}
