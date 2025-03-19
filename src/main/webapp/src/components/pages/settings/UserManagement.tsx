import {
  Box,
  Divider,
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  Typography,
} from "@mui/material";
import { ChangeEvent, useState } from "react";
import Tile from "../../layouts/Tile";

const UserManagement = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const createData = (
    name: string,
    calories: number,
    fat: number,
    carbs: number,
    protein: number
  ) => {
    return { name, calories, fat, carbs, protein };
  };

  const rows = [
    createData("Frozen yoghurt", 159, 6.0, 24, 4.0),
    createData("Ice cream sandwich", 237, 9.0, 37, 4.3),
    createData("Eclair", 262, 16.0, 24, 6.0),
    createData("Cupcake", 305, 3.7, 67, 4.3),
    createData("Gingerbread", 356, 16.0, 49, 3.9),
  ];

  return (
    <Tile title={"User Management"} titleLeftAlign>
      <Box mt={2} pb={2}>
        <Grid container>
          <Grid
            item
            xs={12}
            md={6}
            borderRight={{ sm: 0, md: 0.5 }}
            borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
          >
            <Box
              display={"flex"}
              flexDirection={"column"}
              alignItems={"center"}
              justifyContent={"center"}
            >
              <Typography fontSize={"x-large"} fontWeight={"bold"}>
                2
              </Typography>
              <Typography>Total Users</Typography>
            </Box>
          </Grid>
          <Grid
            item
            xs={12}
            md={12}
            sx={{ display: { sx: "block", md: "none" } }}
          >
            <Divider sx={{ mt: 2, mb: 2 }} />
          </Grid>
          <Grid
            item
            xs={12}
            md={6}
            borderLeft={{ sm: 0, md: 0.5 }}
            borderRight={{ sm: 0, md: 0.5 }}
            borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
          >
            <Box
              display={"flex"}
              flexDirection={"column"}
              alignItems={"center"}
              justifyContent={"center"}
            >
              <Typography fontSize={"x-large"} fontWeight={"bold"}>
                2
              </Typography>
              <Typography>Roles</Typography>
            </Box>
          </Grid>
        </Grid>
        <TableContainer component={Box} mt={5}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell sx={{ fontWeight: "bold" }}>User</TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="right">
                  Joined
                </TableCell>
                <TableCell sx={{ fontWeight: "bold" }} align="right">
                  Role
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => (
                <TableRow
                  key={row.name}
                  sx={{
                    "&:last-child td, &:last-child th": { border: 0 },
                  }}
                >
                  <TableCell component="th" scope="row">
                    {row.name}
                  </TableCell>
                  <TableCell align="right">{row.calories}</TableCell>
                  <TableCell align="right">{row.fat}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={rows.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </Box>
    </Tile>
  );
};

export default UserManagement;
