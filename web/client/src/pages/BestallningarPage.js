import React from "react";

import BestallningarFilter from "../components/bestallningar/BestallningarFilter";
import BestallningarContainer from "../components/bestallningar/BestallningarContainer";
import Navbar from "../components/navbar/Navbar"

const BestallningarPage = ({ match }) => (
  <div>
    <Navbar />
    <div>F:{match.params.filter}</div>
    <BestallningarFilter />
    <BestallningarContainer />
  </div>
);

export default BestallningarPage;
