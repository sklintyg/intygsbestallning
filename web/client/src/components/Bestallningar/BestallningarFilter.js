import React, {Component} from "react";

class BestallningarFilter extends Component {
  render() {
    let value = "";
    return (
      <div>
        <div>Filter</div>
        <input ref={value} />
      </div>
    );
  }
}

export default BestallningarFilter;
