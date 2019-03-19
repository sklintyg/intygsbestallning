import React from 'react';
import Logo from "./logo/Logo";
import Unit from "./unit/Unit";

import User from "./user/User";
import styled from "styled-components";
import IbColors from "../style/IbColors";
import {ActionButton, HeaderSectionContainerHoverable} from "./styles";
import ChangeEnhet from "./changeEnhet";
import Logout from "./logout/LogoutContainer";
import ibValues from '../style/IbValues'

const ComponentWrapper = styled.div`
  display: block;
  background-color: ${IbColors.IB_COLOR_17};
`

const StyledHeader = styled.div`
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  height: ${ibValues.headerHeight};
  background-color: ${IbColors.IB_COLOR_17};
  margin: auto;
  max-width: ${ibValues.maxContentWidth};
  color: ${IbColors.IB_COLOR_20};
`

const HeaderActionsWrapper = styled.div`
 display: flex;
  flex: 0 1 auto;
  justify-content: flex-end;
  
`

//TODO: this could be a property from backend in user object?
const canChangeEnhet = (vardgivare) => {
  let enheter = 0;
  if (vardgivare) {
    vardgivare.forEach((vg) => {
      vg.vardenheter.forEach((ve) => {
        enheter++;
        ve.mottagningar.forEach((mo) => {
          enheter++;
        })
      })
    });
  }
  return enheter > 1;
};



const Header = ({isAuthenticated, namn, userRole, valdVardgivare, valdVardenhet, vardgivare}) => {
  return (
    <ComponentWrapper>
      <StyledHeader>
        <Logo />
        {isAuthenticated && <User id="currentUser" userName={namn} userRole={userRole} />}
        {isAuthenticated && valdVardgivare && valdVardenhet &&
        <Unit valdVardgivare={valdVardgivare} valdVardenhet={valdVardenhet} />}
        <HeaderActionsWrapper>
          { isAuthenticated && canChangeEnhet(vardgivare) && valdVardenhet &&
          <HeaderSectionContainerHoverable>
            <ChangeEnhet/>
          </HeaderSectionContainerHoverable>}

          <HeaderSectionContainerHoverable>
            <ActionButton id="aboutLinkBtn">
              <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24" height="24" viewBox="0 0 24 24">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z" /></svg>
              <br />Om tjänsten </ActionButton>
          </HeaderSectionContainerHoverable>

          { isAuthenticated &&
          <HeaderSectionContainerHoverable>
            <Logout/>
          </HeaderSectionContainerHoverable>
          }
        </HeaderActionsWrapper>
      </StyledHeader>
    </ComponentWrapper>
  )
};

export default Header;
