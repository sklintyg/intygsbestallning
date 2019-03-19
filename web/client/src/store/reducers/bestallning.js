import { combineReducers } from 'redux';
import * as ActionConstants from '../actions/bestallning';

const bestallning = (state = {}, action) => {
    switch (action.type) {
        case ActionConstants.FETCH_BESTALLNING_SUCCESS:
            return action.response;
        case ActionConstants.SETSTATUS_BESTALLNING_SUCCESS:
            return {...state, status: action.response}
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
            return action.message;
        case ActionConstants.FETCH_BESTALLNING_REQUEST:
        case ActionConstants.FETCH_BESTALLNING_SUCCESS:
            return null;
        default:
            return state;
    }
};

const statusErrorMessage = (state = null, action) => {
    switch (action.type) {
        case ActionConstants.SETSTATUS_BESTALLNING_FAILURE:
            return action.message;
        case ActionConstants.SETSTATUS_BESTALLNING_REQUEST:
        case ActionConstants.SETSTATUS_BESTALLNING_SUCCESS:
            return null;
        default:
            return state;
    }
};

const labels = (state = {}, action) => {
    switch (action.type) {
        case ActionConstants.FETCH_BESTALLNING_LABELS_SUCCESS:
            return action.response;
        default:
            return state;
    }
};

export default combineReducers({
    bestallning,
    fetching,
    errorMessage,
    statusErrorMessage,
    labels,
});

export const getBestallning = (state) =>
    state.bestallning.bestallning;

export const isFetching = (state) =>
    state.bestallning.fetching;

export const getErrorMessage = (state) =>
    state.bestallning.errorMessage;

export const isSettingStatus = (state) =>
    state.bestallning.settingStatus;

export const getStatusErrorMessage = (state) =>
    state.bestallning.statusErrorMessage;

export const isFetchingLabels = (state) =>
    state.bestallning.fetchingLabels;

export const getBestallningLabels = (state) =>
    state.bestallning.labels;