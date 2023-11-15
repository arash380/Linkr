import React from "react";
import { Box, Button, Avatar } from "@mui/material";
import Heading from "../../UI/typography/Heading";
import rp from "../../routing/routePaths";
import { useUser } from "../../../contexts/userContext";
import { useNavigate } from "react-router-dom";
import classes from "./Profile.module.css";

const Profile = () => {
  const { user } = useUser();
  const nav = useNavigate();

  return (
    <Box>
      <Heading>{rp.profile.name}</Heading>
      <div className={classes.userInfo}>
        <Avatar className={classes.avatar}>
          {user.firstName[0]}
          {user.lastName[0]}
        </Avatar>
        <span>
          {user.firstName} {user.lastName}
        </span>
      </div>

      <div className={classes.detailContainer}>
        <p>Total Vacation Days Accrued: {user.vacationDays}</p>
        <p>Total Sick Days Accrued: {user.sickDays}</p>
        <p>Total Flex Time Accrued: {user.flexTime}</p>
      </div>

      <Button variant="contained" onClick={() => nav(rp.updatePassword.path)}>
        Change Password
      </Button>
    </Box>
  );
};

export default Profile;
