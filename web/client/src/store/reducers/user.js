import {GET_USER, GET_USER_FAILURE, GET_USER_SUCCESS} from "../actions/UserActions";

const initialUser = {
  isLoading: false,
  isAuthenticated: false,
  errorMessage: null
};
export default (state = initialUser, action) => {
  switch (action.type) {
  case GET_USER:
   return {...state, isLoading: true}
  case GET_USER_SUCCESS:
    return {...state, isLoading: false, isAuthenticated: true, namn: action.payload.namn}
  case GET_USER_FAILURE:
    return {...state, isLoading: false}

  default: return state;
  }
}
