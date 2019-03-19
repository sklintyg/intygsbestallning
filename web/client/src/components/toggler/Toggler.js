import React from 'react';
import styled from 'styled-components'
import * as PropTypes from "prop-types";
import IbColors from "../style/IbColors";


const TogglerTag = styled.a`
  padding: 4px;
`


//TODO: change to use icons for expanded state
const Toggler = ({expanded, handleToggle}) => (
  <TogglerTag onClick={handleToggle}>
    <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_08} width="24" height="24" viewBox="0 0 24 24">
      {expanded ? <path d="M7.41,15.41L12,10.83L16.59,15.41L18,14L12,8L6,14L7.41,15.41Z" /> :
        <path d="M7.41,8.58L12,13.17L16.59,8.58L18,10L12,16L6,10L7.41,8.58Z" />}</svg>
  </TogglerTag>
)


Toggler.propTypes = {
  expanded: PropTypes.bool,
  handleToggle: PropTypes.func
}

export default Toggler;
