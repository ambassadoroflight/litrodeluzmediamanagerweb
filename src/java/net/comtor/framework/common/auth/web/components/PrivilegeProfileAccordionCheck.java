package net.comtor.framework.common.auth.web.components;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.comtor.dao.ComtorDaoException;
import net.comtor.framework.common.auth.element.Privilege;
import net.comtor.framework.common.auth.element.Profile;
import net.comtor.framework.common.auth.web.facade.PrivilegeWebFacade;
import net.comtor.html.HtmlDiv;
import net.comtor.html.HtmlElement;
import net.comtor.html.HtmlH3;
import net.comtor.html.HtmlSpan;
import net.comtor.html.form.HtmlCheckbox;

/**
 *
 * @author juandiego@comtor.net
 * @since Jun 21, 2018
 */
public class PrivilegeProfileAccordionCheck implements HtmlElement {

    private static final Logger LOG = Logger.getLogger(PrivilegeProfileAccordionCheck.class.getName());

    private HtmlDiv div;

    public PrivilegeProfileAccordionCheck() {
    }

    public PrivilegeProfileAccordionCheck(String name, Profile profile) {
        try {
            createAccordion(name, profile);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public String getHtml() {
        return div.getHtml();
    }

    private void createAccordion(String name, Profile profile) throws ComtorDaoException {
        div = new HtmlDiv("profileAccordion", "accordion");

        try {
            LinkedList<Privilege> privileges = new PrivilegeWebFacade().findAll();
            HashMap<String, LinkedList<Privilege>> privilegeGroupMap = getGroupPrivilegeMap(privileges);
            int i = 1;

            for (String group : privilegeGroupMap.keySet()) {
                HtmlH3 sectionTitle = new HtmlH3(group);
                div.addElement(sectionTitle);

                HtmlDiv sectionBlock = new HtmlDiv("section0" + i, "accordionSections");
                
                div.addElement(sectionBlock);
                LinkedList<HtmlCheckbox> checks = new LinkedList<>();

                for (Privilege privilege : privilegeGroupMap.get(group)) {
                    checks.add(createHtmlCheckboxText(name, privilege, profile));
                }

                for (HtmlCheckbox htmlCheckbox : checks) {
                    HtmlSpan checkSpan = new HtmlSpan();
                    checkSpan.addElement(htmlCheckbox);

                    sectionBlock.addElement(checkSpan);
                }
                
                i++;
            }
        } catch (Exception ex) {
            throw new ComtorDaoException(ex);
        }
    }

    private HashMap<String, LinkedList<Privilege>> getGroupPrivilegeMap(LinkedList<Privilege> privileges) {
        HashMap<String, LinkedList<Privilege>> privilegesGroup = new LinkedHashMap<>();

        for (Privilege privilege : privileges) {
            if (privilegesGroup.containsKey(privilege.getCategory())) {
                privilegesGroup.get(privilege.getCategory()).add(privilege);
            } else {
                LinkedList<Privilege> vector = new LinkedList<>();
                vector.add(privilege);
                privilegesGroup.put(privilege.getCategory(), vector);
            }
        }

        return privilegesGroup;
    }

    private HtmlCheckbox createHtmlCheckboxText(String name, Privilege privilege, Profile profile) {
        HtmlCheckbox checkbox = new HtmlCheckbox(name, privilege.getCode(), privilege.getDescription());

        if (profile != null) {
            if (isChecked(privilege.getCode(), profile.getPrivileges())) {
                checkbox.checked(true);
            }
        }

        return checkbox;
    }

    private boolean isChecked(String code, LinkedList<String> privileges) {
        for (String privilege : privileges) {
            if (code.equals(privilege)) {
                return true;
            }
        }

        return false;
    }

}
