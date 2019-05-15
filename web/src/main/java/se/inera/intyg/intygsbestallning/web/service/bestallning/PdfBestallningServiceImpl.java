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

package se.inera.intyg.intygsbestallning.web.service.bestallning;

import static se.inera.intyg.intygsbestallning.web.service.util.PdfUtil.millimetersToPoints;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.dto.BestallningInvanareDto;
import se.inera.intyg.intygsbestallning.common.dto.PdfBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.PdfBestallningResponse;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningMetadata;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningScope;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.bestallning.pdf.BestallningFooter;
import se.inera.intyg.intygsbestallning.web.service.bestallning.pdf.BestallningHeader;
import se.inera.intyg.intygsbestallning.web.service.bestallning.pdf.PageNumberEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

@Service
public class PdfBestallningServiceImpl implements PdfBestallningService {

    private static final String AF_LOGOTYPE_CLASSPATH_URI = "static/images/af_logo.png";
    private static final String IB_LOGOTYPE_CLASSPATH_URI = "static/images/ib_logo.png";

    public static final Color IB_COLOR_07 = new DeviceRgb(0x21, 0x21, 0x21);
    public static final Color IB_COLOR_22 = new DeviceRgb(0x9A, 0x9A, 0x9A);

    public static final float RUBRIK_SIZE = 13.5f;
    public static final float ETIKETT_SIZE = 10.5f;
    public static final float TEXT_SIZE = 8;

    public static final float PAGE_MARGIN_LEFT = millimetersToPoints(20f);
    public static final float PAGE_MARGIN_RIGHT = millimetersToPoints(20f);
    public static final float PAGE_MARGIN_BOTTOM = millimetersToPoints(20f);
    public static final float PAGE_MARGIN_TOP = millimetersToPoints(35f);
    private static final float FRAGA_MARGIN_BOTTOM = millimetersToPoints(4f);
    private static final float FRAGA_PADDING_LEFT = 0;//millimetersToPoints(5f);
    private static final float FRAGA_PADDING_RIGHT = 0;//millimetersToPoints(5f);
    private static final float TEXT_PADDING_BOTTOM = millimetersToPoints(2f);

    private PdfFont normalFont;
    private PdfFont lightFont;

    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningTextService bestallningTextService;
    private PatientService patientService;
    private LogService pdlLogService;

    public PdfBestallningServiceImpl(
            BestallningPersistenceService bestallningPersistenceService,
            BestallningTextService bestallningTextService,
            PatientService patientService,
            LogService pdlLogService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningTextService = bestallningTextService;
        this.patientService = patientService;
        this.pdlLogService = pdlLogService;
    }

    @Override
    public PdfBestallningResponse pdf(PdfBestallningRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request may not be null");
        }

        var id = BestallningUtil.resolveId(request.getBestallningId());

        var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(
                id.get(), request.getHsaId(), request.getOrgNrVardgivare());

        if (bestallning.isEmpty()) {
            throw new IllegalArgumentException("bestallning with id: " + id.get() + " was not found");
        }

        var bestallningTexter = bestallningTextService.getBestallningTexter(bestallning.get());

        var personSvar = patientService.lookupPersonnummerFromPU(bestallning.get().getInvanare().getPersonId());

        if (personSvar.isEmpty()) {
            throw new IbServiceException(IbErrorCodeEnum.BESTALLNING_FEL005_PU_ERROR, "Person was not found in PU");
        }

        var invanareDto = BestallningInvanareDto.Factory.toDto(
                bestallning.get().getInvanare(),
                personSvar.get().getFornamn(),
                personSvar.get().getMellannamn(),
                personSvar.get().getEfternamn(),
                personSvar.get().isSekretessmarkering());

        byte[] data = generatePdf(VisaBestallningDto.Factory.toDto(
                bestallning.get(),
                invanareDto,
                bestallningTexter,
                new VisaBestallningMetadata(
                        AF_LOGOTYPE_CLASSPATH_URI,
                        Collections.emptyList(),
                        request.getScope()
                )
        ));

        if (request.getScope() == VisaBestallningScope.ALL || request.getScope() == VisaBestallningScope.FORFRAGAN) {
            pdlLogService.log(bestallning.get(), LogEvent.BESTALLNING_SKRIV_UT);
        }

