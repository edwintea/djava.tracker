import { TextField } from "@mui/material";
import * as React from "react";
import { Dispatch, SetStateAction } from "react";
import { SxProps } from "@mui/system";
import { Theme } from "@mui/material/styles";
import { OverridableStringUnion } from "@mui/types";
import { TextFieldPropsSizeOverrides } from "@mui/material/TextField/TextField";

const TextFieldRounded = ({
  id,
  label,
  size,
  variant,
  value,
  setValue,
  disabled,
  multiline,
  fullWidth,
  sx,
}: {
  id: string;
  label: string;
  size: OverridableStringUnion<"small" | "medium", TextFieldPropsSizeOverrides>;
  variant: "outlined" | "standard" | "filled";
  value: string;
  setValue: Dispatch<SetStateAction<string>>;
  disabled?: boolean;
  multiline?: boolean;
  fullWidth?: boolean;
  sx?: SxProps<Theme>;
}) => {
  return (
    <TextField
      id={id}
      label={label}
      size={size}
      variant={variant}
      value={value}
      sx={{
        "& .MuiOutlinedInput-root": {
          "& fieldset": {
            borderRadius: 8,
          },
        },
        ...sx,
      }}
      disabled={disabled}
      multiline={multiline}
      fullWidth={fullWidth}
      onChange={(e) => setValue(e.currentTarget.value)}
    />
  );
};

export default TextFieldRounded;
