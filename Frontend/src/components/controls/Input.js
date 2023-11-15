import { TextField } from "@mui/material";
import React from "react";
import { Controller } from "react-hook-form";

const TextInput = ({ control, name, rules, ...rest }) => {
  return (
    <Controller
      render={({ field, fieldState }) => (
        <TextField
          required
          type="standard"
          variant="standard"
          size="medium"
          error={Boolean(fieldState.error)}
          helperText={fieldState.error ? fieldState.error.message : ""}
          {...field}
          {...rest}
        />
      )}
      rules={rules}
      name={name}
      control={control}
    />
  );
};

export default TextInput;
