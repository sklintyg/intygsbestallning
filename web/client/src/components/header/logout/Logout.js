import React, {Fragment} from 'react';
import * as PropTypes from "prop-types";
import {ActionButton} from "../styles";
import {LogoutIcon} from "../../style/IbSvgIcons";

const Logout = (props) => {
  const {handleLogout} = props;


  return (
    <Fragment>
      <ActionButton onClick={handleLogout} id="logoutBtn">
        <LogoutIcon/>
        <br />
        Logga ut</ActionButton>
    </Fragment>
  );
}

Logout.propTypes = {
  handleLogout: PropTypes.func
}

export default Logout;
