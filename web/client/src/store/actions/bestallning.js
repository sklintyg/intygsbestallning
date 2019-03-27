import * as api from '../../api/bestallning'
import { isFetching } from '../reducers/bestallning'

export const FETCH_BESTALLNING_REQUEST = "FETCH_BESTALLNING_REQUEST";
export const FETCH_BESTALLNING_SUCCESS = "FETCH_BESTALLNING_SUCCESS";
export const FETCH_BESTALLNING_FAILURE = "FETCH_BESTALLNING_FAILURE";

export const ACCEPTERA_BESTALLNING_REQUEST = "ACCEPTERA_BESTALLNING_REQUEST";
export const ACCEPTERA_BESTALLNING_SUCCESS = "ACCEPTERA_BESTALLNING_SUCCESS";
export const ACCEPTERA_BESTALLNING_FAILURE = "ACCEPTERA_BESTALLNING_FAILURE";

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

export const accepteraBestallning = id => (dispatch) => {
    dispatch({
        type: ACCEPTERA_BESTALLNING_REQUEST,
        id
    });
    
    return api.accepteraBestallning(id).then(
        response => {
            console.log(response);
            dispatch({
                type: ACCEPTERA_BESTALLNING_SUCCESS,
                response: response
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