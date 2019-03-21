import React from 'react';
import styled from 'styled-components'
import * as PropTypes from "prop-types";
import {CollapseIcon, ExpandIcon} from "../style/IbSvgIcons";


const TogglerTag = styled.a`
  padding: 4px;
`
const Toggler = ({expanded, handleToggle}) => (
  <TogglerTag onClick={handleToggle}>
    {expanded ? <ExpandIcon /> : <CollapseIcon />}
  </TogglerTag>
)


Toggler.propTypes = {
  expanded: PropTypes.bool,
  handleToggle: PropTypes.func
}

export default Toggler;
