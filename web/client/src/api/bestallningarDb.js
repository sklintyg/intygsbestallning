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
        name: 'Bert Sunesson'
      }
    },{
      id: v4(),
      status: 'LAST',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson'
      }
    },{
      id: v4(),
      status: 'ACCEPTERAD',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson'
      }
    },{
      id: v4(),
      status: 'AVVISAD',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson'
      }
    },{
      id: v4(),
      status: 'KLARMARKERAD',
      ankomstDatum: '2019-03-12',
      avslutDatum: '',
      intygName: 'Läkarintyg för läkares skull',
      patient: {
        id: '19121212-1212',
        name: 'Bert Sunesson'
      }
    },
  ];

  export default bestallningar