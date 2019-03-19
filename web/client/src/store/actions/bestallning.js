import * as api from "../../api/bestallning";
import { isFetching, isSettingStatus } from "../reducers/bestallning";

export const FETCH_BESTALLNING_REQUEST = "FETCH_BESTALLNING_REQUEST";
export const FETCH_BESTALLNING_SUCCESS = "FETCH_BESTALLNING_SUCCESS";
export const FETCH_BESTALLNING_FAILURE = "FETCH_BESTALLNING_FAILURE";

export const SETSTATUS_BESTALLNING_REQUEST = "SETSTATUS_BESTALLNING_REQUEST";
export const SETSTATUS_BESTALLNING_SUCCESS = "SETSTATUS_BESTALLNING_SUCCESS";
export const SETSTATUS_BESTALLNING_FAILURE = "SETSTATUS_BESTALLNING_FAILURE";

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
    console.log(id, status);
    if (isSettingStatus(getState())) {
        console.log("du ja");
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
  