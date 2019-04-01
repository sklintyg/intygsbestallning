import React, {Fragment} from 'react';
import PropTypes from "prop-types";
import {ActionButton} from "../styles";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {AboutIcon} from "../../styles/IbSvgIcons";


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
  handleOpen: PropTypes.func.isRequired,
  handleClose: PropTypes.func.isRequired,
  isOpen: PropTypes.bool.isRequired
};

export default About;
