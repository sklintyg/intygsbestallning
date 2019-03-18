import React from 'react';
import icon from './user-icon.png';
import styled from "styled-components";
import {HeaderIcon, HeaderSectionContainer, SingleTextRowContainer, VerticalContainer} from "../styles";
import IbColors from "../../style/IbColors";


const UserComponentWrapper = styled(HeaderSectionContainer)`
  flex: 1 1 auto;
  min-width: 120px;
  padding: 0 16px;
  color: ${IbColors.IB_COLOR_20};
`
const UserTitle = styled.div`
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  
`

const UserRoleTitle = styled.div`
  white-space: nowrap;
  
`

const User = ({userName, userRole}) => {
  return (
    <UserComponentWrapper>
      <HeaderIcon src={icon} alt="unit-logo" />
      <VerticalContainer>
        <SingleTextRowContainer>
          <UserTitle id="currentUserTitle">{userName}</UserTitle>
          <UserRoleTitle id="currentUserRoleTitle">&nbsp;-{userRole}</UserRoleTitle>
        </SingleTextRowContainer>
      </VerticalContainer>
    </UserComponentWrapper>
  )
};


export default User;
