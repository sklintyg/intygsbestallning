import React from 'react';
import Modal from 'react-responsive-modal';
import * as PropTypes from "prop-types";
import SelectEnhet from "../../../selectEnhet";


function ChangeEnhet(props) {
  const {handleOpen, handleClose, isOpen} = props;


  return (
    <div>
      <button onClick={handleOpen}>Byt enhet</button>
      <Modal open={isOpen} onClose={handleClose} center={true} modalId={'change-enhet-dialog'}>
        <h2>Välj enhet</h2>
        <SelectEnhet />

      </Modal>
    </div>
  );
}

ChangeEnhet.propTypes = {
  handleOpen: PropTypes.func,
  handleClose: PropTypes.func,
  modalStates: PropTypes.object
}

export default ChangeEnhet;
