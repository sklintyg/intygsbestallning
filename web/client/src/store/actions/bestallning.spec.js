import * as actions from './bestallning'
import * as api from '../../api/bestallning'
import { functionToTest, mockStore } from '../../testUtils/actionUtils'

describe('actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({bestallning: {fetching: false} })
  })

  describe('fetchBestallning', () => {
    it('success', () => {
      const id = 1;
      const reponse = [{id: 1}]

      api.fetchBestallning = () => {
        return Promise.resolve(reponse)
      }

      const expectedActions = [
        { type: actions.FETCH_BESTALLNING_REQUEST, id },
        { type: actions.FETCH_BESTALLNING_SUCCESS, response: reponse, id }
      ]

      return functionToTest(store, () => (actions.fetchBestallning(id)), expectedActions)
    })

    it('failure', () => {
      const id = 1;
      api.fetchBestallning = () => {
        return Promise.reject({message: 'failed', statusCode: 'ERRORCODE'})
      }

      const expectedActions = [
        { type: actions.FETCH_BESTALLNING_REQUEST, id },
        { type: actions.FETCH_BESTALLNING_FAILURE, payload: { statusCode : "ERRORCODE", message : "failed"} , id }
      ]

      return functionToTest(store, () => (actions.fetchBestallning(id)), expectedActions)
    })
  })
})
