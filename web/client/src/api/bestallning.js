import bestallningarDb, {decorateForBestallning} from "./bestallningarDb";

const labels = {
    intygsTyp: 'af00213',
    version: '1.0',
    texter: {
    'RBK_1': 'Denna förfrågan avser',
    'ETK_1.1': 'Avsändare',
    'ETK_1.2': 'Information',
    'TEXT_1.2.1': 'Arbetsförmedlingen behöver ett medicinskt utlåtande för att klargöra förutsättningarna för arbetssökande som har ett behov av fördjupat stöd. Bland annat för att kunna:' +
      '- utreda och bedöma om den arbetssökande har en funktionsnedsättning som medför nedsatt arbetsförmåga' +
      '- göra en lämplig anpassning för en arbetssökande som deltar i ett arbetsmarknadspolitiskt program och som har blivit sjuk' +
      '- erbjuda (lämpliga) utredande, vägledande, rehabiliterande eller arbetsförberedande insatser' +
      'Utfärda Arbetsförmedlingens medicinska utlåtande via journalsystemet eller Webcert.' +
      'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms. Faktureringsuppgifter finns i slutet av beställningen.',
    'ETK_1.3': 'Samtycke',
    'TEXT_1.3.1': '	Arbetsförmedlingen har erhållit samtycke från den arbetssökande om att skicka denna förfrågan.',
    'RBK_2': 'Invånare',
    'ETK_2.1': 'Personnummer',
    'ETK_2.2': 'Namn',
    'RBK_3': 'Förfrågan',
    'ETK_3.1': 'Syftet med förfrågan',
    'ETK_3.2': 'Beskriv den arbetssökandes bakgrund och nuläge',
    'ETK_3.3': 'Planerade insatser hos Arbetsförmedlingen',
    'RBK_5': 'Kontaktuppgifter arbetsförmedlingen',
    'ETK_5.1': 'Arbetsförmedlare',
    'ETK_5.2': 'Arbetsförmedlingskontor',
    'RBK_6': 'Fakturainformation',
    'ETK_6.1': 'Information',
    'TEXT_6.1.1': 'Följande uppgifter ska användas för att fakturera Arbetsförmedlingen för ett utfärdat medicinskt utlåtande.' +
      'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms.',
    'ETK_6.2': 'Utrednings-ID',
    'ETK_6.3': 'Kostnadsställe',
    'ETK_6.4': 'Moms',
    'TEXT_6.4.1': '25%',
    'ETK_6.5': 'Skicka fakturan till',
    'TEXT_6.5.1': 'Arbetsförmedlingen' +
      'Skanningscentralen' +
      '681 85 Kristinehamn'
  }
};

const delay = ms => new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallning = id =>
    delay(500).then(() => {
        return decorateForBestallning([...bestallningarDb]).find(t => t.id === id);
    });

export const setStatus = (id, status) =>
    delay(500).then(() => {
        return status;
    });

export const getBestallningLabels = (intygsTyp, version) =>
    delay(500).then(() => {
        return labels;
    });