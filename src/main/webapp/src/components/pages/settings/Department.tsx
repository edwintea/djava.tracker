import { Box, Divider, Grid, lighten, Typography } from "@mui/material";
import Tile from "../../layouts/Tile";
import { lightText } from "../../../constants/Colours";

const Department = () => {
  return (
    <>
      <Tile title={"Department & Positions"} titleLeftAlign>
        <Box mt={2} pb={2}>
          <Grid container>
            <Grid
              item
              xs={12}
              md={4}
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
                <Typography>Departments</Typography>
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
              md={4}
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
                <Typography>Positions</Typography>
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
              md={4}
              borderLeft={{ sm: 0, md: 0.5 }}
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
                <Typography>Total Employees</Typography>
              </Box>
            </Grid>
          </Grid>
        </Box>
      </Tile>
      <Tile title={"Organisation Structure"} titleLeftAlign sx={{ mt: 5 }}>
        <Box
          display={"flex"}
          alignItems={"center"}
          justifyContent={"center"}
          height={500}
          width={"100%"}
          mt={2}
          border={1}
          borderColor={lighten(lightText, 0.8)}
          borderRadius={8}
          bgcolor={lighten(lightText, 0.95)}
        >
          <Typography variant={"caption"} color={lightText}>
            Organisation Chart
          </Typography>
        </Box>
      </Tile>
    </>
  );
};

export default Department;
