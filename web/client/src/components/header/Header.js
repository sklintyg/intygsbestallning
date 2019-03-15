import React from 'react';
import Logo from "./logo/Logo";
import Unit from "./unit/Unit";
import HeaderButtons from "./buttons/HeaderButtons";

import User from "./user/User";
import styled from "styled-components";
import IbColors from "../style/IbColors";

const ComponentWrapper = styled.div`
  display: block;
  background-color: ${IbColors.IB_COLOR_17};
`
const StyledHeader = styled.div`
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  height: 80px;
  background-color: ${IbColors.IB_COLOR_17};
  margin: auto;
  max-width: 1440px;
  color: ${IbColors.IB_COLOR_20};
`


const Header = ({isAuthenticated, namn, userRole, valdVardgivare, valdVardenhet, vardgivare}) => {
  return (
    <ComponentWrapper>
      <StyledHeader>
        <Logo />
        {isAuthenticated && <User userName={namn} userRole={userRole} />}
        {isAuthenticated && valdVardgivare && valdVardenhet &&
        <Unit valdVardgivare={valdVardgivare} valdVardenhet={valdVardenhet} />}
        <HeaderButtons isAuthenticated={isAuthenticated} vardgivare={vardgivare} />
      </StyledHeader>
    </ComponentWrapper>
  )
};

export default Header;
