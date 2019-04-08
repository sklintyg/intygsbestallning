import React from 'react'
import BestallningFilterBar from '../components/bestallningFilterBar/BestallningFilterBar'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../components/styles/ibLayout'
import ibColors from '../components/styles/IbColors'
import {IbTypo02, IbTypo07} from '../components/styles/IbTypography'
import ReactChartkick, { PieChart } from 'react-chartkick'
import Chart from 'chart.js'
import styled from 'styled-components'

const Color07 = styled.div`
  color: ${ibColors.IB_COLOR_07};
`

const WelcomeContainer = styled.div`
  display: flex;
  flex-direction: row;
  padding: 50px;
`
const WelcomeTextCol = styled.div`
  flex: 1 0 20%;
  padding-right: 10%;
`
const WelcomeChartCol = styled.div`
  flex: 1 0 auto;
`

ReactChartkick.addAdapter(Chart)
let options = {legend:{position:'bottom', labels:{boxWidth: 14}}}
const BestallningarIndexPage = () => (
  <FlexColumnContainer>
    <BestallningFilterBar />
    <ScrollingContainer>
      <WorkareaContainer>
        <WelcomeContainer>
          <WelcomeTextCol>
          <Color07>
            <IbTypo02 as="h1">Du är nu inloggad i tjänsten Intygsbeställning</IbTypo02>
            <IbTypo07 as="p">Här kan du ta del av och hantera de förfrågningar och beställningar av intyg som har inkommit till din vårdenhet.</IbTypo07>
          </Color07>
          </WelcomeTextCol>
          <WelcomeChartCol>
            <IbTypo02 as="h1">Beställningar just nu</IbTypo02>
            <PieChart width="200px" data={[["Olästa", 44], ["Aktiva", 23], ["Klara", 12]]}  colors={[ibColors.IB_COLOR_21, ibColors.IB_COLOR_06, ibColors.IB_COLOR_16]} library={options} suffix=" st beställningar" />
          </WelcomeChartCol>
        </WelcomeContainer>
      </WorkareaContainer>
    </ScrollingContainer>
  </FlexColumnContainer>
)

export default BestallningarIndexPage
