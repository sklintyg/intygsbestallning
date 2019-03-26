import * as actions from './user'
import * as api from '../../api/userApi';
import * as modals from "./modal";
import { routerAction, functionToTest, mockStore } from '../../testUtils/actionUtils';

describe('actions', () => {
  let store;

  beforeEach(() => {
    store = mockStore({ })
  })

  describe('getUser', () => {
    it('success', () => {
      const reponse = [{name: 'test'}]

      api.fetchAnvandare = () => {
        return Promise.resolve(reponse);
      }

      const expectedActions = [
        { type: actions.GET_USER },
        { type: actions.GET_USER_SUCCESS, payload: reponse }
      ]

      return functionToTest(store, actions.getUser, expectedActions)
    })

    it('failure', () => {
      api.fetchAnvandare = () => {
        return Promise.reject({message: 'failed'});
      }

      const expectedActions = [
        { type: actions.GET_USER },
        { type: actions.GET_USER_FAILURE, payload: 'failed' }
      ]

      return functionToTest(store, actions.getUser, expectedActions)
    })
  })

  describe('selectEnhet', () => {
    const tested = () => actions.selectEnhet('123');

    it('success', () => {
      const reponse = [{name: 'test'}];
      const modalAction = {
        type: 'test'
      };

      api.changeEnhet = () => {
        return Promise.resolve(reponse);
      };
      modals.closeAllModals = () => (modalAction);

      const expectedActions = [
        { type: actions.SET_ENHET },
        { type: actions.SET_ENHET_SUCCESS, payload: reponse },
        routerAction("/bestallningar"),
        modalAction
      ]

      return functionToTest(store, tested, expectedActions)
    })

    it('failure', () => {
      api.changeEnhet = () => {
        return Promise.reject({message: 'failed'});
      }

      const expectedActions = [
        { type: actions.SET_ENHET },
        { type: actions.SET_ENHET_FAILURE, payload: 'failed' }
      ]

      return functionToTest(store, tested, expectedActions)
    })
  })

  describe('logOut', () => {
    const tested = () => actions.logOut();

    it('success', () => {
      api.logoutUser = () => {
        return Promise.resolve({});
      };

      const expectedActions = [
        { type: actions.LOGOUT_USER },
        { type: actions.LOGOUT_SUCCESS, payload: {} },
        routerAction("/")
      ]

      return functionToTest(store, tested, expectedActions)
    })

    it('failure', () => {
      api.logoutUser = () => {
        return Promise.reject({message: 'failed'});
      }

      const expectedActions = [
        { type: actions.LOGOUT_USER },
        { type: actions.LOGOUT_FAILURE, payload: 'failed' }
      ]

      return functionToTest(store, tested, expectedActions)
    })
  })

})
