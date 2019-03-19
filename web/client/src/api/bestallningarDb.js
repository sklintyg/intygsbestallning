import { v4 } from 'node-uuid';

const bestallningar = [{
      id: v4(),
      status: 'OLAST',
      //handelser: [],
      //notifieringar: [],
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      /*vardenhet: {
        id: 'enhet1',
        enhetNamn: 'Enheten',
        epost: 'imälj',
        standardSvar: 'Hej'
      },*/
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson',
        bakgrund: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce bibendum augue odio, eu semper dolor gravida vel.'
      }
    },{
      id: v4(),
      status: 'LAST',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson',
        bakgrund: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce bibendum augue odio, eu semper dolor gravida vel.'
      }
    },{
      id: v4(),
      status: 'ACCEPTERAD',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson',
        bakgrund: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce bibendum augue odio, eu semper dolor gravida vel.'
      }
    },{
      id: v4(),
      status: 'AVVISAD',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson',
        bakgrund: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce bibendum augue odio, eu semper dolor gravida vel.'
      }
    },{
      id: v4(),
      status: 'KLARMARKERAD',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson',
        bakgrund: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce bibendum augue odio, eu semper dolor gravida vel.'
      }
    },
  ];

export default bestallningar


export const decorateForBestallning = (bList) => {
  return bList.map(b => {
    b.handlaggare = {name: 'Handläggarn Handläggarensson', epost: 'handlaggaren@af.se', telefonnummer: '010 - 112233'};
    b.kontor = {name: 'kontorsnamn', adress: 'kontorsadress', kostnadsstalle: '12345'};
    b.utredning = {id:'123'};
    return b;
  });
};