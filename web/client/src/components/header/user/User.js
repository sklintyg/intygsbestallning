import React from 'react';
import icon from './user-icon.png';
import styled from "styled-components";
import {HeaderSectionContainer, SingleTextRowContainer, VerticalContainer} from "../headerStyles";
import IbColors from "../../style/IbColors";


const UnitComponentWrapper = styled(HeaderSectionContainer)`
  flex: 1 1 auto;
  min-width: 120px;
  color: ${IbColors.IB_COLOR_20};
`
const UserTitleTitle = styled.div`
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  
`

const UserRoleTitle = styled.div`
  white-space: nowrap;
  
`
const HeaderIcon = styled.img`
  padding-right: 8px;
`


const User = ({userName, userRole}) => {
  return (
    <UnitComponentWrapper>
      <HeaderIcon src={icon} alt="unit-logo" />
      <VerticalContainer>
        <SingleTextRowContainer>
          <UserTitleTitle>{userName}</UserTitleTitle>
          <UserRoleTitle>&nbsp;-{userRole}</UserRoleTitle>
        </SingleTextRowContainer>
      </VerticalContainer>
    </UnitComponentWrapper>
  )
};


export default User;
