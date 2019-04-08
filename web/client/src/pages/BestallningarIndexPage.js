import React from 'react'
import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/ibLayout'

const BestallningarIndexPage = () => (
  <FlexColumnContainer>
    <BestallningFilterBar />
    <ScrollingContainer>
      <WorkareaContainer>
        <h1>Du är nu inloggad i tjänsten Intygsbeställning</h1>
        <p>Här kan du ta del av och hantera de förfrågningar och beställningar av intyg som har inkommit till din vårdenhet.</p>
      </WorkareaContainer>
    </ScrollingContainer>
  </FlexColumnContainer>
)

export default BestallningarIndexPage
