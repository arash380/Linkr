import { Box, Button } from "@mui/material";
import React from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { useUser } from "../../../contexts/userContext";
import { updatePassword } from "../../../services/api/userAxios";
import TextInput from "../../controls/Input";
import rp from "../../routing/routePaths";
import Heading from "../../UI/typography/Heading";
import classes from "./Profile.module.css";

const UpdatePassword = () => {
  const { control, handleSubmit } = useForm();
  const nav = useNavigate();
  const { user } = useUser();

  const changePassword = async ({ oPass, pass1, pass2 }) => {
    if (pass1 !== pass2) return toast.error("Passwords do not match!");

    try {
      await updatePassword(user.employeeID, user.userName, oPass, pass1);
      nav(rp.profile.path);
    } catch {}
  };

  return (
    <Box>
      <Heading>{rp.updatePassword.name}</Heading>

      <form className={classes.form} onSubmit={handleSubmit(changePassword)}>
        <TextInput
          name="oPass"
          label="Old Password"
          type="password"
          control={control}
          fullWidth
        />
        <TextInput
          name="pass1"
          label="New Password"
          type="password"
          control={control}
          fullWidth
        />
        <TextInput
          name="pass2"
          label="Confirm New Password"
          type="password"
          control={control}
          fullWidth
        />
        <Button type="submit" variant="contained">
          Update Password
        </Button>
      </form>
    </Box>
  );
};

export default UpdatePassword;
