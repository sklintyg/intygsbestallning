import React from "react";

const BestallningFraga = ({ props }) => (
  <div>
    <h2>{props.rubrik}</h2>
    {props.delfragor.map((c, i) => (
      <span key={i}>
        <h3>{c.etikett}</h3>
        <p>{c.text}</p>
      </span>
    ))}
  </div>
);

export default BestallningFraga;