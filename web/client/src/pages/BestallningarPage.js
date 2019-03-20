import React from "react";

import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import FilterListContainer from "../components/bestallningList/FilterListContainer";
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from '../components/layout/body';

const BestallningarPage = () => (
  <FlexColumnContainer>
    <ScrollingContainer>
      <WorkareaContainer>
        <BestallningFilterBar />
        <FilterListContainer />
      </WorkareaContainer>
    </ScrollingContainer>
  </FlexColumnContainer>
);

export default BestallningarPage;
