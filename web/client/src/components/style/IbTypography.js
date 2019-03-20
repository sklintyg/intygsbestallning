import styled from 'styled-components'
import IbColors from "./IbColors";

export const IB_TYPO_01 = styled.div`
  font-weight: 300;
  font-size: 25px;
`

export const IB_TYPO_02 = styled.div`
  font-weight: 500;
  font-size: 18px;
`

export const IB_TYPO_03 = styled.div`
  font-weight: 300;
  font-size: 18px;
`

export const IB_TYPO_05 = styled.div`
  font-weight: 500;
  font-size: 16px;
`

export const IB_TYPO_06 = styled.div`
  font-weight: 500;
  font-size: 14px;
`

export const IB_TYPO_07 = styled.div`
  font-weight: 400;
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



