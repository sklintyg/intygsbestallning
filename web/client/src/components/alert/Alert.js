import React from 'react';
import * as PropTypes from "prop-types";
import {InfoIcon, Security} from "../styles/IbSvgIcons";
import {Alert} from "reactstrap";
import IbColors from "../styles/IbColors";

//TODO: Add support for other types of alert-icons, such as error!
const IbAlert = ({type, children}) => {
  function getIcon(type) {
    switch (type) {
    case 'info':
      return <InfoIcon color={IbColors.IB_COLOR_06} />;
    case 'sekretess':
      return <Security color={IbColors.IB_COLOR_05} />;
    default:
      return null;
    }
  }

  return (
    <Alert color={type === 'sekretess' ? 'warning' : type }>
      <div className={'d-flex'}>
        <div>{getIcon(type)}</div>
        <div>{children}</div>
      </div>
    </Alert>
  );

}


IbAlert.propTypes = {
  type: PropTypes.string
}

export default IbAlert;
