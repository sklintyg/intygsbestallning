export const OPEN_MODAL = 'OPEN_MODAL';
export const CLOSE_MODAL = 'CLOSE_MODAL';
export const CLOSE_ALL_MODALS = 'CLOSE_ALL_MODAL';

export const openModal = (id) => {
  return {
    type: OPEN_MODAL,
    payload: {
      id
    }
  };
};

export const closeModal = (id) => {
  return {
    type: CLOSE_MODAL,
    payload: {
      id
    }
  }
};

export const closeAllModals = () => {
  return {
    type: CLOSE_ALL_MODALS,
    payload: {}
  }
};
