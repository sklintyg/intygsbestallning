import React from 'react';
import './Unit.css';
import icon from './unit-icon.png';

const Unit = ({valdVardgivare, valdVardenhet}) => {
  return (
    <div className="ib-header-unit header-section-container">

      <img className="header-icon" src={icon} alt="unit-logo" />
      <div className="vertical-container">
        <div className="single-text-row-container">
          <div className="vg-name">{valdVardgivare.namn}</div>
          <div className="truncate-when-needed ve-name">&nbsp;-{valdVardenhet.namn}</div>
        </div>
      </div>
    </div>
  )
};

export default Unit;
