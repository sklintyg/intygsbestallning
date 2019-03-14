import bestallningarDb from './bestallningarDb';

const delay = (ms) =>
  new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallningar = (filter) =>
  delay(500).then(() => {
    switch (filter) {
      case 'active':
        return {
          bestallningList: bestallningarDb.filter(t => t.status === 'ACCEPTERAD' || t.status === 'OLAST' || t.status === 'LAST'),
          totalCount: bestallningarDb.length
        }
      case 'completed':
        return {
          bestallningList: bestallningarDb.filter(t => t.status === 'KLARMARKERAD'),
          totalCount: bestallningarDb.length
        }
      case 'rejected':
        return {
          bestallningList: bestallningarDb.filter(t => t.status === 'AVVISAD' || t.status === 'AVVISAD_RADERAD'),
          totalCount: bestallningarDb.length
        }
      default:
        throw new Error(`Unknown filter: ${filter}`);
    }
  });
