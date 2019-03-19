import React from 'react';
import styled from "styled-components";
import {HeaderSectionContainer, SingleTextRowContainer, VerticalContainer} from "../styles";
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
  padding-left: 4px;
  
`

const UserRoleTitle = styled.div`
  white-space: nowrap;
  
`

const User = ({userName, userRole}) => {
  return (
    <UserComponentWrapper>
      <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="36" height="36" viewBox="0 0 24 24">
        <path
          d="M12,4A4,4 0 0,1 16,8A4,4 0 0,1 12,12A4,4 0 0,1 8,8A4,4 0 0,1 12,4M12,14C16.42,14 20,15.79 20,18V20H4V18C4,15.79 7.58,14 12,14Z" />
      </svg>
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
