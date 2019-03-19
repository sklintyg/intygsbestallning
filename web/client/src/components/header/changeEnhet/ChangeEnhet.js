import React, {Fragment} from 'react';
import Modal from 'react-responsive-modal';
import * as PropTypes from "prop-types";
import SelectEnhet from "../../selectEnhet";
import {ActionButton} from "../styles";
import IbColors from "../../style/IbColors";


const ChangeEnhet = ({handleOpen, handleClose, isOpen}) => {

  return (
    <Fragment>
      <ActionButton onClick={handleOpen} id="changeUnitBtn">
        <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24" height="24" viewBox="0 0 24 24">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM6.5 9L10 5.5 13.5 9H11v4H9V9H6.5zm11 6L14 18.5 10.5 15H13v-4h2v4h2.5z"/></svg>
        <br/>
        Byt enhet
      </ActionButton>
      <Modal open={isOpen} onClose={handleClose} center={true} modalId={'changeUnitDialog'}>
        <h2>Välj enhet</h2>
        <SelectEnhet />
      </Modal>
    </Fragment>
  );
};

ChangeEnhet.propTypes = {
  handleOpen: PropTypes.func,
  handleClose: PropTypes.func,
  modalStates: PropTypes.object
};

export default ChangeEnhet;
