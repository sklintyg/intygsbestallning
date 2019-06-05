/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsbestallning.web.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.infra.integration.hsa.model.SelectableVardenhet;
import se.inera.intyg.infra.security.common.model.IntygUser;
import se.inera.intyg.infra.security.common.model.Privilege;
import se.inera.intyg.infra.security.common.model.Role;
import se.inera.intyg.intygsbestallning.web.pdl.PdlActivityEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static se.inera.intyg.infra.security.authorities.AuthoritiesResolverUtil.toMap;
import static se.inera.intyg.intygsbestallning.web.auth.authorities.AuthoritiesConstants.ROLE_VARDADMIN;

/**
 * Intygsbeställning overrides a lot of the default user's behaviour.
 * <p>
 * Since users can be logged in on both Vårdgivare (VG) and on Vårdenhet (VE), where
 * the VG level doesn't require a Medarbetaruppdrag, some data structures are not used
 * while others have been added.
 * </p>
 */
public class IntygsbestallningUser extends IntygUser implements Serializable {

    private static final long serialVersionUID = 8711015219408194075L;
    private static final Logger LOG = LoggerFactory.getLogger(IntygsbestallningUser.class);

    // Tree for handling which VG and VE the user has access to
    private List<IbVardgivare> systemAuthorities = new ArrayList<>();

    // An IB-user must always have a current role and a current IbSelectableHsaEntity
    private Role currentRole;

    // An IB-user must always have a current IbSelectableHsaEntity
    private IbSelectableHsaEntity unitContext;

    // Handles PDL logging state
    private Map<String, List<PdlActivityEntry>> storedActivities;

    // All known roles. Do NOT expose!!!
    @JsonIgnore
    private List<Role> possibleRoles;

    /**
     * Typically used by unit tests.
     */
    public IntygsbestallningUser(String hsaId, String namn) {
        super(hsaId);
        this.storedActivities = new HashMap<>();
        this.hsaId = hsaId;
        this.namn = namn;
    }

    public static IntygsbestallningUser of(final String hsaId, final String namn) {
        return new IntygsbestallningUser(hsaId, namn);
    }

    /**
     * Copy-constructor that takes a populated {@link IntygUser}.
     *
     * @param intygUser User principal, typically constructed in the
     *                  {@link org.springframework.security.saml.userdetails.SAMLUserDetailsService}
     *                  implementor.
     */
    public IntygsbestallningUser(IntygUser intygUser) {
        super(intygUser.getHsaId());
        this.personId = intygUser.getPersonId();

        this.namn = intygUser.getNamn();
        this.titel = intygUser.getTitel();
        this.authenticationScheme = intygUser.getAuthenticationScheme();
        this.vardgivare = intygUser.getVardgivare();
        this.systemRoles = intygUser.getSystemRoles();

        this.authenticationMethod = intygUser.getAuthenticationMethod();

        this.features = intygUser.getFeatures();
        this.roles = intygUser.getRoles();
        this.authorities = intygUser.getAuthorities();
        this.origin = intygUser.getOrigin();

        this.storedActivities = new HashMap<>();

        this.miuNamnPerEnhetsId = intygUser.getMiuNamnPerEnhetsId();
    }

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    // Do not expose, only set method is allowed
    public void setPossibleRoles(List<Role> possibleRoles) {
        this.possibleRoles = possibleRoles;
    }

    public Map<String, List<PdlActivityEntry>> getStoredActivities() {
        return storedActivities;
    }

    public void setStoredActivities(Map<String, List<PdlActivityEntry>> storedActivities) {
        this.storedActivities = storedActivities;
    }

    public List<IbVardgivare> getSystemAuthorities() {
        return systemAuthorities;
    }

    public void setSystemAuthorities(List<IbVardgivare> systemAuthorities) {
        this.systemAuthorities = systemAuthorities;
    }

    public IbSelectableHsaEntity getUnitContext() {
        return unitContext;
    }

    public void setUnitContext(IbSelectableHsaEntity unitContext) {
        this.unitContext = unitContext;
    }

