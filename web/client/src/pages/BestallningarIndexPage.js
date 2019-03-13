import React from 'react';
import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import BodyCenterWrapper from '../components/layout/body';

const BestallningarIndexPage = () => (
  <BodyCenterWrapper>
    <BestallningFilterBar/>
    Hej! Beställningar och pie chart etc. Klicka på ett filter ovan.
  </BodyCenterWrapper>
);

export default BestallningarIndexPage;