import { ReactNode } from "react";
import { Box } from "@mui/material";

const Content = ({ children }: { children: ReactNode }) => {
  return <Box>{children}</Box>;
};

export default Content;
