import React from "react";

import BestallningFilter from "../components/bestallningList/BestallningFilter";
import BestallningListContainer from "../components/bestallningList/BestallningListContainer";
import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import BodyCenterWrapper from '../components/layout/body';

const BestallningarPage = () => (
  <BodyCenterWrapper>
    <BestallningFilterBar />
    <BestallningFilter />
    <BestallningListContainer />
  </BodyCenterWrapper>
);

export default BestallningarPage;
