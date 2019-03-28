import React, {Fragment} from "react";
import { Button } from "reactstrap";

const TableSortHead = ({ currentSortColumn, currentSortDirection, text, sortId, onSort }) => {

  const handleSort = sortColumn => {
    onSort(sortColumn);
  };

  const renderSortIcon = sortColumn => {
    if (currentSortColumn === sortColumn) {
      return currentSortDirection === "DESC" ? " v" : " ^";
    }
  };

  return (
    <Fragment>
      <th>
        <Button
          color="link"
          onClick={() => {
            handleSort(sortId);
          }}
        >
          {text}{renderSortIcon(sortId)}
        </Button>
      </th>
    </Fragment>
  );
};

export default TableSortHead;
