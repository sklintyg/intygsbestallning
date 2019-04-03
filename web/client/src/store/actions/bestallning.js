import * as api from '../../api/bestallning'
import { isFetching } from '../reducers/bestallning'
import {openModal} from "./modal";

export const FETCH_BESTALLNING_REQUEST = "FETCH_BESTALLNING_REQUEST";
export const FETCH_BESTALLNING_SUCCESS = "FETCH_BESTALLNING_SUCCESS";
export const FETCH_BESTALLNING_FAILURE = "FETCH_BESTALLNING_FAILURE";

export const ACCEPTERA_BESTALLNING_REQUEST = "ACCEPTERA_BESTALLNING_REQUEST";
export const ACCEPTERA_BESTALLNING_SUCCESS = "ACCEPTERA_BESTALLNING_SUCCESS";
export const ACCEPTERA_BESTALLNING_FAILURE = "ACCEPTERA_BESTALLNING_FAILURE";

export const REJECT_BESTALLNING_REQUEST = "REJECT_BESTALLNING_REQUEST";
export const REJECT_BESTALLNING_SUCCESS = "REJECT_BESTALLNING_SUCCESS";
export const REJECT_BESTALLNING_FAILURE = "REJECT_BESTALLNING_FAILURE";

export const DELETE_BESTALLNING_REQUEST = "DELETE_BESTALLNING_REQUEST";
export const DELETE_BESTALLNING_SUCCESS = "DELETE_BESTALLNING_SUCCESS";
export const DELETE_BESTALLNING_FAILURE = "DELETE_BESTALLNING_FAILURE";

export const COMPLETE_BESTALLNING_REQUEST = "COMPLETE_BESTALLNING_REQUEST";
export const COMPLETE_BESTALLNING_SUCCESS = "COMPLETE_BESTALLNING_SUCCESS";
export const COMPLETE_BESTALLNING_FAILURE = "COMPLETE_BESTALLNING_FAILURE";

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
        errorResponse => {
            dispatch({
                type: FETCH_BESTALLNING_FAILURE,
                id,
                payload: errorResponse
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
        errorResponse => {
            dispatch({
                type: ACCEPTERA_BESTALLNING_FAILURE,
                id,
                payload: errorResponse
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
        errorResponse => {
            dispatch({
                type: REJECT_BESTALLNING_FAILURE,
                id,
                payload: errorResponse
            });
        }
    );
};

export const completeBestallning = (id) => (dispatch) => {
  dispatch({
    type: COMPLETE_BESTALLNING_REQUEST,
    id
  });

  return api.completeBestallning(id).then(
    () => {
      dispatch({
        type: COMPLETE_BESTALLNING_SUCCESS
      });
    },
    errorResponse => {
      dispatch({
        type: COMPLETE_BESTALLNING_FAILURE,
        id,
        payload: errorResponse
      });
    }
  );
};

export const deleteBestallning = (id, fritextForklaring) => (dispatch) => {
  dispatch({
    type: DELETE_BESTALLNING_REQUEST,
    id
  });

  return api.deleteBestallning(id, fritextForklaring).then(
    () => {
      dispatch({
        type: DELETE_BESTALLNING_SUCCESS
      });
      dispatch(openModal('borttagenBestallning'));
    },
    errorResponse => {
      dispatch({
        type: DELETE_BESTALLNING_FAILURE,
        id,
        payload: errorResponse
      });
    }
  );
};
