package se.inera.intyg.intygsbestallning.web.service.bestallning;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.dto.BestallningInvanareDto;
import se.inera.intyg.intygsbestallning.common.dto.PdfBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningDto;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningScope;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.service.pdl.LogService;
import se.inera.intyg.intygsbestallning.web.service.util.BestallningUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import static se.inera.intyg.intygsbestallning.web.service.util.PdfUtil.millimetersToPoints;

@Service
public class PdfBestallningServiceImpl implements PdfBestallningService {

    private static final String AF_LOGOTYPE_CLASSPATH_URI = "static/images/af_logo.png";

    private static final Color IB_COLOR_07 = new DeviceRgb(0x21, 0x21, 0x21);
    private static final Color IB_COLOR_09 = new DeviceRgb(0x6A, 0x6A, 0x6A);

    private static final float RUBRIK_SIZE = 12;
    private static final float TEXT_SIZE = 10;

    private static final float DEFAULT_BORDER_WIDTH = 0.5f;
    private static final float FRAGA_MARGIN_BOTTOM = millimetersToPoints(5f);
    private static final float FRAGA_PADDING_LEFT = millimetersToPoints(5f);
    private static final float FRAGA_PADDING_RIGHT = millimetersToPoints(5f);
    private static final float TEXT_PADDING_BOTTOM = millimetersToPoints(2f);

    private PdfFont rubrikFont, normalFont;

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
    public byte[] pdf(PdfBestallningRequest request) {
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
            throw new IllegalArgumentException("Person was not found in PU");
        }

        var invanareDto = BestallningInvanareDto.Factory.toDto(
                bestallning.get().getInvanare(),
                personSvar.get().getFornamn(),
                personSvar.get().getMellannamn(),
                personSvar.get().getEfternamn(),
                personSvar.get().isSekretessmarkering());

        byte[] data =  generatePdf(VisaBestallningDto.Factory.toDto(
                bestallning.get(),
                invanareDto,
                AF_LOGOTYPE_CLASSPATH_URI,
                Collections.emptyList(),
                bestallningTexter,
                request.getScope()));

        if (request.getScope() == VisaBestallningScope.ALL || request.getScope() == VisaBestallningScope.FORFRAGAN) {
            pdlLogService.log(bestallning.get(), LogEvent.BESTALLNING_SKRIV_UT);
        }

        return data;
    }

    private byte[] generatePdf(VisaBestallningDto bestallning)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Initialize PDF writer
        PdfWriter writer = new PdfWriter(bos);

        // Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4);

        rubrikFont = loadFont("Roboto-Medium.woff2");
        normalFont = loadFont("Roboto-Regular.woff2");

        for(var fraga : bestallning.getFragor()) {

            Div fragaDiv = new Div()
                    .setBorder(new SolidBorder(IB_COLOR_07, DEFAULT_BORDER_WIDTH))
                    .setPaddingLeft(FRAGA_PADDING_LEFT)
                    .setPaddingRight(FRAGA_PADDING_RIGHT)
                    .setMarginBottom(FRAGA_MARGIN_BOTTOM);

            addRubrik(fragaDiv, fraga.getRubrik());

            for(var delfraga :  fraga.getDelfragor()) {
                if (delfraga.getEtikett() != null) {
                    addEtikett(fragaDiv, delfraga.getEtikett());
                }
                if (delfraga.getText() != null) {
                    addText(fragaDiv, delfraga.getText());
                }
                if (delfraga.getSvar() != null) {
                    addSvar(fragaDiv, delfraga.getSvar());
                }
                if (delfraga.getBild() != null) {
                    addBild(pdf, document, fragaDiv, delfraga.getBild());
                }
            }

            document.add(fragaDiv);
        }

        document.close();

        return bos.toByteArray();
    }

    private void addRubrik(Div parent, String rubrik) {
        parent.add(new Paragraph(rubrik)
                .setFont(rubrikFont)
                .setFontColor(IB_COLOR_07)
                .setFontSize(RUBRIK_SIZE));
    }

    private void addEtikett(Div parent, String etikett) {
        parent.add(new Paragraph(etikett)
                .setFont(rubrikFont)
                .setFontColor(IB_COLOR_09)
                .setFontSize(TEXT_SIZE)
                .setPadding(0)
                .setMargin(0));
    }

    private void addText(Div parent, String text) {
        parent.add(new Paragraph(text)
                .setFont(normalFont)
                .setFontColor(IB_COLOR_07)
                .setFontSize(TEXT_SIZE)
                .setMargin(0)
                .setPaddingBottom(TEXT_PADDING_BOTTOM));
    }

    private void addSvar(Div parent, String svar) {
        parent.add(new Paragraph(svar)
                .setItalic()
                .setFont(normalFont)
                .setFontColor(IB_COLOR_07)
                .setFontSize(TEXT_SIZE)
                .setMargin(0)
                .setPaddingBottom(TEXT_PADDING_BOTTOM));
    }

    private void addBild(PdfDocument pdf, Document document, Div parent, String bild)  {

        float floatErrorMargin = 0.1f;
        float availableWidth = pdf.getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin() - FRAGA_PADDING_LEFT - FRAGA_PADDING_RIGHT - DEFAULT_BORDER_WIDTH * 2 - floatErrorMargin;
        float availableHeight = pdf.getDefaultPageSize().getHeight() - document.getTopMargin() - document.getBottomMargin() - DEFAULT_BORDER_WIDTH * 2 - floatErrorMargin;

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

}
