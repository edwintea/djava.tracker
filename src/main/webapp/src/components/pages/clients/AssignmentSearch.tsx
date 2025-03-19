import {
  Box,
  Grid,
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
import Tile from "../../layouts/Tile";
import { Delete, Edit, Visibility } from "@mui/icons-material";
import { lightText, pastelRed } from "../../../constants/Colours";
import React, { ChangeEvent, useEffect, useState } from "react";
import { getAssignmentPageable } from "../../../api/service/AssignmentApi";
import { assignmentStore } from "../../../stores/AssignmentStore";
import { assignmentTypeFormat } from "../../../utility/Formatter";

const AssignmentSearch = () => {
  const { assignments } = assignmentStore;
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [count, setCount] = useState(0);

  const getAssignments = (
    newPage: number = page,
    rows: number = rowsPerPage
  ) => {
    getAssignmentPageable(newPage, rows).then((result) => {
      if (result) {
        const resultCount = result.headers
          ? Number(result.headers["x-total-count"])
          : 0;
        setCount(resultCount);
      }
    });
  };

  useEffect(() => {
    getAssignments();
  }, []);

  const handleChangePage = (event: unknown, newPage: number) => {
    getAssignments(newPage);
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    const row = parseInt(event.target.value, 10);
    getAssignments(0, row);
    setRowsPerPage(row);
    setPage(0);
  };

  return (
    <Grid container spacing={5}>
      <Grid item xs={12} md={12}>
        <Tile title={"Assignments"} titleLeftAlign>
          <Typography variant={"caption"} color={lightText}>
            {`${count} assignments found`}
          </Typography>
          <TableContainer component={Box}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Client Name</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="center">
                    Type
                  </TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="center">
                    Employees
                  </TableCell>
                  <TableCell />
                </TableRow>
              </TableHead>
              <TableBody>
                {assignments.map((assignment) => (
                  <TableRow
                    key={assignment.id}
                    sx={{
                      "&:last-child td, &:last-child th": { border: 0 },
                    }}
                  >
                    <TableCell component="th" scope="row">
                      {assignment.clientName}
                    </TableCell>
                    <TableCell align="center">
                      <Typography variant={"caption"} color={lightText}>
                        {assignmentTypeFormat(assignment.assignmentType)}
                      </Typography>
                    </TableCell>
                    <TableCell align="center">
                      <Typography variant={"caption"} color={lightText}>
                        {assignment.employeeCount}
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
                ))}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={count}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </Tile>
      </Grid>
    </Grid>
  );
};

export default AssignmentSearch;
