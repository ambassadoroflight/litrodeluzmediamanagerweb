package net.comtor.framework.common.auth.web.facade;

import java.util.LinkedList;
import net.comtor.dao.ComtorDaoException;
import net.comtor.exception.BusinessLogicException;
import net.comtor.framework.error.ObjectValidatorException;
import net.comtor.framework.logic.facade.AbstractWebLogicFacade;
import net.comtor.framework.common.auth.element.Profile;
import net.comtor.framework.common.auth.facade.ProfileDAOFacade;
import net.comtor.i18n.I18n;
import net.comtor.util.StringUtil;

/**
 *
 * @author juandiego@comtor.net
 * @since Jun 21, 2018
 */
public class ProfileWebFacade extends AbstractWebLogicFacade<Profile, Long, ProfileDAOFacade> {

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreAdd(Profile profile) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validateProfile(profile, exceptions);

        return exceptions;
    }

    @Override
    public LinkedList<ObjectValidatorException> validateObjectPreEdit(Profile profile) {
        LinkedList<ObjectValidatorException> exceptions = new LinkedList<>();

        validateProfile(profile, exceptions);

        return exceptions;
    }

    public LinkedList<Long> findAllIDByUser(String login) throws BusinessLogicException {
        try {
            return getDaoFacade().findAllIDByUser(login);
        } catch (ComtorDaoException ex) {
            throw new BusinessLogicException(ex);
        }
    }

    private void validateProfile(Profile profile, LinkedList<ObjectValidatorException> exceptions) {
        String name = profile.getName();

        if (!StringUtil.isValid(name)) {
            exceptions.add(new ObjectValidatorException("name", I18n.tr(getLang(), "profile.message.name.exception")));
        }

        LinkedList<String> privileges = profile.getPrivileges();

        if ((privileges == null) || (privileges.isEmpty())) {
            exceptions.add(new ObjectValidatorException("field_1", I18n.tr(getLang(), "profile.message.privileges.exception")));
        }
    }
}
