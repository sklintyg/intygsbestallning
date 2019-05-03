import React, { Fragment } from 'react'

import ibColors from '../styles/IbColors'
import { IbTypo02, IbTypo03 } from '../styles/IbTypography'
import styled from 'styled-components'
import ReactChartkick, { PieChart } from 'react-chartkick'
import Chart from 'chart.js'

const LegendBox = styled.span`
  display: inline-block;
  width: 14px;
  height: 14px;
  background-color: ${(props) => props.color};
`

const LegendText = styled(IbTypo03)`
  display: inline-block;
  padding-left: 8px;
`
const Legend = ({ color, text }) => (
  <div>
    <LegendBox color={color} />
    <LegendText as="span">{text}</LegendText>
  </div>
)
const customLabel = (tooltipItem, data) => {
  const dataValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]
  const label = data.labels[tooltipItem.index].toLowerCase()

  return dataValue + ' ' + label + ' beställningar.'
}

ReactChartkick.addAdapter(Chart)
let options = {
  legend: {
    display: false,
  },
  tooltips: {
    backgroundColor: ibColors.IB_COLOR_17,
    caretSize: 6,
    bodyFontFamily: 'Roboto',
    bodyFontSize: 12,
    cornerRadius: 3,
    xPadding: 10,
    yPadding: 7,
    callbacks: { label: customLabel },
  },
}
const WelcomeChart = (props) => (
  <Fragment>
    <IbTypo02 as="h1">Beställningar just nu</IbTypo02>
    <PieChart
      width="200px"
      height="220px"
      data={[
        ['Olästa', props.stats.antalOlastaBestallningar],
        ['Aktuella', props.stats.antalAktivaBestallningar],
        ['Färdighanterade', props.stats.antalKlaraBestallningar],
      ]}
      colors={[ibColors.IB_COLOR_21, ibColors.IB_COLOR_06, ibColors.IB_COLOR_16]}
      library={options}
    />
    <div>
      <Legend color={ibColors.IB_COLOR_21} text="Olästa" />
      <Legend color={ibColors.IB_COLOR_06} text="Aktuella" />
      <Legend color={ibColors.IB_COLOR_16} text="Färdighanterade" />
    </div>
  </Fragment>
)

export default WelcomeChart
