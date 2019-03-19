const Config = (bestallning) => {
  const bestallare = {
    rubrik: 'Denna förfrågan avser',
    delfragor: [{
      etikett: 'Beställare',
      bild: 'AF_LOGO'
    }, {
      etikett: 'Information',
      text: 'Arbetsförmedlingen behöver ett medicinskt utlåtande för att klargöra förutsättningarna för arbetssökande som har ett behov av fördjupat stöd. Bland annat för att kunna:\n' +
      '- utreda och bedöma om den arbetssökande har en funktionsnedsättning som medför nedsatt arbetsförmåga\n' +
      '- göra en lämplig anpassning för en arbetssökande som deltar i ett arbetsmarknadspolitiskt program och som har blivit sjuk\n' +
      '- erbjuda (lämpliga) utredande, vägledande, rehabiliterande eller arbetsförberedande insatser\n' +
      'Utfärda Arbetsförmedlingens medicinska utlåtande via journalsystemet eller Webcert.\n' +
      'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms. Faktureringsuppgifter finns i slutet av beställningen.'
    }, {
      etikett: 'Samtycke',
      text: 'Arbetsförmedlingen har erhållit samtycke från den arbetssökande om att skicka denna förfrågan.'
    }]
  };

  const invanare = {
    rubrik: 'Invånare',
    delfragor: [{
      etikett: 'Personnummer',
      text: bestallning.patient.id
    },{
      etikett: 'Namn', 
      text: bestallning.patient.name
    }]
  };

  const forfragan = {
    rubrik: 'Förfrågan',
    delfragor: [{
      etikett: 'Syftet med förfrågan',
      text: bestallning.syfte
    },{
      etikett: 'Beskriv den arbetssökandes bakgrund och nuläge',
      text: bestallning.patient.bakgrund
    },{
      etikett: 'Planerade insatser hos Arbetsförmedlingen',
      text: bestallning.planeradeInsatser
    }]
  }

  const kontakt = {
    rubrik: 'Kontaktuppgifter arbetsförmedlingen',
    delfragor: [{
      etikett: 'Arbetsförmedlare',
      text: bestallning.handlaggare.name + '\n' + 
            bestallning.handlaggare.epost + '\n' + 
            bestallning.handlaggare.telefonnummer
    },{
      etikett: 'Arbetsförmedlingskontor', 
      text: bestallning.kontor.name + '\n' +
            bestallning.kontor.adress
    }]
  }

  const faktura = {
    rubrik: 'Fakturainformation', 
    delfragor: [{
      etikett: 'Information',
      text: 'Följande uppgifter ska användas för att fakturera Arbetsförmedlingen för ett utfärdat medicinskt utlåtande.\n'+
            'Arbetsförmedlingen betalar för utlåtandet, högst 2200 kr inklusive moms.'
    },{
      etikett: 'Utrednings-ID', 
      text: bestallning.utredning.id
    },{
      etikett: 'Kostnadsställe', 
      text: bestallning.kontor.kostnadsstalle
    },{
      etikett: 'Moms', 
      text: '25%'
    },{
      etikett: 'Skicka fakturan till', 
      text: 'Arbetsförmedlingen\n'+
            'Skanningscentralen\n'+
            '681 85 Kristinehamn'
    }]
  }

  return [bestallare, invanare, forfragan, kontakt, faktura];
}

export default Config