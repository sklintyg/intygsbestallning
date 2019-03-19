import React from 'react';
import styled from "styled-components";
import {HeaderSectionContainer, SingleTextRowContainer, VerticalContainer} from "../styles";
import IbColors from "../../style/IbColors";

const UnitComponentWrapper = styled(HeaderSectionContainer)`
  flex: 1 1 auto;
  min-width: 120px;
  padding: 0 16px;
  color: ${IbColors.IB_COLOR_20};
  height: 100%;
`
const VardgivarTitle = styled.div`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-left: 4px;
`

const VardenhetTitle = styled.div`
  white-space: nowrap;
 
`

const Unit = ({valdVardgivare, valdVardenhet}) => {
  return (
    <UnitComponentWrapper>
      <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="36px" height="36px" viewBox="0 0 24 24">
        <path
          d="M5,3V21H11V17.5H13V21H19V3H5M7,5H9V7H7V5M11,5H13V7H11V5M15,5H17V7H15V5M7,9H9V11H7V9M11,9H13V11H11V9M15,9H17V11H15V9M7,13H9V15H7V13M11,13H13V15H11V13M15,13H17V15H15V13M7,17H9V19H7V17M15,17H17V19H15V17Z"  shapeRendering="crispEdges"/>
      </svg>
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
