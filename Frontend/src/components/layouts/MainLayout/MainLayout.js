import { Box, Container, Toolbar } from "@mui/material";
import { useState } from "react";
import SideBar from "../../UI/navigation/SideBar/SideBar";
import TopAppBar from "../../UI/navigation/TopAppBar/TopAppBar";
import classes from "./MainLayout.module.css";
import { Outlet } from "react-router-dom";

const MainLayout = () => {
  const [accountSideBarOpen, setAccountSideBarOpen] = useState(false);

  return (
    <div className={classes.root}>
      <TopAppBar setAccountSideBarOpen={setAccountSideBarOpen} />
      <SideBar
        accountSideBarOpen={accountSideBarOpen}
        setAccountSideBarOpen={setAccountSideBarOpen}
      />

      <Box component="main" className={classes.contentContainer}>
        <Toolbar />
        <Container maxWidth="xl">
          <Outlet />
        </Container>
      </Box>
    </div>
  );
};

export default MainLayout;
