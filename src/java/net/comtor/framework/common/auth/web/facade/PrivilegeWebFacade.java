package net.comtor.framework.common.auth.web.facade;

import java.util.LinkedList;
import net.comtor.aaa.ComtorPrivilege;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.framework.common.auth.element.Privilege;
import net.comtor.framework.common.auth.facade.PrivilegeDAOFacade;

/**
 *
 * @author juandiego@comtor.net
 * @since Jun 21, 2018
 */
public class PrivilegeWebFacade extends AbstractWebLogicFacade<Privilege, String, PrivilegeDAOFacade> {

    public LinkedList<ComtorPrivilege> getUserPrivileges(String login) throws BusinessLogicException {
        try {
            return getDaoFacade().getUserPrivileges(login);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    public LinkedList<Privilege> findAll() throws BusinessLogicException {
        try {
            return getDaoFacade().findAll();
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }
}
