import { combineReducers } from 'redux';

const bestallning = (state = {}, action) => {
    switch (action.type) {
        case 'FETCH_BESTALLNING_SUCCESS':
            return action.response;
        default:
            return state;
    }
};

const fetching = (state = false, action) => {
    switch (action.type) {
        case 'FETCH_BESTALLNING_REQUEST':
            console.log('REQUEST');
            return true;
        case 'FETCH_BESTALLNING_SUCCESS':
        case 'FETCH_BESTALLNING_FAILURE':
            console.log('REQUEST_DONE');
            return false;
        default:
            return state;
    }
};

const errorMessage = (state = null, action) => {
    switch (action.type) {
        case 'FETCH_BESTALLNING_FAILURE':
            return action.message;
        case 'FETCH_BESTALLNING_REQUEST':
        case 'FETCH_BESTALLNING_SUCCESS':
            return null;
        default:
            return state;
    }
};

export default combineReducers({
    bestallning,
    fetching,
    errorMessage,
});

export const getBestallning = (state) =>
    state.bestallning.bestallning;

export const isFetching = (state) =>
    state.bestallning.fetching;

export const getErrorMessage = (state) =>
    state.bestallning.errorMessage;

