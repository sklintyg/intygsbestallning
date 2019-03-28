import * as api from '../../api/bestallning'
import { isFetching } from '../reducers/bestallning'

export const FETCH_BESTALLNING_REQUEST = "FETCH_BESTALLNING_REQUEST";
export const FETCH_BESTALLNING_SUCCESS = "FETCH_BESTALLNING_SUCCESS";
export const FETCH_BESTALLNING_FAILURE = "FETCH_BESTALLNING_FAILURE";

export const ACCEPTERA_BESTALLNING_REQUEST = "ACCEPTERA_BESTALLNING_REQUEST";
export const ACCEPTERA_BESTALLNING_SUCCESS = "ACCEPTERA_BESTALLNING_SUCCESS";
export const ACCEPTERA_BESTALLNING_FAILURE = "ACCEPTERA_BESTALLNING_FAILURE";

export const REJECT_BESTALLNING_REQUEST = "REJECT_BESTALLNING_REQUEST";
export const REJECT_BESTALLNING_SUCCESS = "REJECT_BESTALLNING_SUCCESS";
export const REJECT_BESTALLNING_FAILURE = "REJECT_BESTALLNING_FAILURE";

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

export const accepteraBestallning = (id, fritextForklaring) => (dispatch) => {
    dispatch({
        type: ACCEPTERA_BESTALLNING_REQUEST,
        id
    });
    
    return api.accepteraBestallning(id, fritextForklaring).then(
        () => {
            dispatch({
                type: ACCEPTERA_BESTALLNING_SUCCESS
            });
        },
        error => {
            dispatch({
                type: ACCEPTERA_BESTALLNING_FAILURE,
                id,
                message: error.message || "Something went wrong."
            });
        }
    );
};

export const rejectBestallning = (id, fritextForklaring) => (dispatch) => {
    dispatch({
        type: REJECT_BESTALLNING_REQUEST,
        id
    });

    return api.rejectBestallning(id, fritextForklaring).then(
        () => {
            dispatch({
                type: REJECT_BESTALLNING_SUCCESS
            });
        },
        error => {
            dispatch({
                type: REJECT_BESTALLNING_FAILURE,
                id,
                message: error.message || "Something went wrong."
            });
        }
    );
};