    /**
     * <p>For IB, we select from the systemAuthorities tree rather than the traditional VG -> VE -> E tree.</p>
     * <p>We also set the currentRole and privileges based on the selection.</p>
     *
     * @param hsaIdVardenhet hsaId of the vardenhet
     * @return boolean - if vardenehet is changed
     */
    public boolean changeContext(String hsaIdVardenhet) {
        for (IbVardgivare ibVg : systemAuthorities) {
            for (IbVardenhet ibVardenhet : ibVg.getVardenheter()) {
                if (ibVardenhet.getId().equalsIgnoreCase(hsaIdVardenhet)) {
                    this.unitContext = ibVardenhet;
                    this.currentRole = selectRole(possibleRoles, ROLE_VARDADMIN);
                    this.authorities = toMap(currentRole.getPrivileges(), Privilege::getName);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @see IntygsbestallningUser#changeContext(String)
     */
    @Override
    public boolean changeValdVardenhet(String hsaIdVardenhet) {
        return super.changeValdVardenhet(hsaIdVardenhet) && changeContext(hsaIdVardenhet);
    }

    /**
     * Utility method to get the "medarbetaruppdrag" name that user has on the currently selected vårdenhet.
     *
     * @return The name of the medarbetaruppdrag. (Derived from infrastructure:directory:authorizationmanagement
     *         CommissionType#commissionName)
     * @throws IllegalStateException
     *             if no vardenhet is selected or if the map that maps enhetsId to commissionName hasn't been
     *             initialized.
     */
    @JsonIgnore
    @Override
    public String getSelectedMedarbetarUppdragNamn() {
        if (unitContext == null) {
            throw new IllegalStateException("Cannot resolve current medarbetaruppdrag name, no HSA entity is selected.");
        }
        if (miuNamnPerEnhetsId == null) {
            throw new IllegalStateException("Cannot resolve current medarbetaruppdrag name, map of MiU's is not initialized.");
        }

        // Lookup commissionName from the selected care unit.
        if (miuNamnPerEnhetsId.containsKey(unitContext.getId())) {
            return miuNamnPerEnhetsId.get(unitContext.getId());
        } else {
            LOG.warn("Unable to resolve the 'medarbetaruppdrag' name. Returning null.");
            return null;
        }
    }

    /**
     * In IB we only handle VG/VE level
     *
     * @return
     */
    @Override
    public int getTotaltAntalVardenheter() {
        return (int) vardgivare.stream().flatMap(vg -> vg.getVardenheter().stream()).count();
    }

    /**
     * IB users are never lakare, override this for compatibility reasons.
     *
     * @return false, always false...
     */
    @Override
    public boolean isLakare() {
        return false;
    }


    //
    // overridden methods not used by IB
    //

    @Override
    @JsonIgnore
    public List<String> getSpecialiseringar() {
        LOG.debug("Method getSpecialiseringar() is not implemented. Empty list will be returned and it is by purpose.");
        return new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public List<String> getBefattningar() {
        LOG.debug("Method getBefattningar() is not implemented. Empty list will be returned and it is by purpose.");
        return new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public List<String> getLegitimeradeYrkesgrupper() {
        LOG.debug("Method getLegitimeradeYrkesgrupper() is not implemented. Empty list will be returned and it is by purpose.");
        return new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public SelectableVardenhet getValdVardenhet() {
        LOG.debug("Method getValdVardenhet() is not implemented. 'null' will be returned and it is by purpose."
                + "Please use getUnitContext() instead.");
        return null;
    }

    @Override
    @JsonIgnore
    public SelectableVardenhet getValdVardgivare() {
        LOG.debug("Method getValdVardgivare() is not implemented. 'null' will be returned and it is by purpose.");
        return null;
    }


    //
    // private scope
    //

    private Role selectRole(List<Role> roles, String roleName) {
        for (Role r : roles) {
            if (r.getName().equals(roleName)) {
                return r;
            }
        }
        throw new IllegalStateException("Tried to set unknown role '" + roleName + "'");
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
