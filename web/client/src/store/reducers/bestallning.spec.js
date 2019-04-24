import reducer, { DELETE_FAIL_MODAL_BODY, DELETE_FAIL_MODAL_HEADER } from './bestallning'
import * as actions from '../actions/bestallning'

describe('beställning reducer', () => {
  const stateBefore = { bestallning: { id: undefined, status: undefined }, errorMessage: null, fetching: false }

  test('should return initial state', () => {
    const stateAfter = { ...stateBefore }
    expect(reducer(undefined, {})).toEqual(stateAfter)
  })

  test('should return fetching true', () => {
    const action = {
      type: actions.FETCH_BESTALLNING_REQUEST,
    }
    const stateAfter = { ...stateBefore, fetching: true }
    expect(reducer({}, action)).toEqual(stateAfter)
  })

  test('should return beställning with id 1', () => {
    const bestallning = { id: 1 }
    const action = {
      type: actions.FETCH_BESTALLNING_SUCCESS,
      response: bestallning,
    }
    const stateAfter = { ...stateBefore, bestallning }
    expect(reducer({}, action)).toEqual(stateAfter)
  })

  test('should return ErrorMessage', () => {
    const errorMessage = { message: 'ANY', errorCode: 'BAD_REQUEST', logId: 'aaa' }
    const action = {
      type: actions.FETCH_BESTALLNING_FAILURE,
      payload: { error: errorMessage },
    }
    const stateAfter = { ...stateBefore, errorMessage: { logId: 'aaa', title: 'Tekniskt fel', message:'Prova igen om en stund.' } }
    expect(reducer({}, action)).toEqual(stateAfter)
  })

  test('should change state to Accepterad', () => {
    const status = 'Accepterad'
    const action = {
      type: actions.ACCEPTERA_BESTALLNING_SUCCESS,
    }
    const stateAfter = { ...stateBefore, bestallning: { ...stateBefore.bestallning, status } }
    expect(reducer(stateBefore, action)).toEqual(stateAfter)
  })

  test('should contain modal title and message if delete fails', () => {
    const errorMessage = { message: 'ANY', errorCode: 0 }
    const action = {
      type: actions.DELETE_BESTALLNING_FAILURE,
      payload: errorMessage,
    }
    const stateAfter = {
      ...stateBefore,
      bestallning: {},
      errorMessage: { ...errorMessage, modal: { message: '', title: DELETE_FAIL_MODAL_HEADER } },
    }
    expect(reducer({}, action)).toEqual(stateAfter)
  })
})
