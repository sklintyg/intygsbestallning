import { makeServerRequest } from "./utils";

export const GET_USER = 'GET_USER';
export const GET_USER_SUCCESS = 'GET_USER_SUCCESS';
export const GET_USER_FAILURE = 'GET_USER_FAILURE';

export const getUser = () => {
  return (dispatch) => {

    dispatch({
      type: GET_USER
    });

    return makeServerRequest('anvandare').then(
      json => dispatch({
        type: GET_USER_SUCCESS,
        payload: json
      })
    ).catch(
      error => dispatch({
        type: GET_USER_FAILURE,
        payload: error.message
      })
    );
  }
};
