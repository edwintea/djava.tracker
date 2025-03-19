import {
  Box,
  IconButton,
  lighten,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { lightText, pastelRed } from "../../../constants/Colours";
import { Delete, Edit, Visibility } from "@mui/icons-material";
import Tile from "../../layouts/Tile";
import React, { useEffect } from "react";
import { IPosition, positionStore } from "../../../stores/PositionStore";
import { getPositions } from "../../../api/service/PositionApi";
import { observer } from "mobx-react";

const PositionsTable = () => {
  const { positions } = positionStore;

  useEffect(() => {
    getPositions();
  }, []);

  const decideTopGridLine = (
    positions: IPosition[],
    position: IPosition,
    count: number
  ) => {
    if (count > 0) {
      if (positions[count - 1].departmentName === position.departmentName) {
        return 0;
      }
    } else {
      return 0;
    }
    return 1.5;
  };

  const toShowDepartmentNameOrNot = (
    positions: IPosition[],
    position: IPosition,
    count: number
  ) => {
    if (count > 0) {
      if (positions[count - 1].departmentName === position.departmentName) {
        return "";
      }
    }
    return position.departmentName;
  };

  return (
    <Tile title={"Positions"} titleLeftAlign>
      <Typography variant={"caption"} color={lightText}>
        {`${positions.length} positions found`}
      </Typography>
      <TableContainer component={Box}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ fontWeight: "bold" }} align="left">
                Department
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="center">
                Position
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="center">
                Employees
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="center" />
            </TableRow>
          </TableHead>
          <TableBody>
            {positions.map((position, count) => {
              return (
                <TableRow
                  key={position.id}
                  sx={{
                    "&:last-child td, &:last-child th": { border: 0 },
                    borderTop: decideTopGridLine(positions, position, count),
                    borderTopColor: lighten(lightText, 0.55),
                  }}
                >
                  <TableCell align="left">
                    <Typography sx={{ fontSize: 14 }}>
                      {toShowDepartmentNameOrNot(positions, position, count)}
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant={"caption"} color={lightText}>
                      {position.name}
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant={"caption"} color={lightText}>
                      {position.employeeCount}
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Box
                      sx={{
                        display: "flex",
                        flexDirection: "row",
                        alignItems: "center",
                        justifyContent: "center",
                      }}
                    >
                      <IconButton sx={{ color: lightText }}>
                        <Visibility fontSize={"small"} />
                      </IconButton>
                      <IconButton sx={{ color: lightText }}>
                        <Edit fontSize={"small"} />
                      </IconButton>
                      <IconButton sx={{ color: pastelRed }}>
                        <Delete fontSize={"small"} />
                      </IconButton>
                    </Box>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </Tile>
  );
};

export default observer(PositionsTable);
