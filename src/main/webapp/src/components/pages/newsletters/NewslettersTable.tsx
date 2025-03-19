import {
  Box,
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
import { lightText, pastelRed } from "../../../constants/Colours";
import { Delete, Edit, Visibility } from "@mui/icons-material";
import Tile from "../../layouts/Tile";
import React, { ChangeEvent, useEffect, useState } from "react";
import { getNewsletterPageable } from "../../../api/service/NewsletterApi";
import { newsletterStore } from "../../../stores/NewsletterStore";
import { prettifyDate } from "../../../utility/Formatter";

const NewslettersTable = () => {
  const { newsletters } = newsletterStore;
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [count, setCount] = useState(0);

  const getNewsletters = (
    newPage: number = page,
    rows: number = rowsPerPage
  ) => {
    getNewsletterPageable(newPage, rows).then((result) => {
      if (result) {
        const resultCount = result.headers
          ? Number(result.headers["x-total-count"])
          : 0;
        setCount(resultCount);
      }
    });
  };

  useEffect(() => {
    getNewsletters();
  }, []);

  const handleChangePage = (event: unknown, newPage: number) => {
    getNewsletters(newPage);
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: ChangeEvent<HTMLInputElement>) => {
    const row = parseInt(event.target.value, 10);
    getNewsletters(0, row);
    setRowsPerPage(row);
    setPage(0);
  };

  return (
    <Tile title={"Newsletters"} titleLeftAlign>
      <Typography variant={"caption"} color={lightText}>
        {`${count} newsletters found`}
      </Typography>
      <TableContainer component={Box}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ fontWeight: "bold" }} align="left">
                Subject
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="center">
                Date
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="center" />
            </TableRow>
          </TableHead>
          <TableBody>
            {newsletters.map((newsletter) => {
              return (
                <TableRow
                  key={newsletter.id}
                  sx={{
                    "&:last-child td, &:last-child th": { border: 0 },
                  }}
                >
                  <TableCell align="left">
                    <Typography sx={{ fontSize: 14 }}>
                      {newsletter.subject}
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant={"caption"} color={lightText}>
                      {prettifyDate(newsletter.createdDate)}
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
        <TablePagination
          rowsPerPageOptions={[10, 20, 30, 50]}
          component="div"
          count={count}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>
    </Tile>
  );
};

export default NewslettersTable;
