import React, { Fragment } from 'react'

import ibColors from '../styles/IbColors'
import { IbTypo02 } from '../styles/IbTypography'
import ReactChartkick, { PieChart } from 'react-chartkick'
import Chart from 'chart.js'

const customLabel = (tooltipItem, data) => {
  const dataValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]
  const label = data.labels[tooltipItem.index].toLowerCase()

  return dataValue + ' ' + label + ' beställningar'
}

ReactChartkick.addAdapter(Chart)
let options = {
  legend: {
    position: 'bottom',
    onClick: null,
    labels: { boxWidth: 14, fontSize: 18, fontFamily: 'Roboto', fontColor: ibColors.IB_COLOR_07 },
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
      width="300px"
      data={[
        ['Olästa', props.stats.antalOlastaBestallningar],
        ['Aktuella', props.stats.antalAktivaBestallningar],
        ['Färdighanterade', props.stats.antalKlaraBestallningar],
      ]}
      colors={[ibColors.IB_COLOR_21, ibColors.IB_COLOR_06, ibColors.IB_COLOR_16]}
      library={options}
    />
  </Fragment>
)

export default WelcomeChart
