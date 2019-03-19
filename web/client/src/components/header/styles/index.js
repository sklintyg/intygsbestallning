import styled from "styled-components";
import dividerImage from './divider-border.png'

export const HeaderSectionContainer = styled.div`
  display: flex;
  align-items: center;
  height: 100%;
  border-width: 0 6px;
  border-style: solid;
  border-image-source: url(${dividerImage});
  border-image-slice: 0 50%;
  border-image-repeat: round;
  
`

export const HeaderIcon = styled.img`
  padding-right: 8px;
`

export const VerticalContainer = styled.div`
  flex: 0 1 auto;

  display: flex;
  flex-direction: column;
  min-width: 1px;
`

export const SingleTextRowContainer = styled.div`
 display: flex;
 min-width: 1px;
`
export const ActionButton = styled.button`
  text-align: center
  background-color: transparent
  border: none
  cursor: pointer
  color: #F2F2F2
  margin: 8px
  
`
