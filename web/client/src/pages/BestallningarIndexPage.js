import React from 'react'
import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/ibLayout'
import WelcomeContainer from '../components/welcome/WelcomeContainer'

const BestallningarIndexPage = () => (
  <FlexColumnContainer>
    <BestallningFilterBar />
    <ScrollingContainer>
      <WorkareaContainer>
        <WelcomeContainer />
      </WorkareaContainer>
    </ScrollingContainer>
  </FlexColumnContainer>
)

export default BestallningarIndexPage
