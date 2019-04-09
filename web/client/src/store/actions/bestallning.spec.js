import * as actions from './bestallning'
import * as api from '../../api/bestallningApi'
import { functionToTest, mockStore } from '../../testUtils/actionUtils'

describe('beställning actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({bestallning: {fetching: false} })
  })

  describe('fetchBestallning', () => {
    it('success', () => {
      const id = 1;
      const response = [{id: 1}]

      api.fetchBestallning = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_BESTALLNING_REQUEST, id },
        { type: actions.FETCH_BESTALLNING_SUCCESS, response: response, id }
      ]

      return functionToTest(store, () => (actions.fetchBestallning(id)), expectedActions)
    })

    test('failure', () => {
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
