import React from 'react';
import './Actions.css';

const Actions = () => {
  return (
    <div className="ib-header-actions">

      <div className="header-section-container d-lg-none">
        <button type="button" className="action-button text-nowrap" id="changeSystemRoleLinkBtn">
          Byt roll
        </button>
      </div>

      <div className="header-section-container hidden-sm hidden-xs">
        <button type="button" className="action-button text-nowrap" id="aboutLinkBtn">
         Om tjänsten
        </button>
      </div>

      <div className="header-section-container hidden-sm hidden-xs">
        <button type="button" className="action-button last text-nowrap" id="logoutLinkBtn">
          Logga ut
        </button>
      </div>

    </div>
  )
};

export default Actions;
