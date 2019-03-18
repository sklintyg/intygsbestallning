import React from 'react';
import icon from './unit-icon.png';
import styled from "styled-components";
import {HeaderIcon, HeaderSectionContainer, SingleTextRowContainer, VerticalContainer} from "../styles";
import IbColors from "../../style/IbColors";

const UnitComponentWrapper = styled(HeaderSectionContainer)`
  flex: 1 1 auto;
  min-width: 120px;
  padding: 0 16px;
  color: ${IbColors.IB_COLOR_20};
`
const VardgivarTitle = styled.div`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`

const VardenhetTitle = styled.div`
  white-space: nowrap;
 
`

const Unit = ({valdVardgivare, valdVardenhet}) => {
  return (
    <UnitComponentWrapper>
      <HeaderIcon src={icon} alt="unit-logo" />
      <VerticalContainer>
        <SingleTextRowContainer>
          <VardgivarTitle id="currentVardgivarTitle">{valdVardgivare.namn}</VardgivarTitle>
          <VardenhetTitle id="currentVardenhetTitle">&nbsp;-{valdVardenhet.namn}</VardenhetTitle>
        </SingleTextRowContainer>
      </VerticalContainer>
    </UnitComponentWrapper>
  )
};

export default Unit;
