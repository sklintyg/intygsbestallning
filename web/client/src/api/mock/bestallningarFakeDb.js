import v4 from 'uuid/v4';

const bestallningDb = [{
      id: v4(),
      status: 'OLAST',
      ankomstDatum: '2019-03-12',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson',
      }
    },{
      id: v4(),
      status: 'LAST',
      ankomstDatum: '2019-03-12',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '20121212-1212',
        name: 'Kalle Sunesson'
      }
    },{
      id: v4(),
      status: 'ACCEPTERAD',
      ankomstDatum: '2019-03-12',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Sara Sunesson'
      }
    },{
      id: v4(),
      status: 'AVVISAD',
      ankomstDatum: '2019-03-12',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Avlisa Sunesson'
      }
    },{
      id: v4(),
      status: 'KLARMARKERAD',
      ankomstDatum: '2019-03-12',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Klara Sunesson'
      }
    },
  ];

export default bestallningDb

export const bestallningsConfig = (bestallning) => ({
  id: bestallning.id,
  intygsTyp: bestallning.intygName,
  invanare: {
      personId: bestallning.patient.id,
      namn: bestallning.patient.name
  },
  status: bestallning.status,
  ankomstDatum: bestallning.ankomstDatum,
  fragor: [{
      rubrik: 'Denna förfrågan avser',
      delfragor: [{
        etikett: 'Avsändare',
        bild: 'AF_LOGO'
      }, {
        etikett: 'Information',
        text: 'Arbetsförmedlingen behöver ett medicinskt utlåtande för att klargöra förutsättningarna för arbetssökande som har ett behov av fördjupat stöd. Bland annat för att kunna:' +
        '- utreda och bedöma om den arbetssökande har en funktionsnedsättning som medför nedsatt arbetsförmåga' +
        '- göra en lämplig anpassning för en arbetssökande som deltar i ett arbetsmarknadspolitiskt program och som har blivit sjuk' +
        '- erbjuda (lämpliga) utredande, vägledande, rehabiliterande eller arbetsförberedande insatser' +
        'Utfärda Arbetsförmedlingens medicinska utlåtande via journalsystemet eller Webcert.' +
        'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms. Faktureringsuppgifter finns i slutet av beställningen.',
      }, {
        etikett: 'Samtycke',
        text: 'Arbetsförmedlingen har erhållit samtycke från den arbetssökande om att skicka denna förfrågan.'
      }]
    },{
      rubrik: 'Invånare',
      delfragor: [{
        etikett: 'Personnummer',
        svar: bestallning.patient.id
      },{
        etikett: 'Namn',
        svar: bestallning.patient.name
      }]
    },{
      rubrik: 'Förfrågan',
      delfragor: [{
        etikett: 'Syftet med förfrågan',
        svar: bestallning.syfte
      },{
        etikett: 'Beskriv den arbetssökandes bakgrund och nuläge',
        svar: bestallning.patient.bakgrund
      },{
        etikett: 'Planerade insatser hos Arbetsförmedlingen',
        svar: bestallning.planeradeInsatser
      }]
    },{
      rubrik: 'Kontaktuppgifter arbetsförmedlingen',
      delfragor: [{
        etikett: 'Arbetsförmedlare',
        svar: bestallning.handlaggare.name + '\n' +
              bestallning.handlaggare.epost + '\n' +
              bestallning.handlaggare.telefonnummer
      },{
        etikett: 'Arbetsförmedlingskontor',
        svar: bestallning.kontor.name + '\n' +
              bestallning.kontor.adress
      }]
    },{
      rubrik: 'Fakturainformation',
      delfragor: [{
        etikett: 'Information',
        text: 'Följande uppgifter ska användas för att fakturera Arbetsförmedlingen för ett utfärdat medicinskt utlåtande.' +
        'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms.'
      },{
        etikett: 'Utrednings-ID',
        svar: bestallning.utredning.id
      },{
        etikett: 'Kostnadsställe',
        svar: bestallning.kontor.kostnadsstalle
      },{
        etikett: 'Moms',
        text: '25%'
      },{
        etikett: 'Skicka fakturan till',
        text: 'Arbetsförmedlingen\n' +
        'Skanningscentralen\n' +
        '681 85 Kristinehamn'
      }]
    }]
  });

export const decorateForBestallning = (bList) => {
  return bList.map(b => {
    b.handlaggare = {name: 'Handläggarn Handläggarensson', epost: 'handlaggaren@af.se', telefonnummer: '010 - 112233'};
    b.kontor = {name: 'kontorsnamn', adress: 'kontorsadress', kostnadsstalle: '12345'};
    b.utredning = {id:'123'};
    b.planeradeInsatser = 'Planerade insatser';
    b.patient.bakgrund = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce bibendum augue odio, eu semper dolor gravida vel.';
    b.syfte = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vel ultricies dui. Pellentesque fringilla velit rhoncus, luctus nibh in, tempor turpis.'
    return b;
  });
};
