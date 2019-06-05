package se.inera.intyg.intygsbestallning.web.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.infra.integration.hsa.model.Vardenhet;
import se.inera.intyg.infra.integration.hsa.model.Vardgivare;
import se.inera.intyg.infra.security.authorities.CommonAuthoritiesResolver;
import se.inera.intyg.infra.security.common.exception.GenericAuthenticationException;
import se.inera.intyg.infra.security.common.model.Feature;
import se.inera.intyg.infra.security.common.model.IntygUser;
import se.inera.intyg.infra.security.common.model.Privilege;
import se.inera.intyg.infra.security.common.model.RequestOrigin;
import se.inera.intyg.infra.security.common.model.Role;
import se.inera.intyg.infra.security.common.model.UserOriginType;
import se.inera.intyg.intygsbestallning.web.auth.IbSelectableHsaEntity;
import se.inera.intyg.intygsbestallning.web.auth.IbSelectableHsaEntityType;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IbVardgivare;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.auth.authorities.AuthoritiesConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Magnus Ekstrand on 2019-06-03.
 */
@ExtendWith(MockitoExtension.class)
class IntygsbestallningUserDetailsServiceTest {

    private static final String PERSONAL_HSAID = "TSTNMT2321000156-1024";
    private static final String PERSONAL_NAMN = "Jakob van Solo";
    private static final String PERSONAL_TITEL = "Läkare";

    private static final String VARDGIVARE_HSAID = "IFV1239877878-0001";
    private static final String VARDGIVARE_NAMN = "Testvardgivare";
    private static final String VARDGIVARE_ORGNR = "123456-0987";

    private static final String ENHET_1_HSAID = "IFV1239877878-103H";
    private static final String ENHET_1_NAMN = "Testenhet A";
    private static final String ENHET_2_HSAID = "IFV1239877878-103P";
    private static final String ENHET_2_NAMN = "Testenhet B";

    @Mock
    private CommonAuthoritiesResolver commonAuthoritiesResolver;

    @InjectMocks
    private IntygsbestallningUserDetailsService testee;

    @Test
    void expectAnExceptionWhenNoSAMLCredentialsAreProvided() {
        assertThrows(GenericAuthenticationException.class, () -> {
            testee.loadUserBySAML(null);
        });
    }

    @Test
    void buildSystemAuthoritiesTree() {
        var result = testee.buildSystemAuthoritiesTree(buildListOfVardgivareWithMultipleUnits());

        assertEquals(1, result.size());

        IbVardgivare vg = result.get(0);
        assertEquals(VARDGIVARE_HSAID, vg.getId());
        assertEquals(VARDGIVARE_NAMN, vg.getName());
        assertEquals(ENHET_1_HSAID, vg.getVardenheter().get(0).getId());
        assertEquals(ENHET_1_NAMN, vg.getVardenheter().get(0).getName());
        assertEquals(ENHET_2_HSAID, vg.getVardenheter().get(1).getId());
        assertEquals(ENHET_2_NAMN, vg.getVardenheter().get(1).getName());
    }

    @Test
    void autoSelectHsaEntityWhenThereIsOnlyOneCareUnit() {
        IntygsbestallningUser ibUser = createIbUser();
        ibUser.setUnitContext(createUnitContext());

        testee.tryToSelectHsaEntity(ibUser);

        assertEquals(ENHET_1_HSAID, ibUser.getUnitContext().getId());
        assertEquals(ENHET_1_NAMN, ibUser.getUnitContext().getName());
        assertEquals(IbSelectableHsaEntityType.VE, ibUser.getUnitContext().type());
    }

    @Test
    void checkValdVardgivareAndVardenhetWhenUserOnlyHasSingleCareUnitOnly() {
        IntygUser user = createDefaultUser();
        user.setVardgivare(buildListOfVardgivareWithSingleUnit());

        testee.decorateIntygUserWithDefaultVardenhet(user);

        assertEquals(VARDGIVARE_HSAID, user.getValdVardgivare().getId());
        assertEquals(VARDGIVARE_NAMN, user.getValdVardgivare().getNamn());
        assertEquals(ENHET_1_HSAID, user.getValdVardenhet().getId());
        assertEquals(ENHET_1_NAMN, user.getValdVardenhet().getNamn());
    }

