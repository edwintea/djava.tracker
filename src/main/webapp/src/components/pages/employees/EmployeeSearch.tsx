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
import { Edit, Visibility } from "@mui/icons-material";
import { lightText } from "../../../constants/Colours";
import React, { ChangeEvent, useEffect, useState } from "react";
import { observer } from "mobx-react";
import { employeeStore } from "../../../stores/EmployeeStore";
import { getEmployeesPaged } from "../../../api/service/EmployeeApi";
import { isEmpty } from "../../../utility/StringUtil";
import { profileStore } from "../../../stores/ProfileStore";
import { useNavigate } from "react-router-dom";
import { positionStore } from "../../../stores/PositionStore";
import { departmentStore } from "../../../stores/DepartmentStore";
import { assignmentTypeFormat, prettifyDate } from "../../../utility/Formatter";

const EmployeeSearch = () => {
  const { employees } = employeeStore;
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [count, setCount] = useState(0);

  const navigate = useNavigate();

  const getEmployees = (newPage: number = page, rows: number = rowsPerPage) => {
    getEmployeesPaged(newPage, rows).then((result) => {
      if (result) {
        const resultCount = result.headers
          ? Number(result.headers["x-total-count"])
          : 0;
        setCount(resultCount);
      }
    });
  };

  useEffect(() => {
    getEmployees();
  }, []);

  const handleChangePage = (event: unknown, newPage: number) => {
    getEmployees(newPage);
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    const row = parseInt(event.target.value, 10);
    getEmployees(0, row);
    setRowsPerPage(row);
    setPage(0);
  };

  return (
    <Grid container spacing={5}>
      <Grid item xs={12} md={12}>
        <Tile title={"Employees"} titleLeftAlign>
          <Typography variant={"caption"} color={lightText}>
            {`${count} employees found`}
          </Typography>
          <TableContainer component={Box}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Name</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="center">
                    Employee Since
                  </TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="center">
                    Position
                  </TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="center">
                    Current Assignment
                  </TableCell>
                  <TableCell />
                </TableRow>
              </TableHead>
              <TableBody>
                {employees.map((employee) => {
                  const { positions } = positionStore;
                  const { departments } = departmentStore;
                  const position = positions.find(
                    (pos) => pos.id === employee.position
                  );
                  const department = departments.find(
                    (dep) => dep.id === employee.department
                  );

                  return (
                    <TableRow
                      key={employee.fullName}
                      sx={{
                        "&:last-child td, &:last-child th": { border: 0 },
                      }}
                    >
                      <TableCell component="th" scope="row">
                        <Box
                          sx={{
                            display: "flex",
                            flexDirection: "row",
                            alignItems: "center",
                          }}
                        >
                          <Typography sx={{ fontSize: 14 }}>
                            {employee.fullName}
                          </Typography>
                          {profileStore.profile.employee.id === employee.id && (
                            <Typography
                              sx={{ ml: 1, fontStyle: "italic" }}
                              variant={"caption"}
                            >
                              (You)
                            </Typography>
                          )}
                        </Box>
                      </TableCell>
                      <TableCell align="center">
                        <Typography variant={"caption"} color={lightText}>
                          {isEmpty(employee.sinceDate)
                            ? "Not Set"
                            : prettifyDate(employee.sinceDate)}
                        </Typography>
                      </TableCell>
                      <TableCell align="center">
                        <Typography variant={"caption"} color={lightText}>
                          {isEmpty(employee.position)
                            ? "Not Set"
                            : position
                            ? position.name
                            : "Position not found"}
                        </Typography>
                        <br />
                        {isEmpty(employee.position) ? (
                          <></>
                        ) : (
                          <Typography
                            variant={"caption"}
                            color={lightText}
                            fontWeight={"bold"}
                          >
                            {department ? department.name : ""}
                          </Typography>
                        )}
                      </TableCell>
                      <TableCell align="center">
                        <Typography variant={"caption"} color={lightText}>
                          {employee.isAssignable ? (
                            employee.employeeAssignment ? (
                              <>
                                <Typography
                                  variant={"caption"}
                                  color={lightText}
                                >
                                  {assignmentTypeFormat(
                                    employee.employeeAssignment.assignmentType
                                  )}
                                </Typography>
                                <br />
                                <Typography
                                  variant={"caption"}
                                  color={lightText}
                                  fontWeight={"bold"}
                                >
                                  {employee.employeeAssignment.clientName}
                                </Typography>
                              </>
                            ) : (
                              "Not Set"
                            )
                          ) : (
                            "Not Assignable"
                          )}
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
                          <IconButton
                            sx={{ color: lightText }}
                            onClick={() =>
                              navigate(`/employees/update/${employee.id}`)
                            }
                          >
                            <Edit fontSize={"small"} />
                          </IconButton>
                        </Box>
                      </TableCell>
                    </TableRow>
                  );
                })}
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

export default observer(EmployeeSearch);
