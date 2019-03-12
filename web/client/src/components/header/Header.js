import React from 'react';
import Logo from "./logo/Logo";
import Unit from "./unit/Unit";
import HeaderButtons from "./buttons/HeaderButtons";

import "./Header.css"
import User from "./user/User";


const Header = ({isAuthenticated, namn, userRole, valdVardgivare, valdVardenhet, vardgivare}) => {
  return (
    <div className="header-wrapper">
      <div className="header">
        <Logo/>
        { isAuthenticated && <User userName={namn} userRole={userRole}/>}
        { isAuthenticated && valdVardgivare && valdVardenhet && <Unit valdVardgivare={valdVardgivare} valdVardenhet={valdVardenhet}/>}
        <HeaderButtons isAuthenticated={isAuthenticated} vardgivare={vardgivare}/>
      </div>
    </div>
  )
};

export default Header;
