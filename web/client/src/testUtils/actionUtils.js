import configureMockStore from 'redux-mock-store'
import thunk from 'redux-thunk'

export const functionToTest = (store, actionHelper, expectedActions) =>
  store.dispatch(actionHelper()).then(() => {
    // return of async actions
    expect(store.getActions()).toEqual(expectedActions)
  });

export const routerAction = (url) => (
  {
  "payload": {"args": [url], "method": "push"},
  "type": "@@router/CALL_HISTORY_METHOD"}
  );

const middlewares = [thunk]
export const mockStore = configureMockStore(middlewares)
