/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

/**
 *
 * @author renandenardi
 */
public class GeradorRelatorio {

    private HttpServletResponse response;
    private FacesContext context;
    private ByteArrayOutputStream baos;
    private InputStream stream;
    private String nomerelatorio;
    private String destinorelatorio;
    private HashMap params = new HashMap<>();

    public GeradorRelatorio(String nomerelatorio, String destinorelatorio, HashMap parametros) {
        this.nomerelatorio = nomerelatorio;
        this.destinorelatorio = destinorelatorio;
        this.params = parametros;
        this.context = FacesContext.getCurrentInstance();
        this.response = (HttpServletResponse) context.getExternalContext().getResponse();

    }

    /**
     * public void gerarPDF(List<Relatorio> lista) { JasperReport report; try {
     * report = JasperCompileManager.compileReport(this.diretorioRelatorio +
     * this.nomerelatorio + ".jrxml"); JasperPrint print =
     * JasperFillManager.fillReport(report, null, new
     * JRBeanCollectionDataSource(lista));
     * JasperExportManager.exportReportToPdfFile(print, this.diretorioRelatorio
     * + "/Relatorio_" + this.nomerelatorio + ".pdf"); } catch (JRException ex)
     * { JOptionPane.showMessageDialog(null, "Erro ao gerar relatório: " +
     * ex.getMessage()); }
     *
     * @param lista
     */
    /**
     * Método responsável por gerar relatórios em pdf
     *
     */
    public void gerarPDF() {
        FacesContext contexto = FacesContext.getCurrentInstance();

        stream = this.getClass().getResourceAsStream(this.destinorelatorio);

        Conexao conexao = new Conexao();
        conexao.conectar();

        baos = new ByteArrayOutputStream();
        try {

            JasperReport report;
            report = (JasperReport) JRLoader.loadObject(stream);

            JasperPrint print = JasperFillManager.fillReport(report, params, conexao.getConexao());

            JasperExportManager.exportReportToPdfStream(print, baos); // Exportando relatório para pdf e jogando em um byte array

            response.reset();
            response.setContentType("application/pdf");
            byte[] bytes = baos.toByteArray();
            response.setContentLength(bytes.length);
            response.setHeader("Content-disposition", "inline; filename=" + nomerelatorio + ".pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete(); //resposta http já está pronta

        } catch (JRException ex) {
            FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_WARN, "O sistema não pôde gerar o relatório. Continue usando o sistema normalmente!", null);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar Relatório: " + ex.getMessage());
        }

    }
}
