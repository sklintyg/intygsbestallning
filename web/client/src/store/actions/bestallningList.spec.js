import { functionToTest, mockStore } from '../../testUtils/actionUtils'
import * as actions from '../actions/bestallningList'
import * as api from '../../api/bestallningListApi'

describe('beställningList actions', () => {
  let store

  beforeEach(() => {
    store = mockStore({
      bestallningList: {
        listBestallningarByFilter: { AKTUELLA: { isFetching: false } },
      },
    })
  })

  describe('fetchBestallningList', () => {
    test('success', () => {
      const response = [{}]

      api.fetchBestallningList = () => {
        return Promise.resolve(response)
      }

      const expectedActions = [
        { type: actions.FETCH_BESTALLNINGAR_REQUEST, categoryFilter: 'AKTUELLA' },
        { type: actions.FETCH_BESTALLNINGAR_SUCCESS, categoryFilter: 'AKTUELLA', response },
      ]

      return functionToTest(
        store,
        () => actions.fetchBestallningList({ categoryFilter: 'AKTUELLA', textFilter: '', sortColumn: '', sortDirection: '' }),
        expectedActions
      )
    })

    test('failure', () => {
      const response = [{}]

      api.fetchBestallningList = () => {
        return Promise.reject(response)
      }

      const expectedActions = [
        { type: actions.FETCH_BESTALLNINGAR_REQUEST, categoryFilter: 'AKTUELLA' },
        { type: actions.FETCH_BESTALLNINGAR_FAILURE, categoryFilter: 'AKTUELLA', payload: response },
      ]

      return functionToTest(
        store,
        () => actions.fetchBestallningList({ categoryFilter: 'AKTUELLA', textFilter: '', sortColumn: '', sortDirection: '' }),
        expectedActions
      )
    })
  })
})
