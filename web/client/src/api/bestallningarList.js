import { v4 } from 'node-uuid';

// This is a fake in-memory implementation of something
// that would be implemented by calling a REST server.

/*
  UNDEFINED,
  OLAST,
  LAST,
  AVVISAD,
  AVVISAD_RADERAD,
  ACCEPTERAD,
  KLARMARKERAD
*/

const fakeDatabase = {
  bestallningar: [{
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
],
};

const delay = (ms) =>
  new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallningar = (filter) =>
  delay(500).then(() => {
    switch (filter) {
      case 'active':
        return fakeDatabase.bestallningar.filter(t => t.status === 'ACCEPTERAD' || t.status === 'OLAST' || t.status === 'LAST');
      case 'completed':
        return fakeDatabase.bestallningar.filter(t => t.status === 'KLARMARKERAD');
      case 'rejected':
        return fakeDatabase.bestallningar.filter(t => t.status === 'AVVISAD' || t.status === 'AVVISAD_RADERAD');
      default:
        throw new Error(`Unknown filter: ${filter}`);
    }
  });
