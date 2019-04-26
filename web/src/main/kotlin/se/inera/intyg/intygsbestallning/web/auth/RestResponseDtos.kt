package se.inera.intyg.intygsbestallning.web.auth

import se.inera.intyg.infra.security.common.model.AuthenticationMethod
import se.inera.intyg.infra.security.common.model.Feature
import se.inera.intyg.infra.security.common.model.Role
import se.inera.intyg.intygsbestallning.common.dto.StatResponse
import se.inera.intyg.intygsbestallning.web.auth.SecurityConfig.FAKE_LOGOUT_URL
import se.inera.intyg.intygsbestallning.web.auth.SecurityConfig.SAML_LOGOUT_URL
import se.inera.intyg.intygsbestallning.web.version.VersionInfo

data class ApiErrorResponse(val message: String? = null, val errorCode: String? = null, val logId: String? = null)
data class SessionState(val isHasSession: Boolean = false, val isAuthenticated: Boolean = false, val secondsUntilExpire: Long = 0)
data class GetSessionStatResponse(val sessionState: SessionState, val statResponse: StatResponse? = null)
data class GetAppConfigResponse(val versionInfo: VersionInfo, val loginUrl: String)

class GetUserResponse(user: IntygsbestallningUser) {


    val hsaId: String = user.hsaId
    val namn: String? = user.namn
    val authenticationScheme: String = user.authenticationScheme
    var logoutUrl: String? = null

    val features: Map<String, Feature> = user.features

    val authoritiesTree: List<IbVardgivare> = user.systemAuthorities
    val currentRole: Role? = user.currentRole
    val unitContext: IbSelectableHsaEntity? = user.unitContext
    val totaltAntalVardenheter: Int = user.totaltAntalVardenheter

    init {
        this.logoutUrl = if (user.authenticationMethod == AuthenticationMethod.FAKE) FAKE_LOGOUT_URL else SAML_LOGOUT_URL

    }
}