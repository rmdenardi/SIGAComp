/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author renandenardi
 */
public class Email {

    private final SimpleEmail email;
    private final DAOUsuario servidores = new DAOUsuario();

    public Email(SimpleEmail email) throws EmailException {
        this.email = email;
        gerarConfiguracao();

    }

    /**
     * Método que gera a configuração do envio de email
     *
     * @throws EmailException
     */
    public final void gerarConfiguracao() throws EmailException {
        email.setDebug(true);
        email.setSSLOnConnect(true);
        email.setHostName("smtp.googlemail.com");
        email.setSslSmtpPort("465");
        email.setAuthentication("redeatend.svs@iffarroupilha.edu.br", "@qw590pU");
        email.setFrom("redeatend.svs@iffarroupilha.edu.br", "Rede de Atendimento SVS");

    }

    /**
     * Método que envia email para os servidores
     *
     * @throws EmailException
     * @throws SQLException
     */
    
    public void gerarEmail() throws EmailException, SQLException, EmailException {

        List<String> lista = new ArrayList();
        lista = servidores.retornaEmails();
        
        String[] array = new String[lista.size()];
        
        for (int i = 0; i < lista.size(); i++) {
            array[i] = lista.get(i);
        }
        email.addTo(array); //adicionando uma lista de destinatários do email
        email.send();
    }
    
    public void gerarEmail(String destinatario) throws EmailException{
        email.addTo(destinatario);
        email.send();
    }
    
       
}
