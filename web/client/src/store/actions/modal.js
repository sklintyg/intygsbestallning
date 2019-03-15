export const OPEN_MODAL = 'OPEN_MODAL';
export const CLOSE_MODAL = 'CLOSE_MODAL';
export const CLOSE_ALL_MODALS = 'CLOSE_ALL_MODAL';

export const openModal = (id) => {
  return (dispatch) => {

    dispatch({
      type: OPEN_MODAL,
      payload: {
        id: id
      }
    });
  }
};

export const closeModal = (id) => {
  return (dispatch) => {

    dispatch({
      type: CLOSE_MODAL,
      payload: {
        id: id
      }
    });
  }
};
export const closeAllModals = () => {
  return (dispatch) => {

    dispatch({
      type: CLOSE_ALL_MODALS,
      payload: {}
    });
  }
};
