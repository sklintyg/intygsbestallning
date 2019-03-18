import React from 'react';
import * as PropTypes from "prop-types";
import {ActionButton} from "../styles";

const Logout = (props) => {
  const {handleLogout} = props;


  return (
      <ActionButton onClick={handleLogout} id="logoutBtn">Logga ut</ActionButton>
  );
}

Logout.propTypes = {
  handleLogout: PropTypes.func
}

export default Logout;
