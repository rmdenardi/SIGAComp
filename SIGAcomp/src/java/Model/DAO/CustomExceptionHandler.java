/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DAO;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author renandenardi
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    FacesContext contextoaplicacao = FacesContext.getCurrentInstance();

    /**
     * Método que retorna a pilha de exceções
     *
     * @return
     */
    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    /**
     * Sobreescrevendo o método responsável pela manipulação de exceções do jsf
     *
     * @throws FacesException
     */
    @Override
    public void handle() throws FacesException {
        final Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            //recuperando exceção do contexto
            Throwable exception = context.getException();
            ViewExpiredException viewexception = new ViewExpiredException();

            //tratando exceção
            try {

                if (SessionContext.getInstancia().getAttribute("usuario_id") == null && exception == viewexception) {
                    
                    Application aplicacao = contextoaplicacao.getApplication();
                    NavigationHandler navegar = aplicacao.getNavigationHandler();
                    
                    //usuário navega para a página de login
                    navegar.handleNavigation(contextoaplicacao, null, "/publico/Login.jsf?faces-redirect=true");

                }
                contextoaplicacao.renderResponse();

            } finally {
                iterator.remove();
            }
        }
        getWrapped().handle();
    }

}
