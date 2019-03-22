import React, {Fragment} from 'react';
import * as PropTypes from "prop-types";
import {Button} from "reactstrap";


function LoginOptions(props) {
  const {selectLoginType} = props;

  const handleSelect = (loginType) => () => selectLoginType(loginType);

  return (
    <Fragment>
      <Button color="primary" outline={true} onClick={handleSelect('siths')}>Logga in med SITHS-kort &gt;</Button>
      <br />
      <Button color="primary" outline={true} onClick={handleSelect('sambi')}>Logga in via Sambi &gt;</Button>
    </Fragment>
  )
}

LoginOptions.propTypes = {
  selectLoginType: PropTypes.func
}

export default LoginOptions;
