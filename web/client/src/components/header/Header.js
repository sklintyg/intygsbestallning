import React from 'react';
import Logo from "./logo/Logo";
import Unit from "./unit/Unit";

import User from "./user/User";
import styled from "styled-components";
import IbColors from "../style/IbColors";
import {ActionButton, HeaderSectionContainer} from "./styles";
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
          <HeaderSectionContainer>
            <ChangeEnhet/>
          </HeaderSectionContainer>}

          <HeaderSectionContainer>
            <ActionButton id="aboutLinkBtn"> Om tjänsten </ActionButton>
          </HeaderSectionContainer>

          { isAuthenticated &&
          <HeaderSectionContainer>
            <Logout/>
          </HeaderSectionContainer>
          }
        </HeaderActionsWrapper>
      </StyledHeader>
    </ComponentWrapper>
  )
};

export default Header;
