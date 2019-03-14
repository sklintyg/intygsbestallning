import fakeDatabase from './bestallningarDb';

const delay = (ms) =>
  new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallningar = (filter) =>
  delay(500).then(() => {
    switch (filter) {
      case 'active':
        return fakeDatabase.filter(t => t.status === 'ACCEPTERAD' || t.status === 'OLAST' || t.status === 'LAST');
      case 'completed':
        return fakeDatabase.filter(t => t.status === 'KLARMARKERAD');
      case 'rejected':
        return fakeDatabase.filter(t => t.status === 'AVVISAD' || t.status === 'AVVISAD_RADERAD');
      default:
        throw new Error(`Unknown filter: ${filter}`);
    }
  });
