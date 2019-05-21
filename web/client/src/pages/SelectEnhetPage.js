import React from 'react'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/ibLayout'
import SelectEnhet from '../components/selectEnhet'
import AppFooter from '../components/appFooter/AppFooter'
import styled from 'styled-components'
import ibValues from '../components/styles/IbValues'
import IbColors from '../components/styles/IbColors'
import {IbTypo02} from '../components/styles/IbTypography'

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`
const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;

  img {
    width: 100%;
    padding-bottom: 20px;
  }
`

const SelectEnhetPageHeader = styled.div`
  max-width: 500px;
`

const SelectEnhetPage = () => {
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <SelectEnhetPageHeader>
            <IbTypo02 as="h1" color={IbColors.IB_COLOR_07}>
              Välj enhet
            </IbTypo02>
            <p>Du har behörighet för flera vårdenheter. Välj den du vill logga in på nu. Du kan byta enhet även efter inloggning.</p>
            <SelectEnhet />
          </SelectEnhetPageHeader>
        </PageContainer>
        <AppFooter />
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

export default SelectEnhetPage
