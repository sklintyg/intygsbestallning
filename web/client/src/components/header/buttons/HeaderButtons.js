import React from 'react';
import ChangeEnhet from "./changeEnhet";
import styled from "styled-components";
import {HeaderSectionContainer} from "../headerStyles";

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


const ActionButton = styled.div`
 text-align: center;
  background-color: transparent;
  border: none;
  width: 100%;

  color: #F2F2F2;
  
`
const ComponentWrapper = styled.div`
 display: flex;
  flex: 0 1 auto;
  justify-content: flex-end;
  
`



const HeaderButtons = ({isAuthenticated, vardgivare}) => {
  return (
    <ComponentWrapper>
      { isAuthenticated && canChangeEnhet(vardgivare) &&
      <HeaderSectionContainer>
        <ChangeEnhet className="action-button text-nowrap" id="changeSystemRoleLinkBtn"/>
      </HeaderSectionContainer>}

      <HeaderSectionContainer>
        <ActionButton id="aboutLinkBtn"> Om tjänsten </ActionButton>
      </HeaderSectionContainer>

      { isAuthenticated &&
      <HeaderSectionContainer>
        <ActionButton id="logoutLinkBtn"> Logga ut  </ActionButton>
      </HeaderSectionContainer>
      }
    </ComponentWrapper>
  )
};

export default HeaderButtons;
