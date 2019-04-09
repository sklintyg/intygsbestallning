package se.inera.intyg.intygsbestallning.web.service.bestallning;

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
import java.util.Optional;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestHandlaggare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestInvanare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestKontor;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.util.RivtaUtil;
import se.inera.intyg.schemas.contract.Personnummer;

@Component
@Transactional
public class OrderAssessmentIntygsbestallning implements OrderAssessmentResponderInterface {

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

        try {
            var createBestallningRequest = fromType(orderAssessmentType);
            createBestallningService.create(createBestallningRequest);
            OrderAssessmentResponseType response = new OrderAssessmentResponseType();
            response.setAssessmentId(RivtaUtil.anII(integrationProperties.getSourceSystemHsaId(), ""));
            response.setResult(RivtaUtil.aResultTypeOK());
        } catch (final Exception e) {
            LOG.error("Error in orderAssessment", e);
            OrderAssessmentResponseType response = new OrderAssessmentResponseType();
            response.setAssessmentId(RivtaUtil.anII(integrationProperties.getSourceSystemHsaId(), ""));
            response.setResult(RivtaUtil.aResultTypeError(e));
            return response;
        }

        return null;
    }

    private CreateBestallningRequest fromType(OrderAssessmentType request) {
        if (request == null) {
            throw new IllegalArgumentException("request may not be null");
        }

        if (request.getAssessmentId() != null) {
            throw new IllegalArgumentException("assessmentId may not be set");
        }

        var personnummerString = Optional.ofNullable(request.getCitizen())
                .map(CitizenType::getPersonalIdentity)
                .map(IIType::getExtension)
                .orElseThrow(() -> new IllegalArgumentException("personalIdentity may not be null"));

        var personnummer = Personnummer.createPersonnummer(personnummerString)
                .orElseThrow(() -> new IllegalArgumentException("personIdentity must be a valid personnummer"));

        var vardenhet = Optional.ofNullable(request.getCareUnitId())
                .map(IIType::getExtension)
                .orElseThrow(() -> new IllegalArgumentException("careUnitId may not be null"));

        var intygTyp = Optional.ofNullable(request.getCertificateType())
                .map(CVType::getCode)
                .orElseThrow(() -> new IllegalArgumentException("certificateType may not be null"));

        var intygVersion = bestallningTextService.getLatestVersionForBestallningsbartIntyg(intygTyp);


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
                    request.getAuthorityAdministrativeOfficial().getOfficeName(), null,null, null, null);
        }

        var auktoritet =  new CreateBestallningRequestHandlaggare(
                request.getAuthorityAdministrativeOfficial().getFullName(),
                request.getAuthorityAdministrativeOfficial().getPhoneNumber(),
                request.getAuthorityAdministrativeOfficial().getEmail(),
                request.getAuthorityAdministrativeOfficial().getAuthority().getCode(),
                kontor);

        return new CreateBestallningRequest(
                invanare,
                auktoritet,
                request.getPurpose(),
                request.getPlannedActions(),
                intygTyp,
                intygVersion,
                vardenhet);
    }
}
