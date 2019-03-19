import Label from './label';

const Config = (bestallning, labels) => {
  return [{
    rubrik: Label('RBK_1', labels),
    delfragor: [{
      etikett: Label('ETK_1.1', labels),
      bild: 'AF_LOGO'
    }, {
      etikett: Label('ETK_1.2', labels),
      text: Label('TEXT_1.2.1', labels)
    }, {
      etikett: Label('ETK_1.3', labels),
      text: Label('TEXT_1.3.1', labels)
    }]
  },{
    rubrik: Label('RBK_2', labels),
    delfragor: [{
      etikett: Label('ETK_2.1', labels),
      text: bestallning.patient.id
    },{
      etikett: Label('ETK_2.2', labels), 
      text: bestallning.patient.name
    }]
  },{
    rubrik: Label('RBK_3', labels),
    delfragor: [{
      etikett: Label('ETK_3.1', labels),
      text: bestallning.syfte
    },{
      etikett: Label('ETK_3.2', labels),
      text: bestallning.patient.bakgrund
    },{
      etikett: Label('ETK_3.3', labels),
      text: bestallning.planeradeInsatser
    }]
  },{
    rubrik: Label('RBK_5', labels),
    delfragor: [{
      etikett: Label('ETK_5.1', labels),
      text: bestallning.handlaggare.name + '\n' + 
            bestallning.handlaggare.epost + '\n' + 
            bestallning.handlaggare.telefonnummer
    },{
      etikett: Label('ETK_5.2', labels), 
      text: bestallning.kontor.name + '\n' +
            bestallning.kontor.adress
    }]
  },{
    rubrik: Label('RBK_6', labels), 
    delfragor: [{
      etikett: Label('ETK_6.1', labels),
      text: Label('TEXT_6.1.1', labels)
    },{
      etikett: Label('ETK_6.2', labels), 
      text: bestallning.utredning.id
    },{
      etikett: Label('ETK_6.3', labels), 
      text: bestallning.kontor.kostnadsstalle
    },{
      etikett: Label('ETK_6.4', labels), 
      text: Label('TEXT_6.4.1', labels)
    },{
      etikett: Label('ETK_6.5', labels), 
      text: Label('TEXT_6.5.1', labels)
    }]
  }]
}

export default Config