import {GET_USER, GET_USER_FAILURE, GET_USER_SUCCESS} from "../actions/UserActions";

const INITIAL_STATE = {
  isLoading: false,
  isAuthenticated: false,
  errorMessage: null
};

export default (state = INITIAL_STATE, action) => {
  switch (action.type) {
  case GET_USER:
   return {...state, isLoading: true}
  case GET_USER_SUCCESS:
    return {...state,
      isLoading: false,
      isAuthenticated: true,
      namn: action.payload.namn,
      userRole: action.payload.currentRole.desc,
      valdVardgivare: action.payload.valdVardgivare,
      valdVardenhet: action.payload.valdVardenhet,
      vardgivare: action.payload.vardgivare
    }
  case GET_USER_FAILURE:
    return {...state, isLoading: false}

  default:
    return state;
  }
}
