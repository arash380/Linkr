import React from "react";
import { Box, TextField } from "@mui/material";
import { useUser } from "../../../contexts/userContext";
import { login } from "../../../services/api/userAxios";
import Button from "../../UI/Button/Button";
import classes from "./Login.module.css";

const Login = () => {
  const { setUser } = useUser();

  const loginHandler = async (e) => {
    e.preventDefault();

    const username = e.target.username.value;
    const password = e.target.password.value;

    const t = await login(username, password);
    await setUser(t);
  };

  return (
    <Box className={classes.root}>
      <Box className={classes.loginContainer}>
        <div className={classes.appLogo}>Linkr</div>
        <form onSubmit={loginHandler}>
          <TextField
            required
            label="Username"
            variant="outlined"
            name="username"
          />
          <TextField
            required
            type="password"
            label="Password"
            variant="outlined"
            name="password"
          />
          <Button variant="contained" type="submit">
            LOGIN
          </Button>
        </form>
      </Box>
    </Box>
  );
};

export default Login;
