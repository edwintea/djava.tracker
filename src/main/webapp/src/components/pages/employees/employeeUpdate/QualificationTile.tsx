import { Box, Paper, Typography } from "@mui/material";
import { lightText } from "../../../../constants/Colours";
import React from "react";

const QualificationTile = ({
  title,
  year,
  institution,
  level,
}: {
  title: string;
  year: string;
  institution: string;
  level: string;
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
        <Typography fontWeight={"bold"}>{title}</Typography>
        <Typography
          variant={"caption"}
          color={lightText}
          ml={"auto"}
          fontStyle={"italic"}
        >
          {year}
        </Typography>
      </Box>
      <Typography>{institution}</Typography>
      <Typography
        variant={"caption"}
        color={lightText}
        sx={{
          display: "flex",
          alignItems: "center",
        }}
      >
        {level}
      </Typography>
    </Paper>
  );
};

export default QualificationTile;
