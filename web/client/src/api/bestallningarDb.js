import { v4 } from 'node-uuid';

const bestallningar = [{
      id: v4(),
      status: 'OLAST',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson',
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
      }
    },
  ];

export default bestallningar


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