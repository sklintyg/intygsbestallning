import * as api from "../../api/bestallning";
import { isFetching, isSettingStatus, isFetchingLabels } from "../reducers/bestallning";

export const FETCH_BESTALLNING_REQUEST = "FETCH_BESTALLNING_REQUEST";
export const FETCH_BESTALLNING_SUCCESS = "FETCH_BESTALLNING_SUCCESS";
export const FETCH_BESTALLNING_FAILURE = "FETCH_BESTALLNING_FAILURE";

export const SETSTATUS_BESTALLNING_REQUEST = "SETSTATUS_BESTALLNING_REQUEST";
export const SETSTATUS_BESTALLNING_SUCCESS = "SETSTATUS_BESTALLNING_SUCCESS";
export const SETSTATUS_BESTALLNING_FAILURE = "SETSTATUS_BESTALLNING_FAILURE";

export const FETCH_BESTALLNING_LABELS_REQUEST = "FETCH_BESTALLNING_LABELS_REQUEST";
export const FETCH_BESTALLNING_LABELS_SUCCESS = "FETCH_BESTALLNING_LABELS_SUCCESS";
export const FETCH_BESTALLNING_LABELS_FAILURE = "FETCH_BESTALLNING_LABELS_FAILURE";

export const fetchBestallning = id => (dispatch, getState) => {
    if (isFetching(getState())) {
        return Promise.resolve();
    }

    dispatch({
        type: FETCH_BESTALLNING_REQUEST,
        id
    });

    return api.fetchBestallning(id).then(
        response => {
            dispatch({
                type: FETCH_BESTALLNING_SUCCESS,
                id,
                response: response
            });
        },
        error => {
            dispatch({
                type: FETCH_BESTALLNING_FAILURE,
                id,
                message: error.message || "Something went wrong."
            });
        }
    );
};

export const setStatus = (id, status) => (dispatch, getState) => {
    if (isSettingStatus(getState())) {
        return Promise.resolve();
    }
    
    dispatch({
        type: SETSTATUS_BESTALLNING_REQUEST,
        id,
        status
    });
    
    return api.setStatus(id, status).then(
        response => {
            console.log(response);
            dispatch({
                type: SETSTATUS_BESTALLNING_SUCCESS,
                response: response
            });
        },
        error => {
            dispatch({
                type: SETSTATUS_BESTALLNING_FAILURE,
                id,
                message: error.message || "Something went wrong."
            });
        }
    );
};

export const fetchBestallningLabels = (intygsTyp, version) => (dispatch, getState) => {
    if (isFetchingLabels(getState())) {
        return Promise.resolve();
    }
    
    dispatch({
        type: FETCH_BESTALLNING_LABELS_REQUEST,
        intygsTyp,
        version
    });
    
    return api.getBestallningLabels(intygsTyp, version).then(
        response => {
            console.log(response);
            dispatch({
                type: FETCH_BESTALLNING_LABELS_SUCCESS,
                response: response
            });
        },
        error => {
            dispatch({
                type: FETCH_BESTALLNING_LABELS_FAILURE,
                intygsTyp,
                message: error.message || "Something went wrong."
            });
        }
    );
};