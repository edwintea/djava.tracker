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
import { getClientsPaged } from "../../../api/service/ClientApi";
import { observer } from "mobx-react";
import { clientStore } from "../../../stores/ClientStore";
import { prettifyDate } from "../../../utility/Formatter";

const ClientSearch = () => {
  const { clients } = clientStore;
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [count, setCount] = useState(0);

  const getClients = (newPage: number = page, rows: number = rowsPerPage) => {
    getClientsPaged(newPage, rows).then((result) => {
      if (result) {
        const resultCount = result.headers
          ? Number(result.headers["x-total-count"])
          : 0;
        setCount(resultCount);
      }
    });
  };

  useEffect(() => {
    getClients();
  }, []);

  const handleChangePage = (event: unknown, newPage: number) => {
    getClients(newPage);
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    const row = parseInt(event.target.value, 10);
    getClients(0, row);
    setRowsPerPage(row);
    setPage(0);
  };

  return (
    <Grid container spacing={5}>
      <Grid item xs={12} md={12}>
        <Tile title={"Clients"} titleLeftAlign>
          <Typography variant={"caption"} color={lightText}>
            {`${count} clients found`}
          </Typography>
          <TableContainer component={Box}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Name</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="center">
                    Client Since
                  </TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="center">
                    Employees Assigned
                  </TableCell>
                  <TableCell />
                </TableRow>
              </TableHead>
              <TableBody>
                {clients.map((client) => (
                  <TableRow
                    key={client.id}
                    sx={{
                      "&:last-child td, &:last-child th": { border: 0 },
                    }}
                  >
                    <TableCell component="th" scope="row">
                      {client.name}
                    </TableCell>
                    <TableCell align="center">
                      <Typography variant={"caption"} color={lightText}>
                        {prettifyDate(client.sinceDate)}
                      </Typography>
                    </TableCell>
                    <TableCell align="center">
                      <Typography variant={"caption"} color={lightText}>
                        {client.employeeCount}
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

export default observer(ClientSearch);
