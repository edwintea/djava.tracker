import Content from "../../layouts/Content";
import {
  Box,
  ButtonBase,
  Divider,
  Grid,
  IconButton,
  List,
  ListItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { useEffect } from "react";
import { observer } from "mobx-react";
import Tile from "../../layouts/Tile";
import { ArrowRightRounded } from "@mui/icons-material";
import { lightText } from "../../../constants/Colours";
import { getDashboard } from "../../../api/service/OrganisationApi";
import { organisationStore } from "../../../stores/OrganisationStore";
import { useNavigate } from "react-router-dom";

const PrivilegedDashboard = () => {
  const { employeeOverview, clientOverview, projects, clients } =
    organisationStore.dashboard;
  const navigate = useNavigate();

  useEffect(() => {
    getDashboard();
  }, []);

  return (
    <Content>
      <Typography
        variant={"h6"}
        color={lightText}
        fontSize={"medium"}
        fontWeight={"bolder"}
        letterSpacing={2}
        textTransform={"uppercase"}
        flex={1}
      >
        Dashboard
      </Typography>
      <Grid container spacing={5} mt={0.5}>
        <Grid item xs={12} md={8}>
          <Grid container spacing={5}>
            <Grid item xs={12} md={6} display={"flex"}>
              <Tile title={"Employee Overview"}>
                <Grid container p={2}>
                  <Grid item xs={12} md={6}>
                    <Box
                      display={"flex"}
                      flexDirection={"column"}
                      alignItems={"center"}
                      justifyContent={"center"}
                      borderRight={{ sm: 0, md: 0.5 }}
                      borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
                    >
                      <Typography fontSize={"xx-large"} fontWeight={"bold"}>
                        {employeeOverview.totalEmployees}
                      </Typography>
                      <Typography>Total Employees</Typography>
                      <ButtonBase
                        sx={{ borderRadius: 8 }}
                        onClick={() => navigate("/employees")}
                      >
                        <Typography
                          display={"flex"}
                          variant={"caption"}
                          color={lightText}
                          alignItems={"center"}
                          height={20}
                          pl={1.2}
                        >
                          View all employees
                          <ArrowRightRounded sx={{ color: lightText }} />
                        </Typography>
                      </ButtonBase>
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
                  <Grid item xs={12} md={6}>
                    <Box
                      display={"flex"}
                      flexDirection={"column"}
                      alignItems={"center"}
                      justifyContent={"center"}
                      borderLeft={{ sm: 0, md: 0.5 }}
                      borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
                    >
                      <Typography fontSize={"xx-large"} fontWeight={"bold"}>
                        {employeeOverview.totalOnLeave}
                      </Typography>
                      <Typography>On Leave</Typography>
                      <ButtonBase sx={{ borderRadius: 8 }}>
                        <Typography
                          display={"flex"}
                          variant={"caption"}
                          color={"transparent"}
                          alignItems={"center"}
                          height={20}
                          pl={1.2}
                        >
                          View employees on leave
                          <ArrowRightRounded sx={{ color: "transparent" }} />
                        </Typography>
                      </ButtonBase>
                    </Box>
                  </Grid>
                </Grid>
              </Tile>
            </Grid>
            <Grid item xs={12} md={6}>
              <Tile title={"Client Overview"}>
                <Grid container p={2}>
                  <Grid item xs={12} md={6}>
                    <Box
                      display={"flex"}
                      flexDirection={"column"}
                      alignItems={"center"}
                      justifyContent={"center"}
                      borderRight={{ sm: 0, md: 0.5 }}
                      borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
                    >
                      <Typography fontSize={"xx-large"} fontWeight={"bold"}>
                        {clientOverview.outsourcedBased}
                      </Typography>
                      <Typography>Outsourced Based</Typography>
                      <Typography
                        variant={"caption"}
                        color={lightText}
                        height={20}
                      >
                        {`${clientOverview.outsourcedBased} employees outsourced`}
                      </Typography>
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
                  <Grid item xs={12} md={6}>
                    <Box
                      display={"flex"}
                      flexDirection={"column"}
                      alignItems={"center"}
                      justifyContent={"center"}
                      borderLeft={{ sm: 0, md: 0.5 }}
                      borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
                    >
                      <Typography fontSize={"xx-large"} fontWeight={"bold"}>
                        {clientOverview.projectBased}
                      </Typography>
                      <Typography>Project Based</Typography>
                      <Typography
                        variant={"caption"}
                        color={lightText}
                        height={20}
                      >
                        {`${clientOverview.projectBased} employees in projects`}
                      </Typography>
                    </Box>
                  </Grid>
                </Grid>
              </Tile>
            </Grid>
            <Grid item xs={12} md={12}>
              <Tile title={"Projects"} titleLeftAlign>
                {projects.length > 0 ? (
                  <>
                    <Typography variant={"caption"} color={lightText}>
                      {`${projects.length} projects found`}
                    </Typography>
                    <TableContainer component={Box}>
                      <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                          <TableRow>
                            <TableCell sx={{ fontWeight: "bold" }}>
                              Client
                            </TableCell>
                            <TableCell
                              sx={{ fontWeight: "bold" }}
                              align="right"
                            >
                              Status
                            </TableCell>
                            <TableCell
                              sx={{ fontWeight: "bold" }}
                              align="right"
                            >
                              Employees
                            </TableCell>
                            <TableCell
                              sx={{ fontWeight: "bold" }}
                              align="right"
                            >
                              Target Completion
                            </TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {projects.map((project) => {
                            const {
                              clientName,
                              status,
                              targetCompletion,
                              employees,
                            } = project;

                            return (
                              <TableRow
                                key={`client-${clientName}`}
                                sx={{
                                  "&:last-child td, &:last-child th": {
                                    border: 0,
                                  },
                                }}
                              >
                                <TableCell component="th" scope="row">
                                  {clientName}
                                </TableCell>
                                <TableCell align="right">{status}</TableCell>
                                <TableCell align="right">{employees}</TableCell>
                                <TableCell align="right">
                                  {targetCompletion}
                                </TableCell>
                              </TableRow>
                            );
                          })}
                        </TableBody>
                      </Table>
                    </TableContainer>
                  </>
                ) : (
                  <>
                    <Typography variant={"caption"} color={lightText}>
                      No projects found
                    </Typography>
                  </>
                )}
              </Tile>
            </Grid>
          </Grid>
        </Grid>
        <Grid item xs={12} md={4} sx={{ display: "flex" }}>
          <Tile
            title={"Clients"}
            titleLeftAlign
            sx={{
              flexGrow: 1,
              maxHeight: { sx: -1, md: "75.45vh" },
              overflowY: "scroll",
            }}
          >
            {clients.length > 0 ? (
              <>
                <Typography variant={"caption"} color={lightText}>
                  {`${clients.length} clients found`}
                </Typography>
                <List sx={{ width: "100%", bgcolor: "background.paper" }}>
                  {clients.map((client) => {
                    const { name, employeeCount } = client;
                    return (
                      <ListItem
                        key={Math.random()}
                        disableGutters
                        sx={{ width: "100%" }}
                      >
                        <Box
                          display={"flex"}
                          flexDirection={"row"}
                          width={"100%"}
                        >
                          <Box flexGrow={1}>
                            <Typography
                              variant={"subtitle1"}
                              fontWeight={"bold"}
                            >
                              {name}
                            </Typography>
                            <Typography variant={"caption"} color={lightText}>
                              {`${employeeCount} employees assigned`}
                            </Typography>
                          </Box>
                          <IconButton sx={{ borderRadius: 2 }}>
                            <ArrowRightRounded fontSize={"large"} />
                          </IconButton>
                        </Box>
                      </ListItem>
                    );
                  })}
                </List>
              </>
            ) : (
              <>
                <Typography variant={"caption"} color={lightText}>
                  No clients found
                </Typography>
              </>
            )}
          </Tile>
        </Grid>
      </Grid>
    </Content>
  );
};

export default observer(PrivilegedDashboard);
