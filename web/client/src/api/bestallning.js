import bestallningarDb, {decorateForBestallning} from "./bestallningarDb";

const delay = ms => new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallning = id =>
    delay(500).then(() => {
        const some = [...bestallningarDb];
        console.log(some);
        return decorateForBestallning([...bestallningarDb]).find(t => t.id === id);
    });
