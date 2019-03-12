import React from 'react';
import './HeaderButtons.css';

//Todo: this could be a property from backend in user object?
const canChangeEnhet = (vardgivare) => {
  let enheter = 0;
  if (vardgivare) {
    vardgivare.forEach((vg) => {
      vg.vardenheter.forEach((ve) => {
        enheter++;
        ve.mottagningar.forEach((mo) => {
          enheter++;
        })
      })
    });
  }
  return enheter > 1;
};
const HeaderButtons = ({isAuthenticated, vardgivare}) => {
  return (
    <div className="ib-header-actions">
      { isAuthenticated && canChangeEnhet(vardgivare) &&
      <div className="header-section-container d-lg-none">
        <button type="button" className="action-button text-nowrap" id="changeSystemRoleLinkBtn">
          Byt roll
        </button>
      </div>}

      <div className="header-section-container hidden-sm hidden-xs">
        <button type="button" className="action-button text-nowrap" id="aboutLinkBtn">
         Om tjänsten
        </button>
      </div>

      { isAuthenticated &&
      <div className="header-section-container hidden-sm hidden-xs">
        <button type="button" className="action-button last text-nowrap" id="logoutLinkBtn">
          Logga ut
        </button>
      </div>
      }
    </div>
  )
};

export default HeaderButtons;
