import React, { Fragment } from 'react'

import ibColors from '../styles/IbColors'
import { IbTypo02 } from '../styles/IbTypography'
import ReactChartkick, { PieChart } from 'react-chartkick'
import Chart from 'chart.js'

ReactChartkick.addAdapter(Chart)
let options = { legend: { position: 'bottom', labels: { boxWidth: 14 } } }
const WelcomeChart = (props) => (
  <Fragment>
    <IbTypo02 as="h1">Beställningar just nu</IbTypo02>
    <PieChart
      width="200px"
      data={[['Olästa', props.stats.antalOlastaBestallningar], ['Aktiva', props.stats.antalAktivaBestallningar], ['Klara', props.stats.antalKlaraBestallningar]]}
      colors={[ibColors.IB_COLOR_21, ibColors.IB_COLOR_06, ibColors.IB_COLOR_16]}
      library={options}
      suffix=" st beställningar"
    />
  </Fragment>
)

export default WelcomeChart