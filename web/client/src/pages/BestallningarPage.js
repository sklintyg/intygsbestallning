import React from "react";

import BestallningarFilter from "../components/Bestallningar/BestallningarFilter";
import BestallningarContainer from "../components/Bestallningar/BestallningarContainer";
import Navbar from "../components/Navbar/Navbar"

const BestallningarPage = ({ match }) => (
  <div>
    <Navbar />
    <div>F:{match.params.filter}</div>
    <BestallningarFilter />
    <BestallningarContainer />
  </div>
);

export default BestallningarPage;
