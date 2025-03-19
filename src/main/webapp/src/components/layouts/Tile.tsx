import { Paper, Typography } from "@mui/material";
import { ReactNode } from "react";
import { SxProps } from "@mui/system";
import { Theme } from "@mui/material/styles";

const Tile = ({
  title,
  titleLeftAlign,
  children,
  sx,
}: {
  title?: string;
  titleLeftAlign?: boolean;
  children?: ReactNode;
  sx?: SxProps<Theme>;
}) => {
  return (
    <Paper
      sx={{
        display: "flex",
        flexDirection: "column",
        flexGrow: 1,
        borderRadius: 8,
        p: 2,
        ...sx,
      }}
      elevation={2}
    >
      {title && (
        <Typography
          textTransform={"uppercase"}
          fontSize={"small"}
          letterSpacing={2}
          textAlign={titleLeftAlign ? "left" : "center"}
        >
          {title}
        </Typography>
      )}
      {children}
    </Paper>
  );
};

export default Tile;
