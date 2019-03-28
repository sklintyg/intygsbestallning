import React from 'react';
import * as PropTypes from "prop-types";
import {InfoIcon, Security, Check, ErrorOutline, Warning} from "../styles/IbSvgIcons";
import IbColors from "../styles/IbColors";
import styled from 'styled-components'

const IbAlert = ({type, children}) => {
  function getIcon(type) {
    switch (type) {
    case 'info':
      return <InfoIcon color={IbColors.IB_COLOR_06} />;
    case 'sekretess':
      return <Security color={IbColors.IB_COLOR_05} />;
    case 'observandum':
      return <ErrorOutline color={IbColors.IB_COLOR_05} />;
    case 'confirm':
      return <Check color={IbColors.IB_COLOR_30} />;
    case 'error':
      return <Warning color={IbColors.IB_COLOR_04} />;
    default:
      return null;
    }
  }
  
  function getColor(type) {
    switch (type) {
    case 'info':
      return {text:IbColors.IB_COLOR_06, background:IbColors.IB_COLOR_03};
    case 'sekretess':
      return {text:IbColors.IB_COLOR_05, background:IbColors.IB_COLOR_02};
    case 'observandum':
      return {text:IbColors.IB_COLOR_05, background:IbColors.IB_COLOR_02};
    case 'confirm':
      return {text:IbColors.IB_COLOR_30, background:IbColors.IB_COLOR_29};
    case 'error':
      return {text:IbColors.IB_COLOR_04, background:IbColors.IB_COLOR_01};
    default:
      return {text:'#fff', background:'#000'};
    }
  }

  const Alert = styled.div`
    color: ${getColor(type).text};
    background-color: ${getColor(type).background};
    position: relative;
    border-radius: 4px;
    display: inline-block;
    svg {
      position: absolute;
      top: 2px;
      width: 16px;
    }
    div {
      margin-left: 20px;
    }
    padding: 4px 8px;
  `

  return (
    <Alert>
      {getIcon(type)}
      <div>{children}</div>
    </Alert>
  );

}


IbAlert.propTypes = {
  type: PropTypes.string
}

export default IbAlert;
