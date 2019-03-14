import bestallningarDb from "./bestallningarDb";

const delay = ms => new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallning = id =>
    delay(500).then(() => {
        return bestallningarDb.find(t => t.id === id);
    });
