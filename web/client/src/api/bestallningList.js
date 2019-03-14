import fakeDatabase from './bestallningarDb';

const delay = (ms) =>
  new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallningar = (filter) =>
  delay(500).then(() => {
    switch (filter) {
      case 'active':
        return {
          bestallningList: fakeDatabase.bestallningList.filter(t => t.status === 'ACCEPTERAD' || t.status === 'OLAST' || t.status === 'LAST'),
          totalCount: fakeDatabase.bestallningList.length
        }
      case 'completed':
        return {
          bestallningList: fakeDatabase.bestallningList.filter(t => t.status === 'KLARMARKERAD'),
          totalCount: fakeDatabase.bestallningList.length
        }
      case 'rejected':
        return {
          bestallningList: fakeDatabase.bestallningList.filter(t => t.status === 'AVVISAD' || t.status === 'AVVISAD_RADERAD'),
          totalCount: fakeDatabase.bestallningList.length
        }
      default:
        throw new Error(`Unknown filter: ${filter}`);
    }
  });
