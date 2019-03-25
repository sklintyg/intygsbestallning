import React, {Fragment} from 'react';
import * as PropTypes from "prop-types";
import {ActionButton} from "../styles";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {AboutIcon} from "../../style/IbSvgIcons";


const About = ({handleOpen, handleClose, isOpen}) => {

  return (
    <Fragment>
      <ActionButton onClick={handleOpen} id="changeUnitBtn">
        <AboutIcon />
        <br />
        Om tjänsten
      </ActionButton>
      <Modal isOpen={isOpen} size={'lg'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Om intygsbeställning</ModalHeader>
        <ModalBody>
          Om Intygsbeställning
        </ModalBody>
        <ModalFooter>
          <Button color={'secondary'} outline={true} onClick={handleClose}>Stäng</Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  );
};

About.propTypes = {
  handleOpen: PropTypes.func,
  handleClose: PropTypes.func,
  isOpen: PropTypes.bool
};

export default About;
