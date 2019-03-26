import bestallningarDb from "./bestallningarFakeDb";
import { delay } from "./util";

export const fetchBestallningList = (categoryFilter, textFilter) => {
  return delay(500).then(() => {
    const simpleTextFilter = t =>
      t.patient.id.indexOf(textFilter) >= 0 ||
      t.patient.name.toLowerCase().indexOf(textFilter.toLowerCase()) >= 0;
    const isAccepted = t =>
      t.status === "ACCEPTERAD" ||
      t.status === "OLAST" ||
      t.status === "LAST";
    const isRejected = t =>
      t.status === "AVVISAD" || t.status === "AVVISAD_RADERAD";

    switch (categoryFilter) {
      case 'AKTUELLA':
        const active = bestallningarDb.filter(t => isAccepted(t));
        const filtered =
          textFilter && textFilter !== ""
            ? active.filter(t => simpleTextFilter(t))
            : active;
        return {
          bestallningar: filtered,
          totalCount: active.length
        };
      case 'KLARA':
        const completed = bestallningarDb.filter(
          t => t.status === "KLARMARKERAD"
        );
        return {
          bestallningar: completed.filter(t => simpleTextFilter(t)),
          totalCount: bestallningarDb.length
        };
      case 'AVVISADE':
        const rejected = bestallningarDb.filter(t => isRejected(t));
        const filteredRejected = rejected.filter(t => simpleTextFilter(t));
        return {
          bestallningar: filteredRejected,
          totalCount: bestallningarDb.length
        };
      default:
        throw new Error(`Unknown filter: ${categoryFilter}`);
    }
  });
};