    @Test
    void testDecoratingUserWithRoleAndAuthorities() {
        IntygUser user = createDefaultUser();

        when(commonAuthoritiesResolver.getRole(anyString())).
                thenReturn(createRole(
                        AuthoritiesConstants.ROLE_VARDADMIN,
                        Collections.singletonList(
                                createPrivilege(AuthoritiesConstants.PRIVILEGE_LISTA_BESTALLNINGAR,
                                        Collections.emptyList(),
                                        Collections.emptyList()))));

        testee.decorateIntygUserWithRoleAndAuthorities(user, null, null);

        assertEquals(1, user.getRoles().size());

        Role role = user.getRoles().values().stream().findFirst().orElseThrow();
        assertEquals(AuthoritiesConstants.ROLE_VARDADMIN, role.getName());
        assertEquals(AuthoritiesConstants.PRIVILEGE_LISTA_BESTALLNINGAR, role.getPrivileges().get(0).getName());
    }

    @Test
    void testDecoratingUserWithSystemRoles() {
        IntygUser user = createDefaultUser();

        testee.decorateIntygUserWithSystemRoles(user, null);

        assertEquals(Collections.emptyList(), user.getSystemRoles());
    }

    @Test
    void testUsersDefaultRole() {
        assertEquals(AuthoritiesConstants.ROLE_VARDADMIN, testee.getDefaultRole());
    }

    @Test
    void loadUserByUsername() {
        assertNull(testee.loadUserByUsername(PERSONAL_NAMN));
    }


    // private methods

    private List<Vardgivare> buildListOfVardgivareWithSingleUnit() {
        Vardgivare vardgivare = new Vardgivare(VARDGIVARE_HSAID, VARDGIVARE_NAMN, VARDGIVARE_ORGNR);
        vardgivare.getVardenheter().add(new Vardenhet(ENHET_1_HSAID, ENHET_1_NAMN));

        return Collections.singletonList(vardgivare);
    }

    private List<Vardgivare> buildListOfVardgivareWithMultipleUnits() {
        Vardgivare vardgivare = new Vardgivare(VARDGIVARE_HSAID, VARDGIVARE_NAMN, VARDGIVARE_ORGNR);
        vardgivare.setOrgId(VARDGIVARE_ORGNR);
        vardgivare.getVardenheter().add(new Vardenhet(ENHET_1_HSAID, ENHET_1_NAMN));
        vardgivare.getVardenheter().add(new Vardenhet(ENHET_2_HSAID, ENHET_2_NAMN));

        return Collections.singletonList(vardgivare);
    }

    private IbSelectableHsaEntity createUnitContext() {
        final IbVardgivare ibVardgivare = new IbVardgivare(VARDGIVARE_HSAID, VARDGIVARE_NAMN);
        return new IbVardenhet(ENHET_1_HSAID, ENHET_1_NAMN, ibVardgivare.getId(), ibVardgivare.getName(), VARDGIVARE_ORGNR);
    }

    private IntygsbestallningUser createIbUser() {
        var ibUser = new IntygsbestallningUser(createDefaultUser());
        ibUser.setHsaId(PERSONAL_HSAID);
        ibUser.setNamn(PERSONAL_NAMN);
        ibUser.setVardgivare(buildListOfVardgivareWithMultipleUnits());
        return ibUser;
    }

    private IntygUser createDefaultUser() {
        return createUser(AuthoritiesConstants.ROLE_VARDADMIN,
                createPrivilege(AuthoritiesConstants.PRIVILEGE_LISTA_BESTALLNINGAR,
                        Collections.emptyList(),
                        Collections.emptyList()),
                Collections.emptyMap(),
                UserOriginType.NORMAL.name());
    }

    private IntygUser createUser(String roleName, Privilege p, Map<String, Feature> features, String origin) {
        IntygUser user = new IntygUser(PERSONAL_HSAID);
        user.setTitel(PERSONAL_TITEL);

        HashMap<String, Privilege> pMap = new HashMap<>();
        pMap.put(p.getName(), p);
        user.setAuthorities(pMap);

        user.setOrigin(origin);
        user.setFeatures(features);

        HashMap<String, Role> rMap = new HashMap<>();
        Role role = new Role();
        role.setName(roleName);
        rMap.put(roleName, role);

        user.setRoles(rMap);
        return user;
    }

    private Role createRole(String name, List<Privilege> privileges) {
        return new Role() {{
            setName(name);
            setDesc("A role description");
            setPrivileges(privileges);
        }};
    }

    private Privilege createPrivilege(String name, List<String> intygsTyper, List<RequestOrigin> requestOrigins) {
        Privilege p = new Privilege();
        p.setName(name);
        p.setIntygstyper(intygsTyper);
        p.setRequestOrigins(requestOrigins);
        return p;
    }

}