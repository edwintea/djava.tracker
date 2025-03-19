import { Box, Divider, Grid, lighten, Typography } from "@mui/material";
import { observer } from "mobx-react";
import { organisationStore } from "../../../stores/OrganisationStore";
import { lightText } from "../../../constants/Colours";
import Tile from "../../layouts/Tile";

const Organisation = () => {
  const { organisation, dashboard } = organisationStore;
  const { employeeOverview } = dashboard;

  return (
    <Tile title={"Organisation"} titleLeftAlign>
      <Box mt={2} pb={2}>
        <Grid container spacing={5}>
          <Grid item xs={12} md={6}>
            <Box
              display={"flex"}
              alignItems={"center"}
              justifyContent={"center"}
              height={300}
              width={"100%"}
              border={1}
              borderColor={lighten(lightText, 0.8)}
              borderRadius={8}
              bgcolor={lighten(lightText, 0.95)}
            >
              <Typography variant={"caption"} color={lightText}>
                Maps
              </Typography>
            </Box>
          </Grid>
          <Grid item xs={12} md={6}>
            <Grid container spacing={2}>
              <Grid item xs={12} md={12}>
                <Typography variant={"caption"} color={lightText} flexGrow={1}>
                  Name
                </Typography>
                <Typography
                  fontSize={"x-large"}
                  textAlign={"center"}
                  sx={{ mt: 1 }}
                >
                  {organisation.name}
                </Typography>
              </Grid>
              <Grid item xs={12} md={12}>
                <Typography variant={"caption"} color={lightText} flexGrow={1}>
                  Address
                </Typography>
                <Typography
                  fontSize={"medium"}
                  textAlign={"center"}
                  sx={{ mt: 1 }}
                >
                  Address
                </Typography>
              </Grid>
              <Grid item xs={12} md={12}>
                <Typography variant={"caption"} color={lightText} flexGrow={1}>
                  Since
                </Typography>
                <Typography
                  fontSize={"medium"}
                  textAlign={"center"}
                  sx={{ mt: 1 }}
                >
                  1st January 1970
                </Typography>
              </Grid>
              <Grid item xs={12} md={12}>
                <Typography variant={"caption"} color={lightText} flexGrow={1}>
                  Registration No.
                </Typography>
                <Typography
                  fontSize={"medium"}
                  textAlign={"center"}
                  sx={{ mt: 1 }}
                >
                  SRxxxxxxxxxxx
                </Typography>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
        <Grid container mt={5}>
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
            borderRight={{ sm: 0, md: 0.5 }}
            borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
          >
            <Box
              display={"flex"}
              flexDirection={"column"}
              alignItems={"center"}
              justifyContent={"center"}
            >
              <Typography fontSize={"large"} fontWeight={"bold"}>
                {organisation.joinCode}
              </Typography>
              <Typography>Join Code</Typography>
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
            borderColor={{ sm: "", md: "rgba(0, 0, 0, 0.12)" }}
          >
            <Box
              display={"flex"}
              flexDirection={"column"}
              alignItems={"center"}
              justifyContent={"center"}
            >
              <Typography fontSize={"large"} fontWeight={"bold"}>
                {employeeOverview.totalEmployees}
              </Typography>
              <Typography>Total Employees</Typography>
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Tile>
  );
};

export default observer(Organisation);
