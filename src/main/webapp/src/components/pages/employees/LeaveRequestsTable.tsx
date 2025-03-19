import {
  Box,
  Chip,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  Typography,
} from "@mui/material";
import { lightText, pastelGreen, pastelRed } from "../../../constants/Colours";
import {
  ArrowRightRounded,
  Delete,
  Edit,
  Visibility,
} from "@mui/icons-material";
import Tile from "../../layouts/Tile";
import React, { ChangeEvent, useState } from "react";

const LeaveRequestsTable = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <>
      <Tile title={"New Leave Requests"} titleLeftAlign>
        <Typography variant={"caption"} color={lightText}>
          2 new leave requests found
        </Typography>
        <TableContainer component={Box}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }} align="center">
                  Employee
                </TableCell>
                <TableCell
                  sx={{
                    display: "flex",
                    flexDirection: "row",
                    fontWeight: "bold",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                  align="center"
                >
                  From
                  <ArrowRightRounded />
                  To
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="center">
                  Duration
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="center">
                  Status
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="center" />
              </TableRow>
            </TableHead>
            <TableBody>
              {[0, 1].map((transaction) => {
                return (
                  <TableRow
                    key={transaction}
                    sx={{
                      "&:last-child td, &:last-child th": { border: 0 },
                    }}
                  >
                    <TableCell align="center">20th January 2023</TableCell>
                    <TableCell align="center">
                      Lorem ipsum Lorem ipsum Lorem ipsum
                    </TableCell>
                    <TableCell align="center">2</TableCell>
                    <TableCell align="center">
                      <Chip
                        label={"New"}
                        variant={"outlined"}
                        sx={{ borderColor: pastelGreen }}
                      />
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
      <Tile title={"Leave Request History"} titleLeftAlign sx={{ mt: 5 }}>
        <Typography variant={"caption"} color={lightText}>
          20 leave requests found
        </Typography>
        <TableContainer component={Box}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }} align="center">
                  Employee
                </TableCell>
                <TableCell
                  sx={{
                    display: "flex",
                    flexDirection: "row",
                    fontWeight: "bold",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                  align="center"
                >
                  From
                  <ArrowRightRounded />
                  To
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="center">
                  Duration
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="center">
                  Status
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="center" />
              </TableRow>
            </TableHead>
            <TableBody>
              {[0, 1, 2, 3, 4, 5].map((transaction) => {
                return (
                  <TableRow
                    key={transaction}
                    sx={{
                      "&:last-child td, &:last-child th": { border: 0 },
                    }}
                  >
                    <TableCell align="center">20th January 2023</TableCell>
                    <TableCell align="center">
                      Lorem ipsum Lorem ipsum Lorem ipsum
                    </TableCell>
                    <TableCell align="center">2</TableCell>
                    <TableCell align="center">
                      <Chip
                        label={"Approved"}
                        variant={"filled"}
                        sx={{ backgroundColor: pastelGreen, color: "white" }}
                      />
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
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={6}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </TableContainer>
      </Tile>
    </>
  );
};

export default LeaveRequestsTable;
