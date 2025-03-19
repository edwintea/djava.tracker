import { Box, Button, Chip, Grid, Paper, Typography } from "@mui/material";
import { lightText, primary } from "../../../constants/Colours";
import { assignmentTypeFormat, prettifyDate } from "../../../utility/Formatter";
import dayjs from "dayjs";
import { ArrowRightRounded } from "@mui/icons-material";
import React from "react";

const AssignmentTile = ({
  clientName,
  assignmentType,
  fromDate,
  toDate,
  main,
}: {
  clientName: string;
  assignmentType: string;
  fromDate: string;
  toDate: string;
  main: boolean;
}) => {
  return (
    <Paper
      elevation={4}
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-evenly",
        height: "100%",
        w: "100%",
        borderRadius: 8,
        p: 2,
      }}
    >
      <Box
        sx={{
          display: "flex",
          flexDirection: "row",
          alignItems: "center",
        }}
      >
        <Typography fontWeight={"bold"}>{clientName}</Typography>
        {main && (
          <Chip
            size={"small"}
            label={"Main"}
            variant={"outlined"}
            sx={{ borderColor: primary, ml: 1 }}
          />
        )}
        <Typography variant={"caption"} color={lightText} ml={"auto"}>
          {`${dayjs(toDate).diff(dayjs(), "days", false).toString()} days left`}
        </Typography>
      </Box>
      <Typography>{assignmentTypeFormat(assignmentType)}</Typography>
      <Typography
        variant={"caption"}
        color={lightText}
        sx={{
          display: "flex",
          alignItems: "center",
        }}
      >
        {`${prettifyDate(fromDate)}`}
        <ArrowRightRounded />
        {`${prettifyDate(toDate)}`}
      </Typography>
      <Grid container spacing={1} mt={0.5}>
        <Grid item xs={6} md={3}>
          <Button
            size={"small"}
            variant={"text"}
            sx={{ borderRadius: 8 }}
            fullWidth
            disabled={main}
          >
            Set Main
          </Button>
        </Grid>
        <Grid item xs={6} md={3}>
          <Button
            size={"small"}
            variant={"text"}
            sx={{ borderRadius: 8 }}
            fullWidth
          >
            Extend
          </Button>
        </Grid>
        <Grid item xs={6} md={3}>
          <Button
            size={"small"}
            variant={"outlined"}
            sx={{
              borderRadius: 8,
            }}
            color={"error"}
            fullWidth
          >
            Cancel
          </Button>
        </Grid>
        <Grid item xs={6} md={3}>
          <Button
            size={"small"}
            variant={"contained"}
            sx={{ borderRadius: 8 }}
            fullWidth
          >
            Complete
          </Button>
        </Grid>
      </Grid>
    </Paper>
  );
};

export default AssignmentTile;
