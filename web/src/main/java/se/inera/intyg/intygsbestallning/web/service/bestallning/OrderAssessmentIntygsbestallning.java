package se.inera.intyg.intygsbestallning.web.service.bestallning;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentResponseType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.rivtabp21.OrderAssessmentResponderInterface;
import se.riv.intygsbestallning.certificate.order.v1.CVType;
import se.riv.intygsbestallning.certificate.order.v1.CitizenType;
import se.riv.intygsbestallning.certificate.order.v1.IIType;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import se.inera.intyg.infra.integration.pu.util.PersonIdUtil;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestHandlaggare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestInvanare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestKontor;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationErrorCode;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationException;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.util.RivtaUtil;
import se.inera.intyg.schemas.contract.Personnummer;

@Component
@Transactional
public class OrderAssessmentIntygsbestallning implements OrderAssessmentResponderInterface {

    private static final String KV_INTYGSTYP = "b64ea353-e8f6-4832-b563-fc7d46f29548";
    private static final String KV_MYNDIGHET = "769bb12b-bd9f-4203-a5cd-fd14f2eb3b80";
    private static final String HSA_ID_ROOT = "1.2.752.129.2.1.4.1";


    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CreateBestallningService createBestallningService;
    private IntegrationProperties integrationProperties;
    private BestallningTextService bestallningTextService;

    public OrderAssessmentIntygsbestallning(
            CreateBestallningService createBestallningService,
            IntegrationProperties integrationProperties,
            BestallningTextService bestallningTextService) {
        this.createBestallningService = createBestallningService;
        this.integrationProperties = integrationProperties;
        this.bestallningTextService = bestallningTextService;
    }

    @Override
    public OrderAssessmentResponseType orderAssessment(String logicalAddress, OrderAssessmentType orderAssessmentType) {

        LOG.info("Received request for OrderAssessment");

        var result = Try.of(() -> {
            var createBestallningRequest = fromType(orderAssessmentType);
            var id = createBestallningService.create(createBestallningRequest);
            OrderAssessmentResponseType response = new OrderAssessmentResponseType();
            response.setAssessmentId(RivtaUtil.anII(integrationProperties.getSourceSystemHsaId(), id.toString()));
            response.setResult(RivtaUtil.aResultTypeOK());
            return response;
        });

        if (result.isSuccess()) {
            return result.get();
        } else {
            LOG.error("Error in orderAssessment", result.getCause());
            OrderAssessmentResponseType response = new OrderAssessmentResponseType();
            response.setAssessmentId(RivtaUtil.anII(integrationProperties.getSourceSystemHsaId(), ""));
            response.setResult(RivtaUtil.aResultTypeError(result.getCause()));
            return response;
        }
    }

    private CreateBestallningRequest fromType(OrderAssessmentType request) {

        if (request.getAssessmentId() != null) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL07, List.of());
        }

        var personnummerString = Optional.ofNullable(request.getCitizen())
                .map(CitizenType::getPersonalIdentity)
                .map(IIType::getExtension).get();

        var personnummerRoot = Optional.ofNullable(request.getCitizen())
                .map(CitizenType::getPersonalIdentity)
                .map(IIType::getRoot).get();

        var personnummer = Personnummer.createPersonnummer(personnummerString)
                .orElseThrow(() -> new IbResponderValidationException(
                        IbResponderValidationErrorCode.GTA_FEL05,
                        List.of(personnummerString)));

        // Since createPersonnummer is lax by design, also check that input matches NORMALIZED personnummer.
        if (!personnummerString.equals(personnummer.getPersonnummer())) {
            throw new IbResponderValidationException(
                    IbResponderValidationErrorCode.GTA_FEL05,
                    List.of(personnummerString));
        }

        if (!personnummerRoot.equals(PersonIdUtil.getPersonnummerRoot())
                || (PersonIdUtil.isSamordningsNummer(personnummer) && !personnummerRoot.equals(PersonIdUtil.getSamordningsNummerRoot()))) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(personnummerRoot));
        }

        var vardenhet = Optional.of(request.getCareUnitId()).map(IIType::getExtension).get();
        var vardenhetRoot = Optional.of(request.getCareUnitId()).map(IIType::getRoot).get();
        if (!vardenhetRoot.equals(HSA_ID_ROOT)) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(vardenhetRoot));
        }

        var intygTyp = Optional.ofNullable(request.getCertificateType()).map(CVType::getCode).get();
        var intygTypCodeSystem = Optional.ofNullable(request.getCertificateType()).map(CVType::getCodeSystem).get();

        if (!intygTypCodeSystem.equals(KV_INTYGSTYP)) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(intygTypCodeSystem));
        }

        var intygVersion = Try.of(() -> bestallningTextService.getLatestVersionForBestallningsbartIntyg(intygTyp));
        if (intygVersion.isFailure()) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL08, List.of());
        }


        var invanare = new CreateBestallningRequestInvanare(personnummer, request.getCitizen().getSituationBackground());

        CreateBestallningRequestKontor kontor;
        if (request.getAuthorityAdministrativeOfficial().getOfficeAddress() != null) {
            kontor = new CreateBestallningRequestKontor(
                    request.getAuthorityAdministrativeOfficial().getOfficeName(),
                    request.getAuthorityAdministrativeOfficial().getOfficeCostCenter(),
                    request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalAddress(),
                    request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalCode(),
                    request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalCity());
        } else {
            kontor = new CreateBestallningRequestKontor(
                    request.getAuthorityAdministrativeOfficial().getOfficeName(), null, null, null, null);
        }

        var auktoritet = new CreateBestallningRequestHandlaggare(
                request.getAuthorityAdministrativeOfficial().getFullName(),
                request.getAuthorityAdministrativeOfficial().getPhoneNumber(),
                request.getAuthorityAdministrativeOfficial().getEmail(),
                request.getAuthorityAdministrativeOfficial().getAuthority().getCode(),
                kontor);

        var auktoritetSystemKod = request.getAuthorityAdministrativeOfficial().getAuthority().getCodeSystem();
        if (!auktoritetSystemKod.equals(KV_MYNDIGHET)) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(auktoritetSystemKod));
        }

        var mappadMyndighet = Try.of(() -> MyndighetTyp.valueOf(auktoritet.getMyndighet()));
        if (mappadMyndighet.isFailure()) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL02, List.of(auktoritet.getMyndighet()));
        }

        return new CreateBestallningRequest(
                invanare,
                auktoritet,
                request.getPurpose(),
                request.getPlannedActions(),
                intygTyp,
                intygVersion.get(),
                vardenhet,
                request.getCaseReference());
    }

    private enum MyndighetTyp {
        AF("Arbetsförmedlingen"),
        HSVARD("Hälso- och sjukvården"),
        INVANA("Invånaren"),
        TRANSP("Transportstyrelsen"),
        SOS("Socialstyrelsen"),
        SK("Skatteverket");

        private final String description;

        MyndighetTyp(final String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
