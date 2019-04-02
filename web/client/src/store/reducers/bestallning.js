import { combineReducers } from 'redux';
import * as ActionConstants from '../actions/bestallning';
import { buildClientError } from './util';

const bestallning = (state = {}, action) => {
    switch (action.type) {
        case ActionConstants.FETCH_BESTALLNING_SUCCESS:
            return action.response;
        case ActionConstants.ACCEPTERA_BESTALLNING_SUCCESS:
            return {...state, status: 'ACCEPTERAD'}
        case ActionConstants.REJECT_BESTALLNING_SUCCESS:
            return {...state, status: 'AVVISAD'}
        default:
            return state;
    }
};

const fetching = (state = false, action) => {
    switch (action.type) {
        case ActionConstants.FETCH_BESTALLNING_REQUEST:
            return true;
        case ActionConstants.FETCH_BESTALLNING_SUCCESS:
        case ActionConstants.FETCH_BESTALLNING_FAILURE:
            return false;
        default:
            return state;
    }
};

const errorMessage = (state = null, action) => {
    switch (action.type) {
        case ActionConstants.FETCH_BESTALLNING_FAILURE:
        case ActionConstants.REJECT_BESTALLNING_FAILURE:
        case ActionConstants.ACCEPTERA_BESTALLNING_FAILURE:
            return buildClientError(action.payload, 'error.bestallning').message;
        case ActionConstants.FETCH_BESTALLNING_REQUEST:
        case ActionConstants.FETCH_BESTALLNING_SUCCESS:
        case ActionConstants.REJECT_BESTALLNING_SUCCESS:
        case ActionConstants.REJECT_BESTALLNING_REQUEST:
        case ActionConstants.ACCEPTERA_BESTALLNING_REQUEST:
        case ActionConstants.ACCEPTERA_BESTALLNING_SUCCESS:
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
