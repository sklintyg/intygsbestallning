import bestallningarDb from './bestallningarDb';

const delay = (ms) =>
  new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallningList = (categoryFilter, textFilter) =>

  delay(500).then(() => {
    const simpleTextFilter = t => t.patient.id.indexOf(textFilter) >= 0 || t.patient.name.toLowerCase().indexOf(textFilter.toLowerCase()) >= 0
    const isAccepted = t => t.status === 'ACCEPTERAD' || t.status === 'OLAST' || t.status === 'LAST'
    const isRejected = t => t.status === 'AVVISAD' || t.status === 'AVVISAD_RADERAD'
  
    switch (categoryFilter) {
      case 'active':
        const active = bestallningarDb.filter(t => isAccepted(t))
        const filtered = textFilter && textFilter !== '' ? active.filter(t => simpleTextFilter(t)) : active
        return {
          bestallningList: filtered,
          totalCount: active.length
        }
      case 'completed':
        const completed = bestallningarDb.filter(t => t.status === 'KLARMARKERAD')
        return {
          bestallningList: completed.filter(t => simpleTextFilter(t)),
          totalCount: bestallningarDb.length
        }
      case 'rejected':
      const rejected = bestallningarDb.filter(t => isRejected(t))
        return {
          bestallningList: rejected.filter(t => simpleTextFilter(t)),
          totalCount: bestallningarDb.length
        }
      default:
        throw new Error(`Unknown filter: ${categoryFilter}`);
    }
  });
