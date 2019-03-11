const byId = (state = {}, action) => {
  if (action.response) {
    return {
      ...state,
      ...action.response.entities.bestallningar,
    };
  }
  return state;
};

export default byId;

export const getBestallning = (state, id) => state[id];
