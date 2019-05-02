package net.comtor.framework.common.auth.web.facade;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.html.administrable.ComtorFilterHelper;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.framework.common.auth.element.User;
import net.comtor.framework.common.auth.facade.UserDAOFacade;
import net.comtor.i18n.I18n;
import net.comtor.util.StringUtil;
import net.comtor.util.criterion.ComtorObjectCriterions;
import net.comtor.util.criterion.ComtorObjectListFilter;
import net.comtor.util.query.ComtorQuery;

/**
 *
 * @author juandiego@comtor.net
 * @since Jun 21, 2018
 */
public class UserWebFacade extends AbstractWebLogicFacade<User, String, UserDAOFacade> {

    private static final Logger LOG = Logger.getLogger(UserWebFacade.class.getName());

    @Override
    public LinkedList<User> getObjectsList(ComtorObjectCriterions criterions) throws BusinessLogicException {
        try {
            criterions.addOrderAsc("users.name");

            ComtorQuery comtorQuery = getComtorQuery(criterions);

            return getDaoFacade().findAll(comtorQuery.getQuerySQL(), criterions.getFirstResult(), criterions.getMaxResults(), comtorQuery.getParamsArray());
        } catch (BusinessLogicException | ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    @Override
    public String getWhere(ComtorObjectCriterions criterions, LinkedList<Object> params) {
        String where = ""
                + " WHERE \n"
                + "     1 = 1 \n";

        for (ComtorObjectListFilter filter : criterions.getFilters()) {
            final String id = filter.getId();
            String value = filter.getValue().toString().trim();

            if (StringUtil.isValid(value)) {
                if (id.equals(ComtorFilterHelper.FILTER_NAME)) {
                    StringTokenizer stt = new StringTokenizer(StringUtil.valueOf(value), " ");

                    while (stt.hasMoreTokens()) {
                        String token = "%" + stt.nextToken().replaceAll("'", " ") + "%";

                        where += ""
                                + " AND ( \n"
                                + "     users.login     LIKE ? \n"
                                + "     OR users.name   LIKE ? \n"
                                + "     OR users.email  LIKE ? \n"
                                + " ) \n";

                        params.add(token);
                        params.add(token);
                        params.add(token);
                    }
                } else if (id.equals(ComtorFilterHelper.PARAMETER_NAME) && (filter.getValue() != null)) {
                    // Si hay filtro con parámetro
                } else if (filter.getType() == ComtorObjectListFilter.TYPE_EQUALS) {
                    // Si hay filtro avanzado
                } else {
                    where += " AND " + id + " = ? \n";
                    
                    params.add(value);
                }
            }
        }

        return where;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(User user) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();
        String login = user.getLogin();

        if (StringUtil.isValid(login)) {
            try {
                if (find(login) != null) {
                    exceptions.add(new ObjectValidatorException("login", I18n.tr(getLang(), "user.message.login.exception2")));
                }
            } catch (BusinessLogicException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            exceptions.add(new ObjectValidatorException("login", I18n.tr(getLang(), "user.message.login.exception1")));
        }

        validatePassword(user, exceptions);

        return exceptions;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(User user) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validatePassword(user, exceptions);

        return exceptions;

    }

    private void validatePassword(User user, LinkedList<ObjectValidatorException> exceptions) {
        if (!StringUtil.isValid(user.getPassword())) {
            exceptions.add(new ObjectValidatorException("password", I18n.tr(getLang(), "user.message.password.exception1")));
        } else if (!user.getConfirm_password().equals(user.getPassword())) {
            exceptions.add(new ObjectValidatorException("password", I18n.tr(getLang(), "user.message.password.exception2")));
            exceptions.add(new ObjectValidatorException("confirm_password", I18n.tr(getLang(), "user.message.password.exception2")));
        }
    }
}
