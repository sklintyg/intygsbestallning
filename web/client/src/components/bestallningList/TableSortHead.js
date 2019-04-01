import React, { Fragment } from "react";
import { Button } from "reactstrap";
import { DownIcon, UpIcon, UpDownIcon } from "../styles/IbSvgIcons";

const TableSortHead = ({
  currentSortColumn,
  currentSortDirection,
  text,
  sortId,
  onSort
}) => {
  const handleSort = sortColumn => {
    onSort(sortColumn);
  };

  const renderSortIcon = sortColumn => {
    if (currentSortColumn === sortColumn) {
      return currentSortDirection === "DESC" ? <DownIcon /> : <UpIcon />;
    } else {
      return <UpDownIcon />;
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
          {text}
        </Button>{" "}
        {renderSortIcon(sortId)}
      </th>
    </Fragment>
  );
};

export default TableSortHead;
