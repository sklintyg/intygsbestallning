import bestallningarDb, {decorateForBestallning} from "./bestallningarDb";

const delay = ms => new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallning = id =>
    delay(500).then(() => {
        return decorateForBestallning([...bestallningarDb]).find(t => t.id === id);
    });

export const setStatus = (id, status) =>
    delay(500).then(() => {
        return status;
    })
