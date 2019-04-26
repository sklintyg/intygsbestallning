import {
  GET_USER,
  GET_USER_FAILURE,
  GET_USER_SUCCESS,
  SET_ENHET,
  SET_ENHET_FAILURE,
  SET_ENHET_SUCCESS
} from "../actions/user";
import { buildClientError } from "./util";

const INITIAL_STATE = {
  isLoading: true,
  isAuthenticated: false,
  activeError: null
};

export default (state = INITIAL_STATE, action) => {
  switch (action.type) {
  case GET_USER:
    return {...state, isLoading: true}
  case GET_USER_SUCCESS:
    return {
      ...state,
      isLoading: false,
      isAuthenticated: true,
      activeError: null,
      namn: action.payload.namn,
      userRole: action.payload.currentRole ? action.payload.currentRole.desc : '',
      unitContext: action.payload.unitContext,
      authoritiesTree: action.payload.authoritiesTree,
      totaltAntalVardenheter: action.payload.totaltAntalVardenheter,
      logoutUrl: action.payload.logoutUrl
    }
  case GET_USER_FAILURE:
    return {
      ...state,
      isLoading: false,
      isAuthenticated: false,
      activeError: buildClientError(action.payload, 'error.user')
    }

  case SET_ENHET:
    return {...state, isLoading: true}
  case SET_ENHET_SUCCESS:
    return {
      ...state,
      isLoading: false,
      isAuthenticated: true,
      activeError: null,
      namn: action.payload.namn,
      userRole: action.payload.currentRole ? action.payload.currentRole.desc : '',
      unitContext: action.payload.unitContext,
      authoritiesTree: action.payload.authoritiesTree,
      totaltAntalVardenheter: action.payload.totaltAntalVardenheter
    }
  case SET_ENHET_FAILURE:
    return {
      ...state,
      isLoading: false,
      activeError: buildClientError(action.payload, 'error.user')
    }
  default:
    return state;
  }
}
