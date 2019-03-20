import React from 'react';
import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from '../components/layout/body';

const BestallningarIndexPage = () => (
  <FlexColumnContainer>
    <ScrollingContainer>
      <WorkareaContainer>
        <BestallningFilterBar />
        Hej! Beställningar och pie chart etc. Klicka på ett filter ovan.
      </WorkareaContainer>
    </ScrollingContainer>
  </FlexColumnContainer>
);

export default BestallningarIndexPage;
