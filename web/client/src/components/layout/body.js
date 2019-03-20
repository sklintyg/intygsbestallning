import styled from 'styled-components'
import ibValues from '../style/IbValues'

/**
 *
 * Typical usage is to nest
 * <FlexColumnContainer>
 *     <ScrollingContainer>
 *         <WorkareaContainer>
 * They can also be extendend  e.g to have different background etc.
 */
export const FlexColumnContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  
`

export const ScrollingContainer = styled.div`
  overflow-y: auto;
  background: #fff;
  height: 100%;
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
`
export const WorkareaContainer = styled.div`
  padding: 30px;
`


