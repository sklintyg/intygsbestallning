import React from "react";

import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import FilterListContainer from "../components/bestallningList/FilterListContainer";
import BodyCenterWrapper from '../components/layout/body';

const BestallningarPage = () => (
  <BodyCenterWrapper>
    <BestallningFilterBar />
    <FilterListContainer />
  </BodyCenterWrapper>
);

export default BestallningarPage;
