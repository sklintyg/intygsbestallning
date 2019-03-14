import styled from 'styled-components'
import IbColors from "./IbColors";


export const IB_TYPO_01 = styled.div`
  font-weight: medium;
  font-size: 18px;
`

export const IB_TYPO_06 = styled.div`
  font-weight: medium;
  font-size: 14px;
`

//Base component for a internal link
export const InternalLink = styled.a`
  background: transparent
  cursor: pointer
  text-decoration: none;
  font-size: 14px;
  color: ${IbColors.IB_COLOR_08}
  
  :hover {
    text-decoration: underline;
    color: ${IbColors.IB_COLOR_21}
  }
  
`



