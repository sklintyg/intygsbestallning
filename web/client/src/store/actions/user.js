import {changeEnhet, fetchAnvandare, logoutUser} from "../../api/userApi";
import {push} from 'connected-react-router'
import {closeAllModals} from "./modal";

export const GET_USER = 'GET_USER';
export const GET_USER_SUCCESS = 'GET_USER_SUCCESS';
export const GET_USER_FAILURE = 'GET_USER_FAILURE';
export const LOGOUT_USER = 'LOGOUT_USER';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'LOGOUT_FAILURE';
export const SET_ENHET = 'SET_ENHET';
export const SET_ENHET_SUCCESS = 'SET_ENHET_SUCCESS';
export const SET_ENHET_FAILURE = 'SET_ENHET_FAILURE';

export const getUser = () => {
  return (dispatch) => {

    dispatch({
      type: GET_USER
    });

    return fetchAnvandare().then(
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

export const selectEnhet = (hsaId) => {
  return (dispatch) => {

    dispatch({
      type: SET_ENHET
    });
    return changeEnhet(hsaId).then(json => {
        dispatch({
          type: SET_ENHET_SUCCESS,
          payload: json
        });

        dispatch(push('/bestallningar'));

      dispatch(closeAllModals());

      }
    ).catch(
      error => dispatch({
        type: SET_ENHET_FAILURE,
        payload: error.message
      })
    );
  }
};

export const logOut = () => {
  return (dispatch) => {

    dispatch({
      type: LOGOUT_USER
    });

    return logoutUser().then(json => {
        dispatch({
          type: LOGOUT_SUCCESS,
          payload: {}
        });
        // Go to start page
        dispatch(push('/'));

      }
    ).catch(
      error => dispatch({
        type: LOGOUT_FAILURE,
        payload: error.message
      })
    );
  }
};
