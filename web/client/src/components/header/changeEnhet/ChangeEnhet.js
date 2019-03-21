import React, {Fragment} from 'react';
import Modal from 'react-responsive-modal';
import * as PropTypes from "prop-types";
import SelectEnhet from "../../selectEnhet";
import {ActionButton} from "../styles";
import {ChangeUnitIcon} from "../../style/IbSvgIcons";


const ChangeEnhet = ({handleOpen, handleClose, isOpen}) => {

  return (
    <Fragment>
      <ActionButton onClick={handleOpen} id="changeUnitBtn">
        <ChangeUnitIcon/>
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
