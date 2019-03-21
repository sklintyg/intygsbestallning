import React from 'react';
import styled from "styled-components";
import {HeaderSectionContainer, SingleTextRowContainer, VerticalContainer} from "../styles";
import IbColors from "../../style/IbColors";
import {UserIcon} from "../../style/IbSvgIcons";


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
      <UserIcon/>
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