        return new PdfBestallningResponse(data, generatePdfFilename(request.getScope(), bestallningTexter));
    }

    private byte[] generatePdf(VisaBestallningDto bestallning) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Initialize PDF writer
        PdfWriter writer = new PdfWriter(bos);

        // Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(PAGE_MARGIN_TOP, PAGE_MARGIN_RIGHT, PAGE_MARGIN_BOTTOM, PAGE_MARGIN_LEFT);

        normalFont = loadFont("Roboto-Regular.woff2");
        lightFont = loadFont("Roboto-Light.woff2");

        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new BestallningHeader(lightFont, loadImage(IB_LOGOTYPE_CLASSPATH_URI),
                bestallning.getId()));

        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new BestallningFooter(lightFont));
        PageNumberEvent pageNumberEvent = new PageNumberEvent(lightFont);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, pageNumberEvent);

        for (var fraga : bestallning.getFragor()) {

            Div fragaDiv = new Div()
                    .setPaddingLeft(FRAGA_PADDING_LEFT)
                    .setPaddingRight(FRAGA_PADDING_RIGHT)
                    .setMarginBottom(FRAGA_MARGIN_BOTTOM);

            addRubrik(fragaDiv, fraga.getRubrik());

            for (var delfraga : fraga.getDelfragor()) {

                addEtikett(fragaDiv, delfraga.getEtikett());

                if (delfraga.getText() != null) {
                    addText(fragaDiv, delfraga.getText());
                }
                if (delfraga.getSvar() != null) {
                    addSvar(fragaDiv, delfraga.getSvar());
                }
                if (delfraga.getBild() != null) {
                    addBild(pdf, document, fragaDiv, delfraga.getBild(), 300);
                }
            }

            document.add(fragaDiv);
        }

        pageNumberEvent.writeTotal(pdf);

        document.close();

        return bos.toByteArray();
    }

    private void addRubrik(Div parent, String rubrik) {
        parent.add(new Paragraph(rubrik)
                .setFont(lightFont)
                .setFontColor(IB_COLOR_07)
                .setFontSize(RUBRIK_SIZE));
    }

    private void addEtikett(Div parent, String etikett) {
        parent.add(new Paragraph(etikett)
                .setFont(normalFont)
                .setUnderline()
                .setFontColor(IB_COLOR_07)
                .setFontSize(ETIKETT_SIZE)
                .setPadding(0)
                .setMargin(0));
    }

    private void addText(Div parent, String text) {
        parent.add(new Paragraph(text)
                .setFont(lightFont)
                .setFontColor(IB_COLOR_07)
                .setFontSize(TEXT_SIZE)
                .setMargin(0)
                .setPaddingBottom(TEXT_PADDING_BOTTOM));
    }

    private void addSvar(Div parent, String svar) {
        parent.add(new Paragraph(svar)
                .setFont(lightFont)
                .setFontColor(IB_COLOR_07)
                .setFontSize(TEXT_SIZE)
                .setMargin(0)
                .setPaddingBottom(TEXT_PADDING_BOTTOM));
    }

    private void addBild(PdfDocument pdf, Document document, Div parent, String bild, float maxWidth) {

        float floatErrorMargin = 0.1f;
        float availableWidth = Math.min(maxWidth, pdf.getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin()
                - FRAGA_PADDING_LEFT - FRAGA_PADDING_RIGHT * 2 - floatErrorMargin);

        float availableHeight = pdf.getDefaultPageSize().getHeight() - document.getTopMargin()
                - document.getBottomMargin() * 2 - floatErrorMargin;

        Image image = new Image(loadImage(bild));

        if (image.getImageWidth() > availableWidth || image.getImageHeight() > availableHeight) {
            image.scaleToFit(availableWidth, availableHeight);
        }

        Div imageDiv = new Div()
                .setPaddingTop(TEXT_PADDING_BOTTOM)
                .setPaddingBottom(TEXT_PADDING_BOTTOM);

        imageDiv.add(image);

        parent.add(imageDiv);
    }

    private ImageData loadImage(String uri) {
        try {
            return ImageDataFactory.create(IOUtils.toByteArray(new ClassPathResource(uri).getInputStream()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load image: " + e.getMessage());
        }
    }

    private PdfFont loadFont(String name) {
        try {
            byte[] fontData = IOUtils.toByteArray(new ClassPathResource("pdf/" + name).getInputStream());
            return PdfFontFactory.createFont(fontData, "Winansi", true); // Cp1250
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load font: " + e.getMessage());
        }
    }

    private String generatePdfFilename(VisaBestallningScope scope, BestallningTexter bestallningTexter) {

        String scopeString;
        switch (scope) {
            case FORFRAGAN:
                scopeString = "Forfragan_";
                break;
            case FAKTURERINGSUNDERLAG:
                scopeString = "Fakturaunderlag_";
                break;
            default:
                scopeString = "";
        }

        String utskriftsTidpunkt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy_MM_dd_HHmm"));

        return String.format("%s%s_%s.pdf", scopeString, bestallningTexter.getIntygTypPdfFilename(), utskriftsTidpunkt);
    }
}
