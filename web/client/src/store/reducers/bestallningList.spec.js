import reducer, { BestallningListDefaultState } from './bestallningList'
import * as actions from '../actions/bestallningList'
import { createStore } from 'redux'

describe('bestallninglist reducer', () => {
  let stateBefore = {
    listBestallningarByFilter: {
      AKTUELLA: {
        bestallningList: { ...BestallningListDefaultState },
        errorMessage: null,
        isFetching: false,
      },
      AVVISADE: {
        bestallningList: { ...BestallningListDefaultState },
        errorMessage: null,
        isFetching: false,
      },
      KLARA: {
        bestallningList: { ...BestallningListDefaultState },
        errorMessage: null,
        isFetching: false,
      },
    },
  }

  let store
  beforeEach(() => {
    store = createStore(reducer)
  })

  test('should return default', () => {
    expect(reducer(undefined, {})).toEqual(stateBefore)
  })

  test('should return statebefore for aktuella', () => {
    expect(store.getState().listBestallningarByFilter.AKTUELLA).toEqual(stateBefore.listBestallningarByFilter.AKTUELLA)
  })

  test('should add item in bestallninList for aktuella', () => {
    let action = {
      type: actions.FETCH_BESTALLNINGAR_SUCCESS,
      categoryFilter: 'AKTUELLA',
      response: { id: 1 },
    }

    store.dispatch(action)

    let stateAfter = { ...stateBefore.listBestallningarByFilter.AKTUELLA, bestallningList: { id: 1 } }
    expect(store.getState().listBestallningarByFilter.AKTUELLA).toEqual(stateAfter)
  })
})
