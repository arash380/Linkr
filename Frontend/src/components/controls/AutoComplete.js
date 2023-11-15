import { Autocomplete, TextField } from "@mui/material";
import React from "react";
import { Controller } from "react-hook-form";

const AutoComplete = ({
  control,
  name,
  rules,
  options,
  label,
  getOptionLabel,
  ...rest
}) => {
  const { required, ...restExceptRequired } = rest;
  return (
    <Controller
      render={({
        field: { onChange, value },
        fieldState,
        formState,
        ...props
      }) => (
        <Autocomplete
          {...props}
          {...restExceptRequired}
          disablePortal
          getOptionLabel={getOptionLabel}
          onChange={(e, data) => onChange(data)}
          value={value}
          options={options}
          renderInput={(params) => (
            <TextField
              {...params}
              required={required}
              variant="standard"
              label={label}
              error={fieldState.error ? true : false}
              helperText={fieldState.error ? fieldState.error.message : ""}
            />
          )}
        />
      )}
      rules={rules}
      name={name}
      control={control}
    />
  );
};

export default AutoComplete;
