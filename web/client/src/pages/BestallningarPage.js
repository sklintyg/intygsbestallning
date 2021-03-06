import React from "react";

import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import FilterListContainer from "../components/bestallningList/FilterListContainer";
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from '../components/styles/ibLayout';

const BestallningarPage = () => (
  <FlexColumnContainer>
    <BestallningFilterBar />
    <ScrollingContainer>
      <WorkareaContainer>
        <FilterListContainer />
      </WorkareaContainer>
    </ScrollingContainer>
  </FlexColumnContainer>
);

export default BestallningarPage;
